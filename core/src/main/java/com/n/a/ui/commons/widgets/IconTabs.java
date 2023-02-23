package com.n.a.ui.commons.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.n.a.XYZException;
import com.n.a.ui.commons.ButtonClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Defines a Tabbed Panel that can be controlled by
 * numerous icons. Features tweening.
 */
public class IconTabs extends Table {

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    private ButtonGroup buttonGroup;
    private List<Tab> tabs = new ArrayList<>();
    private ClickListener tabClickListener;
    private Table currentContent;
    private Tab currentTab;
    private Stack contentStack;

    public static class Tab {
        private Button button;
        private Table contentPanel;
        private ButtonClickListener clickListener;
    }

    public IconTabs(Skin skin, ButtonGroup.Orientation orientation) {
        super(skin);
        this.setBackground("panel_2");
        this.tabClickListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Actor target = event.getTarget();
                if( target != null) {
                    if( target.getUserObject() instanceof Tab ) {
                        Tab tab = (Tab) target.getUserObject();
                        setShownContent( tab.contentPanel );
                    }
                } else {
                    setShownContent(null);
                }
            }
        };
        this.contentStack = new Stack();
        this.buttonGroup = new ButtonGroup(skin, orientation);
        this.buttonGroup.setButtonClickListener(this.tabClickListener);

        this.addListener( new ClickListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if( keycode == Input.Keys.A ) {
                    int index = tabs.indexOf(currentTab);
                    showTab( --index );
                    return true;
                } else
                if( keycode == Input.Keys.D ) {
                    int index = tabs.indexOf(currentTab);
                    showTab( ++index );
                    return true;
                }
                return super.keyUp(event, keycode);
            }
        });

    }

    /**
     * Needs to be called before being usable.
     */
    public void construct() {
        switch( this.buttonGroup.getOrientation() )  {
            case HORIZONTAL:
                add(this.buttonGroup).fill().center().row();
                add(this.contentStack).expandY();
                break;
            case VERTICAL:
                add(this.buttonGroup).fill();
                add(this.contentStack).expandX();
                break;
            default:
                throw new XYZException(XYZException.ErrorCode.E0004, "Enum case not defined: " + this.buttonGroup.getOrientation());
        }
        this.pack();
    }

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    public List<Tab> getTabs() {
        return tabs;
    }

    private void setShownContent(Table content) {
        if( this.currentContent != null) {
            if( this.isVisible() && !this.currentContent.hasActions() ) {
                ParallelAction moveActions = new ParallelAction(Actions.moveTo(-Gdx.graphics.getWidth(), 0, 0.4f, Interpolation.pow2), Actions.fadeOut(0.1f));
                SequenceAction sequenceAction = new SequenceAction(moveActions, Actions.visible(false));
                this.currentContent.addAction(sequenceAction);
            } else {
                this.currentContent.setVisible(false);
            }
            this.currentContent = null;
        }
        if( content != null && !content.hasActions()) {
            if( this.isVisible() ) {
                ParallelAction moveActions = new ParallelAction(Actions.fadeIn(0.1f), Actions.moveTo(0, 0, 0.4f, Interpolation.pow2));
                SequenceAction sequenceAction = new SequenceAction(Actions.visible(true), moveActions );
                content.addAction( sequenceAction );
            } else {
                content.setVisible(true);
            }
            this.currentContent = content;
        }
    }

    /**
     * Displays the right tab and also sets the right button to be visually checked.
     * @param index this variable is cyclic, meaning if it goes below or beyond
     *              allowed indices, it will start counting from beginning or end again.
     */
    public void showTab(int index) {
        // go back to beginning
        if( index >= this.tabs.size()) {
            index = 0;
        }
        // go back to end
        if( index < 0) {
            index = this.tabs.size() - 1;
        }
        int i = 0;
        for( Tab tab : this.tabs) {
            if( i == index) {
                tab.button.setChecked(true);
                this.currentTab = tab;
                this.setShownContent(tab.contentPanel);
                return;
            }
            i++;
        }
    }

    /**
     * Adds a Tab to this TabGroup.
     * @param style
     * @param content
     */
    public void addTab(String style, String tooltip, Table content) {
        Tab tab = new Tab();
        tab.button = buttonGroup.addButton(style, tooltip);
        tab.contentPanel = content;
        tab.button.setUserObject(tab);
        tab.contentPanel.setVisible(false);
        this.contentStack.add(tab.contentPanel);
        this.tabs.add(tab);
        this.contentStack.layout();

    }
}
