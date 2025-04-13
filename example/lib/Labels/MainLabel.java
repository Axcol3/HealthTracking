package org.example.lib.Labels;

import javax.swing.*;
import java.awt.*;
import org.example.lib.Database;
import org.example.lib.isValid;

public class MainLabel {
    private final JTextField nameField;
    private final JPasswordField passwordField;

    public MainLabel() {
        // Create the main frame for the "Welcome to Tracking Health" window
        JFrame frame = new JFrame("Welcome to Tracking Health");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300); // Set the size of the window
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setLayout(new BorderLayout()); // Use BorderLayout for layout management

        // Main panel for organizing elements vertically
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40)); // Set padding around the panel

        // Form panel to hold labels and text fields in a 2x2 grid layout
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        // Name field
        JLabel nameLabel = new JLabel("Name:"); // Label for name input
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Set font for the label
        nameField = new JTextField(); // Create a text field for entering the name
        formPanel.add(nameLabel);
        formPanel.add(nameField);

        // Password field
        JLabel passLabel = new JLabel("Password:"); // Label for password input
        passLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Set font for the label
        passwordField = new JPasswordField(); // Create a password field for entering password
        formPanel.add(passLabel);
        formPanel.add(passwordField);

        // Button panel for the Login and Register buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton submit = new JButton("Login"); // Login button
        JButton registrButton = new JButton("Register"); // Register button

        submit.setPreferredSize(new Dimension(120, 30)); // Set preferred size for buttons
        registrButton.setPreferredSize(new Dimension(120, 30));

        buttonPanel.add(submit);
        buttonPanel.add(registrButton);

        // Action for the Login button
        submit.addActionListener(e -> {
            String Name = nameField.getText().trim(); // Get the entered name
            char[] PasswordChars = passwordField.getPassword(); // Get the entered password
            String PasswordStr = new String(PasswordChars);

            isValid validator = new isValid(); // Create an instance of isValid for validation
            Database db = new Database(); // Create an instance of Database to check user

            // Check if the user exists in the database
            if (db.FileSearch(Name)) {
                // Check if the entered password matches the stored password
                if (validator.PasswordChecker(Name, PasswordStr)) {
                    // Successful login
                    JOptionPane.showMessageDialog(frame, "✅ Welcome, " + Name + "!");
                    new HomeLabel(Name); // Open the HomeLabel screen for the logged-in user
                    frame.dispose(); // Close the login window
                } else {
                    // Incorrect login or password
                    JOptionPane.showMessageDialog(frame, "❌ Incorrect login or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // User does not exist in the database
                JOptionPane.showMessageDialog(frame, "❌ Incorrect login or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action for the Register button
        registrButton.addActionListener(e -> {
            // Open the Registration window
            new RegistrationLabel();
            frame.dispose(); // Close the login window
        });

        // Add form panel and button panel to the main panel
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20)); // Add vertical space between panels
        mainPanel.add(buttonPanel);

        // Add the main panel to the frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // Make the frame visible
        frame.setVisible(true);
    }
}
