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
import com.mygdx.game.screens.mainMenu.MainMenuScreen;
import com.mygdx.game.utils.GameManager;
import com.mygdx.game.utils.Preferences;


public class SettingsScreen extends ScreenAdapter {

    private Stage stage;
    private Ayu game;
    AssetManager assetManager;
    private Viewport viewport;
    Screen menuScreen;
    GameManager gameManager;



    public SettingsScreen(Ayu game, Screen menuScreen) {
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
        Preferences prefs = gameManager.getPrefs();

        String sTemp;
        if(prefs.getSound()){
            sTemp="On";
        }else{
            sTemp="Off";
        }

        ActorText soundText = new ActorText("Sound: ", 50f, Gdx.graphics.getHeight()*0.9f, 0.8f, Color.CORAL);
        final ActorText soundClickText = new ActorTextClickYesNo(sTemp, 50f+soundText.getWidth(), Gdx.graphics.getHeight()*0.9f, 0.8f, Color.CORAL);
       // ActorText playText = new ActorTextClickable("Play", (Gdx.graphics.getWidth()*0.1f), Gdx.graphics.getHeight()*0.9f - 180, 0.7f, Color.CORAL, game, new ScreenAyu(game));
       // ActorText topPlayersText = new ActorTextClickable("Top players", (Gdx.graphics.getWidth()*0.1f), Gdx.graphics.getHeight()*0.9f - 240, 0.7f, Color.CORAL, game, new ScreenAyu(game));
       // ActorText optionsText = new ActorTextClickable("Options", (Gdx.graphics.getWidth()*0.1f), Gdx.graphics.getHeight()*0.9f - 300, 0.7f, Color.CORAL, game, new ScreenAyu(game));

        final ActorText tileNumberText = new ActorText("Number of tiles: ", 50f, Gdx.graphics.getHeight()*0.75f, 0.8f, Color.CORAL);
        final TextField tileNumber = new TextField(""+prefs.getTileAmmount(), assetManager.get(AssetDescriptors.GAME_SKIN));
        tileNumber.setTextFieldFilter(new TextField.TextFieldFilter() {
            // Accepts all Alphanumeric Characters except
            public boolean acceptChar(TextField textField, char c) {
                if (Character.toString(c).matches("^[0-9]")) {
                    return true;
                }
                return false;
            }
        });
        tileNumber.setPosition(50f+ tileNumberText.getWidth(), Gdx.graphics.getHeight()*0.75f - tileNumberText.getHeight());

        ActorText p1Text = new ActorText("P1 name: ", 50f, Gdx.graphics.getHeight()*0.6f, 0.8f, Color.CORAL);
        final TextField p1input = new TextField(prefs.getPlayer1name(), assetManager.get(AssetDescriptors.GAME_SKIN));
        p1input.setPosition(50f+ p1Text.getWidth(), Gdx.graphics.getHeight()*0.6f - p1Text.getHeight());

        ActorText p2Text = new ActorText("P1 name: ", 50f, Gdx.graphics.getHeight()*0.45f, 0.8f, Color.CORAL);
        final TextField p2input = new TextField(prefs.getPlayer2name(), assetManager.get(AssetDescriptors.GAME_SKIN));
        p2input.setPosition(50f+ p2Text.getWidth(), Gdx.graphics.getHeight()*0.45f - p2Text.getHeight());
        ActorText backText = new ActorTextClickable("Back", 50f, Gdx.graphics.getHeight()*0.30f, 0.8f, Color.CORAL, game, menuScreen);
        final ActorTextBounds saveText = new ActorTextBounds("Save", Gdx.graphics.getWidth()-150f, Gdx.graphics.getHeight()*0.30f, 0.8f, Color.CORAL, game);

        stage.addActor(soundText);
        stage.addActor(soundClickText);
        stage.addActor(tileNumberText);
        stage.addActor(tileNumber);
        stage.addActor(p1Text);
        stage.addActor(p1input);
        stage.addActor(p2Text);
        stage.addActor(p2input);
        stage.addActor(backText);
        stage.addActor(saveText);
        //stage.addActor(topPlayersText);
        //stage.addActor(optionsText);

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (saveText.getBounds().contains(x, y)) {
                    game.setScreen(menuScreen);
                    Boolean tempBool;
                    int tempInt = Integer.parseInt(tileNumber.getText());
                    if(tempInt%2==0){
                        tempInt++;
                    }

                    if(soundClickText.getDisplayText().equals("On")){
                        tempBool=true;
                    }else{
                        tempBool=false;
                    }
                    Preferences savePref = new Preferences(tempBool, tempInt, p1input.getText(), p2input.getText());
                    gameManager.setPrefs(savePref);
                    gameManager.saveOptions();
                }
                return true;
            }
        });

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
