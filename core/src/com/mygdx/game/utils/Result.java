package com.mygdx.game.utils;

public class Result {
    String player1;
    String player2;
    String winner;

    public Result(String player1, String player2, String winner) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
    }

    public Result() {
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public String getWinner() {
        return winner;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String toString(){
        return(player1+" vs "+player2+"; winner: "+winner);
    }
}
