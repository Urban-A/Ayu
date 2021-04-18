package com.mygdx.game.screens.playing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Ayu;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.assets.RegionNames;
import com.mygdx.game.debug.DebugCameraController;
import com.mygdx.game.hud.HUD;
import com.mygdx.game.utils.Constants;
import com.mygdx.game.utils.GdxUtils;

public class ScreenAyuRenderer {
    public static final Logger log = new Logger(ScreenAyuRenderer.class.getName(), Logger.DEBUG);
    public OrthographicCamera camera;
    public OrthographicCamera cameraFont;
    public Viewport viewportFont;
    AssetManager am;

    public OrthographicCamera boardCam;
    public Viewport boardV;

    DebugCameraController dcc;
    ShapeRenderer sr;

    ScreenAyuController controller;

    Ayu gameMain;

    SpriteBatch batch;
    HUD hud;

    /*private TextureRegion mineRegion;
    private TextureRegion minecellRegion;
    private TextureRegion minecell1Region;
    private TextureRegion minecell2Region;
    private TextureRegion minecell3Region;
    private TextureRegion minecell4Region;
    private TextureRegion minecell5Region;
    private TextureRegion minecell6Region;
    private TextureRegion minecell7Region;
    private TextureRegion minecell8Region;
    private TextureRegion minecell_flagRegion;
    private TextureRegion minecelleRegion;
    private Animation<TextureRegion> mineAnimation;*/
    private TextureRegion top_region;
    private TextureRegion bottom_region;
    private TextureRegion left_region;
    private TextureRegion right_region;
    private TextureRegion white_region;
    private TextureRegion black_region;


    public ScreenAyuRenderer(Ayu game, ScreenAyuController controller) {
        this.gameMain = game;
        this.batch = game.batch;

        am = gameMain.getAssetManager();
        TextureAtlas gamePlayAtlas = am.get(AssetDescriptors.GAME_PLAY);
        top_region= gamePlayAtlas.findRegion(RegionNames.BORDER_TOP);
        bottom_region = gamePlayAtlas.findRegion(RegionNames.BORDER_BOTTOM);
        left_region = gamePlayAtlas.findRegion(RegionNames.BORDER_LEFT);
        right_region = gamePlayAtlas.findRegion(RegionNames.BORDER_RIGHT);
        white_region = gamePlayAtlas.findRegion(RegionNames.PIECE_WHITE);
        black_region = gamePlayAtlas.findRegion(RegionNames.PIECE_BLACK);


        this.controller = controller;

        cameraFont = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewportFont = new ScreenViewport(cameraFont);

        camera = new OrthographicCamera();
        sr = new ShapeRenderer();

        boardCam = new OrthographicCamera(controller.width, controller.height + Constants.HUD_H);
        boardV = new FitViewport(controller.width, controller.height + Constants.HUD_H, boardCam);
        boardV.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        controller.state = ScreenAyuController.GameState.BEGIN;
        hud = new HUD(0, (float) (controller.height + Constants.HUD_H), (float) controller.width, Constants.HUD_H, gamePlayAtlas, controller);
        hud.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), boardCam, boardV);
        hud.start();
        dcc = new DebugCameraController();
        dcc.setStartPosition((float)controller.width / 2, (float)(controller.height + Constants.HUD_H) / 2);
    }

    public void inputHandle() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) gameMain.safeExit();

    }


    public void render(float delta) {
        update(); //Also state
        dcc.handleDebugInput(delta);
        dcc.applyTo(boardCam);
        batch.totalRenderCalls = 0;
        boardCam.update();
        GdxUtils.clearScreen(Color.LIGHT_GRAY);
        //DebugViewportUtils.drawGrid(boardV,sr);
        batch.begin();
        batch.setProjectionMatrix(boardCam.combined);
        hud.renderUpdate(cameraFont, batch, delta);
        //log.debug("Board1:"+batch.totalRenderCalls);
        drawBoard();
        batch.end();
        //log.debug("Total RenderCalls:"+batch.totalRenderCalls);
        inputHandle();
    }

    private void drawBoardNet4Debug() {
        sr.setColor(Color.LIGHT_GRAY);
        sr.setProjectionMatrix(boardCam.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        {
            sr.rect(0, 0, controller.width, controller.height + Constants.HUD_H);
        }
        sr.end();

        sr.setProjectionMatrix(boardCam.combined);
        sr.setColor(Color.DARK_GRAY);
        sr.begin(ShapeRenderer.ShapeType.Line);
        {
            for (int x = 0; x <= controller.width; x++) { //vertical lines
                sr.line(x, 0, x, controller.height);
            }

            for (int y = 0; y <= controller.height; y++) { //horizontal lines
                sr.line(0, y, controller.width, y);
            }
        }
        sr.end();
    }

    private void drawBoard() {
        batch.setProjectionMatrix(boardCam.combined);
        for (int iStevec1 = 0; iStevec1 < controller.width; iStevec1++) {
            for (int iStevec2 = 0; iStevec2 < controller.height; iStevec2++) {
                if(controller.playingField[iStevec1][iStevec2].contains(ScreenAyuController.CellState.EMPTY))
                {
                    if(iStevec1!=0){
                        batch.draw(left_region, iStevec1, iStevec2, 1, 1);
                    }
                    if(iStevec2!=0){
                        batch.draw(bottom_region, iStevec1, iStevec2, 1, 1);
                    }
                    if(iStevec1!=controller.width-1)
                    {
                        batch.draw(right_region, iStevec1, iStevec2, 1, 1);
                    }
                    if(iStevec2!=controller.height-1)
                    {
                        batch.draw(top_region, iStevec1, iStevec2, 1, 1);
                    }

                    continue;
                }
                if(controller.playingField[iStevec1][iStevec2].contains(ScreenAyuController.CellState.BLACK))
                {
                    batch.draw(black_region, iStevec1, iStevec2, 1, 1);
                    continue;
                }
                if(controller.playingField[iStevec1][iStevec2].contains(ScreenAyuController.CellState.WHITE))
                {
                    batch.draw(white_region, iStevec1, iStevec2, 1, 1);
                    continue;
                }
            }
        }
    }

    private void update() {
        hud.update();
    }

    public void dispose() {
        batch.dispose();
        sr.dispose();
        log.debug("Dispose Screen");
    }


    public void resize(int width, int height) {
        viewportFont.update(width, height, true);
        boardV.update(width, height, true);
        controller.resize(boardCam,boardV,hud);
        hud.resize(width, height,boardCam,boardV);
    }

}
