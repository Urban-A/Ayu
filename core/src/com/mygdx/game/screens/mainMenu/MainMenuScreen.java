package com.mygdx.game.screens.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Ayu;
import com.mygdx.game.screens.LeaderboardScreen;
import com.mygdx.game.screens.SettingsScreen;
import com.mygdx.game.screens.actors.ActorText;
import com.mygdx.game.screens.actors.ActorTextClickable;
import com.mygdx.game.screens.playing.ScreenAyu;

public class MainMenuScreen extends ScreenAdapter {
    private Stage stage;
    private Ayu game;
    AssetManager assetManager;
    private Viewport viewport;

    public MainMenuScreen(Ayu game){
        this.game=game;
    }

    @Override
    public void show(){
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);

        ActorText gameTitle = new ActorText("Ayu ", (Gdx.graphics.getWidth()/2)-50, Gdx.graphics.getHeight()*0.9f, 1, Color.CORAL);
        ActorText playText = new ActorTextClickable("Play", (Gdx.graphics.getWidth()*0.1f), Gdx.graphics.getHeight()*0.9f - 180, 0.7f, Color.CORAL, game, new ScreenAyu(game, this));
        ActorText topPlayersText = new ActorTextClickable("Top players", (Gdx.graphics.getWidth()*0.1f), Gdx.graphics.getHeight()*0.9f - 240, 0.7f, Color.CORAL, game, new LeaderboardScreen(game, this));
        ActorText optionsText = new ActorTextClickable("Options", (Gdx.graphics.getWidth()*0.1f), Gdx.graphics.getHeight()*0.9f - 300, 0.7f, Color.CORAL, game, new SettingsScreen(game, this));

        stage.addActor(gameTitle);
        stage.addActor(playText);
        stage.addActor(topPlayersText);
        stage.addActor(optionsText);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 0.08f, 0.08f, 0.08f, 1 );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        inputhandle();
    }

    public void inputhandle(){
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) game.safeExit();
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

