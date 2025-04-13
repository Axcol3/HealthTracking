package org.example.lib.Labels;

import org.example.lib.Person;
import org.example.lib.Database;
import org.example.lib.isValid;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class HomeLabel {
    private static final String LAST_POPUP_FILE = "/Users/alina/Desktop/ex/src/main/java/org/example/lib/last_popup.txt";

    JFrame frame = new JFrame("Home ");
    Database db = new Database();
    String storedPassword;
    String storedName;
    String name;
    int activity; // User's activity level

    // Constructor. This runs when the user logs in.
    public HomeLabel(String userName) {
        Person currentUser = loadUserFromFile(userName); // Load user info from file
        name = userName;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the window when user exits
        frame.setSize(800, 600); // Set window size
        frame.setLocationRelativeTo(null); // Center window
        frame.setLayout(new BorderLayout());

        LocalDate today = LocalDate.now(); // Get today's date
        LocalDate lastShownDate = readLastShownDate(); // Get last date when popup was shown

        // Button to save user's history to a file
        JButton historyButton = new JButton("History");
        historyButton.addActionListener(e -> {
            String userFilePath = "/Users/alina/Desktop/ex/src/main/java/org/example/lib/Users/" + userName + ".json";
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save file as...");
            fileChooser.setSelectedFile(new File("history.json"));

            int userSelection = fileChooser.showSaveDialog(frame);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File destinationFile = fileChooser.getSelectedFile();
                try {
                    Files.copy(Path.of(userFilePath), destinationFile.toPath());
                    JOptionPane.showMessageDialog(frame, "File saved to:\n" + destinationFile.getAbsolutePath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error:\n" + ex.getMessage());
                }
            }
        });
        frame.add(historyButton, BorderLayout.SOUTH); // Add button to bottom of window

        // Show popup only if today's date is different from last shown date
        if (!today.equals(lastShownDate)) {
            JPanel updatePanel = new JPanel();
            updatePanel.setLayout(new BoxLayout(updatePanel, BoxLayout.Y_AXIS));

            JLabel updateLabel = new JLabel("Update your information:");
            JTextField bloodField = new JTextField(20);
            JTextField weightField = new JTextField(20);
            JTextField heightField = new JTextField(20);

            JLabel bloodLabel = new JLabel("Blood type:");
            JLabel weightLabel = new JLabel("Weight (kg):");
            JLabel heightLabel = new JLabel("Height (cm):");
            JLabel activityLabel = new JLabel("Activity level:");

            JButton saveButton = new JButton("Save");

            saveButton.addActionListener(e -> {
                String blood = bloodField.getText().trim();
                String weight = weightField.getText().trim();
                String height = heightField.getText().trim();

                isValid validator = new isValid();
                int control = 0;
                control = validator.checkIsNumber(blood) + validator.checkIsNumber(weight) + validator.checkIsNumber(height) + validator.checkIsNumber(String.valueOf(activity));

                // Check if all fields are filled correctly
                if (!blood.isEmpty() && !weight.isEmpty() && !height.isEmpty() && activity > 0) {
                    if (currentUser != null) {
                        // Update user data
                        currentUser.setBlood(blood);
                        currentUser.setWeight(weight);
                        currentUser.setHeight(height);
                        currentUser.setActivity(String.valueOf(activity));
                        currentUser.setName(userName);

                        currentUser.addHistory("Updated user: " + userName);
                        currentUser.setLastUpdated(today); // Save today's date

                        db.saveUser(currentUser); // Save user to file

                        JOptionPane.showMessageDialog(frame, "Data updated!");
                        frame.remove(updatePanel); // Remove update form
                        frame.revalidate();
                        frame.repaint();

                        JLabel label = new JLabel("Welcome back, " + userName + "!", SwingConstants.CENTER);
                        label.setFont(new Font("Arial", Font.PLAIN, 16));
                        frame.add(label, BorderLayout.CENTER);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                }
            });

            updatePanel.add(updateLabel);
            updatePanel.add(bloodLabel);
            updatePanel.add(bloodField);
            updatePanel.add(weightLabel);
            updatePanel.add(weightField);
            updatePanel.add(heightLabel);
            updatePanel.add(heightField);
            updatePanel.add(activityLabel);

            // Buttons to choose activity level
            JPanel panel4 = new JPanel(new FlowLayout());

            JButton active = new JButton("Active");
            JButton averageActive = new JButton("Average");
            JButton noActive = new JButton("Inactive");

            Color defaultColor = active.getBackground(); // Remember default color

            panel4.add(active);
            panel4.add(averageActive);
            panel4.add(noActive);

            // Set color and activity when a button is clicked
            active.addActionListener(e -> {
                resetButtonColors(active, averageActive, noActive, defaultColor);
                active.setBackground(Color.GREEN);
                activity = 1;
            });

            averageActive.addActionListener(e -> {
                resetButtonColors(active, averageActive, noActive, defaultColor);
                averageActive.setBackground(Color.YELLOW);
                activity = 2;
            });

            noActive.addActionListener(e -> {
                resetButtonColors(active, averageActive, noActive, defaultColor);
                noActive.setBackground(Color.RED);
                activity = 3;
            });

            updatePanel.add(panel4);
            updatePanel.add(saveButton);

            frame.add(updatePanel, BorderLayout.CENTER);
            saveCurrentDate(today); // Save today's date to file
        } else {
            JLabel label = new JLabel("Welcome " + name, SwingConstants.CENTER);
            currentUser.addHistory("Login success");
            db.saveUser(currentUser);
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            frame.add(label, BorderLayout.CENTER);
        }

        Home(); // Create right panel with buttons
        frame.setVisible(true); // Show window
    }

    // Reset the button colors to default
    private void resetButtonColors(JButton a, JButton b, JButton c, Color color) {
        a.setBackground(color);
        b.setBackground(color);
        c.setBackground(color);
    }

    // Load user from JSON file
    private static Person loadUserFromFile(String userName) {
        String userFilePath = "/Users/alina/Desktop/ex/src/main/java/org/example/lib/Users/" + userName + ".json";
        File userFile = new File(userFilePath);
        if (userFile.exists()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                return objectMapper.readValue(userFile, Person.class);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error loading user: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "User file not found.");
        }
        return null;
    }

    // Read the last date when popup was shown
    private static LocalDate readLastShownDate() {
        try {
            Path path = Paths.get(LAST_POPUP_FILE);
            if (!Files.exists(path)) return null;
            String dateStr = Files.readString(path).trim();
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    // Save today's date to the file
    private static void saveCurrentDate(LocalDate date) {
        try {
            Files.writeString(Paths.get(LAST_POPUP_FILE), date.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create right-side panel with buttons to update/delete user
    public void Home() {
        String filePath = "/Users/alina/Desktop/ex/src/main/java/org/example/lib/Users/" + name + ".json";
        isValid validator = new isValid();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject jsonObject = new JSONObject(content);
            storedPassword = jsonObject.getString("password");
            storedName = jsonObject.getString("name");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        Person currentUser = loadUserFromFile(name);

        // Button to update password
        JButton updatePassword = new JButton("Update Password");
        updatePassword.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(3, 2));
            JTextField oldPasswordField = new JPasswordField();
            JTextField newPasswordField = new JPasswordField();
            JTextField confirmPasswordField = new JPasswordField();

            panel.add(new JLabel("Old password:"));
            panel.add(oldPasswordField);
            panel.add(new JLabel("New password:"));
            panel.add(newPasswordField);
            panel.add(new JLabel("Confirm new password:"));
            panel.add(confirmPasswordField);

            int result = JOptionPane.showConfirmDialog(frame, panel, "Update Password", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String newPass = newPasswordField.getText();
                String confirmPass = confirmPasswordField.getText();
                int control = validator.StrongPassword(newPass, confirmPass, 0);
                if (control == 0) {
                    currentUser.setPassword(db.hashPassword(newPass));
                    currentUser.addHistory("Password changed.");
                    db.saveUser(currentUser);
                    JOptionPane.showMessageDialog(frame, "Password changed.");
                }
            }
        });

        // Button to update name
        JButton updateName = new JButton("Update Name");
        updateName.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(frame, "Enter new username:");
            if (newName != null && !newName.trim().isEmpty()) {
                db.renameUserFile(name, newName);
                currentUser.addHistory(name + " changed to " + newName);
                currentUser.setName(newName);
                db.saveUser(currentUser);
                JOptionPane.showMessageDialog(frame, "Name changed: " + newName);
            }
        });

        // Button to delete user
        JButton deleteUser = new JButton("Delete User");
        deleteUser.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this user?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                db.deleteUserFile(name);
                frame.dispose(); // Close the window
            }
        });

        // Add all buttons to right-side panel
        mainPanel.add(updatePassword);
        mainPanel.add(updateName);
        mainPanel.add(deleteUser);
        frame.add(mainPanel, BorderLayout.EAST); // Add on the right
        frame.revalidate();
        frame.repaint();
    }
}
