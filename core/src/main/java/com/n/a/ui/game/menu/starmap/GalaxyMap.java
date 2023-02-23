package com.n.a.ui.game.menu.starmap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.n.a.game.space.GalaxyMask;

public class GalaxyMap extends Table {

    private Drawable pointer;
    private int sectorCoordinatesX = 0;
    private int sectorCoordinatesY = 0;
    private GalaxyMask mask;

    public GalaxyMap( Skin skin) {
        super(skin);
        this.pointer = skin.getDrawable("sonar_marker_planet");
    }

    public void setMask(GalaxyMask mask) {
        this.mask = mask;
    }

    /**
     * This needs to be updated in order for the map to function properly!
     * @param sectorCoordinatesX
     * @param sectorCoordinatesY
     */
    public void setCurrentCoordinates(int sectorCoordinatesX, int sectorCoordinatesY) {
        this.sectorCoordinatesX = sectorCoordinatesX;
        this.sectorCoordinatesY = sectorCoordinatesY;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(this.mask.getImageRegion(), this.getX(), this.getY());
        Vector2 normalizedCoordinates = this.mask.centerCoordinates(this.sectorCoordinatesX, this.sectorCoordinatesY);

        int x = (int) (this.getX() + normalizedCoordinates.x);
        int y = (int) (this.getY() + normalizedCoordinates.y);

        batch.setColor(Color.CYAN);
        this.pointer.draw(batch, x-10, y-10, 20, 20);
        batch.setColor(Color.WHITE);
        super.draw(batch, parentAlpha);
    }
}
