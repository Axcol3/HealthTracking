package org.example.lib;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONArray;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {

    // Method to create or update user information in a JSON file
    public void UserJSON(String name, String password, String age, String blood, String gender, String weight, String height, String activity) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register module to handle date-time formats
        String directoryPath = "/Users/alina/Desktop/ex/src/main/java/org/example/lib/Users/";
        String fileName = name + ".json"; // User file name based on username
        File file = new File(directoryPath + fileName); // Create a file object for the user file

        Person person;

        // Check if file exists, and load existing user data if available
        if (file.exists()) {
            try {
                person = objectMapper.readValue(file, Person.class);
                System.out.println("Loaded existing user.");
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
                return;
            }
        } else {
            // If the file doesn't exist, create a new user
            person = new Person();
            person.setName(name); // Set user name
            person.setHistory(new ArrayList<>()); // Initialize the user's history
            System.out.println("Created new user.");
        }

        // Update or add user information
        person.setPassword(hashPassword(password)); // Hash and set the password
        person.setAge(age);
        person.setBlood(blood);
        person.setGender(gender);
        person.setWeight(weight);
        person.setHeight(height);
        person.setActivity(activity);
        person.addHistory("A new user has been created"); // Add creation entry to history
        LocalDate today = LocalDate.now();
        person.setLastUpdated(today); // Set the last updated date

        // Ensure history is initialized if it's null, then add the update entry
        if (person.getHistory() == null) {
            person.setHistory(new ArrayList<>());
        }
        person.getHistory().add("Updated: " + today);

        // Save updated user data to the file
        try {
            objectMapper.writeValue(file, person);
            System.out.println("File successfully updated: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    // Method to check if a user file exists
    public boolean FileSearch(String name) {
        String directoryPath = "/Users/alina/Desktop/ex/src/main/java/org/example/lib/Users/";
        String fileName = name + ".json"; // User file name based on username

        File directory = new File(directoryPath);

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                // Check if the user file exists in the directory
                for (File file : files) {
                    if (file.getName().equals(fileName)) {
                        System.out.println("File found: " + file.getAbsolutePath());
                        return true;
                    }
                }
            }
            System.out.println("File not found.");
        } else {
            System.out.println("The specified path is not a directory.");
        }
        return false;
    }

    // Method to hash a password using SHA-256 algorithm
    public String hashPassword(String input) {
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());

            // Convert each byte to a hexadecimal string
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // Handle the exception if the algorithm is not found
        }
        return hexString.toString();
    }

    // Method to save a user's data in a JSON file
    public void saveUser(Person person) {
        String filePath = "/Users/alina/Desktop/ex/src/main/java/org/example/lib/Users/" + person.getName() + ".json";

        try {
            JSONObject jsonObject;
            File file = new File(filePath);

            if (file.exists()) {
                // Read existing user data if the file exists
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                jsonObject = new JSONObject(content);
            } else {
                // If the file doesn't exist, create a new JSON object
                jsonObject = new JSONObject();
            }
            int control = 0;

            // Update the user's information in the JSON file if the values have changed
            updateIfDifferent(jsonObject, "name", person.getName());
            updateIfDifferent(jsonObject, "age", person.getAge());
            updateIfDifferent(jsonObject, "password", person.getPassword());
            updateIfDifferent(jsonObject, "weight", person.getWeight());
            updateIfDifferent(jsonObject, "height", person.getHeight());
            updateIfDifferent(jsonObject, "blood", person.getBlood());
            updateIfDifferent(jsonObject, "gender", person.getGender());
            updateIfDifferent(jsonObject, "activity", person.getActivity());

            // Update the last updated date
            jsonObject.put("lastUpdated", person.getLastUpdated().toString());

            // Update history with new entries if they are not already present
            JSONArray oldHistory = jsonObject.has("history") ? jsonObject.getJSONArray("history") : new JSONArray();
            List<String> newHistory = person.getHistory();
            for (String entry : newHistory) {
                if (!oldHistory.toList().contains(entry)) {
                    oldHistory.put(entry);
                }
            }

            // Save the updated history back to the JSON object
            jsonObject.put("history", oldHistory);

            // Write the updated data to the user file
            Files.write(Paths.get(filePath), jsonObject.toString(4).getBytes());
            System.out.println("User data successfully saved.");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Helper method to update a field in the JSON object if the new value is different
    private void updateIfDifferent(JSONObject json, String key, String newValue) {
        if (newValue != null && (!json.has(key) || !newValue.equals(json.optString(key)))) {
            json.put(key, newValue);
        }
    }

    // Method to rename a user's file
    public void renameUserFile(String oldName, String newName) {
        String userDir = "/Users/alina/Desktop/ex/src/main/java/org/example/lib/Users/";
        File oldFile = new File(userDir + oldName + ".json");
        File newFile = new File(userDir + newName + ".json");

        if (oldFile.exists()) {
            boolean success = oldFile.renameTo(newFile);
            if (success) {
                JOptionPane.showMessageDialog(null, "File was renamed!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to rename file.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Old file not found.");
        }
    }

    // Method to delete a user's file
    public void deleteUserFile(String userName) {
        String userFilePath = "/Users/alina/Desktop/ex/src/main/java/org/example/lib/Users/" + userName + ".json";
        File file = new File(userFilePath);

        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                JOptionPane.showMessageDialog(null, "The user has been successfully deleted.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete file.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "File not found.");
        }
    }

}
