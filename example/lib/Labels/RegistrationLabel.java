package org.example.lib.Labels;

import org.example.lib.*;
import org.example.lib.Error;

import javax.swing.*;
import java.awt.*;

public class RegistrationLabel {

    // Input fields for user information
    private final JTextField nameField, ageField, weightField, heightField, bloodField;
    private final JPasswordField passwordField, confirmPasswordField;
    private int gender = 0;
    private int activity = 0;

    public RegistrationLabel() {
        JFrame frame = new JFrame("Registration");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close app when window is closed
        frame.setSize(600, 600); // Window size
        frame.setLocationRelativeTo(null); // Center the window
        frame.setLayout(new BorderLayout());

        GridBagConstraints gbc = new GridBagConstraints(); // Used for layout settings

        // Main panel to hold all other panels vertically
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Panel with input fields for name, age, password
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Age:"));
        ageField = new JTextField();
        panel.add(ageField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        panel.add(confirmPasswordField);

        // Panel for gender selection
        JPanel panel2 = new JPanel(new GridLayout(1, 3, 10, 10));
        panel2.add(new JLabel("Gender: "));
        JButton buttonMale = new JButton("Male");
        JButton buttonFemale = new JButton("Female");
        panel2.add(buttonMale);
        panel2.add(buttonFemale);
        Color defaultColor = buttonMale.getBackground(); // Save default color

        // Set gender and button color on click
        buttonMale.addActionListener(e -> {
            buttonFemale.setBackground(defaultColor);
            buttonMale.setBackground(Color.BLUE);
            buttonMale.setOpaque(true);
            gender = 1; // Male
        });

        buttonFemale.addActionListener(e -> {
            buttonMale.setBackground(defaultColor);
            buttonFemale.setBackground(Color.PINK);
            buttonFemale.setOpaque(true);
            gender = 2; // Female
        });

        // Panel with weight, height, and blood pressure inputs
        JPanel panel3 = new JPanel(new GridLayout(3, 2, 10, 10));
        panel3.add(new JLabel("Weight(kg): "));
        weightField = new JTextField(20);
        panel3.add(weightField, gbc);

        panel3.add(new JLabel("Height(cm): "));
        heightField = new JTextField(20);
        panel3.add(heightField, gbc);

        panel3.add(new JLabel("Blood pressure: "));
        bloodField = new JTextField(20);
        panel3.add(bloodField, gbc);

        // Panel for activity level selection
        JPanel panel4 = new JPanel(new GridLayout(1, 4, 10, 10));
        panel4.add(new JLabel("How active are you?: "));

        JButton active = new JButton("Active");
        JButton averageActive = new JButton("Average");
        JButton noActive = new JButton("Inactive");

        panel4.add(active);
        panel4.add(averageActive);
        panel4.add(noActive);

        // Set activity level and highlight selected button
        active.addActionListener(e -> {
            noActive.setBackground(defaultColor);
            averageActive.setBackground(defaultColor);
            active.setBackground(Color.GREEN);
            active.setOpaque(true);
            activity = 1; // Active
        });

        averageActive.addActionListener(e -> {
            noActive.setBackground(defaultColor);
            active.setBackground(defaultColor);
            averageActive.setBackground(Color.YELLOW);
            averageActive.setOpaque(true);
            activity = 2; // Average
        });

        noActive.addActionListener(e -> {
            active.setBackground(defaultColor);
            averageActive.setBackground(defaultColor);
            noActive.setBackground(Color.RED);
            noActive.setOpaque(true);
            activity = 3; // Inactive
        });

        // Bottom panel with "Back" and "Submit" buttons
        Panel panel5 = new Panel(new FlowLayout(FlowLayout.LEFT, 50, 60));
        JButton back = new JButton("Back");
        panel5.add(back);

        // Go back to main screen
        back.addActionListener(e -> {
            new MainLabel(); // Open MainLabel window
            frame.dispose(); // Close current window
        });

        panel5.setLayout(new FlowLayout(FlowLayout.RIGHT, 50, 60));
        JButton submit = new JButton("Submit");
        panel5.add(submit);

        // When user clicks "Submit"
        submit.addActionListener(e -> {
            String Name = nameField.getText().trim();
            String Age = ageField.getText().trim();
            String Weight = weightField.getText().trim();
            String Height = heightField.getText().trim();
            String Blood = bloodField.getText().trim();

            char[] PasswordChars = passwordField.getPassword();
            String PasswordStr = new String(PasswordChars);

            char[] confirmPasswordChars = confirmPasswordField.getPassword();
            String ConfirmPasswordStr = new String(confirmPasswordChars);

            isValid IsValid = new isValid();
            int control = 0;

            // Validate input data
            control = IsValid.checkIsString(Name) +
                    IsValid.checkIsNumber(Age) +
                    IsValid.checkIsNumber(Weight) +
                    IsValid.checkIsNumber(Height) +
                    IsValid.checkIsNumber(Blood) +
                    IsValid.StrongPassword(PasswordStr, ConfirmPasswordStr, control);

            // If everything is valid and gender/activity is selected
            if (control == 0 && gender != 0 && activity != 0) {
                Database db = new Database();

                // Save user info to JSON file
                db.UserJSON(Name, PasswordStr, Age, Blood, Integer.toString(gender), Weight, Height, Integer.toString(activity));

                JOptionPane.showMessageDialog(frame, "✅ Welcome, " + Name + "!");
                new HomeLabel(Name); // Go to Home screen
                frame.dispose(); // Close current screen
            } else {
                new Error("Invalid input!"); // Show error message
            }
        });

        // Add all panels to the main panel
        mainPanel.add(panel);
        mainPanel.add(panel2);
        mainPanel.add(panel3);
        mainPanel.add(panel4);
        mainPanel.add(panel5);

        mainPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 9)); // Add green border
        frame.add(mainPanel, BorderLayout.CENTER); // Add main panel to frame

        frame.setVisible(true); // Show window
    }
}
