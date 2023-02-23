package com.n.a.ui.commons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * A simple visual separator that can also be used as spacer.
 */
public class Separator extends Actor {

    private NinePatchDrawable drawable;

    public Separator(Skin skin, String ninepatchName) {
        this.drawable = new NinePatchDrawable( skin.getPatch(ninepatchName) );
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor( 1,1, 1, parentAlpha);
        this.drawable.draw(batch, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
