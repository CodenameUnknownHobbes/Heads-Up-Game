package com.headupgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HeadsUpGameApplication {
    private static final String BASE_URL = "http://localhost:8080/api/";

    public static void main(String[] args) throws IOException {
        List<String> players = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Player Registration
        while (true) {
            System.out.print("Enter player name (or type 'start' to begin the game): ");
            String playerName = reader.readLine().trim();

            if (playerName.equalsIgnoreCase("start")) {
                if (players.size() < 2) {
                    System.out.println("You need at least 2 players to start the game.");
                    continue;
                }
                break;
            }

            if (!playerName.isEmpty()) {
                registerPlayer(playerName);
                players.add(playerName);
                System.out.println("Player registered: " + playerName);
            }
        }

        // Fetch Categories
        fetchCategories();
    }

    private static void registerPlayer(String playerName) {
        try {
            URL url = new URL(BASE_URL + "register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "\"" + playerName + "\"";
            conn.getOutputStream().write(jsonInputString.getBytes());

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Player registered successfully.");
            } else {
                System.out.println("Failed to register player.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fetchCategories() {
        try {
            URL url = new URL(BASE_URL + "categories");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Display categories
                System.out.println("Available Categories:");
                System.out.println(response.toString());
            } else {
                System.out.println("Failed to fetch categories.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
