package com.mygdx.game.screens.playing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.hud.GameObjectRestartButton;
import com.mygdx.game.hud.HUD;
import com.mygdx.game.hud.MyClickListener;
import com.mygdx.game.utils.GameManager;
import com.mygdx.game.utils.GdxUtils;
import com.mygdx.game.utils.Result;

import java.util.EnumSet;

public class ScreenAyuController implements GestureDetector.GestureListener{
    public static final Logger log = new Logger(ScreenAyuController.class.getName(), Logger.DEBUG);
    private GestureDetector gestureDetector;
    Array<MyClickListener> listeners;
    public Sound clap;

    public int[][] groups;
    public int width;
    public int height;

    public float animationTime;
    public int endX;
    public int endY;

    public Viewport boardV;
    HUD hud;

    GameManager gameManager;



    public enum GameState {
        BEGIN, //blank board clock is not running
        RUNNING, //clock starts to run
        END_WIN, //end
        END_LOSE
    }


    public GameState state;
    public CellState turnState;

    public void advanceTurn(){
        if(turnState==CellState.WHITE){
            turnState=CellState.BLACK;
        }else{
            turnState=CellState.WHITE;
        }
    }

    public enum CellState {
        BLACK, WHITE, EMPTY, MOVABLE;

        public static EnumSet<CellState> WH_MOVABLE = EnumSet.of(WHITE, MOVABLE);
        public static EnumSet<CellState> BL_MOVABLE = EnumSet.of(BLACK, MOVABLE);
    }
    public EnumSet<CellState>[][] playingField;
    public Boolean increaseAnimationTime=false;

    public EnumSet<CellState> tempState;
    public Boolean selectedExists;
    public int selected[];

    public void update(float delta) {
        if(increaseAnimationTime){
            animationTime += delta;
        }
    }

    public void resetAnimationTime() {
        animationTime = 0;
    }

    public boolean isEndAnimationCell(int x, int y) {
        if (state == GameState.END_LOSE) {
            return ((x == endX) && (y == endY));
        }
        return false;
    }

    public ScreenAyuController(int width, int height, AssetManager assetManager)
    {
        this.width = width;
        this.height = height;
        selected = new int[2];
        selectedExists=false;
        groups = new int[width][height];
        playingField = (EnumSet<CellState>[][]) new EnumSet<?>[width][height];
        for (int iStevec1 = 0; iStevec1 < width; iStevec1++) {
            for (int iStevec2 = 0; iStevec2 < height; iStevec2++) {
                playingField[iStevec1][iStevec2] = EnumSet.allOf(CellState.class);
            }
        }
        turnState = CellState.WHITE;
        clearAndInit();
        findGroups();
        listeners = new Array<MyClickListener>();
        clap = assetManager.get(AssetDescriptors.RESET_SOUND);

        gameManager = GameManager.INSTANCE;

    }

    public void resize(Camera boardCam, Viewport vp, HUD hud) {
        this.boardV = vp;
        if (this.hud != null) listeners.removeValue(this.hud, false);
        this.hud = hud;
        listeners.add(hud);
        Vector3 tmp = new Vector3(1, 0, 0);
        GdxUtils.ProjectWorldCoordinatesInScreenCoordinates(boardCam, tmp);
        gestureDetector = new GestureDetector(tmp.x / 2.5f, 0.2f, 0.5f, 0.5f, this);
        Gdx.input.setInputProcessor(gestureDetector);
    }

    public void newGame() {
        hud.start();
        state = GameState.BEGIN;
        turnState = CellState.WHITE;
        selectedExists=false;
        hud.setState(GameObjectRestartButton.STATE.START);
    }


    public void clearAndInit() {
        clearPlayingfield();
        placeStones();
    }

    public void clearPlayingfield(){
        for (int iStevec1 = 0; iStevec1 < width; iStevec1++) {
            for (int iStevec2 = 0; iStevec2 < height; iStevec2++) {
                playingField[iStevec1][iStevec2].clear();
            }
        }
    }

