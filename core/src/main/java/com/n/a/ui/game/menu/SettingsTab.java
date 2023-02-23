package com.n.a.ui.game.menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.n.a.XYZGame;
import com.n.a.ui.commons.Growl;
import com.n.a.ui.commons.Header;
import com.n.a.ui.commons.Separator;
import com.n.a.ui.game.GameSettingsPanel;

public class SettingsTab extends Table {
    private XYZGame game;
    private Header header;

    private Table content;
    private Table buttonPanel;
    private ClickListener applyButtonListener;
    private GameSettingsPanel gameSettingsPanel;
    private Growl messageGrowl;
    private Separator separator;
    private TextButton applyButton;
    private TextButton resetButton;

    public SettingsTab(Skin skin ) {
        super(skin);
        this.content = new Table(skin);
        this.buttonPanel = new Table(skin);
    }

    /**
     * Required to apply the game settings
     * @param XYZGame
     */
    public void setGame(XYZGame XYZGame) {
        this.game = XYZGame;
    }

    public void setApplyButtonListener(ClickListener applyButtonListener) {
        if( this.applyButtonListener != null) {
            this.applyButton.removeListener(applyButtonListener);
        }
        this.applyButtonListener = applyButtonListener;
        this.applyButton.addListener(this.applyButtonListener);
    }

    public void construct() {

        // this.content.align(Align.top);
        this.content.clear();
        this.content.add("Game Settings:").minHeight(25).pad(15).fill();
        this.content.row();
        this.content.add(this.gameSettingsPanel).top().pad(15).padBottom(300);
        this.content.pack();

        this.buttonPanel.clear();
        this.buttonPanel.align(Align.top);
        this.buttonPanel.add(this.resetButton).minSize(120, 50).left();
        this.buttonPanel.add(this.applyButton).minSize(120, 50).right();
        this.buttonPanel.pack();

        // TODO this code is common for all menu tabs!!!! Outsource this to a common Tab class?
        this.clear();
        this.add(this.header).fill().top().pad(10);;
        this.row();
        this.add(messageGrowl).maxSize(1150, 13);
        this.row();
        this.add(content).minSize(500, 500); // .minSize(500, 400)
        this.row();
        this.add(this.separator).minSize(1150, 10);
        this.row();
        this.add(this.buttonPanel).padTop(20);
        this.pack();
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Growl getMessageGrowl() {
        return messageGrowl;
    }

    public void setMessageGrowl(Growl messageGrowl) {
        this.messageGrowl = messageGrowl;
    }

    public GameSettingsPanel getGameSettingsPanel() {
        return gameSettingsPanel;
    }

    public void setGameSettingsPanel(GameSettingsPanel gameSettingsPanel) {
        this.gameSettingsPanel = gameSettingsPanel;
    }

    public TextButton getApplyButton() {
        return applyButton;
    }

    public void setApplyButton(TextButton applyButton) {
        this.applyButton = applyButton;
    }

    public Separator getSeparator() {
        return separator;
    }

    public void setSeparator(Separator separator) {
        this.separator = separator;
    }

    public TextButton getResetButton() {
        return resetButton;
    }

    public void setResetButton(TextButton resetButton) {
        this.resetButton = resetButton;
    }
}
