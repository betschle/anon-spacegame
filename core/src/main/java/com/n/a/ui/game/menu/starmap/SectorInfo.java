package com.n.a.ui.game.menu.starmap;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.n.a.game.space.StarSystem;
import com.n.a.util.StandardFormats;

public class SectorInfo extends Table {

    private String format =  "%.2f";
    private Label nameLabel;
    private Label coordinatesLabel;
    private Label discoveredLabel;

    public SectorInfo( Skin skin) {
        super(skin);
    }

    public void construct() {

        this.add("Name:", "default-no-bg").center();
        this.add("Coordinates:", "default-no-bg").center();
        this.add("Discovered:", "default-no-bg").center();
        this.row();
        this.nameLabel = this.add("Gamma Microbica", "default-transparent").minWidth(150).expand().center().getActor();
        this.nameLabel.setAlignment(Align.center);
        this.coordinatesLabel = this.add("X Y", "default-transparent").minWidth(150).expand().center().getActor();
        this.coordinatesLabel.setAlignment(Align.center);
        this.discoveredLabel = this.add("30 %", "default-transparent").minWidth(150).expand().center().getActor();
        this.discoveredLabel.setAlignment(Align.center);
        this.row();

        this.pack();
    }

    public void updateWithSector(StarSystem starSystem, int x, int y) {
        this.nameLabel.setText( starSystem.getSun().getGraphics().getModel().getDisplayName() );
        this.coordinatesLabel.setText( x + " | " + y);
        this.discoveredLabel.setText(StandardFormats.PERCENT.format(starSystem.getDiscoveredFactor() * 100f ) + " %" );
    }


}
