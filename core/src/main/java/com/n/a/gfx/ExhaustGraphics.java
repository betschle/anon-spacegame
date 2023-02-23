package com.n.a.gfx;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages 3 State Animation for Jet exhausts
 */
public class ExhaustGraphics extends Group {


    /** The engine points. */
    private List<EnginePoint> engines = new ArrayList<>();
    /** Contains 3 states of engine exhausts for high, medium, low thrust. **/
    public List<Animation<TextureRegion>> engineExhausts = new ArrayList< Animation<TextureRegion> >();

    private float time;

    // for now, self-constructing
    public ExhaustGraphics(TextureAtlas atlas) {
        engineExhausts.add( createExhaustAnimation( atlas.findRegion("ship/engine100-1"), atlas.findRegion("ship/engine100-2")) );
        engineExhausts.add( createExhaustAnimation( atlas.findRegion("ship/engine60-1"), atlas.findRegion("ship/engine60-2")) );
        engineExhausts.add( createExhaustAnimation( atlas.findRegion("ship/engine30-1"), atlas.findRegion("ship/engine30-2")) );

        this.addEngine(0f, -5f);
        this.addEngine(0f, 5f);
        this.addEngine(-3f, 0f);
        this.setSize(3f,3f);
        this.setOrigin(Align.center);
        setThrust(0);
    }

    private Animation createExhaustAnimation(TextureRegion exhaust1, TextureRegion exhaust2) {
        Array keyframes = new Array();
        keyframes.add(exhaust1);
        keyframes.add(exhaust2);
        Animation exhaust = new Animation(0.3f, keyframes);
        exhaust.setPlayMode(Animation.PlayMode.LOOP);
        return exhaust;
    }

    public List<EnginePoint> getEngines() {
        return engines;
    }

    public Animation<TextureRegion> getParticleAnimation() {
        return engines.get(0).trailEmitter.getAnimation();
    }

    public void setParticleAnimation(String particleRegion, Animation<TextureRegion> animation) {
        for( EnginePoint anchor : engines) {
            anchor.trailEmitter.setAnimation(particleRegion, animation);
        }
    }

    public void setThrust(float thrust) {
        for( EnginePoint anchor : engines) {
            anchor.setThrust(thrust);
        }
    }

    public void setThrust(int anchorIndex, float thrust) {
        engines.get(anchorIndex).setThrust(thrust);
    }

    /**
     * Adds an anchor point that decides where and how many animations are drawn
     * @param offsetx
     * @param offsety
     */
    public void addEngine(float offsetx, float offsety) {
        EnginePoint enginePoint = new EnginePoint();
        enginePoint.setSize(5, 5);
        enginePoint.setOrigin(Align.center);
        enginePoint.setPosition(offsetx, offsety-1);
        engines.add( enginePoint );
        this.addActor(enginePoint);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta;
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        super.drawChildren(batch, parentAlpha);
        for( EnginePoint anchor : engines) {
            Animation<TextureRegion> currentDrawn = engineExhausts.get(anchor.getCurrentIndex());
            TextureRegion keyFrame = currentDrawn.getKeyFrame(time);
            batch.draw( keyFrame,
                    this.getX() + anchor.getX() - keyFrame.getRegionWidth()+2,
                    this.getY() + anchor.getY() - keyFrame.getRegionHeight(),
                    this.getOriginX(), this.getOriginY(),
                    keyFrame.getRegionWidth(), keyFrame.getRegionHeight(),
                    this.getScaleX(), this.getScaleY(), this.getRotation());

        }
    }
}
