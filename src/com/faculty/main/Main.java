package com.faculty.main;

import com.faculty.view.LoginView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Use the OS look and feel where available for a more native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // fall back to default look and feel
        }

        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
