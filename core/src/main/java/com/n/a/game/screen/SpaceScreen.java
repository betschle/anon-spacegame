package com.n.a.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.n.a.game.DataPack;
import com.n.a.game.Game;
import com.n.a.game.Player;
import com.n.a.game.SpaceShip;
import com.n.a.game.discovery.PlanetScanner;
import com.n.a.game.settings.generator.ShipGraphicsSettings;
import com.n.a.game.space.Galaxy;
import com.n.a.game.space.GalaxyMask;
import com.n.a.game.space.Planet;
import com.n.a.game.space.Sector;
import com.n.a.game.space.SectorGenerator;
import com.n.a.game.space.SectorManager;
import com.n.a.game.space.SectorManagerListener;
import com.n.a.game.space.StarSystem;
import com.n.a.game.space.StarSystemSectorFactory;
import com.n.a.gfx.particles.ParticleRenderer;
import com.n.a.XYZGame;
import com.n.a.game.*;
import com.n.a.game.discovery.DiscoveryListener;
import com.n.a.game.planet.PlanetTrait;
import com.n.a.game.repository.StarSystemSettingsRepository;
import com.n.a.game.settings.generator.StarSystemSettings;
import com.n.a.game.space.*;
import com.n.a.gfx.EnginePoint;
import com.n.a.gfx.Parallax;
import com.n.a.gfx.PlanetGraphics;
import com.n.a.gfx.ShipGraphics;
import com.n.a.ui.commons.ApplyGameSettingsListener;
import com.n.a.ui.commons.ButtonClickListener;
import com.n.a.ui.commons.widgets.ButtonGroup;
import com.n.a.ui.commons.GrowlStack;
import com.n.a.ui.commons.widgets.IconTabs;
import com.n.a.ui.commons.widgets.TextButtonTabs;
import com.n.a.ui.game.menu.GalaxyTab;
import com.n.a.ui.game.menu.HelpTab;
import com.n.a.ui.game.menu.SettingsTab;
import com.n.a.ui.hud.PlanetHUD;
import com.n.a.ui.hud.PlayerHUD;
import com.n.a.ui.hud.SonarMap;
import com.n.a.ui.game.menu.StarMapTab;
import com.n.a.util.MathUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Screen where the ship is located in outer space.
 * This is where the main gameplay takes place.
 */
public class SpaceScreen extends AbstractScreen {
    // todo abstract this screen in such a way i can launch manual tests easily

    Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    private Stage background;
    /**Stage where entities are being added. */
    private Stage root;
    /** Stage where HUD elements are added. Is rendered above the root, but still below the UI. */
    private Stage hud;
    /** The stage where UI Components are added. Is rendered above root and UI stages. */
    private Stage ui;
    /** The layout table of HUD Components, stretches the entire screen. */
    private Table hudLayout;
    private Table uiLayout;
    //
    private Game XYZGame;
    private Player player;
    /** Current player sector position */
    private int currentX, currentY;
    private Galaxy galaxy;

    // UI stuffs
    private GalaxyTab galaxyTab;
    private IconTabs tabMenu;
    private GrowlStack growlStack;
    private StarSystem system;
    private StarMapTab starMapTab;
    private SettingsTab settingsTab;
    private PlayerHUD playerHUD;
    private PlanetHUD planetHUD;
    private SonarMap sonarMap;
    private ParticleRenderer particleRenderer;
    private Parallax parallax;

    private BitmapFont font;

    private float elapsedTime = 0;
    public static float scale = 2;
    private boolean loaded = false;

    private String helpText =
            "== Player Controls ==\n" +
            "ASDW - Move around\n" +
            "Tab - Change Selected Planet\n" +
            "Space - Scan Selected Planet\n" +
            "CTRL L/R - Reset engine thrust\n" +
            "\n"+
            "== Menu Controls ==\n" +
            "ESC - Show/hide main menu\n" +
            "F1 - Toggle this help text\n" +
            "M - Enter StarMap Screen\n" +
            "G - Enter GalaxyMap Screen\n" +
            "\n"+
            "== Misc Controls ==\n" +
            "F2, F3, F4 - Toggle Debug Draw\n" +
            "L - Next Song\n" +
            "R - Regenerate System\n"
            ;


    private Sound soundReward;
    private Sound soundBeep;
    private Sound ambient;
    private long ambientId;

