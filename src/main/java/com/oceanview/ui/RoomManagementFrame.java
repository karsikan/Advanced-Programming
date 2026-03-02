package com.oceanview.ui;

import com.oceanview.model.Room;
import com.oceanview.service.AdminService;
import com.oceanview.service.AuthService;
import com.oceanview.service.ReservationService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class RoomManagementFrame extends JFrame {

    private final ReservationService service;
    private final AdminService adminService;
    private DefaultTableModel model;
    private JTable table;
    private JLabel lblAvail, lblOccupied, lblTotal;
    private List<Room> currentRooms;

    public RoomManagementFrame(ReservationService service) {
        this.service = service;
        this.adminService = new AdminService();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Ocean View Resort – Room Management");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(750, 500));

        JPanel root = new JPanel(new BorderLayout());
        root.add(UITheme.headerPanel("🏨", "Room Management", "Live status of all rooms"), BorderLayout.NORTH);

        // ── Stats banner ──────────────────────────────────────────────────────
        JPanel stats = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        stats.setBackground(Color.WHITE);
        stats.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));
        lblAvail = badgeLabel("🟢 Available: —", UITheme.SUCCESS);
        lblOccupied = badgeLabel("🔴 Occupied: —", UITheme.DANGER);
        lblTotal = badgeLabel("🏨 Total: —", UITheme.ACCENT_BLUE);
        stats.add(lblAvail);
        stats.add(lblOccupied);
        stats.add(lblTotal);

        // ── Table ─────────────────────────────────────────────────────────────
        String[] cols = { "ID", "Room No.", "Type", "Rate / Night (LKR)", "Status", "Amenities" };
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

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row,
                    int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                String status = model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "";
                if (!sel) {
                    switch (status) {
                        case "AVAILABLE" -> {
                            c.setBackground(new Color(240, 253, 244));
                            c.setForeground(UITheme.PRIMARY_DARK);
                        }
                        case "OCCUPIED" -> {
                            c.setBackground(new Color(255, 241, 242));
                            c.setForeground(new Color(185, 28, 28));
                        }
                        case "MAINTENANCE" -> {
                            c.setBackground(new Color(255, 251, 235));
                            c.setForeground(new Color(146, 64, 14));
                        }
                        default -> {
                            c.setBackground(Color.WHITE);
                            c.setForeground(UITheme.TEXT_DARK);
                        }
                    }
                }
                if (col == 4) {
                    setFont(getFont().deriveFont(Font.BOLD));
                }
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        });

        // Column widths
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(340);

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

        JButton refreshBtn = UITheme.primaryBtn("🔄 Refresh");
        refreshBtn.addActionListener(e -> loadData());

        JButton closeBtn = UITheme.dangerBtn("✖ Close");
        closeBtn.addActionListener(e -> dispose());

        if (AuthService.isAdmin()) {
            JButton addBtn = UITheme.accentBtn("➕ Add Room");
            addBtn.addActionListener(e -> showRoomDialog(null));

            JButton editBtn = UITheme.secondaryBtn("📝 Edit Selected");
            editBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(this, "Please select a room to edit.");
                    return;
                }
                showRoomDialog(currentRooms.get(row));
            });

            JButton deleteBtn = UITheme.dangerBtn("🗑 Delete");
            deleteBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(this, "Please select a room to delete.");
                    return;
                }
                Room r = currentRooms.get(row);
                int opt = JOptionPane.showConfirmDialog(this, "Delete Room " + r.getRoomNumber() + "?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    if (adminService.deleteRoom(r.getId())) {
                        loadData();
                        JOptionPane.showMessageDialog(this, "Room deleted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete room (it might have reservations).");
                    }
                }
            });

            btnBar.add(addBtn);
            btnBar.add(editBtn);
            btnBar.add(deleteBtn);
        }

        btnBar.add(refreshBtn);
        btnBar.add(closeBtn);

        root.add(stats, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(btnBar, BorderLayout.SOUTH);
        add(root);
    }

    private void loadData() {
        model.setRowCount(0);
        currentRooms = service.getAllRooms();
        int avail = 0, occ = 0;
        for (Room r : currentRooms) {
            model.addRow(new Object[] {
                    r.getId(),
                    r.getRoomNumber(), r.getRoomType(),
                    String.format("%,.2f", r.getRatePerNight()),
                    r.getStatus(), r.getAmenities()
            });
            if ("AVAILABLE".equals(r.getStatus()))
                avail++;
            else if ("OCCUPIED".equals(r.getStatus()))
                occ++;
        }
        lblAvail.setText("🟢 Available: " + avail);
        lblOccupied.setText("🔴 Occupied: " + occ);
        lblTotal.setText("🏨 Total: " + currentRooms.size());
    }

    private void showRoomDialog(Room room) {
        JTextField noField = new JTextField(room != null ? room.getRoomNumber() : "");
        JComboBox<String> typeBox = new JComboBox<>(new String[] { "STANDARD", "DELUXE", "SUITE", "OCEAN_VIEW" });
        if (room != null)
            typeBox.setSelectedItem(room.getRoomType());
        JTextField rateField = new JTextField(room != null ? String.valueOf(room.getRatePerNight()) : "");

        JPanel p = new JPanel(new GridLayout(3, 2, 10, 10));
        p.add(new JLabel("Room Number:"));
        p.add(noField);
        p.add(new JLabel("Type:"));
        p.add(typeBox);
        p.add(new JLabel("Rate/Night:"));
        p.add(rateField);

        int res = JOptionPane.showConfirmDialog(this, p, room == null ? "Add New Room" : "Edit Room",
                JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                double rate = Double.parseDouble(rateField.getText());
                String msg = adminService.saveRoom(
                        room != null ? room.getId() : null,
                        noField.getText(),
                        (String) typeBox.getSelectedItem(),
                        rate);

                if ("SUCCESS".equals(msg)) {
                    loadData();
                    JOptionPane.showMessageDialog(this, "Room saved successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid rate value.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JLabel badgeLabel(String text, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(color);
        l.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 1, true),
                new EmptyBorder(4, 12, 4, 12)));
        return l;
    }
}
