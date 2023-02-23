package com.n.a.ui.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.n.a.XYZException;
import com.n.a.game.discovery.ProximitySensor;
import com.n.a.game.space.Planet;
import com.n.a.game.space.StarSystem;
import com.n.a.util.XYZUtil;
import com.n.a.util.PlanetDistanceSorter;
import com.n.a.gfx.PlanetGraphics;
import com.n.a.gfx.ShipGraphics;

import java.util.ArrayList;
import java.util.List;

/**
 * This map only shows objects within a certain radius.
 * It is capable of detecting unknown objects but
 * cannot not display the nature of such objects (e.g.
 * whether it's a planet or asteroid).
 */
public class SonarMap extends Group {

    // SonarMap should operate over ProximitySensor directly <- Is this a good idea? I do have acess to player from here
    // and use OrbitDistances as BO. Then just synchronize the data
    // must be updated when the system changes
    private Skin skin;
    private ShipGraphics player;
    private float refreshRate = 2.5f;
    private float timer = 0;
    private int range = 5000;
    private float scale = 0.02f;
    private float starPointerRotation = 0f;
    private List<HideSprite> icons = new ArrayList<>();

    private Actor selectedActor = null;
    private PlanetGraphics star;
    private Sprite background;
    private Sprite spinner;
    private Sprite starpointer;

    private class HideSprite {
        Sprite sprite = null;
        PlanetGraphics planet;
        float x;
        float y;
        float opacity = 1f;
        boolean hidden = false;

        public HideSprite( PlanetGraphics planet, Sprite sprite) {
            if( planet == null) throw new XYZException(XYZException.ErrorCode.E0004, "Planet cant be null");
            this.sprite = sprite;
            this.planet = planet;
        }
    }

    public SonarMap(Skin skin, ShipGraphics player) {
        this.skin = skin;
        this.player = player;
    }

    /**
     * must be invoked every time a star system is entered
     * @param system
     * @param sensor
     */
    public void init(StarSystem system, ProximitySensor sensor) {
        this.icons.clear();
        this.createIconsFromOrbits(sensor.getTargets());
        this.star = system.getSun().getGraphics();

        Sprite sunSprite = new Sprite(skin.getSprite("sonar_marker_star"));
        this.icons.add( new HideSprite( system.getSun().getGraphics(), sunSprite));
        this.background = new Sprite( skin.getSprite("sonar_bg"));
        this.setSize(200, 200);
        this.background.setSize(this.getWidth(), this.getHeight());
        this.spinner = new Sprite( skin.getSprite("sonar_circle"));
        this.starpointer = new Sprite(skin.getSprite("sonar_circle_inner"));
    }

    private void createIconsFromOrbits(List<PlanetDistanceSorter.Target> orbits) {
        if( orbits.isEmpty() ) return;
        for(PlanetDistanceSorter.Target orbit : orbits ) {
            Sprite sprite = this.skin.getSprite("sonar_marker_planet" );

            this.icons.add(new HideSprite( (PlanetGraphics) orbit.getActor(), sprite));

            // add satellites
            List<Planet> satellites = ((PlanetGraphics) orbit.getActor()).getModel().getSatellites();
            for(Planet satellite : satellites ) {
                String planetType = satellite.getGraphics().getModel().getPlanetType();
                if( !planetType.toLowerCase().contains("rings")) {
                    Sprite sprite2 = this.skin.getSprite("sonar_marker_poi" );
                    this.icons.add(new HideSprite(satellite.getGraphics(), sprite2));
                }
            }
        }
        this.timer = 0f; // reset to invoke an update
    }

    public Actor getSelectedActor() {
        return selectedActor;
    }

    public void setSelectedActor(Actor selectedActor) {
        this.selectedActor = selectedActor;
    }

    public float getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(float refreshRate) {
        this.refreshRate = refreshRate;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public float getScale() {
        return scale;
    }

    @Override
    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public void act(float delta) {
        this.timer = this.timer + delta;
        if( this.timer > this.refreshRate) {
            this.timer = 0f;
            for( HideSprite sprites : this.icons ) {
                PlanetGraphics planetGfx = sprites.planet;
                Vector2 componentPosition = XYZUtil.getWorldPosition(this);
                Vector2 planetPosition = XYZUtil.getWorldPosition(planetGfx);
                Vector2 worldPlayerPosition = XYZUtil.getWorldPosition( this.player );
                sprites.x = ((planetPosition.x - worldPlayerPosition.x ) * this.scale) + componentPosition.x + this.getWidth()/2f - sprites.sprite.getWidth()/2f;
                sprites.y = ((planetPosition.y - worldPlayerPosition.y ) * this.scale) + componentPosition.y + this.getHeight()/2f - sprites.sprite.getHeight()/2f;

                float distance = Vector2.dst(worldPlayerPosition.x, worldPlayerPosition.y, planetPosition.x, planetPosition.y);
                sprites.hidden = distance > this.range;
                sprites.opacity = 1 - ( Math.min( distance, this.range) / this.range);
            }
        }
        this.spinner.setPosition(this.getX() + this.getWidth()/2f - this.spinner.getWidth()/2f,
                                 this.getY() + this.getHeight()/2f - this.spinner.getHeight()/2f );
        this.starpointer.setPosition(this.getX() + this.getWidth()/2f - this.starpointer.getWidth()/2f,
                    this.getY() + this.getHeight()/2f - this.starpointer.getHeight()/2f );

        this.spinner.setRotation( this.player.getRotation() - 90 );
        this.updateStarPointerRotation();
    }

    /**
     * Rotate the pointer to a selected target.
     * If no selected Actor was defined, it
     * points towards the star in the current star system.
     */
    private void updateStarPointerRotation() {
        Vector2 playerPos = XYZUtil.getWorldPosition( this.player );
        Vector2 selectedPos = XYZUtil.getWorldPosition( this.selectedActor != null ? this.selectedActor : this.star);
        Vector2 t1 = playerPos.sub(selectedPos);
        float angle = t1.angleDeg();
        this.starPointerRotation =  angle - 270 ;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        this.background.setPosition(this.getX(), this.getY());
        this.background.draw(batch);
        for (HideSprite sprites : icons) {
            if( !sprites.hidden ) {
                sprites.opacity = sprites.opacity * 0.97f;

                if( sprites.planet.getModel().hasDiscoveredTraits() ) {
                    sprites.sprite.setColor( Color.YELLOW );
                } else {
                    sprites.sprite.setColor( Color.GREEN );
                }
                sprites.sprite.setPosition(sprites.x, sprites.y);
                sprites.sprite.draw(batch, sprites.opacity);
            }
        }
        this.spinner.draw(batch);

        this.starpointer.setRotation( this.starPointerRotation );
        this.starpointer.draw(batch);
        batch.setColor(Color.WHITE);
    }
}
