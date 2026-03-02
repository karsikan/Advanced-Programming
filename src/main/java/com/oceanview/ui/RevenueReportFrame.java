package com.oceanview.ui;

import com.oceanview.service.DashboardService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Revenue Report – shows revenue by room type, monthly trends, and allows CSV
 * export.
 * Satisfies "Proposed reports to facilitate decision-making" requirement.
 * Redesigned with Ocean View Resort Premium UI Theme.
 */
public class RevenueReportFrame extends JFrame {

    private final DashboardService dashSvc;

    public RevenueReportFrame() {
        this.dashSvc = new DashboardService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Ocean View Resort – Revenue & Occupancy Report");
        setSize(850, 650);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(750, 550));

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_PAGE);
        root.add(UITheme.headerPanel("📈", "Revenue & Occupancy Report", "Financial insights and booking trends"),
                BorderLayout.NORTH);

        // ── Tabs ──────────────────────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UITheme.FONT_HEADER);
        tabs.setBackground(Color.WHITE);
        tabs.setOpaque(true);
        tabs.setBorder(new EmptyBorder(10, 20, 10, 20));

        tabs.addTab("💰 Revenue by Room Type", buildRevenueByTypePanel());
        tabs.addTab("📅 Monthly Reservations", buildMonthlyPanel());

        root.add(tabs, BorderLayout.CENTER);

        // ── Action Buttons ────────────────────────────────────────────────────
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        btnBar.setBackground(Color.WHITE);
        btnBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER));

        JButton exportBtn = UITheme.accentBtn("📥  Export to CSV");
        exportBtn.addActionListener(e -> exportToCSV());

        JButton closeBtn = UITheme.dangerBtn("✖  Close");
        closeBtn.addActionListener(e -> dispose());

        btnBar.add(exportBtn);
        btnBar.add(closeBtn);

        root.add(btnBar, BorderLayout.SOUTH);
        add(root);
    }

    private JPanel buildRevenueByTypePanel() {
        JPanel p = new JPanel(new BorderLayout(0, 15));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(15, 15, 15, 15));

        String[] cols = { "Room Type", "Revenue (LKR)", "% Share" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        Map<String, Double> rev = dashSvc.getRevenueByRoomType();
        double total = rev.values().stream().mapToDouble(d -> d).sum();

        for (Map.Entry<String, Double> e : rev.entrySet()) {
            double pct = total > 0 ? e.getValue() / total * 100 : 0;
            model.addRow(new Object[] {
                    e.getKey().replace("_", " "),
                    String.format("LKR %,.2f", e.getValue()),
                    String.format("%.1f%%", pct)
            });
        }
        // Total row
        model.addRow(new Object[] { "TOTAL", String.format("LKR %,.2f", total), "100%" });

        JTable table = new JTable(model);
        UITheme.styleTable(table);

        // Highlight total row
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row,
                    int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (row == model.getRowCount() - 1) {
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                    if (!sel)
                        c.setBackground(new Color(236, 253, 245));
                } else {
                    if (!sel)
                        c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildMonthlyPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 15));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(15, 15, 15, 15));

        String[] cols = { "Month", "Reservations Count", "Visual Trend" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        Map<String, Integer> monthly = dashSvc.getMonthlyReservations();
        int max = monthly.values().stream().mapToInt(i -> i).max().orElse(1);

        for (Map.Entry<String, Integer> e : monthly.entrySet()) {
            String bar = "█".repeat((int) (e.getValue() * 25.0 / (max == 0 ? 1 : max)));
            model.addRow(new Object[] { e.getKey(), e.getValue(), bar });
        }

        JTable table = new JTable(model);
        UITheme.styleTable(table);

        // Custom renderer for bar column
        table.getColumnModel().getColumn(2).setCellRenderer((t, v, sel, foc, row, col) -> {
            JLabel l = new JLabel(v != null ? v.toString() : "");
            l.setForeground(UITheme.PRIMARY_LIGHT);
            l.setFont(new Font("Courier New", Font.BOLD, 14));
            l.setBorder(new EmptyBorder(0, 10, 0, 10));
            if (sel)
                l.setForeground(Color.WHITE);
            return l;
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private void exportToCSV() {
        try (FileWriter w = new FileWriter("revenue_report.csv")) {
            w.write("Room Type,Revenue (LKR)\n");
            for (Map.Entry<String, Double> e : dashSvc.getRevenueByRoomType().entrySet()) {
                w.write(e.getKey() + "," + String.format("%.2f", e.getValue()) + "\n");
            }
            w.write("\nMonth,Reservations\n");
            for (Map.Entry<String, Integer> e : dashSvc.getMonthlyReservations().entrySet()) {
                w.write(e.getKey() + "," + e.getValue() + "\n");
            }
            JOptionPane.showMessageDialog(this,
                    "✅ Exported to revenue_report.csv in the project root folder.",
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
