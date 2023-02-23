package com.n.a.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.n.a.XYZGame;
import com.n.a.gfx.Parallax;
import com.n.a.ui.commons.ButtonClickListener;
import com.n.a.ui.commons.widgets.TextButtonTabs;
import com.n.a.util.sequences.NumberGenerator;

/**
 * Basic entry point for the XYZ Game from which games can be started/
 * loaded and Settings adjusted.
 *
 * TODO use https://snowb.org/ for Bitmap fonts
 */
public class MainMenuScreen extends AbstractScreen {


    private Parallax parallax; // for parallax I need the texture atlas
    private BitmapFont font;
    private Table centerMenu;
    private TextButtonTabs tabs;
    private TextButton newGameButton;
    private TextButton exitButton;
    private TextButton settingsButton;
    private ButtonClickListener mainClickListener;

    public MainMenuScreen (final XYZGame game) {
        super(game);

        // game.getComponentFactory().getTextTabs();
        // TODO need IconTabs except with Text and any contet
        Skin skin = this.game.getComponentFactory().getSkin();
        this.font = skin.getFont("default");

        TextureRegion[] regions = new TextureRegion[]{skin.getRegion("star_1"),
                                        skin.getRegion("star_2"),
                                        skin.getRegion("star_3"),
                                        skin.getRegion("star_4") };

        this.parallax = new Parallax(10, regions, new NumberGenerator(33));
        this.parallax.setDirection(0, 1f);
        this.parallax.setSpeed(4f);
        this.parallax.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.background.addActor(this.parallax);

        this.mainClickListener = new ButtonClickListener( game.getSoundManager(), game.getComponentFactory().getMainButtonSounds() );
        this.centerMenu = new Table();
        this.newGameButton = new TextButton("Start Demo", game.getComponentFactory().getSkin(), "interactive");
        this.newGameButton.addListener( this.mainClickListener );
        this.newGameButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SpaceScreen spaceScreen = game.getSpaceScreen();
                spaceScreen.create();
                game.setScreen(spaceScreen);
            }
        } );

        Table tableDummy = new Table(game.getComponentFactory().getSkin());
        tableDummy.setBackground("XYZ_Logo");

        this.settingsButton = new TextButton("Settings", game.getComponentFactory().getSkin(), "interactive");
        this.settingsButton.addListener( this.mainClickListener );

        this.exitButton = new TextButton("Exit", game.getComponentFactory().getSkin(), "interactive");
        this.exitButton.addListener(this.mainClickListener);
        this.exitButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getMusicPlayer().dispose();
                Gdx.app.exit();
            }
        } );

        this.centerMenu.add(tableDummy).size(171 * 2, 41 * 2).padBottom(130f).fill();
        this.centerMenu.row();
        this.centerMenu.add(this.newGameButton).size(250, 50).pad(10f).fill();
        this.centerMenu.row();
        this.centerMenu.add(this.settingsButton).size(250, 50).pad(10f).fill();
        this.centerMenu.row();
        this.centerMenu.add(this.exitButton).size(250, 50).pad(10f).fill();
        this.centerMenu.pack();
        this.centerMenu.setPosition( Gdx.graphics.getWidth()/2f - this.centerMenu.getWidth()/2f,
                                     Gdx.graphics.getHeight()/2f - this.centerMenu.getHeight()/2f);

        this.hud.addActor(centerMenu);

        Gdx.input.setInputProcessor(hud);
    }

    @Override
    public void show() {
        this.game.getMusicPlayer().playNextSong("main");
    }

    @Override
    public void render(float delta) {
        this.game.getMusicPlayer().update(delta);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        background.act();
        hud.act();
        root.act();

        background.draw();
        root.draw();
        hud.draw();

        hud.getBatch().begin();
        font.draw(hud.getBatch(), "XYZ " + XYZGame.VERSION, Gdx.graphics.getWidth() - 120, Gdx.graphics.getHeight() - 20);
        hud.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        this.centerMenu.pack();
        this.centerMenu.setPosition( width/2f - this.centerMenu.getWidth()/2f,
                                height/2f - this.centerMenu.getHeight()/2f);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.newGameButton.removeListener(this.mainClickListener);
        this.exitButton.removeListener(this.mainClickListener);
    }
}
