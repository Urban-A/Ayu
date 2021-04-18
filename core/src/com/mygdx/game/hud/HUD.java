package com.mygdx.game.hud;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.assets.RegionNames;
import com.mygdx.game.screens.playing.ScreenAyuController;
import com.mygdx.game.utils.GameManager;
import com.mygdx.game.utils.GdxUtils;

public class HUD implements Disposable, MyClickListener {

    ScreenAyuController controller;

    GameObjectRestartButton btn;
    private TextureRegion restart_region;
    private Animation<TextureRegion> restartAnimation;

    BitmapFont font;
    float duration;
    Vector3 textMinesInCoordinateToScreen;
    Vector3 textDurationInCoordinateToScreen;
    GlyphLayout tmplayout;

    GameManager gameManager;

    float x;
    float y;
    float w;
    float h;

    Boolean clicked=false;


    public HUD(float x, float y, float w, float h, TextureAtlas gamePlayAtlas, ScreenAyuController controller) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.controller = controller;
        textMinesInCoordinateToScreen = new Vector3();
        textDurationInCoordinateToScreen = new Vector3();
        restart_region = gamePlayAtlas.findRegion(RegionNames.RESTART);
        restartAnimation = new Animation(0.15f,gamePlayAtlas.findRegions(RegionNames.RESTART_ROTATE), Animation.PlayMode.NORMAL);
        btn = new GameObjectRestartButton(x + w / 2 - 0.5f, y - h / 2 - 0.5f, 1, 1);
        gameManager=GameManager.INSTANCE;

    }

    public void start() {
        duration = 0;
    }

    void updateDuration(float duration) {
        this.duration = duration;
    }


    public void renderUpdate(Camera cameraFont, SpriteBatch batch, float dt) {

        if(clicked){
            batch.draw(restartAnimation.getKeyFrame(controller.animationTime), btn.bounds.x, btn.bounds.y, btn.bounds.width, btn.bounds.height);
            if(restartAnimation.isAnimationFinished(controller.animationTime)){
                clicked=false;
                controller.resetAnimationTime();
                controller.increaseAnimationTime=false;
            }
        }else{
            batch.draw(restart_region, btn.bounds.x, btn.bounds.y, btn.bounds.width, btn.bounds.height);
        }

        batch.setProjectionMatrix(cameraFont.combined);
        font.setColor(Color.RED);
        tmplayout = new GlyphLayout(font, controller.turnState.toString());
        font.draw(batch, tmplayout, textMinesInCoordinateToScreen.x, textMinesInCoordinateToScreen.y);
        tmplayout = new GlyphLayout(font, GdxUtils.GetFormatedInMMSS((int) duration));
        font.draw(batch, tmplayout, textDurationInCoordinateToScreen.x-tmplayout.width, textDurationInCoordinateToScreen.y);
        if (controller.state == ScreenAyuController.GameState.RUNNING) {
            updateDuration(duration + dt);
        }
    }

    @Override
    public void dispose() {
        font.dispose();
    }

    @Override
    public boolean onClickEvent(float x, float y) {
        return btn.onClickEvent(x, y);  //deligate
    }

    public void setState(GameObjectRestartButton.STATE state) {
        //btn.setState(state);
    }

    public void update() {
        if (btn.changeState4LogicUpdate) {
            switch (btn.newState) { //change gameMain state
                case WIN:
                case START:
                case LOSE:
                    controller.clearAndInit();
                    break;
            }

            if(gameManager.getPrefs().getSound()){
                controller.clap.play();
            }

            controller.newGame();
            clicked=true;
            controller.resetAnimationTime();
            controller.increaseAnimationTime=true;
            btn.changeState4LogicUpdate = false;
        }
    }
    /*
      Font based objects must be recalculated on resize event.
      Also position of text must be changed.
   */
    private void createFontBasedObjects(Camera boardCam, Viewport boardV) {
        font = GdxUtils.getTTFFontInWorldUnits(1, boardV.getWorldHeight());
        textMinesInCoordinateToScreen.set(x, y - h / 2 + 0.5f, 0);
        GdxUtils.ProjectWorldCoordinatesInScreenCoordinates(boardCam, textMinesInCoordinateToScreen);
        textDurationInCoordinateToScreen.set(x + w, y - h / 2 + 0.5f, 0);
        GdxUtils.ProjectWorldCoordinatesInScreenCoordinates(boardCam, textDurationInCoordinateToScreen);
    }

    public void resize(int width, int height, Camera boardCam, Viewport boardV) {
        if (font != null) font.dispose();
        createFontBasedObjects(boardCam,boardV);
        start();
    }

}
