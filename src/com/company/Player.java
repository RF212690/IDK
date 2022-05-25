package com.company;

public class Player {
    int playerColor;
    int playerRating;
    String playerName;

    public int getPlayerColor() {
        return playerColor;
    }

    public int getPlayerRating() {
        return playerRating;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerColor(int playerColor) {
        this.playerColor = playerColor;
    }

    public void setPlayerRating(int playerRating) {
        this.playerRating = playerRating;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Player(int playerColor, int playerRating, String playerName) {
        this.playerColor = playerColor;
        this.playerRating = playerRating;
        this.playerName = playerName;
    }
}
