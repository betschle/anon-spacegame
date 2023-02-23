package com.n.a.ui.commons;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Header extends Table {

    private Label titleLabel;
    private Separator titleSeparator;

    public Header (Skin skin) {
        super(skin);
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public void setTitleLabel(Label titleLabel) {
        this.titleLabel = titleLabel;
    }

    public Separator getTitleSeparator() {
        return titleSeparator;
    }

    public void setTitleSeparator(Separator titleSeparator) {
        this.titleSeparator = titleSeparator;
    }

    public void construct() {
        this.add(this.titleLabel).colspan(1).fill().top().pad(10);
        this.row();
        this.add(this.titleSeparator).minSize(1150, 10)
                .colspan(3)
                .padBottom(10f)
                .fill();
        this.pack();
    }
}
