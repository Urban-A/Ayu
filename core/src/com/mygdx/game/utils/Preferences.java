package com.mygdx.game.utils;

public class Preferences {
    Boolean sound;
    int tileAmmount;
    String player1name;
    String Player2name;

    public Preferences(Boolean sound, int tileAmmount, String player1name, String player2name) {
        this.sound = sound;
        this.tileAmmount = tileAmmount;
        this.player1name = player1name;
        Player2name = player2name;
    }

    public Preferences() {
        this.sound = true;
        this.tileAmmount = 7;
        this.player1name = "white";
        Player2name = "black";
    }

    public Boolean getSound() {
        return sound;
    }

    public void setSound(Boolean sound) {
        this.sound = sound;
    }

    public int getTileAmmount() {
        return tileAmmount;
    }

    public void setTileAmmount(int tileAmmount) {
        this.tileAmmount = tileAmmount;
    }

    public String getPlayer1name() {
        return player1name;
    }

    public void setPlayer1name(String player1name) {
        this.player1name = player1name;
    }

    public String getPlayer2name() {
        return Player2name;
    }

    public void setPlayer2name(String player2name) {
        Player2name = player2name;
    }
}
