package org.example.lib;

import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class isValid {

    // Method without parameters - always returns error
    public Integer checkIsNumber() {
        Error error = new Error("Invalid input!");
        return 1;
    }

    // Check if the input is a valid number (integer or decimal with max 1 digit after dot)
    public Integer checkIsNumber(String num) {
        // If not digits, dots, or slashes or more than one dot or starts/ends with dot
        if (!(num.matches("[0-9./]+")) || num.indexOf(".") != num.lastIndexOf(".")
                || num.charAt(0) == '.' || num.charAt(num.length() - 1) == '.') {
            Error error = new Error("Invalid input!");
            return 1;
        }

        //If decimal has more than 1 digit after the dot
        if (num.contains(".") && num.substring(num.indexOf(".")).length() > 1) {
            Error error = new Error("Please input only one digit after dot!");
        }

        return 0;
    }

    //Method without parameters: always returns error
    public Integer checkIsString() {
        Error error = new Error("Invalid Name! Use only letters!");
        return 1;
    }

    //Check if the input string contains only letters (Latin or Cyrillic)
    public Integer checkIsString(String str) {
        if (!(str.matches("[a-zа-яA-ZА-Я]+"))) {
            Error error = new Error("Invalid input! Use only letters!");
            return 1;
        }
        return 0;
    }

    //Check password strength and if it matches confirmation
    public Integer StrongPassword(String password, String confirmPassword, int control) {
        //Check if passwords match
        if (!password.equals(confirmPassword)) {
            new Error("Passwords do not match!");
            control++;
        }

        // Check minimum length
        if (password.length() < 8) {
            new Error("Too short! Please enter at least 8 characters!");
            control++;
        }

        //Check for at least one letter
        if (!password.matches(".*[a-zA-Zа-яА-Я].*")) {
            new Error("Password must contain at least one letter!");
            control++;
        }

        //Check for at least one number
        if (!password.matches(".*[0-9].*")) {
            new Error("Password must contain at least one number!");
            control++;
        }

        // Check for at least one special character
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\\\[\\\\]{};':\"\\\\\\\\|,.<>/?].*")) {
            new Error("Password must contain at least one special character!");
            control++;
        }

        return control;
    }

    // Check if the entered password matches the stored password
    public boolean PasswordChecker(String name, String password) {
        Database db = new Database();
        String passwordStr = db.hashPassword(password); // Hash entered password

        String filePath = "/Users/alina/Desktop/ex/src/main/java/org/example/lib/Users/" + name + ".json";

        try {
            // Read file content
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Convert content to JSON object
            JSONObject jsonObject = new JSONObject(content);

            // Get saved password from JSON
            String storedPassword = jsonObject.getString("password");

            // Compare saved password with entered password
            return storedPassword.equals(passwordStr);

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return false; // Return false if file can't be read or password doesn't match
    }
}
