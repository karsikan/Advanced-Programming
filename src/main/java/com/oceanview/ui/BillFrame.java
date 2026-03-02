package com.oceanview.ui;

import com.oceanview.model.Reservation;
import com.oceanview.service.AuthService;
import com.oceanview.service.BillingService;
import com.oceanview.service.ReservationService;
import com.oceanview.util.DateUtil;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.print.PrinterException;

public class BillFrame extends JFrame {

    private JTextField searchField;
    private JTextArea billArea;
    private final ReservationService service;
    private final BillingService billingService;
    private String currentBill = "";

    public BillFrame(ReservationService service) {
        this.service = service;
        this.billingService = new BillingService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Ocean View Resort – Generate Bill");
        setSize(620, 640);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout());
        root.add(UITheme.headerPanel("🧾", "Generate Bill", "Enter Reservation ID to generate invoice"),
                BorderLayout.NORTH);

        // ── Search bar ────────────────────────────────────────────────────────
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
        searchField.addActionListener(e -> generateBill());
        JButton genBtn = UITheme.primaryBtn("💰 Generate");
        genBtn.addActionListener(e -> generateBill());
        searchBar.add(idLbl, BorderLayout.WEST);
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(genBtn, BorderLayout.EAST);
        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.add(searchBar);
        topWrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));

        // ── Bill area ─────────────────────────────────────────────────────────
        billArea = new JTextArea();
        billArea.setFont(new Font("Courier New", Font.PLAIN, 13));
        billArea.setEditable(false);
        billArea.setBackground(new Color(250, 255, 250));
        billArea.setForeground(UITheme.TEXT_DARK);
        billArea.setBorder(new EmptyBorder(16, 18, 16, 18));
        billArea.setText(
                "\n   Enter a Reservation ID and click 'Generate'.\n\n   A detailed invoice will appear here.");

        JScrollPane scroll = new JScrollPane(billArea);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(UITheme.BG_PAGE);
        center.setBorder(new EmptyBorder(14, 20, 0, 20));
        center.add(scroll, BorderLayout.CENTER);

        // ── Buttons ───────────────────────────────────────────────────────────
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnBar.setBackground(Color.WHITE);
        btnBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER));
        JButton printBtn = UITheme.secondaryBtn("🖨 Print Bill");
        printBtn.addActionListener(e -> doPrint());
        JButton closeBtn = UITheme.dangerBtn("✖ Close");
        closeBtn.addActionListener(e -> dispose());
        btnBar.add(printBtn);
        btnBar.add(closeBtn);

        root.add(topWrapper, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(btnBar, BorderLayout.SOUTH);
        add(root);
    }

    private void generateBill() {
        String id = searchField.getText().trim();
        if (id.isEmpty()) {
            billArea.setText("\n   ⚠ Please enter a Reservation ID.");
            return;
        }
        Reservation r = service.getReservation(id.toUpperCase());
        if (r == null) {
            billArea.setText("\n   ❌ No reservation found: " + id.toUpperCase());
            return;
        }
        // Role check
        if (AuthService.isCustomer()) {
            Integer gid = AuthService.getLoggedInGuestId();
            if (gid == null || r.getGuestId() != gid) {
                billArea.setText("\n   🚫 Access Denied: You can only view your own bills.");
                return;
            }
        }
        long nights = DateUtil.calculateNights(r.getCheckInDate(), r.getCheckOutDate());
        currentBill = billingService.generateBillText(r, nights);
        billArea.setText(currentBill);
        billArea.setCaretPosition(0);
    }

    private void doPrint() {
        if (currentBill.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Generate a bill first.", "No Bill", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            billArea.print();
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Print error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
