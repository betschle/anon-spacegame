package com.n.a.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.n.a.XYZException;
import com.n.a.util.sequences.NumberGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Particle-pased Parallax scroller.
 *
 * Size must be set in order for this to work!
 *
 * @author B5cully
 */
public class Parallax extends Group {

    // TODO add distribution/frequency of certain texture regions, such that I can also spawn in tiny galaxy textures in the background layers
    private float dirx;
    private float diry;
    private float speed;

    /** Star textures to shuffle from */
    private TextureRegion[] stars = new TextureRegion[4]; // textures to be used for all layers
    /** Star Colors to shuffle from */
    private Color[] colors;

    private List<StarLayer> starLayer = new ArrayList<>();

    private NumberGenerator numgen;

    /**
     * Utility class for a single Star Point
     */
    private class StarPoint {
        private Vector2 pos = new Vector2();
        private int texture;
        private int color;

        public StarPoint(float posX, float posY, int texture, int color) {
            this.pos.set(posX, posY);
            this.texture = texture;
            this.color = color;
        }

        public void draw(Batch batch, float transparency) {
            Color color =  colors[this.color];
            batch.setColor( color.r, color.g, color.b, transparency);
            batch.draw(stars[ texture ], this.pos.x, this.pos.y );
            batch.setColor( Color.WHITE );
        }

        /**
         * Checks if this star's position needs to be reset.
         */
        public void checkReset() {
            if( this.pos.x > getWidth() ) {
                this.pos.x = 0;
            }
            if( this.pos.x < 0 ) {
                this.pos.x = getWidth();
            }

            if( this.pos.y > getHeight() ) {
                this.pos.y = 0;
            }
            if( this.pos.y < 0 ) {
                this.pos.y = getHeight();
            }
        }

        public void updatePosition(float delta, float layerSpeed) {
            this.pos.set( this.pos.x - dirx * delta * layerSpeed,
                          this.pos.y - diry * delta * layerSpeed );
        }
    }

    /**
     * A Star Layer helper class
     */
    private class StarLayer {

        protected StarPoint[] points;
        protected float layerSpeed;
        protected float transparency = 1f;

        public StarLayer( int starcount, float speed, float transparency) {
            this.points = new StarPoint[starcount];
            this.layerSpeed = speed;
            this.transparency = transparency;
        }

        public void draw(Batch batch) {
            for( StarPoint point : points){
                point.draw(batch, transparency);
            }
        }

        /**
         * Checks if a star's position needs to be reset.
         * @param delta
         */
        public void checkPointReset(float delta) {
            for( StarPoint point : this.points){
                point.updatePosition(delta, this.layerSpeed);
                point.checkReset();
            }
        }

        public void init(float width, float height) {
            for( int i =0; i < this.points.length; i++ ){
                this.points[i] = new StarPoint(
                                    numgen.getRandomFloat( width, 0),
                                    numgen.getRandomFloat( height, 0),
                                    numgen.getRandomInteger( stars.length, 0),
                                    numgen.getRandomInteger( colors.length, 0)
                );
            }
        }
    }

    public Parallax( int layerAmount, TextureRegion[] regions, NumberGenerator generator){
        this.stars = regions;
        this.numgen = generator;
        this.init(layerAmount);
    }

    public Parallax( int layerAmount, NumberGenerator generator, TextureAtlas atlas ){
        this.stars[0] = atlas.findRegion("prt/star", 1);
        this.stars[1] = atlas.findRegion("prt/star", 2);
        this.stars[2] = atlas.findRegion("prt/star", 3);
        this.stars[3] = atlas.findRegion("prt/star", 4);
        this.numgen = generator;
        this.init(layerAmount);
    }

    private void init(int layerAmount) {
        this.colors = new Color[] {
                Color.WHITE,
                Color.WHITE,
                Color.WHITE,
                Color.WHITE,
                Color.WHITE,
                Color.WHITE,
                Color.WHITE,
                Color.valueOf("87acff"), // blue
                Color.valueOf("ffc3c3"), // red
                Color.valueOf("ffc3c3"), // red
                Color.valueOf("ffeec3"), // yellow
                Color.valueOf("ffeec3"), // yellow
                Color.valueOf("ffc3f6") // pink
        };

        for( int i = 0; i < layerAmount; i++) {
            this.starLayer.add( new StarLayer(150, (i+1) * 5f,( (i+1f)/layerAmount) ));
        }
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        for( StarLayer starLayer : this.starLayer) {
            starLayer.init(width, height);
        }
    }

    public void setDirection(float dirx, float diry) {
        this.dirx = dirx;
        this.diry = diry;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if( this.starLayer.isEmpty() ) throw new XYZException(XYZException.ErrorCode.E0001);
        for( StarLayer starLayer : this.starLayer) {
            starLayer.checkPointReset(delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if( this.starLayer.isEmpty() ) throw new XYZException(XYZException.ErrorCode.E0001);
        for( StarLayer starLayer : this.starLayer) {
            starLayer.draw(batch);
        }
        batch.setColor(Color.WHITE);
    }

}