    private SectorManager<StarSystem> sectorManager = new SectorManager<>();
    private SectorGenerator<StarSystem> sectorGenerator;
    private GalaxyMask galaxyMask;
    private TextButtonTabs.TextButtonTabModel helpTabModel;
    private HelpTab helpTab;

    public SpaceScreen(XYZGame game) {
        super(game);
    }

    /**
     * Sets up logging via JUL
     */
    public void setupLogging() {
        InputStream stream = null;
        try {
            LogManager logManager = LogManager.getLogManager();
            FileHandle internal = Gdx.files.internal("logging.properties");
            stream = internal.read();
            logManager.readConfiguration( stream  );
            stream.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Could not initialize logging with default config (file: assets/logging.properties)", e);
            if( stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void initSound() {
        this.soundReward = Gdx.audio.newSound(Gdx.files.internal("sfx/ui/reward.wav"));
        this.soundBeep = Gdx.audio.newSound(Gdx.files.internal("sfx/ui/beep1.wav"));
        this.ambient = Gdx.audio.newSound(Gdx.files.internal("sfx/ambient/space2.wav"));
    }

    private void initHUD(TextureAtlas atlas) {
        this.hud = new Stage();

        this.hudLayout = new Table();
        this.hudLayout.setPosition(0,0);
        this.hudLayout.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.hud.addActor(this.hudLayout);

        this.playerHUD = new PlayerHUD( this.game.getComponentFactory().getSkin() );
        this.playerHUD.update(player);

        this.planetHUD = new PlanetHUD( this.game.getComponentFactory().getSkin());
        // this.planetHUD.setFocusedPlanet( this.system.getSun() );
        this.sonarMap = new SonarMap(this.game.getComponentFactory().getSkin(), this.player.getPlayerShipGraphics());
        this.growlStack = new GrowlStack(game.getComponentFactory().getSkin(), 10);

        this.hudLayout.add(this.growlStack).minSize(250, 200).top();
        this.hudLayout.add().pad(10).minWidth(900);
        this.hudLayout.row();
        this.hudLayout.add().minHeight(150); // spacer
        this.hudLayout.add().minHeight(150);
        this.hudLayout.row();
        this.hudLayout.add(this.playerHUD).prefSize(200, 50).padBottom(5).left();
        this.hudLayout.add().pad(10).minSize(900, 50);
        this.hudLayout.row();
        this.hudLayout.add(this.sonarMap).prefSize(200, 200).pad(10).left();
        this.hudLayout.add().pad(10).minWidth(900);
        this.hudLayout.pack();
    }

    private void initUI() {
        Skin skin = this.game.getComponentFactory().getSkin();

        this.font = skin.getFont("default");
        this.ui = new Stage();
        this.uiLayout = new Table();
        this.uiLayout.setPosition(0,0);
        this.uiLayout.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.ui.addActor(this.uiLayout);

        this.starMapTab = new StarMapTab( skin, this.game.getComponentFactory());
        this.starMapTab.setPlayerGraphics(this.player.getPlayerShipGraphics());
        this.starMapTab.setVisible(false);
        ButtonClickListener starmapButtonListener = new ButtonClickListener(this.game.getSoundManager(), this.game.getComponentFactory().getStarmapButtonSounds());
        this.starMapTab.setZoomButtonSounds( starmapButtonListener );
        this.starMapTab.getStarMap().setPlanetButtonSounds( starmapButtonListener );
        this.starMapTab.getStarMap().setFollowPlayer();

        this.galaxyTab = this.game.getComponentFactory().getGalaxyTab(this.galaxy);

        this.tabMenu = new IconTabs(skin, ButtonGroup.Orientation.HORIZONTAL);
        this.tabMenu.getButtonGroup().setButtonClickListener( new ButtonClickListener( this.game.getSoundManager(), this.game.getComponentFactory().getMainButtonSounds()));

        this.helpTabModel = new TextButtonTabs.TextButtonTabModel();

        this.helpTabModel.addEntry("Keymappings", this.helpText);
        this.helpTabModel.addEntry("Introduction", "The universe is vast and so is XYZ! " +
                "\n\nThis game is all about exploration and research, so go out and find some planets! " +
                "\n\nThis game does not show you all planets in a system right away: you build up the map as you discover celestial bodies. Using the sonar at the bottom left, " +
                "you can find objects of interest in your immediate proximity." +
                "\n\nOnce you spotted a planet on the sonar, it's time to get closer for a scan! Once it's close enough, press [TAB] to target it, and [SPACE] to discover it. This will add" +
                "the planet to your map." +
                "\n\nWith subsequent scans you discover a planet's properties, also called traits. These traits directly determine its appearance and color. " +
                "You may find exotic planets with exotic traits, perhaps even traces of life out there. " +
                "\n\nFor each traits you earn discovery points, which in future updates, allow you to purchase more equipment and improve your ship." +
                "\n\nYou can try finding more exotic constellations such as asteroid fields, planet rings, binary star systems. Many more to come!");

        this.helpTabModel.addEntry("Development", "This game is still in early development. Some features presented might still be buggy or unpolished!\n\n" +
                                "Let me know how you like it! Thanks for playing!" );

        this.helpTabModel.addEntry("Sonar Map", "The Sonar Map on the bottom left displays targets within your sensor range. Inside you find two rotating elements: " +
                "\n\n- a circle representing your spacecraft's spatial rotation" +
                "\n- a target arrow which rotates to the currently selected target" +
                "\n\nDifferent targets may have different icon representations on the sonar. Currently the sonar can distinguish between planets and stars." +
                "\n\nPlease note: the target arrow ALWAYS rotates to face the central star in the system you're currently in when no target was selected."
        );
        this.helpTabModel.addEntry("Sectors", "The Galaxy in XYZ is divided into sectors. Each sector usually contains a complete star system. " +
                "\n\nRight now, you can leave one sector to load the next one. The Galaxy you landed in creates around 30.000 star systems. Galaxy generation is a fresh mechanic " +
                "that is still a little buggy and needs more refinement. Right now what sectors are generated depends on the luminosity of the galaxy. You may find sectors that only have one star in" +
                "areas with low luminosity."
        );
        this.helpTab = this.game.getComponentFactory().getHelpTab(helpTabModel);
        this.helpTab.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.settingsTab = game.getComponentFactory().getSettingsTab(this.game.getGameSettings());
        this.settingsTab.setApplyButtonListener(new ApplyGameSettingsListener(this.game, settingsTab) );
        this.tabMenu.addTab("menuGalaxy", "Galaxy Map", this.galaxyTab);
        this.tabMenu.addTab("menuStarMap", "Sector Map", this.starMapTab);
        this.tabMenu.addTab("menuSettings", "Settings", this.settingsTab);
        this.tabMenu.addTab("menuHelp", "Help", helpTab);

        this.tabMenu.getButtonGroup().pack();
        this.tabMenu.construct();
        this.tabMenu.pack();
        this.tabMenu.setVisible(false);

        this.uiLayout.add(this.tabMenu).fill().center();
    }

    /**
     * Initializes the sector manager that
     * makes the world scroll
     */
    private void initSectorManager() {
        DataPack defaultDataPack = XYZGame.getDataPackManager().findDataPack(XYZGame.DEFAULT_DATAPACK);
        this.galaxy = defaultDataPack.getGalaxyFactory().createGalaxy( "env/galaxy_milkyway" );
        logger.log(Level.INFO, "Theoretical amount of galaxies for this galaxy mask: " + this.galaxy.getGalaxyMask().countAverage(2));


        // TODO move to Game
        StarSystemSectorFactory galaxyGenerator = new StarSystemSectorFactory();
        galaxyGenerator.setGalaxy( this.galaxy );
        galaxyGenerator.setNumberGenerator( defaultDataPack.getNumberGenerator() );
        galaxyGenerator.setDataPack(defaultDataPack);

        this.sectorGenerator = galaxyGenerator;
        float maxExtent = defaultDataPack.getStarSystemFactory().getMaxExtent();
        this.sectorManager.setSectorBoundsX( (int)(maxExtent * 2.5f) );
        this.sectorManager.setSectorBoundsY( (int)(maxExtent * 2.5f) ); // 45000
        this.sectorManager.setSectorGenerator(this.sectorGenerator);
        this.sectorManager.addListener(new SectorManagerListener<StarSystem>() {
            @Override
            public void onSectorPreChange(int oldX, int oldY, int newX, int newY) {

            }

            @Override
            public void onSectorPostChange(int oldX, int oldY, int newX, int newY) {

            }

            @Override
            public void onNewSectorGenerated(Sector<StarSystem> sector) {

            }

            @Override
            public void onSectorLoaded(Sector<StarSystem> sector) {
                growlStack.showSuccessGrowl("New Sector entered: " + sector.getXCoordinates() + " | " + sector.getYCoordinates());
                logger.log(Level.SEVERE, "Sector loaded: " + sector.getXCoordinates()+ " " + sector.getYCoordinates());
                StarSystem chunk = sector.getChunk();;

                currentX = sector.getXCoordinates();
                currentY = sector.getYCoordinates();

                float x = (sector.getXCoordinates() ) * (float) sectorManager.getSectorBoundsX();
                float y = (sector.getYCoordinates() ) * (float) sectorManager.getSectorBoundsY();
                logger.log(Level.SEVERE, "Spawn Star System at: " + x + " " + y );
                chunk.getGraphics().setPosition(x,y);
                switchStarSystemTo(chunk);
            }

            @Override
            public void onSectorSaved(Sector<StarSystem> sector) {

            }
        });
        //
    }

    public void create() {
        this.setupLogging();
        logger.log(Level.INFO, "Creating XYZ application...");

        this.XYZGame = Game.get("Testbed", 678739996);
        this.XYZGame.createNewWorld(null);

        this.initSectorManager();

        DataPack defaultDataPack = XYZGame.getDataPackManager().findDataPack(XYZGame.DEFAULT_DATAPACK);

        this.logger.log(Level.FINEST, "Setting up stage...");
        this.background = new Stage();
        this.root = new Stage();

        ShipGraphicsSettings playerSettings = new ShipGraphicsSettings();
        playerSettings.shipTextureRegion = "ship/ship";
        playerSettings.trailParticleTextureRegion = "prt/smoke";

        // TODO outsource to PlayerFactory
        this.player = new Player();
        SpaceShip spaceShip = defaultDataPack.getSpaceShipFactory().createSpaceShip(playerSettings);
        this.player.setPlayerShipGraphics(spaceShip.getShipGraphics());
        this.player.setPlanetScanner( new PlanetScanner(spaceShip));

        this.player.getPlanetScanner().addDiscoveryListener(new DiscoveryListener() {
            @Override
            public void onPlanetTraitDiscovered(Planet planet, PlanetTrait planetTrait) {
                if( planetTrait != null ) {
                    player.addDiscoveryPointsFromTrait(planetTrait);
                    planetHUD.update();
                    starMapTab.initTargetList();
                    starMapTab.getSectorInfo().updateWithSector(system, currentX, currentY);
                    playerHUD.update(player);
                    String messageFormat = "[GOLD]%s : [RED][ +%d ] [GOLD]%s [WHITE]discovered!";
                    String message = String.format(messageFormat, planet.getDisplayName( game.getGameSettings().isGodmode() ), planetTrait.getDiscoveryPoints(), planetTrait.getName()  );
                    growlStack.showSuccessGrowl( message );
                    soundReward.play();
                }
            }

            @Override
            public void onPlanetDiscovered(PlanetGraphics planet) {
                player.addDiscoveryPoints(1);
                applySystemValuesToStarMap(system);
                growlStack.showSuccessGrowl("New Planet Discovered & registered! It was named [GOLD]" + planet.getModel().getName() );
                soundBeep.play();
            }
        });

        // init starting position
        this.player.getPlayerShipGraphics().setPosition(this.sectorManager.getSectorBoundsX() * 21 +  1200,
                                                        this.sectorManager.getSectorBoundsX() *-31 - 3000);
        //
        // create particle node
        this.particleRenderer = new ParticleRenderer();
        for( EnginePoint point : this.player.getPlayerShipGraphics().exhaust.getEngines() ) {
            particleRenderer.registerParticles(point.getTrail().getParticles());
        }

        // set up the parallax
        this.parallax = new Parallax(5, defaultDataPack.getNumberGenerator(), defaultDataPack.getTextureAtlas());
        this.parallax.setPosition(0, 0);
        this.parallax.setSize( Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.background.addActor(parallax);
        this.background.setDebugAll(true);


        root.addActor(particleRenderer);
        root.addActor(player.getPlayerShipGraphics());

        this.gainFocusPlayer();

        this.initSound();
        this.initHUD(defaultDataPack.getTextureAtlas());
        this.initUI();
        this.player.getPlayerShip().startEngines();

        loaded = true;
        this.logger.log(Level.INFO, "System Stats: " + defaultDataPack.getStarSystemFactory().toString() );

        this.ambientId = this.ambient.loop(0.8f, 1f, 0.2f);
    }

    public void showTabMenuWith(int tabIndex){
        if( !this.tabMenu.hasActions() ) {
            this.tabMenu.showTab(tabIndex);
            this.tabMenu.addAction(new SequenceAction(Actions.visible(true), Actions.fadeIn(0.1f, Interpolation.pow2)));
        } else {
            // fallback in case of hiccup
            this.tabMenu.setVisible(true);
        }
    }

    public void showTabMenu(){
        if( !this.tabMenu.hasActions() ) {
            this.tabMenu.addAction(new SequenceAction(Actions.visible(true), Actions.fadeIn(0.1f, Interpolation.pow2)));
        } else {
            // fallback in case of hiccup
            this.tabMenu.setVisible(true);
        }
    }

    public void hideTabMenu(){
        if( !this.tabMenu.hasActions() ) {
            this.tabMenu.addAction(new SequenceAction(Actions.fadeOut(0.1f, Interpolation.pow2), Actions.visible(false)));
        } else {
            // fallback in case of hiccup
            this.tabMenu.setVisible(false);
        }
    }

    // TODO Temporary, use multiplexer later
    public void gainFocusPlayer() {
        Gdx.input.setInputProcessor(this.root);
        this.root.setKeyboardFocus(this.player.getPlayerShipGraphics());
    }

    // TODO Temporary, use multiplexer later
    public void gainFocusMenu() {
        Gdx.input.setInputProcessor(this.ui);
        this.ui.setKeyboardFocus(this.starMapTab);
    }

    /**
     * Applies star system changes to the starmapTab only.
     * @param enteredSystem
     */
    private void applySystemValuesToStarMap(StarSystem enteredSystem) {
        this.starMapTab.setGodmode(this.game.getGameSettings().isGodmode());
        this.starMapTab.setTargetActors( this.game.getGameSettings().isGodmode() ? enteredSystem.getTargetableActors() : enteredSystem.getDiscoveredTargets());
        this.starMapTab.getSectorInfo().updateWithSector(this.system, this.currentX, this.currentY);
    }

    /**
     * Invoked when the system changed (such as when entering a new sector)
     * @param enteredSystem
     */
    private void applySystemValuesToUI(StarSystem enteredSystem) {

        this.player.getPlayerShip().setTargetableActors(enteredSystem.getTargetableActors());
        this.player.getPlanetScanner().setSystem(enteredSystem);
        this.applySystemValuesToStarMap(enteredSystem);
        this.sonarMap.init(enteredSystem, this.player.getPlayerShip().getProximitySensor());
    }

    /**
     * Switches star system by removing old system graphics, adding new system graphics and updating the UI
     * accordingly.
     * @param enteredSystem
     */
    private void switchStarSystemTo( StarSystem enteredSystem ) {
        if( this.system != null) {
            this.system.getGraphics().remove(); // TODO dispose of properly
        }
        this.system = enteredSystem;
        this.root.addActor(this.system.getGraphics());
        this.starMapTab.getStarMap().setFollowPlayer();
        this.applySystemValuesToUI(this.system);
        this.game.getSoundManager().playSoundOnce("beep3");

        this.particleRenderer.toFront();
        this.player.getPlayerShipGraphics().toFront();
        if( enteredSystem.getTargetableActors().size() <= 1) {
            this.growlStack.showErrorGrowl("Warning: There is only one [GOLD]Star [RED]in this system!");
        }
    }

    /**
     * Regenerates the currently displayed system.
     */
    public void regenerate() {
        DataPack defaultDataPack = XYZGame.getDataPackManager().findDataPack(XYZGame.DEFAULT_DATAPACK);
        StarSystemSettings solSystem = null;
        if( this.system != null && this.system.getSettings() != null) {
            solSystem = this.XYZGame.getDataPackManager().<StarSystemSettings>findInAllResources("XYZ-core", this.system.getSettings().getName());
        } else {
            StarSystemSettingsRepository repository = defaultDataPack.getRepository(StarSystemSettingsRepository.class);
            solSystem = defaultDataPack.getNumberGenerator().getRandomEntry(repository.all());
        }
        StarSystem starSystem = defaultDataPack.getStarSystemFactory().generateStarSystem(solSystem);
        starSystem.getGraphics().setPosition( this.currentX * this.sectorManager.getSectorBoundsX(),
                                              this.currentY * this.sectorManager.getSectorBoundsY());
        this.switchStarSystemTo(starSystem);
    }

    @Override
    public void show() {
        this.game.getMusicPlayer().playNextSong("song2");
        this.growlStack.showWarningGrowl("Hey there, thanks for playing XYZ!");
    }

    @Override
    public void render(float delta) {
        this.game.getMusicPlayer().update(delta);
        elapsedTime += delta ;

        if( loaded ) {
            int sectorXCoordinate = this.sectorManager.getSectorXCoordinate(player.getPlayerShip().getShipGraphics().getX());
            int sectorYCoordinate = this.sectorManager.getSectorYCoordinate(player.getPlayerShip().getShipGraphics().getY());

            this.galaxyTab.getGalaxyMap().setCurrentCoordinates(sectorXCoordinate, sectorYCoordinate);
            this.sectorManager.checkSector( player.getPlayerShip().getShipGraphics().getX(),
                                            player.getPlayerShip().getShipGraphics().getY() );

            Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            ShipGraphics playerGfx = player.getPlayerShipGraphics();
            Vector2 direction = MathUtil.getDirectionalVector(playerGfx.getCurrentThrust() , (float) Math.toRadians( playerGfx.getRotation() ) );
            this.parallax.setDirection( direction.x, direction.y );
            this.parallax.setSpeed( playerGfx.getCurrentThrust() );

            background.act();
            hud.act();
            ui.act();
            root.act();

            root.getCamera().position.x = playerGfx.getX() + playerGfx.getWidth() / 2f;
            root.getCamera().position.y = playerGfx.getY() + playerGfx.getHeight() / 2f;

            background.draw();
            root.draw();
            hud.draw();
            ui.draw();

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                if( !this.tabMenu.isVisible() ) {
                    this.showTabMenuWith(0);
                    this.gainFocusMenu();
                } else {
                    this.hideTabMenu();
                    this.gainFocusPlayer();
                }
            }

            // Menu Shortcuts
            if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                if( !this.tabMenu.isVisible() ) {
                    this.showTabMenuWith(0);
                    this.gainFocusMenu();
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                if( !this.tabMenu.isVisible() ) {
                    this.showTabMenuWith(1);
                    this.gainFocusMenu();
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
                if( !this.tabMenu.isVisible() ) {
                    this.showTabMenuWith(3);
                    this.gainFocusMenu();
                }
            }

            // Debug
            if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                this.game.getMusicPlayer().playNextSong();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                this.regenerate();
                growlStack.showWarningGrowl("System regenerated!");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
                boolean isDebug = this.root.isDebugAll();
                this.root.setDebugAll(!isDebug);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
                boolean isDebug = this.hud.isDebugAll();
                this.hud.setDebugAll(!isDebug);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
                boolean isDebug = this.ui.isDebugAll();
                this.ui.setDebugAll(!isDebug);
            }

            // Player
            if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT)) {
                this.player.getPlayerShip().getShipGraphics().resetCurrentThrust();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                if( this.planetHUD.getFocusedPlanet() != null) {
                    this.player.getPlanetScanner().scanTarget(this.planetHUD.getFocusedPlanet().getGraphics());
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
                PlanetGraphics planetGfx = (PlanetGraphics) this.player.getPlayerShip().getProximitySensor().getClosestTargetAsActor();
                if( planetGfx != null) {
                    this.planetHUD.setFocusedPlanet(planetGfx.getModel());
                    this.sonarMap.setSelectedActor(planetGfx);
                    this.growlStack.showInfoGrowl("Target acquired: " + planetGfx.getModel().getDisplayName(this.game.getGameSettings().isGodmode()));
                } else {
                    this.planetHUD.setFocusedPlanet(null);
                    this.sonarMap.setSelectedActor(null);
                    this.growlStack.showInfoGrowl("Target set to local star");
                }
                this.starMapTab.getStarMap().setFollowTarget( planetGfx );
            }

            hud.getBatch().begin();
            font.draw(hud.getBatch(), "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 20);
            font.draw(hud.getBatch(), "XYZ " + XYZGame.VERSION, Gdx.graphics.getWidth() - 120, Gdx.graphics.getHeight() - 20);
            hud.getBatch().end();
        }
    }

    /**
     * Updates the a godmode to apply on the UI as well
     * @param godmode
     */
    public void updateGodmode(boolean godmode) {
        this.starMapTab.setGodmode(godmode);
        if( godmode ) {
            this.starMapTab.setTargetActors(system.getTargetableActors());
        } else {
            this.starMapTab.setTargetActors(system.getDiscoveredTargets() );
        }
        this.starMapTab.initTargetList();
    }

    @Override
    public void resize(int width, int height) {
        this.hudLayout.setSize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.font.dispose();
        this.XYZGame.getDataPackManager().unloadAllDataPacks();
    }
}