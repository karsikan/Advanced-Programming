package com.oceanview.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class HelpFrame extends JFrame {

    public HelpFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Ocean View Resort – Help & Guide");
        setSize(680, 660);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout());
        root.add(UITheme.headerPanel("❓", "Help & Usage Guide", "How to use the Ocean View Resort System"),
                BorderLayout.NORTH);

        // ── Help content ──────────────────────────────────────────────────────
        JPanel content = new JPanel();
        content.setBackground(UITheme.BG_PAGE);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(16, 22, 16, 22));

        content.add(helpSection("🔑  Getting Started",
                "Login with your credentials:\n" +
                        "   • Admin  →  admin / admin123\n" +
                        "   • Staff  →  staff / staff123\n" +
                        "   • Manager →  manager / manager123"));

        content.add(Box.createVerticalStrut(10));

        content.add(helpSection("📋  New Reservation",
                "1. Click 'New Reservation' from the sidebar.\n" +
                        "2. Fill in guest name, address, and 10-digit contact number.\n" +
                        "3. Set check-in and check-out dates (YYYY-MM-DD format).\n" +
                        "4. Choose an available room from the dropdown.\n" +
                        "5. Click 'Save Reservation' — a unique ID will be generated.\n" +
                        "   Note: Room is automatically marked OCCUPIED (DB trigger)."));

        content.add(Box.createVerticalStrut(10));

        content.add(helpSection("🔍  View & Manage Reservation",
                "1. Click 'View Reservation' from the sidebar.\n" +
                        "2. Enter the Reservation ID (e.g. OVR-20240220-1234).\n" +
                        "3. For CONFIRMED reservations:\n" +
                        "   • ✅ 'Check Out Guest' → marks room AVAILABLE automatically.\n" +
                        "   • 🚫 'Cancel Reservation' → cancels and frees the room."));

        content.add(Box.createVerticalStrut(10));

        content.add(helpSection("🧾  Print Bill",
                "1. Click 'Generate Bill' from the sidebar.\n" +
                        "2. Enter Reservation ID and click 'Generate'.\n" +
                        "3. Review the invoice (includes tax & service charge):\n" +
                        "   • Government Tax: 10%\n" +
                        "   • Service Charge:  5%\n" +
                        "4. Click '🖨 Print Bill' to print."));

        content.add(Box.createVerticalStrut(10));

        content.add(helpSection("🏨  Room Types & Rates",
                "  Standard    LKR  5,000/night  →  AC, Wi-Fi, TV\n" +
                        "  Deluxe      LKR  8,500/night  →  + Mini-Bar, Balcony\n" +
                        "  Suite       LKR 15,000/night  →  + Jacuzzi, Living Room\n" +
                        "  Ocean View  LKR 20,000/night  →  + Panoramic Ocean View"));

        content.add(Box.createVerticalStrut(10));

        content.add(helpSection("⚙️  System Info",
                "  Database:    MySQL (via phpMyAdmin / XAMPP)\n" +
                        "  Triggers:    4 automated DB triggers for room management\n" +
                        "  Reports:     Revenue report with CSV export\n" +
                        "  Version:     Ocean View Resort HMS v1.0\n" +
                        "  Support:     Contact IT Department for technical help"));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // ── Buttons ───────────────────────────────────────────────────────────
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        btnBar.setBackground(Color.WHITE);
        btnBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER));
        JButton closeBtn = UITheme.dangerBtn("✖ Close");
        closeBtn.addActionListener(e -> dispose());
        btnBar.add(closeBtn);

        root.add(scroll, BorderLayout.CENTER);
        root.add(btnBar, BorderLayout.SOUTH);
        add(root);
    }

    private JPanel helpSection(String title, String body) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, UITheme.PRIMARY_LIGHT),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UITheme.BORDER),
                        new EmptyBorder(12, 16, 12, 16))));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(UITheme.PRIMARY_DARK);

        JTextArea b = new JTextArea(body);
        b.setFont(UITheme.FONT_BODY);
        b.setForeground(UITheme.TEXT_DARK);
        b.setBackground(Color.WHITE);
        b.setEditable(false);
        b.setLineWrap(true);
        b.setWrapStyleWord(true);
        b.setBorder(null);

        card.add(t, BorderLayout.NORTH);
        card.add(b, BorderLayout.CENTER);
        return card;
    }
}
