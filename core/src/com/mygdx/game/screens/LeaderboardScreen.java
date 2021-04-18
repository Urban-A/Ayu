package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Ayu;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.screens.actors.ActorText;
import com.mygdx.game.screens.actors.ActorTextBounds;
import com.mygdx.game.screens.actors.ActorTextClickYesNo;
import com.mygdx.game.screens.actors.ActorTextClickable;
import com.mygdx.game.utils.GameManager;
import com.mygdx.game.utils.Preferences;

public class LeaderboardScreen extends ScreenAdapter {
    private Stage stage;
    private Ayu game;
    AssetManager assetManager;
    private Viewport viewport;
    Screen menuScreen;
    GameManager gameManager;



    public LeaderboardScreen(Ayu game, Screen menuScreen) {
        this.game=game;
        assetManager=game.getAssetManager();
        this.menuScreen=menuScreen;
    }

    @Override
    public void show(){
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        gameManager=GameManager.INSTANCE;


        ActorText titleText = new ActorText("Top 3 palyers by wins", 50f, Gdx.graphics.getHeight()*0.9f, 0.9f, Color.CORAL);
        ActorText leaderboardText = new ActorText(gameManager.getBest3(), 50f, Gdx.graphics.getHeight()*0.7f, 0.6f, Color.CORAL);
        ActorText backText = new ActorTextClickable("Back", 50f, Gdx.graphics.getHeight()*0.30f, 0.8f, Color.CORAL, game, menuScreen);

        stage.addActor(titleText);
        stage.addActor(leaderboardText);
        stage.addActor(backText);
        //stage.addActor(topPlayersText);
        //stage.addActor(optionsText);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
