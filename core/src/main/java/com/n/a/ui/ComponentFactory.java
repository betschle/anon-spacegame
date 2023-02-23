package com.n.a.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.n.a.XYZGame;
import com.n.a.game.settings.GameSettings;
import com.n.a.sfx.ButtonSounds;
import com.n.a.game.space.Galaxy;
import com.n.a.ui.commons.ButtonClickListener;
import com.n.a.ui.commons.CheckBoxSounds;
import com.n.a.ui.commons.Growl;
import com.n.a.ui.commons.Header;
import com.n.a.ui.commons.Separator;
import com.n.a.ui.commons.*;
import com.n.a.ui.commons.widgets.ListWithTooltips;
import com.n.a.ui.commons.widgets.TextButtonTabs;
import com.n.a.ui.game.GameSettingsPanel;
import com.n.a.ui.game.menu.GalaxyTab;
import com.n.a.ui.game.menu.HelpTab;
import com.n.a.ui.game.menu.SettingsTab;
import com.n.a.ui.game.menu.starmap.GalaxyMap;
import com.n.a.ui.game.planet.PlanetDetailPanel;
import com.n.a.ui.game.planet.PlanetPanel;

/**
 * Factory for more complex XYZ Components.
 *
 * TODO outsource: Manages creation of UI Components and their "sound style".
 */
public class ComponentFactory {

    private XYZGame game;
    private Skin skin;
    // TODO Map of button sounds
    private ButtonSounds mainButtonSounds;
    private ButtonSounds starmapButtonSounds;
    private ButtonSounds checkBoxButtonSounds;
    private ListenerFactory listenerFactory;
    /** Factory for input components. */
    private InputComponentFactory inputComponentFactory;
    /** Factory for navigational components. */
    private NavigationalComponentFactory navigationalComponentFactory;
    /** Factory for information components */
    private InformationComponentFactory informationComponentFactory;
    private ContainerComponentFactory containerComponentFactory;

    public ComponentFactory(XYZGame game) {
        this.game = game;
        // TODO outsource skin loading to screen?
        this.skin = new Skin( new TextureAtlas( Gdx.files.internal("ui/skins/XYZ-core/styles.atlas") ));
        this.skin.load( Gdx.files.internal("ui/skins/XYZ-core/styles.json") ); // TODO belongs to DataPack!

        this.inputComponentFactory = new InputComponentFactory( this.skin );
        this.navigationalComponentFactory = new NavigationalComponentFactory( this.skin );
        this.informationComponentFactory = new InformationComponentFactory( this.skin );
        this.containerComponentFactory = new ContainerComponentFactory(this.skin);
        this.listenerFactory = new ListenerFactory();

        this.mainButtonSounds = new ButtonSounds("click1", "click2", "tap2");
        this.starmapButtonSounds = new ButtonSounds("click_soft1", "click_soft2", "tap2");
        this.checkBoxButtonSounds = new ButtonSounds("switch4", "switch1", "tap2");


        // Enable Color Markup for the growl font
        // TODO color markup crashes when using on a libgdx text field.
        // Use the CamoTactics text field instead!
        BitmapFont camotactics_bs_20px = skin.getFont("camotactics_bs_20px");
        camotactics_bs_20px.getData().markupEnabled = true;
    }

    public Skin getSkin() {
        return skin;
    }

    public ButtonSounds getMainButtonSounds() {
        return mainButtonSounds;
    }

    public ButtonSounds getStarmapButtonSounds() {
        return starmapButtonSounds;
    }

    public Table getGameSettingsTab(String header) {
        Table table = new Table();
        Header headerComponent = this.informationComponentFactory.getHeader(header);
        table.add(headerComponent).expand();
        return table;
    }

