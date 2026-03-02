package com.oceanview.ui;

import com.oceanview.service.AuthService;
import com.oceanview.service.ReservationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Legacy Main Menu - redirects to the new Dashboard for better UX.
 */
public class MainMenuFrame extends JFrame {

    private final ReservationService reservationService = new ReservationService();

    public MainMenuFrame() {
        initComponents();
        // Automatically redirect to Dashboard on next tick
        SwingUtilities.invokeLater(() -> {
            dispose();
            new DashboardFrame().setVisible(true);
        });
    }

    private void initComponents() {
        setTitle("Ocean View Resort – Redirecting...");
        setSize(400, 300);
        setLocationRelativeTo(null);
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UITheme.BG_PAGE);
        JLabel l = new JLabel("Loading Dashboard...", SwingConstants.CENTER);
        l.setFont(UITheme.FONT_HEADER);
        l.setForeground(UITheme.PRIMARY);
        p.add(l, BorderLayout.CENTER);
        add(p);
    }
}
