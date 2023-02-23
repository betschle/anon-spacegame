package com.n.a.gfx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Actor-based Sprite graphics that syncs the
 * underlying sprite with the actors' rotation and position
 */
public class SpriteGraphics extends Actor {

    // TODO What can I replace with this class?
    protected Sprite sprite;

    public SpriteGraphics( Sprite sprite ) {
        this.sprite = sprite;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.sprite.setPosition(this.getX(), this.getY());
        this.sprite.setRotation(this.getRotation());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        this.sprite.draw(batch);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
