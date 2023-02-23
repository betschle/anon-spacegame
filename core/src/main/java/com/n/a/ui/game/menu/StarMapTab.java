package com.n.a.ui.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.n.a.XYZException;
import com.n.a.game.space.Planet;
import com.n.a.ui.ComponentFactory;
import com.n.a.util.XYZUtil;
import com.n.a.util.PlanetDistanceSorter;
import com.n.a.util.StandardFormats;
import com.n.a.gfx.PlanetGraphics;
import com.n.a.gfx.ShipGraphics;
import com.n.a.ui.commons.ButtonClickListener;
import com.n.a.ui.commons.Separator;
import com.n.a.ui.commons.TargetItem;
import com.n.a.ui.game.menu.starmap.SectorInfo;
import com.n.a.ui.game.menu.starmap.StarMap;
import com.n.a.ui.game.planet.PlanetPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Star Map Screen displays stars, orbits in a down scaled map and
 * a list of discovered planets and some info about them.
 */
public class StarMapTab extends Table {

    // TODO IMPORTANT: cut this class into smaller pieces, its getting rather big.
    private static Logger logger = Logger.getLogger(StarMapTab.class.getCanonicalName());;

    private List<Actor> targetActors = new ArrayList<>();
    /** A list of currently displayed planets. This list is assumed to be filtered by a sensor or similar. */
    private List<PlanetGraphics> targetPlanetGraphics = new ArrayList<>();
    private Planet selectedPlanet = null;
    // TODO this should be outsourced to Player BO and then set from the outside
    private PlanetDistanceSorter sorter  = new PlanetDistanceSorter();

    private Batch shapeBatch;
    private ShipGraphics playerGraphics;

    // Outsource to StarMap
    private Button zoomInButton;
    private Button zoomOutButton;
    private Button resetZoomButton;

    private Button resetFocusButton;
    private Button focusSelectedPlanetButton;
    //

    private StarMap starMap;
    private SectorInfo sectorInfo;
    private PlanetPanel planetPanel;
    /** Group for the list of targets. Outer Scroll Group */
    private Table iconGroup;
    /** Inner scroll Group */
    private Table iconScrollGroup = new Table();
    private final ScrollPane iconScrollPane;
    /** Group for map and map control buttons */
    private Table mapGroup = new Table();
    /** Group for map control buttons */
    private Table mapControlGroup = new Table();
    /** Group of buttons to control map focus */
    private Table focusGroup = new Table();
    private Label titleLabel;
    private List<TargetItem> targetButtons = new ArrayList<>();

    private ChangeListener zoomInListener;
    private ChangeListener zoomOutListener;
    private ChangeListener resetZoomListener;
    private ChangeListener planetButtonListener;
    private ChangeListener resetFocusListener;
    private ChangeListener focusSelectedPlanetListener;
    /** Triggers sounds played on zoom buttons */
    private ButtonClickListener zoomButtonSounds;

    private boolean godmode;
    private float time = 0;
    private float distanceRefreshRate = 0.2f;
    private String distanceFormat = "%.1f";

    // TODO make starmap directly operate on a sensor object?

