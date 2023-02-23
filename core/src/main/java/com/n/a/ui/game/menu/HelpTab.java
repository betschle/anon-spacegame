package com.n.a.ui.game.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.n.a.ui.commons.Header;
import com.n.a.ui.commons.widgets.TextButtonTabs;

public class HelpTab extends Table {

    private Header header;
    private TextButtonTabs<TextButton> tabs;


    public HelpTab( Skin skin ) {
        super(skin);
    }

    public void construct() {
        this.add(this.header).fill().top().pad(10);;
        this.row();
        this.add(this.tabs).fill();
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public TextButtonTabs<TextButton> getTabs() {
        return tabs;
    }

    public void setTabs(TextButtonTabs<TextButton> tabs) {
        this.tabs = tabs;
    }
}
