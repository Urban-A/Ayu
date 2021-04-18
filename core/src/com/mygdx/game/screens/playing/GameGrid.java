package com.mygdx.game.screens.playing;

import java.util.EnumSet;

public class GameGrid {
    public int width;
    public int height;

    public enum CellState {
        BLACK, WHITE, EMPTY, MOVABLE;

        public static EnumSet<CellState> WH_MOVABLE = EnumSet.of(WHITE, MOVABLE);
        public static EnumSet<CellState> BL_MOVABLE = EnumSet.of(BLACK, MOVABLE);
    }
    public EnumSet<CellState>[][] playingField;

    public GameGrid(int width, int height)
    {
        this.width = width;
        this.height = height;
        playingField = (EnumSet<CellState>[][]) new EnumSet<?>[width][height];
        clearAndInit();
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
                if(iStevec1%2==0){
                    if(iStevec2%2!=0){
                        playingField[iStevec1][iStevec2].copyOf(CellState.BL_MOVABLE);
                    }
                }else if(iStevec2%2==0){
                    playingField[iStevec1][iStevec2].copyOf(CellState.WH_MOVABLE);
                }else{
                    playingField[iStevec1][iStevec2].add(CellState.EMPTY);
                }
            }
        }
    }

}