    public StarMapTab(Skin skin, ComponentFactory factory) {
        super(skin);
        this.shapeBatch = new SpriteBatch(10);

        this.zoomInListener = new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if( zoomInButton.isPressed() ) {
                    starMap.increaseScale(1f);
                }
                event.stop();
            }
        };
        this.zoomOutListener = new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if( zoomOutButton.isPressed() ) {
                    starMap.decreaseScale(1f);
                }
                event.stop();
            }
        };
        this.resetZoomListener = new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if( resetZoomButton.isPressed() ) {
                    starMap.resetScale();
                }
                event.stop();
            }
        };

        this.planetButtonListener = new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {

                select( (Planet) actor.getUserObject() );
                event.stop();
            }
        };


        this.focusSelectedPlanetListener = new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {

                if( selectedPlanet != null) {
                    starMap.setFollowTarget(selectedPlanet.getGraphics());
                }
            }
        };

        this.resetFocusListener = new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                starMap.setFollowPlayer();
            }
        };

        //
        this.titleLabel = new Label("Sector Map", skin, "title-no-bg");
        this.titleLabel.setAlignment(Align.center, Align.center);
        //

        this.planetPanel = factory.getPlanetPanel();

        this.starMap = new StarMap(skin);
        this.starMap.setPlanetIconListener( this.planetButtonListener );

        this.zoomInButton = new Button(skin, "zoomIn");
        this.zoomInButton.setSize(32, 32);
        this.zoomInButton.addListener(this.zoomInListener);
        this.zoomInButton.addListener( new TextTooltip("Zoom in", getSkin()));
        this.zoomInButton.setDisabled(false);

        this.resetZoomButton = new Button(skin, "plus");
        this.resetZoomButton.setSize(32, 32);
        this.resetZoomButton.addListener( this.resetZoomListener );
        this.resetZoomButton.addListener( new TextTooltip("Reset zoom", getSkin()));
        this.resetZoomButton.setDisabled(false);

        this.zoomOutButton = new Button(skin, "zoomOut");
        this.zoomOutButton.setSize(32, 32);
        this.zoomOutButton.addListener( this.zoomOutListener );
        this.zoomOutButton.addListener( new TextTooltip("Zoom out", getSkin()));
        this.zoomOutButton.setDisabled(false);

        this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.mapControlGroup.add(this.zoomOutButton).pad(2f);
        this.mapControlGroup.add(this.resetZoomButton).pad(2f);
        this.mapControlGroup.add(this.zoomInButton).pad(2f);

        this.resetFocusButton = new Button(skin, "centerHome");
        this.resetFocusButton.setSize(32, 32);
        this.resetFocusButton.addListener(this.resetFocusListener);
        this.resetFocusButton.addListener( new TextTooltip("Center to Player", getSkin()));

        this.focusSelectedPlanetButton = new Button(skin, "centerPlanet");
        this.focusSelectedPlanetButton.setSize(32, 32);
        this.focusSelectedPlanetButton.addListener(this.focusSelectedPlanetListener);
        this.focusSelectedPlanetButton.addListener( new TextTooltip("Center to selected Planet", getSkin()));

        this.focusGroup = new Table();
        this.focusGroup.add(this.resetFocusButton).pad(2f);
        this.focusGroup.add(this.focusSelectedPlanetButton).pad(2f);
        this.mapControlGroup.add().minSize(250, 20);
        this.mapControlGroup.add(this.focusGroup).padRight(20f).right();

        this.sectorInfo = new SectorInfo(skin);
        this.sectorInfo.construct();
        this.sectorInfo.align(Align.topLeft);

        this.mapGroup.add(this.mapControlGroup).pad(2f).fill();
        this.mapGroup.row();
        this.mapGroup.add(this.starMap);

        //
        this.iconScrollGroup = new Table(getSkin());
        this.iconScrollGroup.align(Align.top);
        this.iconScrollPane = new ScrollPane(this.iconScrollGroup, skin, "default-vertical");
        this.iconScrollPane.setFlickScroll(false);
        this.iconGroup = new Table(getSkin());
        this.iconGroup.add(iconScrollPane).height(400).top();
        //

        add(this.titleLabel).colspan(1).fill().top().pad(10);
        add(this.sectorInfo).colspan(2).fill().pad(10);;
        row();
        add( new Separator(this.getSkin(),  "panel_inset_brass_1")).minSize(1150, 10)
                .colspan(3)
                .padBottom(10f)
                .fill();
        row();
        add("Targets:", "default-no-bg")
                .padLeft(10).padBottom(10)
                .expand().center();
        add("Map:", "default-no-bg")
                .padLeft(5).padBottom(10)
                .expand().center();
        add("Planet Info:", "default-no-bg")
                .padLeft(5).padBottom(10)
                .expand().center();
        row();
        add(this.iconGroup).top().padLeft(10);;
        add(this.mapGroup).top().padLeft(5);;
        add(this.planetPanel).top().padLeft(5).padRight(10);;

    }
    public ShipGraphics getPlayerGraphics() {
        return playerGraphics;
    }

    public void setPlayerGraphics(ShipGraphics playerGraphics) {
        this.playerGraphics = playerGraphics;
        this.starMap.updatePlayerIcon(playerGraphics);
    }

    public boolean isGodmode() {
        return godmode;
    }

    /**
     * Sets the godmode. If Godmode is on, ALL available targets
     * are displayed on the  map, instead of all discovered targets.
     * @param godmode
     */
    public void setGodmode(boolean godmode) {
        this.godmode = godmode;
        this.planetPanel.setGodmode(godmode);
    }

    public void updateLocalization() {
        this.titleLabel.setText("Sector Map");
    }

    public StarMap getStarMap() {
        return starMap;
    }

    public PlanetPanel getPlanetPanel() {
        return planetPanel;
    }

    public List<Actor> getTargetActors() {
        return targetActors;
    }

    public SectorInfo getSectorInfo() {
        return sectorInfo;
    }

    /**
     * Selects a planet and also makes sure the right button is checked.
     * @param planetToSelect
     */
    private void select(Planet planetToSelect) {
        // user object is a planet
        for( TargetItem button : this.targetButtons) {
            Planet otherPlanet = (Planet) button.getUserObject();
            if( Objects.equals(otherPlanet.getGraphics().getId(), planetToSelect.getGraphics().getId()) ){
                button.setSelected(true);
                this.selectedPlanet = planetToSelect;
                this.planetPanel.setPlanet( this.selectedPlanet );
                this.iconScrollPane.scrollTo( button.getX(), button.getY(), button.getWidth(), button.getHeight());
            } else {
                button.setSelected(false);
            }
        }
    }

    /**
     * Sets the Sounds to use
     * @param zoomButtonSounds
     */
    public void setZoomButtonSounds(ButtonClickListener zoomButtonSounds) {
        if( zoomButtonSounds == null) return;
        if( this.zoomButtonSounds != null) {
            this.zoomInButton.removeListener(this.zoomButtonSounds);
            this.zoomOutButton.removeListener(this.zoomButtonSounds);
            this.resetZoomButton.removeListener(this.zoomButtonSounds);
            this.focusSelectedPlanetButton.removeListener(this.zoomButtonSounds);
            this.resetFocusButton.removeListener(this.zoomButtonSounds);
        }

        this.zoomButtonSounds = zoomButtonSounds;

        this.zoomInButton.addListener(this.zoomButtonSounds);
        this.zoomOutButton.addListener(this.zoomButtonSounds);
        this.resetZoomButton.addListener(this.zoomButtonSounds);
        this.focusSelectedPlanetButton.addListener(this.zoomButtonSounds);
        this.resetFocusButton.addListener(this.zoomButtonSounds);
    }

    /**
     * Selects the target actors that are displayed in this map.
     * @param targets
     */
    public void setTargetActors(List<Actor> targets) {
        this.targetActors = targets;
        this.targetPlanetGraphics = XYZUtil.actorToPlanetGraphicsList(targets);
        this.starMap.updateWithTargets(targets);
        this.planetPanel.setPlanet(null);
        this.sorter.setActors(targets);
        this.initTargetList();
    }

    /** To be called if the target list changed, e.g. if a planet was discovered. */
    public void initTargetList() {
        for( TargetItem buttons : this.targetButtons) {
            buttons.remove();
        }

        // update sorter and starmap with current targets
        this.sorter.setActors(this.targetActors);
        this.sorter.calculate( this.playerGraphics.getX(), this.playerGraphics.getY() );
        this.sorter.sort();
        this.starMap.updateWithTargets(this.targetActors);

        this.targetButtons.clear();
        int i = 0;
        for( Actor actor : this.targetActors) {
            if( actor instanceof PlanetGraphics) {
                //
                Planet planet = ((PlanetGraphics)actor).getModel();
                String planetName = planet.getDisplayName(this.isGodmode());

                TargetItem targetItem = new TargetItem(this.getSkin());
                targetItem.construct();
                targetItem.setAsPlanetGraphics( planetName,
                        StandardFormats.DISTANCE.format(1000f ) + " km",
                        planet.hasDiscoveredTraits() || this.godmode ?  planet.getArchetypeSettings().getIcon() : "planetType_unknown");
                targetItem.setUserObject( planet );
                //

                this.targetButtons.add(targetItem);
                this.iconScrollGroup.add(targetItem).width(200).top().fill().row();
                i++;
            }
        }
        this.updateTargetList();
    }

    /**
     * Synchronizes the sorted target list with what is being displayed
     * in the text buttons.
     */
    public void updateTargetList() {
        List<PlanetDistanceSorter.Target> orbitDistances = this.sorter.getTargets();
        // assert that orbit distance list is the same length as text button list
        if( orbitDistances.size() != this.targetButtons.size() ) throw new XYZException(XYZException.ErrorCode.E0003,
                "size of button list must be the same as size of list for calculated distances! " + orbitDistances.size() + " / " + this.targetButtons.size());

        for(int i =0; i < orbitDistances.size(); i++) {
            PlanetDistanceSorter.Target distance = orbitDistances.get(i);
            Planet planet = ((PlanetGraphics) distance.getActor()).getModel();
            String planetName = planet.getDisplayName(this.godmode);

            TargetItem targetItem = this.targetButtons.get(i);
            targetItem.setAsPlanetGraphics(
                            planetName,
                    StandardFormats.DISTANCE.format(distance.getDistance() ) + " km",
                            planet.hasDiscoveredTraits() || this.godmode ?  planet.getArchetypeSettings().getIcon() : "planetType_unknown");
            targetItem.setUserObject( planet );

            if( this.selectedPlanet != null) {
                if (Objects.equals(this.selectedPlanet.getGraphics().getId(), planet.getGraphics().getId())) {
                    targetItem.setSelected(true);
                } else {
                    targetItem.setSelected(false);
                }
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.time += delta;
        if( this.time > this.distanceRefreshRate) {
            this.sorter.calculate( this.playerGraphics.getX(), this.playerGraphics.getY() );
            this.sorter.sort();
            this.updateTargetList();
            this.time = 0;
        }
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        super.drawChildren(batch, parentAlpha);


        Color color = batch.getColor();
        batch.end();
        this.starMap.renderOrbits(this.targetPlanetGraphics , this.shapeBatch, parentAlpha);
        batch.begin();
        batch.setColor(color);

    }
}
