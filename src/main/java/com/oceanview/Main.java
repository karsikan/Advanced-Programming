package com.oceanview;

import com.oceanview.db.DatabaseConnection;
import com.oceanview.ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Initialize database on startup
        try {
            DatabaseConnection.getInstance();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to initialize database:\n" + e.getMessage(),
                    "Startup Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Start Web Service (Task B Requirement: Distributed Application)
        new com.oceanview.service.ReportWebService().start();

        // Launch UI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new LoginFrame().setVisible(true);
        });
    }
}
