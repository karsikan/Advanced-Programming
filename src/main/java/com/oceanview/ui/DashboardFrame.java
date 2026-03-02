package com.oceanview.ui;

import com.oceanview.service.AuthService;
import com.oceanview.service.DashboardService;
import com.oceanview.service.ReservationService;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class DashboardFrame extends JFrame {

    private final DashboardService dashSvc = new DashboardService();
    private final ReservationService resSvc = new ReservationService();

    // Stat label references for live refresh
    private JLabel lblTotalRes, lblAvailRooms, lblOccupied, lblRevenue;
    private JLabel lblOccRate, lblCheckIns, lblCheckOuts;
    private JPanel revenueBarPanel;
    private JTextArea auditArea;

    public DashboardFrame() {
        initComponents();
        refreshStats();
    }

    private void initComponents() {
        setTitle("🌊 Ocean View Resort – Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 740);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 650));

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMain(), BorderLayout.CENTER);
        add(root);
    }

    // ── SIDEBAR ───────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sb = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, UITheme.PRIMARY_DARK,
                        0, getHeight(), new Color(4, 65, 50));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setPreferredSize(new Dimension(220, 0));
        sb.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Logo area
        JPanel logo = new JPanel();
        logo.setOpaque(false);
        logo.setLayout(new BoxLayout(logo, BoxLayout.Y_AXIS));
        logo.setBorder(new EmptyBorder(24, 16, 20, 16));
        JLabel logoIcon = new JLabel("🌊", SwingConstants.CENTER);
        logoIcon.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel logoText = new JLabel("OCEAN VIEW");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoText.setForeground(Color.WHITE);
        logoText.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel logoSub = new JLabel("RESORT");
        logoSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        logoSub.setForeground(UITheme.BORDER);
        logoSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.add(logoIcon);
        logo.add(logoText);
        logo.add(logoSub);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(220, 1));
        sep.setForeground(new Color(16, 120, 90));

        // Nav buttons - Role Based Visibility
        java.util.List<String[]> navItems = new java.util.ArrayList<>();
        navItems.add(new String[] { "🏠", "Dashboard" });

        if (AuthService.isAdmin()) {
            navItems.add(new String[] { "📋", "New Reservation" });
            navItems.add(new String[] { "👁", "View Reservation" });
            navItems.add(new String[] { "📑", "All Reservations" });
            navItems.add(new String[] { "🏨", "Room Management" });
            navItems.add(new String[] { "💰", "Revenue Report" });
            navItems.add(new String[] { "🧾", "Generate Bill" });
            navItems.add(new String[] { "👤", "User Management" });
        } else if (AuthService.isCustomer()) {
            navItems.add(new String[] { "📅", "Book Room" });
            navItems.add(new String[] { "📖", "My Reservations" });
            navItems.add(new String[] { "🧾", "Check Bill" });
        } else {
            // STAFF role
            navItems.add(new String[] { "📋", "New Reservation" });
            navItems.add(new String[] { "👁", "View Reservation" });
            navItems.add(new String[] { "📑", "All Reservations" });
            navItems.add(new String[] { "🏨", "Room Management" });
            navItems.add(new String[] { "🧾", "Generate Bill" });
        }
        navItems.add(new String[] { "❓", "Help" });

        sb.add(logo);
        sb.add(sep);
        sb.add(Box.createVerticalStrut(10));

        for (String[] nav : navItems) {
            sb.add(navBtn(nav[0], nav[1]));
        }

        sb.add(Box.createVerticalGlue());

        // User info at bottom
        JPanel userInfo = new JPanel(new BorderLayout(8, 4));
        userInfo.setOpaque(false);
        userInfo.setBorder(new EmptyBorder(14, 16, 14, 16));
        JLabel userIcon = new JLabel("👤  " + AuthService.getLoggedInUser());
        userIcon.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userIcon.setForeground(Color.WHITE);
        String role = AuthService.getLoggedInRole();
        JLabel roleLbl = new JLabel(role);
        roleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        roleLbl.setForeground(UITheme.BORDER);
        JButton logoutBtn = new JButton("⏻ Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        logoutBtn.setForeground(new Color(252, 165, 165));
        logoutBtn.setBackground(new Color(30, 60, 45));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(new EmptyBorder(5, 10, 5, 10));
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            AuthService.logout();
            dispose();
            new LoginFrame().setVisible(true);
        });
        userInfo.add(userIcon, BorderLayout.NORTH);
        userInfo.add(roleLbl, BorderLayout.CENTER);
        userInfo.add(logoutBtn, BorderLayout.SOUTH);
        sb.add(userInfo);
        return sb;
    }

    private JButton navBtn(String emoji, String label) {
        JButton b = new JButton(emoji + "  " + label);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setForeground(new Color(200, 240, 220));
        b.setBackground(new Color(0, 0, 0, 0));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(new EmptyBorder(9, 22, 9, 22));
        b.setMaximumSize(new Dimension(220, 44));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(new Color(16, 120, 90));
                b.setOpaque(true);
                b.setForeground(Color.WHITE);
            }

            public void mouseExited(MouseEvent e) {
                b.setOpaque(false);
                b.setForeground(new Color(200, 240, 220));
            }
        });
        b.addActionListener(e -> handleNav(label));
        return b;
    }

    private void handleNav(String label) {
        switch (label) {
            case "New Reservation", "Book Room" -> new ReservationFormFrame(resSvc).setVisible(true);
            case "View Reservation" -> new ViewReservationFrame(resSvc).setVisible(true);
            case "All Reservations", "My Reservations" -> new AllReservationsFrame(resSvc).setVisible(true);
            case "Room Management" -> new RoomManagementFrame(resSvc).setVisible(true);
            case "Revenue Report" -> new RevenueReportFrame().setVisible(true);
            case "Generate Bill", "Check Bill" -> new BillFrame(resSvc).setVisible(true);
            case "Help" -> new HelpFrame().setVisible(true);
            case "User Management" -> new UserManagementFrame().setVisible(true);
            case "Dashboard" -> refreshStats();
        }
    }

    // ── MAIN CONTENT ──────────────────────────────────────────────────────────
    private JPanel buildMain() {
        JPanel main = new JPanel(new BorderLayout(0, 0));
        main.setBackground(UITheme.BG_PAGE);

        // Top header bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER),
                new EmptyBorder(12, 24, 12, 24)));
        JLabel pageTitle = new JLabel("📊  Dashboard Overview");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pageTitle.setForeground(UITheme.PRIMARY_DARK);
        String dt = new SimpleDateFormat("EEEE, dd MMM yyyy  |  HH:mm").format(new Date());
        JLabel dtLbl = new JLabel(dt);
        dtLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dtLbl.setForeground(UITheme.TEXT_MUTED);
        JButton refreshBtn = UITheme.secondaryBtn("🔄 Refresh");
        refreshBtn.addActionListener(e -> refreshStats());
        topBar.add(pageTitle, BorderLayout.WEST);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        right.add(dtLbl);
        right.add(refreshBtn);
        topBar.add(right, BorderLayout.EAST);

        // Content scroll
        JPanel content = new JPanel();
        content.setBackground(UITheme.BG_PAGE);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Row 1 – 4 stats
        JPanel row1 = new JPanel(new GridLayout(1, 4, 14, 0));
        row1.setOpaque(false);
        row1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        lblTotalRes = statLabel("—");
        lblAvailRooms = statLabel("—");
        lblOccupied = statLabel("—");
        lblRevenue = statLabel("—");
        row1.add(statCard("📋 Total Reservations", lblTotalRes, UITheme.ACCENT_BLUE));
        row1.add(statCard("🟢 Available Rooms", lblAvailRooms, UITheme.SUCCESS));
        row1.add(statCard("🔴 Occupied Rooms", lblOccupied, UITheme.DANGER));
        row1.add(statCard("💰 Total Revenue (LKR)", lblRevenue, UITheme.ACCENT));

        // Row 2 – 3 stats
        JPanel row2 = new JPanel(new GridLayout(1, 3, 14, 0));
        row2.setOpaque(false);
        row2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        lblOccRate = statLabel("—");
        lblCheckIns = statLabel("—");
        lblCheckOuts = statLabel("—");
        row2.add(statCard("📈 Occupancy Rate", lblOccRate, UITheme.INFO));
        row2.add(statCard("📥 Today Check-Ins", lblCheckIns, UITheme.PRIMARY_LIGHT));
        row2.add(statCard("📤 Today Check-Outs", lblCheckOuts, UITheme.WARNING));

        // Row 3 – Revenue chart + Audit log
        JPanel row3 = new JPanel(new GridLayout(1, 2, 14, 0));
        row3.setOpaque(false);
        row3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 270));
        revenueBarPanel = new JPanel();
        revenueBarPanel.setLayout(new BoxLayout(revenueBarPanel, BoxLayout.Y_AXIS));
        revenueBarPanel.setBackground(Color.WHITE);
        JPanel revenueCard = wrapCard("💰 Revenue by Room Type", revenueBarPanel);
        auditArea = new JTextArea(8, 20);
        auditArea.setEditable(false);
        auditArea.setFont(new Font("Courier New", Font.PLAIN, 11));
        auditArea.setBackground(Color.WHITE);
        auditArea.setForeground(UITheme.TEXT_DARK);
        JScrollPane auditScroll = new JScrollPane(auditArea);
        auditScroll.setBorder(null);
        JPanel auditCard = wrapCard("🔍 Recent Audit Log", auditScroll);
        row3.add(revenueCard);
        row3.add(auditCard);

        content.add(row1);
        content.add(Box.createVerticalStrut(12));
        content.add(row2);
        content.add(Box.createVerticalStrut(12));
        content.add(row3);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        main.add(topBar, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        return main;
    }

    // ── HELPERS ───────────────────────────────────────────────────────────────
    private JPanel statCard(String title, JLabel valLbl, Color accent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UITheme.BORDER, 1),
                        new EmptyBorder(14, 18, 14, 18))));
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setForeground(UITheme.TEXT_MUTED);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valLbl.setForeground(accent);
        p.add(t, BorderLayout.NORTH);
        p.add(valLbl, BorderLayout.CENTER);
        return p;
    }

    private JLabel statLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 26));
        return l;
    }

    private JPanel wrapCard(String title, JComponent content) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(14, 16, 14, 16)));
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(UITheme.PRIMARY_DARK);
        card.add(t, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    public void refreshStats() {
        lblTotalRes.setText(String.valueOf(dashSvc.getTotalReservations()));
        lblAvailRooms.setText(String.valueOf(dashSvc.getAvailableRooms()));
        lblOccupied.setText(String.valueOf(dashSvc.getOccupiedRooms()));
        lblRevenue.setText(String.format("%,.0f", dashSvc.getTotalRevenue()));
        lblOccRate.setText(String.format("%.1f%%", dashSvc.getOccupancyRate()));
        lblCheckIns.setText(String.valueOf(dashSvc.getTodayCheckIns()));
        lblCheckOuts.setText(String.valueOf(dashSvc.getTodayCheckOuts()));

        // Revenue bars
        revenueBarPanel.removeAll();
        Map<String, Double> rev = dashSvc.getRevenueByRoomType();
        double max = rev.values().stream().mapToDouble(d -> d).max().orElse(1);
        for (Map.Entry<String, Double> e : rev.entrySet()) {
            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
            JLabel name = new JLabel(e.getKey());
            name.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            name.setPreferredSize(new Dimension(110, 28));
            int pct = (int) (e.getValue() / max * 100);
            JPanel bar = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(UITheme.PRIMARY_LIGHT);
                    int w = (int) (getWidth() * pct / 100.0);
                    g2.fillRoundRect(0, 4, w, getHeight() - 8, 6, 6);
                }
            };
            bar.setOpaque(false);
            JLabel val = new JLabel(String.format(" LKR %,.0f", e.getValue()));
            val.setFont(new Font("Segoe UI", Font.BOLD, 11));
            val.setForeground(UITheme.PRIMARY_DARK);
            val.setPreferredSize(new Dimension(105, 28));
            row.add(name, BorderLayout.WEST);
            row.add(bar, BorderLayout.CENTER);
            row.add(val, BorderLayout.EAST);
            revenueBarPanel.add(row);
            revenueBarPanel.add(Box.createVerticalStrut(4));
        }
        revenueBarPanel.revalidate();
        revenueBarPanel.repaint();

        // Audit log
        java.util.List<String[]> logs = dashSvc.getRecentAuditLog(8);
        StringBuilder sb = new StringBuilder();
        if (logs.isEmpty()) {
            sb.append("  No audit entries yet.");
        } else {
            for (String[] row : logs) {
                sb.append(String.format("  [%s]  %-20s ID: %s%n", row[0], row[1], row[2]));
            }
        }
        auditArea.setText(sb.toString());
    }
}
