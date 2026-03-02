package com.oceanview.service;

import com.oceanview.db.DatabaseConnection;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Service for fetching dashboard statistics and business reports.
 * Uses the database VIEW v_reservation_report for efficient queries.
 */
public class DashboardService {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    public int getTotalReservations() {
        return queryInt("SELECT COUNT(*) FROM reservations WHERE status != 'CANCELLED'");
    }

    public int getAvailableRooms() {
        return queryInt("SELECT COUNT(*) FROM rooms WHERE status = 'AVAILABLE'");
    }

    public int getOccupiedRooms() {
        return queryInt("SELECT COUNT(*) FROM rooms WHERE status = 'OCCUPIED'");
    }

    public int getTotalRooms() {
        return queryInt("SELECT COUNT(*) FROM rooms");
    }

    public double getTotalRevenue() {
        String sql = "SELECT IFNULL(SUM(total_amount), 0) FROM reservations WHERE status IN ('CONFIRMED','CHECKED_OUT')";
        try (Statement s = getConn().createStatement(); ResultSet rs = s.executeQuery(sql)) {
            if (rs.next())
                return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    public int getTodayCheckIns() {
        return queryInt("SELECT COUNT(*) FROM reservations WHERE check_in_date = CURDATE() AND status='CONFIRMED'");
    }

    public int getTodayCheckOuts() {
        return queryInt("SELECT COUNT(*) FROM reservations WHERE check_out_date = CURDATE() AND status='CONFIRMED'");
    }

    public double getOccupancyRate() {
        int total = getTotalRooms();
        if (total == 0)
            return 0;
        return (double) getOccupiedRooms() / total * 100;
    }

    /** Revenue grouped by room type */
    public Map<String, Double> getRevenueByRoomType() {
        Map<String, Double> map = new LinkedHashMap<>();
        String sql = """
                SELECT rm.room_type, IFNULL(SUM(r.total_amount), 0) as revenue
                FROM rooms rm LEFT JOIN reservations r ON rm.id = r.room_id
                AND r.status IN ('CONFIRMED','CHECKED_OUT')
                GROUP BY rm.room_type ORDER BY revenue DESC""";
        try (Statement s = getConn().createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next())
                map.put(rs.getString("room_type"), rs.getDouble("revenue"));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return map;
    }

    /** Monthly reservation counts for the current year */
    public Map<String, Integer> getMonthlyReservations() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        for (String m : months)
            map.put(m, 0);

        String sql = """
                SELECT DATE_FORMAT(created_at, '%m') as month, COUNT(*) as cnt
                FROM reservations WHERE status != 'CANCELLED'
                AND YEAR(created_at) = YEAR(NOW())
                GROUP BY month""";
        try (Statement s = getConn().createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                int idx = Integer.parseInt(rs.getString("month")) - 1;
                map.put(months[idx], rs.getInt("cnt"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return map;
    }

    /** Recent audit log entries */
    public java.util.List<String[]> getRecentAuditLog(int limit) {
        java.util.List<String[]> list = new java.util.ArrayList<>();
        String sql = "SELECT action, table_name, record_id, changed_at, details FROM audit_log ORDER BY id DESC LIMIT ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[] {
                        rs.getString("changed_at"), rs.getString("action"),
                        rs.getString("record_id"), rs.getString("details") });
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return list;
    }

    private int queryInt(String sql) {
        try (Statement s = getConn().createStatement(); ResultSet rs = s.executeQuery(sql)) {
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }
}
