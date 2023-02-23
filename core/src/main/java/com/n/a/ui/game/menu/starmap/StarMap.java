package com.n.a.ui.game.menu.starmap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.n.a.game.space.Planet;
import com.n.a.util.XYZUtil;
import com.n.a.util.StandardFormats;
import com.n.a.game.space.EllipsoidOrbit;
import com.n.a.gfx.PlanetGraphics;
import com.n.a.gfx.ShipGraphics;
import com.n.a.ui.commons.ButtonClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * This map is renders planetary objects and other objects
 * of interest as simplified icon onto a down-scaled map. Renders
 * icons on screen and also orbits of planets, if available.
 */
public class StarMap extends Group {
    // TODO should be located inside of a scroll pane <- OOOH I might be able to drag better if I do that
    // DONE Set focused actor on this!!
    // TODO bake orbits to render to an image for performance boost
    private static Logger logger = Logger.getLogger(StarMap.class.getCanonicalName());
    private float refreshRate = 0.2f;
    private float timer = 0f;

    // scrolling/following mechanism
    /** The followed actor in world space. */
    private Actor followedTarget;
    /** Focal point in local coordinates of this component */
    private float focusx = 0f;
    /** Focal point in local coordinates of this component */
    private float focusy = 0;

    // dragging functionality
    private boolean dragging = false;
    private float dragStartx = 0f;
    private float dragStarty = 0;
    private float dragDeltax = 0;
    private float dragDeltay = 0;

    private final float maxScale = 0.2f;
    private final float minScale = 0.005f;
    private float initialScale = 0.01f;
    private float scale = initialScale;
    private float scaleDelta = initialScale / 10f;

    private Skin skin;
    private String zoomFormat = "%.3f";
    private BitmapFont font;
    private Drawable background;
    private Drawable backgroundFrame;
    private PlayerIcon playerIcon;
    private ButtonGroup planetIconGroup = new ButtonGroup();
    private ArrayList<Button> planetIcons = new ArrayList<>();
    /** Detects changes on selected planets */
    private ChangeListener planetIconListener;
    /** Triggers sounds played on planet buttons */
    private ButtonClickListener planetButtonSounds;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private boolean followPlayer = true;

    private class PlayerIcon extends Actor {
        private float time = 0;
        private Animation<TextureRegion> animated;
        private ShipGraphics player;

        public PlayerIcon( Skin skin, ShipGraphics player) {
            Array<TextureRegion> playerMarker = skin.getRegions("starmap_marker_self");
            this.animated = new Animation(0.5f, playerMarker, Animation.PlayMode.LOOP);
            this.player = player;
            this.setSize(24,24);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            this.time += delta;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            TextureRegion keyFrame = this.animated.getKeyFrame(this.time);

            Color oldcolor = batch.getColor();
            Color color = Color.CYAN;
            batch.setColor(color.r, color.g, color.b, parentAlpha);
            batch.draw(keyFrame, this.getX(), this.getY());
            batch.setColor(oldcolor);
        }
    }

    public StarMap( Skin skin) {
        final StarMap map = this; // Hack
        this.setSize(500, 500);
        this.skin = skin;
        this.font = skin.getFont("default");
        this.background = new TiledDrawable( (TextureRegionDrawable) skin.getDrawable("pattern_grid"));
        this.backgroundFrame = new NinePatchDrawable( skin.getPatch("panel_1"));
        this.planetIconGroup.setMaxCheckCount(1);
        this.planetIconGroup.setMinCheckCount(0);
        this.planetIconGroup.setUncheckLast(true);
        // enable culling so icons are only drawn within this area
        this.setCullingArea(new Rectangle(this.getX() + 15, this.getY() + 15, this.getWidth() - 30,this.getHeight() - 30));

        this.addListener( new DragListener(){

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                super.dragStart(event, x, y, pointer);
                dragStartx = x + focusx;
                dragStarty = y + focusy;
                dragging = true;
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                super.drag(event, x, y, pointer);
                if( pointer == 0) {
                    if (followedTarget != null) {
                        setFollowTarget(null);
                    } else {
                        // this kinda works but needs to be additive to focus somehow
                        focusx = dragStartx - x;
                        focusy = dragStarty - y;
                    }
                    refreshPositions();
                }
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                super.dragStop(event, x, y, pointer);
                dragStartx = 0;
                dragStarty = 0;
                dragging = false;
                dragDeltax = 0;
                dragDeltay = 0;
            }
        } );

