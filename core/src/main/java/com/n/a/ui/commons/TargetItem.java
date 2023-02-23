package com.n.a.ui.commons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * A single target item for a target list.
 * It can be selected, but is not interactive.
 */
public class TargetItem extends Table {

    private Label targetName;
    private Label targetDistance;
    private Image icon;
    private boolean selected;

    /**
     * Creates a target item with default styles.
     * @param skin
     */
    public TargetItem(Skin skin) {
        this(skin, "targetItem");
    }

    /**
     * Creates a target item with different styles.
     * @param skin
     */
    public TargetItem(Skin skin, String style) {
        super(skin);
        this.setBackground("panel_4");
        this.targetName = new Label("Name", this.getSkin(), style);
    }

    public void construct() {
        this.targetDistance = new Label("Name", getSkin(), "targetItem-gray");
        this.icon = new Image(getSkin(), "planetType_unknown");

        Table table = new Table();
        table.add(this.targetName).width(150).row();
        table.add(this.targetDistance).width(150);

        this.add(this.icon).pad(3).minSize(16,16);
        this.add(table).expand();
        this.pack();
    }

    public void setAsPlanetGraphics(String name, String distance, String icon) {
        this.targetName.setText(name);
        this.targetDistance.setText(distance);
        this.icon.setDrawable(getSkin().getDrawable(icon));
    }

    /**
     * Selects this target item by coloring the
     * labels
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        this.setBackground(this.selected ? "panel_4_selected" : "panel_4");
    }
}
