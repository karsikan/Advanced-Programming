package com.oceanview.ui;

import com.oceanview.model.User;
import com.oceanview.service.AdminService;
import com.oceanview.service.AuthService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class UserManagementFrame extends JFrame {

    private final AdminService adminService;
    private DefaultTableModel model;
    private JTable table;
    private List<User> currentUsers;

    public UserManagementFrame() {
        this.adminService = new AdminService();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Ocean View Resort – User Management");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 400));

        JPanel root = new JPanel(new BorderLayout());
        root.add(UITheme.headerPanel("👤", "User Management", "Manage employee and admin accounts"),
                BorderLayout.NORTH);

        // ── Table ─────────────────────────────────────────────────────────────
        String[] cols = { "ID", "Username", "Role" };
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);
        UITheme.styleTable(table);

        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(UITheme.BG_PAGE);
        center.setBorder(new EmptyBorder(14, 18, 0, 18));
        center.add(scroll, BorderLayout.CENTER);

        // ── Buttons ───────────────────────────────────────────────────────────
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        btnBar.setBackground(Color.WHITE);
        btnBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER));

        JButton addBtn = UITheme.accentBtn("➕ Add User");
        addBtn.addActionListener(e -> showUserDialog(null));

        JButton editBtn = UITheme.secondaryBtn("📝 Edit Selected");
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to edit.");
                return;
            }
            showUserDialog(currentUsers.get(row));
        });

        JButton deleteBtn = UITheme.dangerBtn("🗑 Delete");
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to delete.");
                return;
            }
            User u = currentUsers.get(row);
            if (u.getUsername().equals("admin")) {
                JOptionPane.showMessageDialog(this, "The primary admin account cannot be deleted.");
                return;
            }
            int opt = JOptionPane.showConfirmDialog(this, "Delete User: " + u.getUsername() + "?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                if (adminService.deleteUser(u.getId())) {
                    loadData();
                    JOptionPane.showMessageDialog(this, "User deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete user.");
                }
            }
        });

        JButton closeBtn = UITheme.primaryBtn("✖ Close");
        closeBtn.addActionListener(e -> dispose());

        btnBar.add(addBtn);
        btnBar.add(editBtn);
        btnBar.add(deleteBtn);
        btnBar.add(closeBtn);

        root.add(center, BorderLayout.CENTER);
        root.add(btnBar, BorderLayout.SOUTH);
        add(root);
    }

    private void loadData() {
        model.setRowCount(0);
        currentUsers = adminService.getAllUsers();
        for (User u : currentUsers) {
            model.addRow(new Object[] { u.getId(), u.getUsername(), u.getRole() });
        }
    }

    private void showUserDialog(User user) {
        JTextField userField = new JTextField(user != null ? user.getUsername() : "");
        if (user != null)
            userField.setEditable(false); // Can't rename existing user
        JPasswordField passField = new JPasswordField(user != null ? user.getPassword() : "");
        JComboBox<String> roleBox = new JComboBox<>(new String[] { "STAFF", "ADMIN" });
        if (user != null)
            roleBox.setSelectedItem(user.getRole());

        JPanel p = new JPanel(new GridLayout(3, 2, 10, 10));
        p.add(new JLabel("Username:"));
        p.add(userField);
        p.add(new JLabel("Password:"));
        p.add(passField);
        p.add(new JLabel("Role:"));
        p.add(roleBox);

        int res = JOptionPane.showConfirmDialog(this, p, user == null ? "Add New User" : "Edit User",
                JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String msg = adminService.saveUser(
                    user != null ? user.getId() : null,
                    userField.getText(),
                    new String(passField.getPassword()),
                    (String) roleBox.getSelectedItem());

            if ("SUCCESS".equals(msg)) {
                loadData();
                JOptionPane.showMessageDialog(this, "User account saved successfully.");
            } else {
                JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
