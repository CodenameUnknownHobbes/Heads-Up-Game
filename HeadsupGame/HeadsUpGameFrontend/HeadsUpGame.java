

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HeadsUpGame extends JFrame {
    private JTextField playerNameField;
    private DefaultListModel<String> playerListModel;
    private JList<String> playerList;
    private JButton registerButton;
    private JButton startGameButton;

    private List<String> players = new ArrayList<>();

    public HeadsUpGame() {
        setTitle("Heads UP!");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        playerNameField = new JTextField(20);
        registerButton = new JButton("Register");
        startGameButton = new JButton("Start Game");
        startGameButton.setEnabled(false); // Disable until at least 2 players are registered

        playerListModel = new DefaultListModel<>();
        playerList = new JList<>(playerListModel);

        add(new JLabel("Enter Player Name:"));
        add(playerNameField);
        add(registerButton);
        add(new JLabel("Registered Players:"));
        add(new JScrollPane(playerList));
        add(startGameButton);

        registerButton.addActionListener(new RegisterPlayerAction());
        startGameButton.addActionListener(new StartGameAction());

        setVisible(true);
    }

    private class RegisterPlayerAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String playerName = playerNameField.getText().trim();
            if (!playerName.isEmpty()) {
                CompletableFuture.runAsync(() -> {
                    try {
                        HttpClient client = HttpClient.newHttpClient();
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/api/register"))
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofString("\"" + playerName + "\""))
                                .build();

                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                        if (response.statusCode() == 200) {
                            players.add(playerName);
                            SwingUtilities.invokeLater(() -> updatePlayerList());
                        } else {
                            JOptionPane.showMessageDialog(null, "Error registering player.");
                        }
                    } catch (IOException | InterruptedException ex) {
                        ex.printStackTrace();
                    }
                });
                playerNameField.setText(""); // Clear input field
            }
        }
    }

    private class StartGameAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Fetch categories from backend and display them
            CompletableFuture.runAsync(() -> {
                try {
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8080/api/categories"))
                            .GET()
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == 200) {
                        // Handle categories response here
                        String categoriesJson = response.body();
                        // You can parse and display categories as needed
                        JOptionPane.showMessageDialog(null, "Categories fetched successfully:\n" + categoriesJson);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error fetching categories.");
                    }
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    private void updatePlayerList() {
        playerListModel.clear();
        for (String player : players) {
            playerListModel.addElement(player);
        }
        
        if (players.size() >= 2) {
            startGameButton.setEnabled(true); // Enable button if at least 2 players are registered
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HeadsUpGame::new);
    }
}
