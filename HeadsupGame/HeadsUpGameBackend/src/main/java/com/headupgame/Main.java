package com.headupgame;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ExcelReader excelReader = new ExcelReader();
        
        try {
            Map<String, Category> categories = excelReader.readExcel("src/main/resources/HeadsUpGameDataSet-copy.xlsx");
            HeadsUpGame game = new HeadsUpGame(categories);
            
            // Example of registering players and starting the game.
            game.registerPlayer("Alice");
            game.registerPlayer("Bob");
            game.startGame();

            System.out.println("Game initialized with categories: " + categories.keySet());
            
            // Further game logic can be added here

            game.endGame(); // End the game at some point
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