    /**
     * Gets a Help Tab for the main game menu.
     * @param model
     * @return
     */
    public HelpTab getHelpTab(TextButtonTabs.TextButtonTabModel model) {
        HelpTab helpTab = new HelpTab(this.skin);
        helpTab.setHeader( this.informationComponentFactory.getHeader("Help"));

        TextButtonTabs.ButtonSettings buttonSettings = new TextButtonTabs.ButtonSettings();
        buttonSettings.styleClass = TextButton.TextButtonStyle.class;
        buttonSettings.styleName = "interactive-checked";
        buttonSettings.minButtonHeight = 60;
        buttonSettings.minButtonWidth = 160;

        TextButtonTabs.ContentTabSettings contentTabSettings = new TextButtonTabs.ContentTabSettings();
        // TODO contentTabSettings not working properly with scrollpane
        // TODO ScrollPaneSettings?
        contentTabSettings.minScrollContainerHeight = 600;
        contentTabSettings.minContentWidth = 600;
        contentTabSettings.minContentHeight = 400;

        TextButtonTabs textTabs = this.getTextTabs(model, buttonSettings, contentTabSettings );
        helpTab.setTabs( textTabs );
        helpTab.construct();
        helpTab.pack();
        return helpTab;
    }

    /**
     * Gets a Text Button Tab that can be used to create a library of text, selectable by categories.
     * @param tabTextModel
     * @return
     */
    public TextButtonTabs getTextTabs(TextButtonTabs.TextButtonTabModel tabTextModel,
                                      TextButtonTabs.ButtonSettings buttonSettings,
                                      TextButtonTabs.ContentTabSettings contentTabSettings ) {
        TextButtonTabs tabText = new TextButtonTabs(this.skin );
        tabText.setContentTabSettings(contentTabSettings);
        tabText.setButtonSettings(buttonSettings);
        tabText.setButtonClickListener( new ButtonClickListener(game.getSoundManager(), new ButtonSounds("click3", "tap2", "tap1") ));
        tabText.setTextScrollPane(
                this.navigationalComponentFactory.getScrollContentPane("default-vertical", contentTabSettings.minContentHeight));
        tabText.setTextButtonScrollPane(
                this.navigationalComponentFactory.getScrollContentPane("default-vertical", contentTabSettings.minContentHeight));
        tabText.setTabTextModel(tabTextModel);
        tabText.construct();
        tabText.pack();
        return tabText;

    }

    /**
     * Obtains a planet detail panel.
     * @return
     */
    public PlanetDetailPanel getPlanetDetailView( ) {
        InformationComponentFactory icFactory = this.informationComponentFactory;
        PlanetDetailPanel planetDetailPanel = new PlanetDetailPanel(this.skin);

        planetDetailPanel.setSeparator( new Separator(skin, "panel_inset_brass_1"));
        planetDetailPanel.getNamePair().setLabel( icFactory.getLabel("Name", Align.left))
                                      .setValue( icFactory.getLabel("Alpha Majoris", Align.right) );

        planetDetailPanel.getTypePair().setLabel( icFactory.getLabel("Type", Align.left))
                                      .setValue( icFactory.getLabel("Planet", Align.right) );

        planetDetailPanel.getDiscoveredPair().setLabel( icFactory.getLabel("Discovered", Align.left))
                                            .setValue( icFactory.getLabel("0 %", Align.right));

        planetDetailPanel.getSizePair().setLabel( icFactory.getLabel("Size", Align.left))
                                      .setValue( icFactory.getLabel("0 er", Align.right));

        planetDetailPanel.getHabitabilityPair().setLabel( icFactory.getLabel("Habitability", Align.left))
                                              .setValue( icFactory.getProgressBar(0, 1, 0.01f, false));

        planetDetailPanel.getAtmospherePair().setLabel( icFactory.getLabel("Atmosphere", Align.left))
                .setValue( icFactory.getProgressBar(0, 1, 0.01f, false)) ;

        planetDetailPanel.getTemperaturePair().setLabel( icFactory.getLabel("Temperature", Align.left))
                .setValue( icFactory.getProgressBar(0, 1, 0.01f, false));

        planetDetailPanel.getRadiationPair().setLabel( icFactory.getLabel("Radiation", Align.left))
                .setValue( icFactory.getProgressBar(0, 1, 0.01f, false));

        planetDetailPanel.getToxicityPair().setLabel( icFactory.getLabel("Toxicity", Align.left))
                .setValue( icFactory.getProgressBar(0, 1, 0.01f, false));
        planetDetailPanel.construct();
        planetDetailPanel.updateFromModel();
        return planetDetailPanel;
    }

