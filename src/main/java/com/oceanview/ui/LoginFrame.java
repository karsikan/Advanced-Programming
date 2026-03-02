package com.oceanview.ui;

import com.oceanview.service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private final AuthService authService;

    public LoginFrame() {
        this.authService = new AuthService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Ocean View Resort – Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        // ── Gradient background ───────────────────────────────────────────────
        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, UITheme.PRIMARY_DARK,
                        0, getHeight(), new Color(2, 75, 55));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new BorderLayout());

        // ── Header ────────────────────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(40, 0, 20, 0));

        JLabel logo = new JLabel("🌊", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLbl = new JLabel("OCEAN VIEW RESORT");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLbl = new JLabel("Hotel Management System");
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subLbl.setForeground(UITheme.BORDER);
        subLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(logo);
        header.add(Box.createVerticalStrut(8));
        header.add(titleLbl);
        header.add(Box.createVerticalStrut(4));
        header.add(subLbl);

        // ── Login Card ────────────────────────────────────────────────────────
        JPanel card = new JPanel();
        card.setBackground(new Color(255, 255, 255, 245));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 2, true),
                new EmptyBorder(30, 35, 30, 35)));

        // Username
        JLabel userLbl = new JLabel("👤  Username");
        userLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userLbl.setForeground(UITheme.PRIMARY_DARK);
        userLbl.setAlignmentX(0f);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        usernameField.setBackground(new Color(240, 253, 244));
        usernameField.setAlignmentX(0f);

        // Password
        JLabel passLbl = new JLabel("🔒  Password");
        passLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLbl.setForeground(UITheme.PRIMARY_DARK);
        passLbl.setAlignmentX(0f);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        passwordField.setBackground(new Color(240, 253, 244));
        passwordField.setAlignmentX(0f);
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    performLogin();
            }
        });

        // Message
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setForeground(UITheme.DANGER);
        messageLabel.setAlignmentX(0f);

        // Login button
        JButton loginBtn = UITheme.primaryBtn("  🔐   LOGIN  ");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        loginBtn.setAlignmentX(0f);
        loginBtn.addActionListener(e -> performLogin());

        // Register button
        JButton registerBtn = new JButton("Don't have an account? Register Here");
        registerBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        registerBtn.setForeground(UITheme.PRIMARY);
        registerBtn.setContentAreaFilled(false);
        registerBtn.setBorderPainted(false);
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerBtn.setAlignmentX(0f);
        registerBtn.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
        });

        card.add(userLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(14));
        card.add(passLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(8));
        card.add(messageLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(registerBtn);

        // ── Card wrapper ──────────────────────────────────────────────────────
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(0, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        wrapper.add(card, gbc);

        // ── Footer ────────────────────────────────────────────────────────────
        JLabel footer = new JLabel("© 2026 Ocean View Resort, Galle, Sri Lanka", SwingConstants.CENTER);
        footer.setForeground(UITheme.BORDER);
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        footer.setBorder(new EmptyBorder(0, 0, 16, 0));

        bg.add(header, BorderLayout.NORTH);
        bg.add(wrapper, BorderLayout.CENTER);
        bg.add(footer, BorderLayout.SOUTH);
        add(bg);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("⚠ Please enter username and password.");
            return;
        }
        if (authService.login(username, password)) {
            messageLabel.setForeground(UITheme.SUCCESS);
            messageLabel.setText("✅ Welcome, " + username + "!");
            Timer t = new Timer(600, e -> {
                dispose();
                new DashboardFrame().setVisible(true);
            });
            t.setRepeats(false);
            t.start();
        } else {
            messageLabel.setForeground(UITheme.DANGER);
            messageLabel.setText("❌ Invalid username or password!");
            passwordField.setText("");
        }
    }
}
