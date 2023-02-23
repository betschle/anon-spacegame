package com.n.a.ui.commons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * A text with a message and icon that vanishes
 * after a while
 */
public class Growl extends Table {

    // State machine of a growl: Show -> Wait -> Hide -> Finished
    /** State: currently showing the growl */
    public final static int STATE_SHOW = 0; //
    /** State: currently waiting, such that the user can read the growl */
    public final static int STATE_WAIT = 1; //
    /** State: currently hiding the growl */
    public final static int STATE_HIDE = 2;
    /** State: finished growl, with listeners invoked */
    public final static int STATE_FINISHED = 3;

    private int state = STATE_FINISHED; // 0 = hide, 1 = show
    private AlphaAction hideAction = new AlphaAction();
    private AlphaAction showAction = new AlphaAction();
    private List<GrowlListener> listeners = new ArrayList<>();
    private float time = 1; // delay before fade out begins TODO make this configurable
    private float delayRate = 0.4f;

    private Label label;
    private Image icon;

    public Growl(Skin skin) {
        super(skin);
        this.label = new Label("", skin, "camotactics_bs_20px", Color.WHITE);
        this.icon = new Image();
        this.add(icon).minSize(24,24).padRight(10f);
        this.add(label).size(200, 20).fill();
        this.pack();
    }

    /**
     * Gets the current growl state.
     * State machine of a growl: Show -> Wait -> Hide -> Finished
     * @return
     */
    public int getState() {
        return state;
    }

    /**
     * Causes the breadcrumb to fade in. This method is not supposed to be called twice in a row!
     */
    public void show() {
        this.state = STATE_SHOW;
        this.time = 1;
        this.showAction.reset();
        this.showAction.setDuration(0.1f);
        this.showAction.setAlpha(1);
        this.addAction( this.showAction );
    }

    /**
     * Causes the breadcrumb to fade out. Fades out more slowly than
     * it fades in. This method is not supposed to be called twice in a row!
     */
    private void hide() {
        this.state = STATE_HIDE;
        this.time = 0;
        this.hideAction.reset();
        this.hideAction.setDuration(1f);
        this.hideAction.setAlpha(0);
        this.addAction( this.hideAction );
    }

    public void addListener(GrowlListener listener) {
        if( listener != null) {
            this.listeners.add(listener);
        }
    }

    private void fireOnHide() {
        for( GrowlListener listener : this.listeners ) {
            listener.onHide(this);
        }
    }

    private void fireOnShow() {
        for( GrowlListener listener : this.listeners ) {
            listener.onShow(this);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if( this.state == STATE_WAIT) {
            this.time = this.time - (delayRate * delta);
            if( this.time < 0 ) {
                this.hide();
            }
        } else
        if( this.state == STATE_HIDE && this.hideAction.isComplete() ) {
            this.removeAction(this.hideAction);
            this.fireOnHide();
            this.state = STATE_FINISHED;
        } else
        if( this.state == STATE_SHOW && this.showAction.isComplete() ) {
            this.removeAction(this.showAction);
            this.fireOnShow();
            this.state = STATE_WAIT;
        }
    }


    /**
     * Sets this growl element as warning, with corresponding color and icon.
     * @param warning
     */
    public void setAsWarning(String warning) {
        this.label.setText("[YELLOW]" +warning);
        this.icon.setDrawable( getSkin().getDrawable("icon_attention"));
        this.icon.setColor(Color.YELLOW);
        this.pack();
    }

    /**
     * Sets this growl element as info, with corresponding color and icon.
     * @param info
     */
    public void setAsInfo(String info) {
        this.label.setText("[CYAN]" +info);
        this.icon.setDrawable( getSkin().getDrawable("icon_info"));
        this.icon.setColor(Color.CYAN);
        this.pack();
    }

    /**
     * Sets this growl element as success message, with corresponding color and icon.
     * @param info
     */
    public void setAsSuccessful(String info) {
        this.label.setText("[GREEN]" + info);
        this.icon.setDrawable( getSkin().getDrawable("icon_success"));
        this.icon.setColor(Color.GREEN);
        this.pack();
    }

    /**
     * Sets this growl element as error message, with corresponding color and icon.
     * @param info
     */
    public void setAsError(String info) {
        this.label.setText("[RED]" + info);
        this.icon.setDrawable( getSkin().getDrawable("icon_cancel"));
        this.icon.setColor(Color.RED);
        this.pack();
    }
}
