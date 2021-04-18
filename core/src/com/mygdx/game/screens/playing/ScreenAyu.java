package com.mygdx.game.screens.playing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.Ayu;
import com.mygdx.game.screens.mainMenu.MainMenuScreen;
import com.mygdx.game.utils.Constants;
import com.mygdx.game.utils.GameManager;

public class ScreenAyu extends ScreenAdapter {
    public static final Logger log = new Logger(ScreenAyu.class.getName(), Logger.DEBUG);
    ScreenAyuController controller;
    ScreenAyuRenderer renderer;
    GameManager gameManager;

    Ayu gameMain;
    MainMenuScreen menuScreen;

    public ScreenAyu(Ayu game, MainMenuScreen menuScreen) {
        this.gameMain = game;
        this.menuScreen=menuScreen;
    }

    @Override
    public void show() {
        super.show();

        gameManager=GameManager.INSTANCE;
        controller = new ScreenAyuController(gameManager.getPrefs().getTileAmmount(), gameManager.getPrefs().getTileAmmount()/*Constants.FIELD_HEIGHT*/, gameMain.getAssetManager());
        renderer = new ScreenAyuRenderer(gameMain,controller);

    }

    public void inputHandle() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) gameMain.safeExit();
        if (Gdx.input.isKeyPressed(Input.Keys.H)) gameMain.setScreen(new MainMenuScreen(gameMain));

    }

    @Override
    public void render(float delta) {
        controller.update(delta);
        renderer.render(delta);
        inputHandle();
    }

    @Override
    public void hide() {
        log.debug("hide");
        dispose();
    }

    @Override
    public void dispose() {
        //renderer.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        renderer.resize(width, height);
    }


}