    /**
     * Gets a {@link GameSettingsPanel} panel
     * @param gameSettings
     * @return
     */
    public GameSettingsPanel getGameSettingsPanel( GameSettings gameSettings ) {
        GameSettingsPanel gameSettingsPanel = new GameSettingsPanel( this.skin );
        gameSettingsPanel.setModel(gameSettings);
        Button checkBox = this.inputComponentFactory.getButton("checkBox_1");
        checkBox.addListener( new CheckBoxSounds(game.getSoundManager(), checkBoxButtonSounds));

        gameSettingsPanel.getMusicVolumePair()
                .setLabel( this.informationComponentFactory.getLabel("Music Volume", Align.left))
                .setValue( new Slider(0, 1, 0.05f, false, this.skin));
        gameSettingsPanel.getSfxVolumePair()
                .setLabel( this.informationComponentFactory.getLabel("SFX Volume", Align.left))
                .setValue( new Slider(0, 1, 0.05f, false, this.skin));
        gameSettingsPanel.getUiVolumePair()
                .setLabel( this.informationComponentFactory.getLabel("GUI Volume", Align.left))
                .setValue( new Slider(0, 1, 0.05f, false, this.skin));
        gameSettingsPanel.getGodModePair()
                .setLabel( this.informationComponentFactory.getLabel("Godmode", Align.left) )
                .setValue( checkBox );

        gameSettingsPanel.construct();
        gameSettingsPanel.updateFromModel();
        return gameSettingsPanel;
    }

    /**
     * Gets a default {@link PlanetPanel} component.
     * @return
     */
    public PlanetPanel getPlanetPanel() {
        TextButtonTabs.ButtonSettings buttonSettings = new TextButtonTabs.ButtonSettings();
        buttonSettings.styleClass = ImageTextButton.ImageTextButtonStyle.class;
        buttonSettings.styleName = "trait";
        buttonSettings.minButtonHeight = 20;
        buttonSettings.minButtonWidth = 120;

        ListWithTooltips<TextButton> traitList = new ListWithTooltips<>(this.skin);
        traitList.setButtonSettings(buttonSettings);
        traitList.setColumnCount(2);

        PlanetPanel planetPanel = new PlanetPanel(this.skin);
        planetPanel.setPlanetDetailView( getPlanetDetailView() );
        planetPanel.setTraitsPanel( traitList );
        planetPanel.construct();
        return planetPanel;
    }

    /**
     *
     * @param gameSettings
     * @return
     */
    public SettingsTab getSettingsTab( GameSettings gameSettings) {
        SettingsTab settingsTab = new SettingsTab(this.skin);
        settingsTab.setGame(game);
        settingsTab.setHeader( this.informationComponentFactory.getHeader("Settings"));
        settingsTab.setGameSettingsPanel( this.getGameSettingsPanel(gameSettings));
        settingsTab.setSeparator( new Separator(skin, "panel_inset_brass_1"));
        settingsTab.setMessageGrowl( new Growl(skin));

        ButtonClickListener buttonSounds = new ButtonClickListener(game.getSoundManager(), new ButtonSounds("beep2", "tap2", "tap1"));
        TextButton applyButton = this.inputComponentFactory.getTextButton("Apply", false);
        applyButton.addListener(buttonSounds);
        TextButton resetButton = this.inputComponentFactory.getTextButton("Reset", false);
        resetButton.addListener(buttonSounds);

        settingsTab.setApplyButton(applyButton);
        settingsTab.setResetButton(resetButton);
        settingsTab.construct();
        return settingsTab;
    }

    public GalaxyTab getGalaxyTab( Galaxy galaxy ) {

        GalaxyMap galaxyMap = new GalaxyMap(this.skin);
        galaxyMap.setMask( galaxy.getGalaxyMask() );

        GalaxyTab galaxyTab = new GalaxyTab(this.skin);
        galaxyTab.setHeader( this.informationComponentFactory.getHeader("Galaxy Map"));
        galaxyTab.setGalaxyMap(galaxyMap );
        galaxyTab.setModel( GalaxyTab.galaxyToModel(galaxy) );
        galaxyTab.construct();
        return galaxyTab;
    }

}
