package com.oceanview.ui;

import com.oceanview.model.Reservation;
import com.oceanview.service.AuthService;
import com.oceanview.service.ReservationService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class AllReservationsFrame extends JFrame {

    private final ReservationService service;
    private DefaultTableModel tableModel;
    private JLabel countLabel;

    public AllReservationsFrame(ReservationService service) {
        this.service = service;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Ocean View Resort – All Reservations");
        setSize(1100, 580);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 450));

        JPanel root = new JPanel(new BorderLayout());
        root.add(UITheme.headerPanel("📑", "All Reservations", "Complete reservation register"), BorderLayout.NORTH);

        // ── Info bar ──────────────────────────────────────────────────────────
        JPanel infoBar = new JPanel(new BorderLayout());
        infoBar.setBackground(Color.WHITE);
        infoBar.setBorder(new EmptyBorder(8, 20, 8, 20));
        countLabel = new JLabel("Loading...");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        countLabel.setForeground(UITheme.PRIMARY);
        infoBar.add(countLabel, BorderLayout.WEST);

        // ── Table ─────────────────────────────────────────────────────────────
        String[] cols = { "Reservation ID", "Guest Name", "Contact", "Room No.", "Type", "Check-In", "Check-Out",
                "Amount (LKR)", "Status" };
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        UITheme.styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Status column renderer
        table.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row,
                    int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                String status = v != null ? v.toString() : "";
                if (!sel) {
                    switch (status) {
                        case "CONFIRMED" -> {
                            c.setBackground(new Color(236, 253, 245));
                            c.setForeground(UITheme.SUCCESS);
                        }
                        case "CANCELLED" -> {
                            c.setBackground(new Color(255, 241, 242));
                            c.setForeground(UITheme.DANGER);
                        }
                        case "CHECKED_OUT" -> {
                            c.setBackground(new Color(239, 246, 255));
                            c.setForeground(UITheme.ACCENT_BLUE);
                        }
                        default -> {
                            c.setBackground(Color.WHITE);
                            c.setForeground(UITheme.TEXT_DARK);
                        }
                    }
                }
                setFont(getFont().deriveFont(Font.BOLD));
                setHorizontalAlignment(CENTER);
                setBorder(new EmptyBorder(0, 6, 0, 6));
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(UITheme.BG_PAGE);
        center.setBorder(new EmptyBorder(10, 18, 0, 18));
        center.add(scroll, BorderLayout.CENTER);

        // ── Buttons ───────────────────────────────────────────────────────────
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        btnBar.setBackground(Color.WHITE);
        btnBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER));
        JButton refreshBtn = UITheme.primaryBtn("🔄 Refresh");
        refreshBtn.addActionListener(e -> loadData());
        JButton closeBtn = UITheme.dangerBtn("✖ Close");
        closeBtn.addActionListener(e -> dispose());
        btnBar.add(refreshBtn);
        btnBar.add(closeBtn);

        root.add(infoBar, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(btnBar, BorderLayout.SOUTH);
        add(root);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Reservation> list;
        if (AuthService.isCustomer()) {
            Integer gid = AuthService.getLoggedInGuestId();
            list = (gid != null) ? service.getReservationsByGuest(gid) : java.util.Collections.emptyList();
            setTitle("Ocean View Resort – My Reservations");
        } else {
            list = service.getAllReservations();
        }
        for (Reservation r : list) {
            tableModel.addRow(new Object[] {
                    r.getReservationId(), r.getGuestName(), r.getGuestContact(),
                    r.getRoomNumber(), r.getRoomType(),
                    r.getCheckInDate(), r.getCheckOutDate(),
                    String.format("%,.2f", r.getTotalAmount()), r.getStatus()
            });
        }
        countLabel.setText("📋 Total Reservations: " + list.size());
    }
}
