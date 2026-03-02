package com.oceanview.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Central UI Theme class – Ocean View Resort Design System
 * All colours, fonts, and component styles are defined here.
 */
public class UITheme {

    // ── Colour Palette ────────────────────────────────────────────────────────
    public static final Color PRIMARY = new Color(6, 95, 70); // Deep teal
    public static final Color PRIMARY_LIGHT = new Color(16, 185, 129); // Emerald
    public static final Color PRIMARY_DARK = new Color(2, 55, 40); // Dark teal
    public static final Color ACCENT = new Color(251, 191, 36); // Golden
    public static final Color ACCENT_BLUE = new Color(59, 130, 246); // Blue
    public static final Color DANGER = new Color(220, 38, 38); // Red
    public static final Color WARNING = new Color(245, 158, 11); // Amber
    public static final Color SUCCESS = new Color(16, 185, 129); // Green
    public static final Color INFO = new Color(99, 102, 241); // Indigo

    // Backgrounds
    public static final Color BG_DARK = new Color(15, 23, 42);
    public static final Color BG_CARD = new Color(30, 41, 59);
    public static final Color BG_PAGE = new Color(236, 253, 245); // Mint white
    public static final Color BG_PANEL = new Color(255, 255, 255);
    public static final Color BG_HEADER = new Color(6, 95, 70);

    // Text
    public static final Color TEXT_DARK = new Color(15, 30, 20);
    public static final Color TEXT_MUTED = new Color(100, 130, 110);
    public static final Color TEXT_WHITE = Color.WHITE;
    public static final Color TEXT_LINK = new Color(6, 182, 212);

    // Borders
    public static final Color BORDER = new Color(167, 243, 208);

    // ── Fonts ─────────────────────────────────────────────────────────────────
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO = new Font("Courier New", Font.PLAIN, 13);
    public static final Font FONT_BTN = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_TABLE_H = new Font("Segoe UI", Font.BOLD, 13);

    // ── Component Factories ───────────────────────────────────────────────────

    /** Primary action button (teal) */
    public static JButton primaryBtn(String text) {
        return styledBtn(text, PRIMARY, TEXT_WHITE);
    }

    /** Danger button (red) */
    public static JButton dangerBtn(String text) {
        return styledBtn(text, DANGER, TEXT_WHITE);
    }

    /** Secondary button (outline style) */
    public static JButton secondaryBtn(String text) {
        JButton b = styledBtn(text, new Color(230, 250, 240), PRIMARY);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 1, true),
                new EmptyBorder(8, 18, 8, 18)));
        return b;
    }

    public static JButton accentBtn(String text) {
        return styledBtn(text, ACCENT, TEXT_DARK);
    }

    private static JButton styledBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(FONT_BTN);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setBorder(new EmptyBorder(9, 22, 9, 22));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(bg.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(bg);
            }
        });
        return b;
    }

    /** Formatted text field */
    public static JTextField textField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(FONT_BODY);
        f.setToolTipText(placeholder);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        f.setBackground(new Color(240, 253, 244));
        return f;
    }

    /** Formatted combo box */
    public static JComboBox<String> comboBox(String... items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(FONT_BODY);
        cb.setBackground(new Color(240, 253, 244));
        cb.setBorder(BorderFactory.createLineBorder(BORDER));
        return cb;
    }

    /** Section title label */
    public static JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_HEADER);
        l.setForeground(PRIMARY);
        l.setBorder(new EmptyBorder(0, 0, 6, 0));
        return l;
    }

    /** Header panel with gradient */
    public static JPanel headerPanel(String emoji, String title, String subtitle) {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_DARK, getWidth(), 0, PRIMARY);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        p.setLayout(new BorderLayout(10, 0));
        p.setBorder(new EmptyBorder(16, 24, 16, 24));
        JLabel t = new JLabel(emoji + "  " + title);
        t.setFont(FONT_TITLE);
        t.setForeground(TEXT_WHITE);
        JLabel s = new JLabel(subtitle);
        s.setFont(FONT_SMALL);
        s.setForeground(new Color(167, 243, 208));
        JPanel col = new JPanel(new GridLayout(2, 1));
        col.setOpaque(false);
        col.add(t);
        col.add(s);
        p.add(col, BorderLayout.WEST);
        return p;
    }

    /** Styled JTable header and rows */
    public static void styleTable(JTable table) {
        table.setFont(FONT_TABLE);
        table.setRowHeight(32);
        table.setShowGrid(true);
        table.setGridColor(new Color(209, 250, 229));
        table.setSelectionBackground(new Color(167, 243, 208));
        table.setSelectionForeground(PRIMARY_DARK);
        table.setBackground(Color.WHITE);
        table.getTableHeader().setFont(FONT_TABLE_H);
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));
        table.setIntercellSpacing(new Dimension(8, 0));
    }

    /** Card panel with rounded border */
    public static JPanel cardPanel() {
        JPanel p = new JPanel();
        p.setBackground(BG_PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(18, 22, 18, 22)));
        return p;
    }

    /** Form row: label + field */
    public static JPanel formRow(String labelText, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(PRIMARY_DARK);
        lbl.setPreferredSize(new Dimension(140, 30));
        row.add(lbl, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }
}
