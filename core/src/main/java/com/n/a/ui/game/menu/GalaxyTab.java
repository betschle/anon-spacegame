package com.n.a.ui.game.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.n.a.util.StandardFormats;
import com.n.a.game.space.Galaxy;
import com.n.a.ui.commons.Header;
import com.n.a.ui.game.menu.starmap.GalaxyMap;

public class GalaxyTab extends Table {

    private GalaxyTabModel model;
    private Header header;
    private GalaxyMap galaxyMap;

    public static class GalaxyTabModel {
        public String galaxyName;
        public String galaxyType;
        public int estimatedSystems;
        // public int visitedSystems;
    }

    public GalaxyTab (Skin skin) {
        super(skin);
    }

    public void construct() {
        this.add(header).fill().top().pad(10);
        this.row();
        if( this.model != null) {
            Table panel = new Table(getSkin());
            panel.add("Name:", "default-no-bg").left().padRight(10).fill();
            panel.add(model.galaxyName, "default-no-bg").right();
            panel.row();
            panel.add("Type:", "default-no-bg").left().padRight(10).fill();
            panel.add(model.galaxyType, "default-no-bg").right();
            panel.row();
            panel.add("Systems:", "default-no-bg").left().padRight(10).fill();
            panel.add( "ca. " + StandardFormats.MONEY_HUD.format(model.estimatedSystems), "default-no-bg").right();
            this.add(panel).padTop(20f).fill();
            this.row();
        }
        this.add(galaxyMap).size(260, 260).pad(10).padBottom(230);
    }

    public GalaxyTabModel getModel() {
        return model;
    }

    public void setModel(GalaxyTabModel model) {
        this.model = model;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public GalaxyMap getGalaxyMap() {
        return galaxyMap;
    }

    public void setGalaxyMap(GalaxyMap galaxyMap) {
        this.galaxyMap = galaxyMap;
    }

    public static GalaxyTabModel galaxyToModel(Galaxy galaxy) {
        GalaxyTabModel model = new GalaxyTabModel();
        model.galaxyName = galaxy.getName();
        model.estimatedSystems = galaxy.getTheoreticalAmountOfSystems();
        model.galaxyType = "Spiral Galaxy";

        return model;
    }
}
