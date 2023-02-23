package com.n.a.ui.commons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * A group of Growls that stack.
 */
public class GrowlStack extends Table {

    private Growl[] growls;

    public GrowlStack(Skin skin, int size) {
        this.growls = new Growl[size];
        for( int i =0; i < this.growls.length; i++) {
            this.growls[i] = new Growl(skin);
            this.add(this.growls[i]).padBottom(2).padTop(2).left().row();
        }
        this.pack();
    }

    /**
     * Gets the next usable growl that is in finished state
     * @return may return null if all the growls are busy
     */
    private Growl getNextGrowl() {
        for( Growl growl : this.growls) {
            if( growl.getState() == Growl.STATE_FINISHED) {
                return growl;
            }
        }
        return null;
    }

    public void showInfoGrowl(String text) {
        Growl growl = getNextGrowl();
        if( growl != null) {
            growl.setAsInfo(text);
            growl.show();
            this.pack();
        }
    }

    public void showSuccessGrowl(String text) {
        Growl growl = getNextGrowl();
        if( growl != null) {
            growl.setAsSuccessful(text);
            growl.show();
            this.pack();
        }
    }

    public void showWarningGrowl(String text) {
        Growl growl = getNextGrowl();
        if( growl != null) {
            growl.setAsWarning(text);
            growl.show();
            this.pack();
        }
    }

    public void showErrorGrowl(String text) {
        Growl growl = getNextGrowl();
        if( growl != null) {
            growl.setAsError(text);
            growl.show();
            this.pack();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