        this.addListener( new ClickListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                getStage().setScrollFocus(map);
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                getStage().setScrollFocus(map);
                super.clicked(event, x, y);
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                if( amountY < 0 ) {
                    increaseScale(3f);
                    return true;
                } else {
                    decreaseScale(3f);
                    return true;
                }
            }
        } );
    }

    /**
     * Sets the focus to follow the player.
     */
    public void setFollowPlayer() {
        this.followedTarget = this.playerIcon.player;
        this.refreshPositions();
    }

    /**
     * Sets the focus to follow any actor.
     * @param actor if the actor is null, the focus stays at a set position.
     */
    public void setFollowTarget(Actor actor) {
        this.followedTarget = actor;
        this.refreshPositions();
    }

    /**
     * Sets the listener that triggers sounds on the planet icons.
     * @param planetButtonSounds
     */
    public void setPlanetButtonSounds(ButtonClickListener planetButtonSounds) {
        if( planetButtonSounds == null) return;
        this.planetButtonSounds = planetButtonSounds;
        for( Button button : planetIcons) {
            button.addListener(planetButtonSounds);
        }
    }

    /**
     * Sets the icon listener that is triggered once an icon is being clicked on.
     * @param planetIconListener
     */
    public void setPlanetIconListener(ChangeListener planetIconListener) {
        if( planetIconListener == null) return;
        this.planetIconListener = planetIconListener;
        for( Button button : planetIcons) {
            button.addListener(planetIconListener);
        }
    }

    /**
     * Increases the scale. Causes a zoom out
     */
    public void increaseScale(float speed) {
        this.scale = MathUtils.clamp( this.scale + (this.scaleDelta * speed), minScale, maxScale);
        this.refreshPositions();
    }

    /**
     * Decreases the scale. Causes a zoom in
     */
    public void decreaseScale(float speed) {
        this.scale = MathUtils.clamp( this.scale - (this.scaleDelta * speed), minScale, maxScale);
        this.refreshPositions();
    }

    /**
     * Resets the scale to its default value
     */
    public void resetScale() {
        this.scale = this.initialScale;
        this.refreshPositions();
    }

    /**
     * Updates the player icon. Necessary on initialization
     * and also when the ship graphics object changes (e.g. on
     * saving or loading)
     * @param player
     */
    public void updatePlayerIcon(ShipGraphics player) {
        this.playerIcon = new PlayerIcon(skin, player);
        this.addActor(this.playerIcon);
    }

    /**
     * Updates the starmap with this orbit. The list of orbits will be used to poll
     * planet positions in order to draw them. The list itself will not be modified.
     * @param target
     */
    public void updateWithTargets(List<Actor> target) {
        for( Button icon : this.planetIcons) {
            if( this.planetButtonSounds != null) {
                icon.removeListener(this.planetButtonSounds);
            }
            icon.remove();
        }
        this.planetIcons.clear();

        for( Actor actor : target) {
            if( actor instanceof PlanetGraphics) {
                Planet model = ((PlanetGraphics)actor).getModel();
                Button planetButton = null;
                if( model.hasDiscoveredTraits() ) {
                    Drawable planetType = this.skin.getDrawable(model.getArchetypeSettings().getIcon());
                    Drawable planetTypePurple = this.skin.getDrawable(model.getArchetypeSettings().getIcon() + "_purple");
                    planetButton = new Button(planetType, planetTypePurple,planetTypePurple);
                } else {
                    Drawable planetTypeUnknown = this.skin.getDrawable("planetType_unknown");
                    Drawable planetTypeUnknownPurple = this.skin.getDrawable("planetType_unknown_purple");
                    planetButton = new Button(planetTypeUnknown, planetTypeUnknownPurple, planetTypeUnknownPurple
                    );
                }
                planetButton.setProgrammaticChangeEvents(true);
                planetButton.setUserObject(model);
                this.planetIconGroup.add(planetButton);
                this.planetIcons.add(planetButton);
                this.addActor(planetButton);
            }
        }
        if( this.planetIconListener != null) {
            this.setPlanetIconListener(this.planetIconListener);
        }
        if( this.planetButtonSounds != null) {
            this.setPlanetButtonSounds(this.planetButtonSounds);
        }
        this.playerIcon.toFront();
        this.refreshPositions();
    }

    /**
     * Refreshes the positions of the planet icons
     */
    private void refreshPositions() {
        this.playerIcon.setPosition( this.getWidth() / 2f + (this.playerIcon.player.getX() * scale) - focusx,
                                     this.getHeight() / 2f +(this.playerIcon.player.getY() * scale) - focusy, Align.center
        );
        for (Button icon : this.planetIcons) {
            Planet planet = (Planet)  icon.getUserObject();
            Vector2 worldPosition = XYZUtil.getWorldPosition(planet.getGraphics());
            icon.setPosition(
                    this.getWidth() / 2f + (worldPosition.x * scale) - this.focusx,
                    this.getHeight() /2f + (worldPosition.y * scale) - this.focusy, Align.center);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if( this.followedTarget !=  null ) {
            Vector2 pos = XYZUtil.getWorldPosition(this.followedTarget);
            this.focusx = pos.x * scale + this.dragDeltax;
            this.focusy = pos.y * scale + this.dragDeltay;
        } else {
            this.focusx = this.focusx + this.dragDeltax;
            this.focusy = this.focusy + this.dragDeltay;
        }
        this.timer = this.timer + delta;
        if( this.timer > this.refreshRate ) {
            this.timer = 0;
            this.refreshPositions();
        }
    }

    public void renderOrbits(List<PlanetGraphics> targets, Batch batch, float parentAlpha) {
        Vector2 componentPos = XYZUtil.getWorldPosition(this);
        this.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.setColor(1, 1, 1, parentAlpha);
        for( PlanetGraphics target : targets) {
            if( target.getOrbit() instanceof EllipsoidOrbit) {
                EllipsoidOrbit ellipsoidOrbit = (EllipsoidOrbit) target.getOrbit();
                Vector2 worldPos = XYZUtil.getWorldPosition(target.getParent());
                this.shapeRenderer.ellipse(
                        componentPos.x + getWidth()/2f + ((worldPos.x - ellipsoidOrbit.getWidth() )  * scale)  - focusx,
                        componentPos.y + getHeight()/2f +((worldPos.y - ellipsoidOrbit.getHeight() ) * scale) - focusy,
                        ellipsoidOrbit.getWidth() * 2f * scale,
                        ellipsoidOrbit.getHeight() * 2f * scale
                );
            }
        }
        this.shapeRenderer.end();
        //this.shapeRenderer.flush();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.backgroundFrame.draw(batch, this.getX(), this.getY(), this.getWidth(),this.getHeight());
        this.background.draw(batch, this.getX() + 15, this.getY() + 15, this.getWidth() - 30,this.getHeight() - 30);
        super.draw(batch, parentAlpha);
        this.font.draw(batch, "x " + StandardFormats.FACTOR.format(this.scale), this.getX() + 30, this.getY() + 30);
    }
}
