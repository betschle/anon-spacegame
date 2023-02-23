package com.n.a.ui.commons;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.n.a.game.EntityID;

public class AbstractPanel extends Table {

    private EntityID id; // TODO implement
    protected Skin skin;
    protected Label heading; // TODO Implement
    protected Drawable background; // TODO Implement

    protected AbstractPanel ( Skin skin) {
        this.skin = skin;
        this.background = new NinePatchDrawable( skin.getPatch("panel_inset_1") );
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.background.draw(batch, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        super.draw(batch, parentAlpha);
    }
}
