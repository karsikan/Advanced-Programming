package com.oceanview.ui;

import com.oceanview.model.Reservation;
import com.oceanview.service.ReservationService;
import com.oceanview.util.DateUtil;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class ViewReservationFrame extends JFrame {

    private JTextField searchField;
    private JTextArea detailsArea;
    private final ReservationService service;
    private Reservation currentReservation;
    private JButton checkOutBtn, cancelBtn;

    public ViewReservationFrame(ReservationService service) {
        this.service = service;
        initComponents();
    }

    private void initComponents() {
        setTitle("Ocean View Resort – View Reservation");
        setSize(640, 620);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_PAGE);
        root.add(UITheme.headerPanel("🔍", "View Reservation", "Search by Reservation ID"), BorderLayout.NORTH);

        JPanel searchBar = new JPanel(new BorderLayout(10, 0));
        searchBar.setBackground(Color.WHITE);
        searchBar.setBorder(new EmptyBorder(14, 22, 14, 22));
        JLabel idLbl = new JLabel("Reservation ID:");
        idLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        idLbl.setForeground(UITheme.PRIMARY_DARK);
        searchField = new JTextField();
        searchField.setFont(UITheme.FONT_BODY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        searchField.setBackground(new Color(240, 253, 244));
        searchField.setToolTipText("e.g. OVR-20240220-1234");
        searchField.addActionListener(e -> doSearch());
        JButton searchBtn = UITheme.primaryBtn("🔍 Search");
        searchBtn.addActionListener(e -> doSearch());
        searchBar.add(idLbl, BorderLayout.WEST);
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchBtn, BorderLayout.EAST);

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.add(searchBar, BorderLayout.CENTER);
        topWrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));


        detailsArea = new JTextArea();
        detailsArea.setFont(UITheme.FONT_MONO);
        detailsArea.setEditable(false);
        detailsArea.setBackground(new Color(248, 254, 250));
        detailsArea.setForeground(UITheme.TEXT_DARK);
        detailsArea.setBorder(new EmptyBorder(14, 18, 14, 18));
        detailsArea.setText("\n   Enter a Reservation ID above and click Search.\n\n   Example: OVR-20240220-1234");

        JScrollPane scroll = new JScrollPane(detailsArea);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(UITheme.BG_PAGE);
        centerPanel.setBorder(new EmptyBorder(14, 20, 0, 20));
        centerPanel.add(scroll, BorderLayout.CENTER);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        btnBar.setBackground(Color.WHITE);
        btnBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER));

        checkOutBtn = UITheme.primaryBtn(" Check Out Guest");
        checkOutBtn.setVisible(false);
        checkOutBtn.addActionListener(e -> doCheckOut());

        cancelBtn = UITheme.accentBtn(" Cancel Reservation");
        cancelBtn.setBackground(UITheme.WARNING);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setVisible(false);
        cancelBtn.addActionListener(e -> doCancelReservation());

        JButton closeBtn = UITheme.dangerBtn(" Close");
        closeBtn.addActionListener(e -> dispose());

        btnBar.add(checkOutBtn);
        btnBar.add(cancelBtn);
        btnBar.add(closeBtn);

        root.add(topWrapper, BorderLayout.NORTH);
        root.add(centerPanel, BorderLayout.CENTER);
        root.add(btnBar, BorderLayout.SOUTH);
        add(root);
    }

    private void doSearch() {
        String id = searchField.getText().trim();
        if (id.isEmpty()) {
            detailsArea.setText("\n   Please enter a Reservation ID.");
            checkOutBtn.setVisible(false);
            cancelBtn.setVisible(false);
            return;
        }
        Reservation r = service.getReservation(id.toUpperCase());
        if (r == null) {
            detailsArea.setText("\n   No reservation found with ID: " + id.toUpperCase());
            checkOutBtn.setVisible(false);
            cancelBtn.setVisible(false);
            return;
        }
        this.currentReservation = r;
        long nights = DateUtil.calculateNights(r.getCheckInDate(), r.getCheckOutDate());
        detailsArea.setText(String.format("""
                ══════════════════════════════════════════════
                   🌊 OCEAN VIEW RESORT – RESERVATION DETAILS
                ══════════════════════════════════════════════

                  Reservation ID   : %s
                  Status           : %s
                  Created On       : %s

                ──── 👤 GUEST INFORMATION ──────────────────────
                  Guest Name       : %s
                  Address          : %s
                  Contact Number   : %s

                ──── 🏨 ROOM INFORMATION ───────────────────────
                  Room Number      : %s
                  Room Type        : %s
                  Rate Per Night   : LKR %,.2f

                ──── 📅 BOOKING INFORMATION ────────────────────
                  Check-In Date    : %s
                  Check-Out Date   : %s
                  Total Nights     : %d
                  Total Amount     : LKR %,.2f

                ══════════════════════════════════════════════
                """,
                r.getReservationId(), r.getStatus(), r.getCreatedAt(),
                r.getGuestName(), r.getGuestAddress(), r.getGuestContact(),
                r.getRoomNumber(), r.getRoomType(), r.getRatePerNight(),
                r.getCheckInDate(), r.getCheckOutDate(), nights, r.getTotalAmount()));

        boolean isConfirmed = "CONFIRMED".equalsIgnoreCase(r.getStatus());
        checkOutBtn.setVisible(isConfirmed);
        cancelBtn.setVisible(isConfirmed);
    }

    private void doCheckOut() {
        if (currentReservation == null)
            return;
        int c = JOptionPane.showConfirmDialog(this,
                "Confirm check-out for " + currentReservation.getGuestName() + "?\n" +
                        "Room will be auto-released by DB trigger.",
                "Confirm Check-Out", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            if (service.checkOutReservation(currentReservation.getReservationId())) {
                JOptionPane.showMessageDialog(this,
                        "Check-out complete! Room is now AVAILABLE.", "Done", JOptionPane.INFORMATION_MESSAGE);
                doSearch();
            } else {
                JOptionPane.showMessageDialog(this, "Check-out failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void doCancelReservation() {
        if (currentReservation == null)
            return;
        int c = JOptionPane.showConfirmDialog(this,
                "Cancel reservation " + currentReservation.getReservationId() + "?\nThis cannot be undone.",
                "Confirm Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            if (service.cancelReservation(currentReservation.getReservationId(), currentReservation.getRoomId())) {
                JOptionPane.showMessageDialog(this,
                        "Reservation cancelled. Room is now available.", "Cancelled",
                        JOptionPane.INFORMATION_MESSAGE);
                doSearch();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Cancellation failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
