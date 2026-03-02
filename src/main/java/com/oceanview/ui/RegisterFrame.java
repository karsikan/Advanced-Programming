package com.oceanview.ui;

import com.oceanview.service.AuthService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField nameField, addressField, contactField, userField;
    private JPasswordField passField;
    private JLabel msgLabel;
    private final AuthService authService = new AuthService();

    public RegisterFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Ocean View Resort – Guest Registration");
        setSize(500, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        // Header
        root.add(UITheme.headerPanel("📝", "Guest Registration", "Create your account to start booking"),
                BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 40, 20, 40));

        nameField = styledField("Full Name");
        addressField = styledField("Address");
        contactField = styledField("Contact Number");
        userField = styledField("Username");
        passField = new JPasswordField();
        stylePasswordField(passField);

        form.add(label("Full Name:"));
        form.add(nameField);
        form.add(Box.createVerticalStrut(10));
        form.add(label("Address:"));
        form.add(addressField);
        form.add(Box.createVerticalStrut(10));
        form.add(label("Contact No:"));
        form.add(contactField);
        form.add(Box.createVerticalStrut(15));
        form.add(new JSeparator());
        form.add(Box.createVerticalStrut(15));
        form.add(label("Choose Username:"));
        form.add(userField);
        form.add(Box.createVerticalStrut(10));
        form.add(label("Choose Password:"));
        form.add(passField);
        form.add(Box.createVerticalStrut(15));

        msgLabel = new JLabel(" ");
        msgLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(msgLabel);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        btnPanel.setOpaque(false);
        JButton regBtn = UITheme.primaryBtn("✅ Register Now");
        regBtn.addActionListener(e -> doRegister());
        JButton backBtn = UITheme.secondaryBtn("⬅ Back to Login");
        backBtn.addActionListener(e -> dispose());
        btnPanel.add(regBtn);
        btnPanel.add(backBtn);
        form.add(btnPanel);

        root.add(form, BorderLayout.CENTER);
        add(root);
    }

    private void doRegister() {
        String result = authService.registerCustomer(
                nameField.getText().trim(),
                addressField.getText().trim(),
                contactField.getText().trim(),
                userField.getText().trim(),
                new String(passField.getPassword()).trim());

        if ("SUCCESS".equals(result)) {
            msgLabel.setForeground(UITheme.SUCCESS);
            msgLabel.setText("✅ Registration successful! Please login.");
            Timer t = new Timer(2000, e -> dispose());
            t.setRepeats(false);
            t.start();
        } else {
            msgLabel.setForeground(UITheme.DANGER);
            msgLabel.setText("❌ " + result);
        }
    }

    private JTextField styledField(String hint) {
        JTextField f = new JTextField();
        f.setFont(UITheme.FONT_BODY);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(7, 10, 7, 10)));
        f.setBackground(new Color(245, 250, 245));
        return f;
    }

    private void stylePasswordField(JPasswordField f) {
        f.setFont(UITheme.FONT_BODY);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(7, 10, 7, 10)));
        f.setBackground(new Color(245, 250, 245));
    }

    private JLabel label(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(UITheme.PRIMARY_DARK);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
}