    public void placeStones(){
        for (int iStevec1 = 0; iStevec1 < width; iStevec1++) {
            for (int iStevec2 = 0; iStevec2 < height; iStevec2++) {
                if(iStevec2%2==0){
                    if(iStevec1%2!=0){
                        playingField[iStevec1][iStevec2] = EnumSet.of(CellState.BLACK);
                        //playingField[iStevec1][iStevec2].add(CellState.BLACK);
                    }else{
                        playingField[iStevec1][iStevec2].add(CellState.EMPTY);
                    }
                }else if(iStevec1%2==0){
                    playingField[iStevec1][iStevec2] = CellState.WH_MOVABLE.clone();
                }else{
                    playingField[iStevec1][iStevec2].add(CellState.EMPTY);
                }
            }
        }
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    public void clicked(float sx, float sy, boolean longClicked) {
        Vector3 touch = new Vector3(sx, sy, 0);
        log.debug("Clicked scr:" + sx + ", " + sy);
        boardV.unproject(touch);
        for (MyClickListener item : listeners) item.onClickEvent(touch.x, touch.y); //notifyAll
        if ((state == GameState.END_WIN) || (state == GameState.END_LOSE))
            return;
        if (state == GameState.BEGIN) {
            state = GameState.RUNNING;
        }
        int x = (int) touch.x;
        int y = (int) touch.y;
        log.debug("Clicked:" + x + ", " + y);
        if ((x > width) || (y > height) || (x < 0) || (y < 0))
            return; //Clicked out of ScreenMinesweeperController

        if(x<width&&x>=0&&y<height&&x>=0)
        {

                if(selectedExists){
                    if(playingField[x][y].contains(CellState.EMPTY)){
                        if(isAdjecentSelected(x, y, selected[0], selected[1])||isAdjecentGroup(x, y, selected[0], selected[1])){
                            playingField[selected[0]][selected[1]]=playingField[x][y].clone();
                            playingField[x][y]=tempState.clone();
                            if(!checkWin()){
                                advanceTurn();
                                recalculateMovable();
                                findGroups();
                            }else {
                                String sTemp = gameManager.getPrefs().getPlayer2name();
                                if(turnState==CellState.WHITE){
                                    sTemp = gameManager.getPrefs().getPlayer1name();
                                }
                                gameManager.saveResult(new Result(gameManager.getPrefs().getPlayer1name(), gameManager.getPrefs().getPlayer2name(), sTemp));
                            }
                            selectedExists=false;
                        }
                    }
                }else if(isMovable(x,y)){
                    selected[0]=x;
                    selected[1]=y;
                    tempState=playingField[x][y].clone();
                    selectedExists=true;
                }
        }
    }

    public void recalculateMovable(){
        for (int iStevec1 = 0; iStevec1 < width; iStevec1++) {
            for (int iStevec2 = 0; iStevec2 < height; iStevec2++) {
                if(playingField[iStevec1][iStevec2].contains(turnState)){
                    playingField[iStevec1][iStevec2].add(CellState.MOVABLE);
                }else{
                    playingField[iStevec1][iStevec2].remove(CellState.MOVABLE);
                }

            }
        }
    }

    public Boolean isMovable(int x, int y){
        return(playingField[x][y].contains(CellState.MOVABLE));
    }

    public Boolean isAdjecentSelected(int x, int y, int oldX, int oldY){
        if((oldX==x+1)||(oldX==x-1)){
            if(oldY==y+1||oldY==y-1||oldY==y){
                return (true);
            }
        }
        if((oldY==y+1)||(oldY==y-1)){
            if(oldX==x+1||oldX==x-1||oldX==x) {
                return (true);
            }
        }
        return(false);
    }

    public Boolean isAdjecentGroup(int x, int y, int oldX, int oldY){
        int iGroup=groups[oldX][oldY];
        if(y+1<=height-1){
            if(x+1<=width-1){
                if(iGroup==groups[x+1][y+1]){
                    return true;
                }
            }
            if(iGroup==groups[x][y+1]){
                return true;
            }
            if(x-1>=0){
                if(iGroup==groups[x-1][y+1]){
                    return true;
                }
            }
        }

        if(x+1<=width-1){
            if(iGroup==groups[x+1][y]){
                return true;
            }
        }
        if(x-1>=0){
            if(iGroup==groups[x-1][y]){
                return true;
            }
        }

        if(y-1>=0){
            if(x+1<=width-1){
                if(iGroup==groups[x+1][y-1]){
                    return true;
                }
            }
            if(iGroup==groups[x][y-1]){
                return true;
            }
            if(x-1>=0){
                if(iGroup==groups[x-1][y-1]){
                    return true;
                }
            }
        }

        return(false);
    }

    public void findGroups(){
        int groupNum=0, x=0, y=0;
        for (int iStevec1 = 0; iStevec1 < width; iStevec1++) {
            for (int iStevec2 = 0; iStevec2 < height; iStevec2++) {
                groups[iStevec1][iStevec2]=groupNum;
            }
        }
        groupNum++;

        for (int iStevec2 = 0; iStevec2 < height; iStevec2++) {
            for (int iStevec1 = 0; iStevec1 < width; iStevec1++) {
                if(playingField[iStevec1][iStevec2].contains(turnState)){
                    Boolean bFound=false;
                    y=iStevec2-1;
                    if(y>=0){
                        x=iStevec1+1;
                        if(x<=width-1){
                            if(groups[x][y]!=0){
                                groups[iStevec1][iStevec2]=groups[x][y];
                                bFound=true;
                            }
                        }
                        x=iStevec1;
                        if(groups[x][y]!=0){
                            groups[iStevec1][iStevec2]=groups[x][y];
                            bFound=true;
                        }
                        x=iStevec1-1;
                        if(x>=0){
                            if(groups[x][y]!=0){
                                groups[iStevec1][iStevec2]=groups[x][y];
                                bFound=true;
                            }
                        }
                    }
                    y=iStevec2;
                    x=iStevec1-1;
                    if(x>=0){
                        if(groups[x][y]!=0){
                            groups[iStevec1][iStevec2]=groups[x][y];
                            bFound=true;
                        }
                    }

                    if(!bFound){
                        groups[iStevec1][iStevec2]=groupNum;
                        groupNum++;
                    }

                }
            }
        }

        Boolean bChanged=false;
        do {
            bChanged=false;
            for (int iStevec2 = height-1; iStevec2 >= 0; iStevec2--) {
                for (int iStevec1 = 0; iStevec1 < width; iStevec1++) {
                    if(playingField[iStevec1][iStevec2].contains(turnState)){
                        x=iStevec1+1;
                        if(x<=width-1){
                            y=iStevec2;
                            if(groups[x][y]!=0&&groups[x][y]!=groups[iStevec1][iStevec2]){
                                if(groups[x][y]<groups[iStevec1][iStevec2]){
                                    groups[iStevec1][iStevec2]=groups[x][y];
                                }else{
                                    groups[x][y]=groups[iStevec1][iStevec2];
                                }

                                bChanged=true;
                            }
                        }
                        y=iStevec2+1;
                        if(y<=height-1){
                            x=iStevec1+1;
                            if(x<=width-1){
                                if(groups[x][y]!=0&&groups[x][y]!=groups[iStevec1][iStevec2]){
                                    if(groups[x][y]<groups[iStevec1][iStevec2]){
                                        groups[iStevec1][iStevec2]=groups[x][y];
                                    }else{
                                        groups[x][y]=groups[iStevec1][iStevec2];
                                    }

                                    bChanged=true;
                                }
                            }
                            x=iStevec1;
                            if(groups[x][y]!=0&&groups[x][y]!=groups[iStevec1][iStevec2]){
                                if(groups[x][y]<groups[iStevec1][iStevec2]){
                                    groups[iStevec1][iStevec2]=groups[x][y];
                                }else{
                                    groups[x][y]=groups[iStevec1][iStevec2];
                                }
                                bChanged=true;
                            }

                            x=iStevec1-1;
                            if(x>=0){
                                if(groups[x][y]!=0&&groups[x][y]!=groups[iStevec1][iStevec2]){
                                    if(groups[x][y]<groups[iStevec1][iStevec2]){
                                        groups[iStevec1][iStevec2]=groups[x][y];
                                    }else{
                                        groups[x][y]=groups[iStevec1][iStevec2];
                                    }
                                    bChanged=true;
                                }
                            }
                        }
                    }
                }
            }
        log.info("In loop");
        }while(bChanged);
    }

    public Boolean checkWin(){
        findGroups();
        Boolean bPassed=true;
        for (int iStevec2 = height-1; iStevec2 >= 0; iStevec2--) {
            String sOut= new String();
            for (int iStevec1 = 0; iStevec1 < width; iStevec1++) {
                sOut+=Integer.toString(groups[iStevec1][iStevec2]) + " ";
                if(groups[iStevec1][iStevec2]>1){
                    bPassed=false;
                }
            }
            log.info(""+sOut);
        }
        if(bPassed){
            state=GameState.END_WIN;
        }
        return(bPassed);
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        clicked(x, y, false);
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        clicked(x, y, true);
        return true;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

}
