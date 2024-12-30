package main.java.com.headupgame;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GameController {
    
    private List<Player> players = new ArrayList<>();
    private Map<String, List<String>> wordsByCategory = new HashMap<>();

    public GameController() {
        try {
            // Load words from the Excel file into the map
            ExcelReader excelReader = new ExcelReader();
            wordsByCategory = excelReader.readExcel("src/main/resources/HeadsUpGameDataSet-copy.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/register")
    public String registerPlayer(@RequestBody String playerName) {
        players.add(new Player(playerName));
        return "Player registered: " + playerName;
    }

    @PostMapping("/select-category")
    public String selectCategory(@RequestParam String playerName, @RequestParam String category) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                player.setSelectedCategory(category);
                return "Category selected for " + playerName + ": " + category;
            }
        }
        return "Player not found.";
    }

    @PostMapping("/guess")
    public String guessWord(@RequestParam String playerName, @RequestParam boolean isCorrect) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                if (isCorrect) {
                    player.setScore(player.getScore() + 1);
                    return "Correct guess!";
                } else {
                    return "Incorrect guess!";
                }
            }
        }
        return "Player not found.";
    }

    @GetMapping("/scores")
    public List<Player> getScores() {
        return players;
    }

    @GetMapping("/categories")
    public Map<String, List<String>> getCategories() {
        return wordsByCategory; // Return available categories and their words
    }
}
