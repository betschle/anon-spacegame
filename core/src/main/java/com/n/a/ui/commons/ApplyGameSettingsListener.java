package com.n.a.ui.commons;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.n.a.XYZGame;
import com.n.a.ui.game.menu.SettingsTab;

public class ApplyGameSettingsListener extends ClickListener {

    private XYZGame game;
    private SettingsTab settingsTab;

    public ApplyGameSettingsListener(XYZGame game, SettingsTab settingsTab) {
        this.game = game;
        this.settingsTab = settingsTab;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        game.applyGameSettings( settingsTab.getGameSettingsPanel().getModel() );
        settingsTab.getMessageGrowl().setAsSuccessful("New Settings applied!");
        settingsTab.getMessageGrowl().show();
    }
}
