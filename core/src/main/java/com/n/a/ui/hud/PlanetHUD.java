package com.n.a.ui.hud;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.n.a.XYZException;
import com.n.a.game.space.Planet;
import com.n.a.util.StandardFormats;
import com.n.a.game.planet.PlanetTrait;

import java.util.ArrayList;
import java.util.List;

/**
 * Deals with selecting targeted planets
 */
public class PlanetHUD extends Group {

    private Skin skin;
    private ProgressBar bar;
    private BitmapFont font;
    private Sprite background;
    private List<Sprite> icons = new ArrayList<>();

    private String percentageFormat = "%6.1f";
    private Planet focusedPlanet;
    private String percentage = "0";
    private String planetName = "?";
    private Sprite targetor;

    public PlanetHUD(Skin skin) {
        this.skin = skin;
        this.background = new Sprite( skin.getRegion("planet_HUD"));
        this.targetor = new Sprite( skin.getRegion("planet_selection"));
        this.font = skin.get("default", BitmapFont.class);
        this.bar = new ProgressBar(0, 1, 0.001f, false, skin, "default");
        this.bar.setAnimateDuration(0.6f);
        this.bar.setAnimateInterpolation(Interpolation.pow2);
        this.bar.setSize(228, 14);
        this.bar.setPosition(9,9);
        this.bar.setValue(0f);
        addActor(this.bar);
    }

    /**
     * Gets the focused planet.
     * @return
     */
    public Planet getFocusedPlanet() {
        return focusedPlanet;
    }

    public void setFocusedPlanet(Planet focusedPlanet) {
        if( this.focusedPlanet != null) {
            this.focusedPlanet.getGraphics().removeActor(this);
        }
        if( focusedPlanet != null) {
            focusedPlanet.getGraphics().addActor(this);
        }
        this.focusedPlanet = focusedPlanet;
        this.update();

    }

    /**
     * Updates the values stored in the HUD
     */
    public void update() {
        if( this.focusedPlanet != null) {
            this.percentage = StandardFormats.PERCENT.format(this.focusedPlanet.getPlanetStats().getPercentageDiscovered());
            boolean isDiscovered = this.focusedPlanet.hasDiscoveredTraits();
            this.planetName = isDiscovered ? this.focusedPlanet.getName() : "?";
            this.targetor.setSize(
                    this.focusedPlanet.getGraphics().getWidth() * this.focusedPlanet.getGraphics().getPlanetScale(),
                    this.focusedPlanet.getGraphics().getHeight() * this.focusedPlanet.getGraphics().getPlanetScale());
            this.targetor.setPosition(
                    -(this.focusedPlanet.getGraphics().getWidth() / 2f * this.focusedPlanet.getGraphics().getPlanetScale()),
                    -(this.focusedPlanet.getGraphics().getHeight() / 2f * this.focusedPlanet.getGraphics().getPlanetScale()));
            initIcons();
        }
    }

    private void initIcons() {
        this.icons.clear();
        if( this.focusedPlanet == null) return;
        List<PlanetTrait> traits = this.focusedPlanet.getPlanetStats().getTraits();
        List<PlanetTrait> discovered = this.focusedPlanet.getPlanetStats().getDiscoveredTraits();
        for( int i =0; i < 4 && i < traits.size(); i++){
            TextureRegion region = null;
            PlanetTrait trait = traits.get(i);
            if( discovered.isEmpty() ) {
                region = skin.getRegion("planetType_unknown");
            } else
            if( discovered.contains(trait) && trait.getIcon() != null) {
                region = skin.getRegion(trait.getIcon());
            } else {
                region = skin.getRegion("planetType_unknown");
            }
            if( region == null) throw new XYZException(XYZException.ErrorCode.E3101, trait.getIcon());

            Sprite sprite = new Sprite(region);
            sprite.setPosition( 17 + i * (11 + sprite.getWidth()), 50);
            this.icons.add( sprite );
        }
        float discoverFactor = discovered.isEmpty() ? 0 : discovered.size() / (float) traits.size();
        this.bar.setValue( discoverFactor );
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if( this.focusedPlanet != null) {
            this.background.draw(batch);
            for( Sprite icon : icons){
                icon.draw(batch);
            }
            this.font.draw(batch, planetName, 15, 37);
            this.font.draw(batch, percentage +" %", 190, 37 );
            this.targetor.draw(batch);
        }
        super.draw(batch, parentAlpha);
    }
}
