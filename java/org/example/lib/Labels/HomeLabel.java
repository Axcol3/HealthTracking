package org.example.lib.Labels;

import org.example.lib.Person;
import org.example.lib.Database;
import org.example.lib.isValid;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;
import javax.swing.table.DefaultTableModel;

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
    int activity;

    public HomeLabel(String userName) {
        Person currentUser = loadUserFromFile(userName);
        name = userName;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        LocalDate today = LocalDate.now();
        LocalDate lastShownDate = readLastShownDate();

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
        frame.add(historyButton, BorderLayout.SOUTH);

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

            if (currentUser != null) {
                bloodField.setText(currentUser.getBlood());
                weightField.setText(currentUser.getWeight());
                heightField.setText(currentUser.getHeight());
                activity = Integer.parseInt(currentUser.getActivity());
            }

            JButton saveButton = new JButton("Save");

            saveButton.addActionListener(e -> {
                String blood = bloodField.getText().trim();
                String weight = weightField.getText().trim();
                String height = heightField.getText().trim();

                isValid validator = new isValid();
                int control = 0;
                control = validator.checkIsNumber(blood) + validator.checkIsNumber(weight) + validator.checkIsNumber(height) + validator.checkIsNumber(String.valueOf(activity));

                if (!blood.isEmpty() && !weight.isEmpty() && !height.isEmpty() && activity > 0) {
                    if (currentUser != null) {
                        currentUser.setBlood(blood);
                        currentUser.setWeight(weight);
                        currentUser.setHeight(height);
                        currentUser.setActivity(String.valueOf(activity));
                        currentUser.setName(userName);

                        currentUser.addHistory("Updated user: " + userName);
                        currentUser.setLastUpdated(today);

                        db.saveUser(currentUser);

                        JOptionPane.showMessageDialog(frame, "Data updated!");
                        frame.remove(updatePanel);
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

            JPanel panel4 = new JPanel(new FlowLayout());

            JButton active = new JButton("Active");
            JButton averageActive = new JButton("Average");
            JButton noActive = new JButton("Inactive");

            Color defaultColor = active.getBackground();

            panel4.add(active);
            panel4.add(averageActive);
            panel4.add(noActive);

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
            saveCurrentDate(today);
        } else {
            JLabel label = new JLabel("Welcome " + name, SwingConstants.CENTER);
            currentUser.addHistory("Login success");
            db.saveUser(currentUser);
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            frame.add(label, BorderLayout.CENTER);
        }

        showUserTable(currentUser);

        Home();
        frame.setVisible(true);
    }

    private void showUserTable(Person currentUser) {
        String[] columnNames = {"Attribute", "Value"};
        Object[][] data = {
                {"Name", currentUser.getName()},
                {"Blood Type", currentUser.getBlood()},
                {"Weight", currentUser.getWeight()},
                {"Height", currentUser.getHeight()},
                {"Activity Level", currentUser.getActivity()}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    private void resetButtonColors(JButton a, JButton b, JButton c, Color color) {
        a.setBackground(color);
        b.setBackground(color);
        c.setBackground(color);
    }

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

    private static void saveCurrentDate(LocalDate date) {
        try {
            Files.writeString(Paths.get(LAST_POPUP_FILE), date.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

        JButton deleteUser = new JButton("Delete User");
        deleteUser.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this user?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                db.deleteUserFile(name);
                frame.dispose();
            }
        });

        mainPanel.add(updatePassword);
        mainPanel.add(updateName);
        mainPanel.add(deleteUser);
        frame.add(mainPanel, BorderLayout.EAST);
        frame.revalidate();
        frame.repaint();
    }
}
