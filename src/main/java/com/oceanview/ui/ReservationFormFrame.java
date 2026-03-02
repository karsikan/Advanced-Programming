package com.oceanview.ui;

import com.oceanview.model.Room;
import com.oceanview.model.Guest;
import com.oceanview.service.AuthService;
import com.oceanview.service.ReservationService;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ReservationFormFrame extends JFrame {

    private JTextField nameField, addressField, contactField;
    private JTextField checkInField, checkOutField;
    private JComboBox<String> roomCombo;
    private JLabel roomInfoLabel, messageLabel;
    private final ReservationService service;
    private List<Room> availableRooms;

    public ReservationFormFrame(ReservationService service) {
        this.service = service;
        initComponents();
        loadRooms();
    }

    private void initComponents() {
        setTitle("Ocean View Resort – New Reservation");
        setSize(580, 660);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_PAGE);

        // Header
        root.add(UITheme.headerPanel("📋", "New Reservation", "Fill in guest and booking details below"),
                BorderLayout.NORTH);

        // ── Form card ─────────────────────────────────────────────────────────
        JPanel form = new JPanel();
        form.setBackground(Color.WHITE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 30, 12, 30));

        // Section: Guest Info
        form.add(sectionHeader("👤  Guest Information"));
        form.add(Box.createVerticalStrut(8));
        nameField = styledField("Full Name");
        addressField = styledField("Address");
        contactField = styledField("Contact Number (10 digits)");
        form.add(formRow("Guest Name *", nameField));
        form.add(Box.createVerticalStrut(8));
        form.add(formRow("Address *", addressField));
        form.add(Box.createVerticalStrut(8));
        form.add(formRow("Contact No. *", contactField));
        form.add(Box.createVerticalStrut(16));

        // Pre-fill if Customer
        if (AuthService.isCustomer()) {
            Integer gid = AuthService.getLoggedInGuestId();
            if (gid != null) {
                Guest g = service.getGuestById(gid);
                if (g != null) {
                    nameField.setText(g.getName());
                    addressField.setText(g.getAddress());
                    contactField.setText(g.getContactNumber());
                    nameField.setEditable(false);
                    addressField.setEditable(false);
                    contactField.setEditable(false);
                    nameField.setBackground(new Color(245, 245, 245));
                    addressField.setBackground(new Color(245, 245, 245));
                    contactField.setBackground(new Color(245, 245, 245));
                }
            }
            setTitle("Ocean View Resort – Book Room");
        }

        // Section: Booking
        form.add(sectionHeader("📅  Booking Details"));
        form.add(Box.createVerticalStrut(8));
        checkInField = styledField("YYYY-MM-DD");
        checkOutField = styledField("YYYY-MM-DD");
        checkInField.setText(LocalDate.now().toString());
        checkOutField.setText(LocalDate.now().plusDays(1).toString());
        form.add(formRow("Check-In Date *", checkInField));
        form.add(Box.createVerticalStrut(8));
        form.add(formRow("Check-Out Date *", checkOutField));
        form.add(Box.createVerticalStrut(16));

        // Section: Room
        form.add(sectionHeader("🏨  Room Selection"));
        form.add(Box.createVerticalStrut(8));
        roomCombo = new JComboBox<>();
        roomCombo.setFont(UITheme.FONT_BODY);
        roomCombo.setBackground(new Color(240, 253, 244));
        roomCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        roomCombo.addActionListener(e -> updateRoomInfo());

        roomInfoLabel = new JLabel("  ");
        roomInfoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        roomInfoLabel.setForeground(UITheme.PRIMARY);
        roomInfoLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, UITheme.PRIMARY_LIGHT),
                new EmptyBorder(4, 8, 4, 8)));

        form.add(formRow("Select Room *", roomCombo));
        form.add(Box.createVerticalStrut(6));
        form.add(roomInfoLabel);
        form.add(Box.createVerticalStrut(16));

        // Feedback
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(messageLabel);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        // ── Buttons ───────────────────────────────────────────────────────────
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        btnBar.setBackground(Color.WHITE);
        btnBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER));

        JButton saveBtn = UITheme.primaryBtn("💾  Save Reservation");
        JButton clearBtn = UITheme.secondaryBtn("🔄  Clear");
        JButton closeBtn = UITheme.dangerBtn("✖  Close");
        saveBtn.addActionListener(e -> doSave());
        clearBtn.addActionListener(e -> clearForm());
        closeBtn.addActionListener(e -> dispose());
        btnBar.add(saveBtn);
        btnBar.add(clearBtn);
        btnBar.add(closeBtn);

        root.add(scroll, BorderLayout.CENTER);
        root.add(btnBar, BorderLayout.SOUTH);
        add(root);
    }

    // ── Load / Update ─────────────────────────────────────────────────────────
    private void loadRooms() {
        availableRooms = service.getAvailableRooms();
        roomCombo.removeAllItems();
        if (availableRooms.isEmpty()) {
            roomCombo.addItem("⚠ No rooms available");
        } else {
            for (Room r : availableRooms) {
                roomCombo.addItem("Room " + r.getRoomNumber() + "  |  " + r.getRoomType()
                        + "  |  LKR " + String.format("%,.0f", r.getRatePerNight()) + " /night");
            }
            updateRoomInfo();
        }
    }

    private void updateRoomInfo() {
        int idx = roomCombo.getSelectedIndex();
        if (availableRooms != null && idx >= 0 && idx < availableRooms.size()) {
            Room r = availableRooms.get(idx);
            roomInfoLabel.setText("  ✨ Amenities: " + r.getAmenities());
        }
    }

    private void doSave() {
        int idx = roomCombo.getSelectedIndex();
        if (availableRooms == null || availableRooms.isEmpty() || idx < 0) {
            showMsg("⚠ No available room selected.", UITheme.WARNING);
            return;
        }
        Room selectedRoom = availableRooms.get(idx);
        String result = service.createReservation(
                nameField.getText(), addressField.getText(), contactField.getText(),
                selectedRoom.getId(), checkInField.getText(), checkOutField.getText(),
                AuthService.isCustomer() ? AuthService.getLoggedInGuestId() : null);
        if (result.startsWith("SUCCESS:")) {
            String id = result.split(":")[1];
            showMsg("✅ Reservation saved! ID: " + id, UITheme.SUCCESS);
            loadRooms();
        } else {
            showMsg("❌ " + result, UITheme.DANGER);
        }
    }

    private void clearForm() {
        nameField.setText("");
        addressField.setText("");
        contactField.setText("");
        checkInField.setText(LocalDate.now().toString());
        checkOutField.setText(LocalDate.now().plusDays(1).toString());
        messageLabel.setText(" ");
        loadRooms();
    }

    private void showMsg(String txt, Color c) {
        messageLabel.setText(txt);
        messageLabel.setForeground(c);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(UITheme.FONT_BODY);
        f.setToolTipText(placeholder);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(7, 11, 7, 11)));
        f.setBackground(new Color(240, 253, 244));
        return f;
    }

    private JPanel formRow(String label, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(UITheme.PRIMARY_DARK);
        lbl.setPreferredSize(new Dimension(145, 36));
        row.add(lbl, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private JLabel sectionHeader(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(UITheme.PRIMARY);
        l.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER),
                new EmptyBorder(0, 0, 4, 0)));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return l;
    }
}
