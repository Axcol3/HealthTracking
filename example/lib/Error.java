package org.example.lib;

import javax.swing.*;
import java.awt.*;
//Error window it works when input didn't pass isValid
public class Error {
    // Synchronized to ensure only one thread can access this at a time
    private static boolean isWindowOpen = false;
    public Error(String message){
        if(isWindowOpen){
            return;
        }
        String msg = message;
        isWindowOpen = true;
        JFrame frame = new JFrame();
        frame.setTitle("Error");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 200);
        frame.setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 60));
        JLabel label = new JLabel(msg);
        panel.add(label);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 60));

        JButton button = new JButton("OK");
        panel.add(button);
        frame.add(panel);

        button.addActionListener(e -> {
            frame.dispose();
            isWindowOpen = false;
        });

        frame.setVisible(true);
    }
}
