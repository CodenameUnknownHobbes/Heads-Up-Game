package com.headupgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HeadsUpGame {
    private Map<String, Category> categories; // Holds all categories and their words
    private List<Player> players;
    private boolean isGameActive;

    public HeadsUpGame(Map<String, Category> categories) {
        this.categories = categories;
        players = new ArrayList<>();
        isGameActive = false;
    }

    public void registerPlayer(String name) {
        if (!isGameActive) {
            players.add(new Player(name));
        }
    }

    public void startGame() {
        isGameActive = true;
    }

    public void endGame() {
        isGameActive = false;
        displayScores();
    }

    public void incrementScore(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                player.incrementScore();
                break;
            }
        }
    }

    public void displayScores() {
        System.out.println("Scores:");
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getScore());
        }
    }
}