package com.n.a.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.n.a.game.Player;
import com.n.a.util.StandardFormats;

/**
 * Displays information about the player on the HUD.
 */
public class PlayerHUD extends Table {

    private Skin skin;
    private String numberFormat = "%,09d";
    private ImageTextButton chipsButton;
    private ImageTextButton researchButton;

    public static class PlayerHUDModel {

    }

    public PlayerHUD(Skin skin) {
        this.skin = skin;
        this.construct();
    }

    private void construct() {
        this.chipsButton = new ImageTextButton( StandardFormats.MONEY_HUD.format(0), this.skin, "chips");
        this.researchButton = new ImageTextButton( StandardFormats.MONEY_HUD.format(0), this.skin, "discovery");
        this.researchButton.getImageCell().right();
        this.researchButton.getLabelCell().left();

        add(this.chipsButton).pad(3).fill();
        row();
        add(this.researchButton).pad(3).fill();
        pack();
    }

    public void update(Player player) {
        this.chipsButton.setText( StandardFormats.MONEY_HUD.format(player.getChips()) );
        this.researchButton.setText( StandardFormats.MONEY_HUD.format(player.getDiscoveryPoints()) );
    }

}
