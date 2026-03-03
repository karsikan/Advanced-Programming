package com.oceanview.dao;

import com.oceanview.db.DatabaseConnection;
import com.oceanview.model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    public String insertReservation(Reservation r) {
        String sql = """
                INSERT INTO reservations (reservation_id, guest_id, user_id, room_id,
                check_in_date, check_out_date, total_amount, status, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, r.getReservationId());
            ps.setInt(2, r.getGuestId());
            if (r.getUserId() != null)
                ps.setInt(3, r.getUserId());
            else
                ps.setNull(3, java.sql.Types.INTEGER);
            ps.setInt(4, r.getRoomId());
            ps.setString(5, r.getCheckInDate());
            ps.setString(6, r.getCheckOutDate());
            ps.setDouble(7, r.getTotalAmount());
            ps.setString(8, r.getStatus());
            ps.setString(9, r.getCreatedAt());
            return ps.executeUpdate() > 0 ? "SUCCESS" : "No rows affected during insertion.";
        } catch (SQLException e) {
            System.err.println("ReservationDAO insert error: " + e.getMessage());
            return "Database Error: " + e.getMessage();
        }
    }

    public Reservation findById(String reservationId) {
        String sql = """
                SELECT r.*, g.name, g.address, g.contact_number,
                       rm.room_number, rm.room_type, rm.rate_per_night
                FROM reservations r
                JOIN guests g ON r.guest_id = g.id
                JOIN rooms rm ON r.room_id = rm.id
                WHERE r.reservation_id = ?
                """;
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, reservationId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("ReservationDAO findById error: " + e.getMessage());
        }
        return null;
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        String sql = """
                SELECT r.*, g.name, g.address, g.contact_number,
                       rm.room_number, rm.room_type, rm.rate_per_night
                FROM reservations r
                JOIN guests g ON r.guest_id = g.id
                JOIN rooms rm ON r.room_id = rm.id
                ORDER BY r.created_at DESC
                """;
        try (Statement stmt = getConn().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next())
                list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("ReservationDAO getAll error: " + e.getMessage());
        }
        return list;
    }

    public boolean updateStatus(String reservationId, String status) {
        String sql = "UPDATE reservations SET status = ? WHERE reservation_id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, reservationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ReservationDAO updateStatus error: " + e.getMessage());
        }
        return false;
    }

    public List<Reservation> getReservationsByUserId(int userId) {
        List<Reservation> list = new ArrayList<>();
        String sql = """
                SELECT r.*, g.name, g.address, g.contact_number,
                       rm.room_number, rm.room_type, rm.rate_per_night
                FROM reservations r
                JOIN guests g ON r.guest_id = g.id
                JOIN rooms rm ON r.room_id = rm.id
                WHERE r.user_id = ?
                ORDER BY r.created_at DESC
                """;
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("ReservationDAO getByUserId error: " + e.getMessage());
        }
        return list;
    }

    public boolean reservationExists(String reservationId) {
        String sql = "SELECT 1 FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, reservationId);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("ReservationDAO exists error: " + e.getMessage());
        }
        return false;
    }

    private Reservation mapRow(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();
        r.setReservationId(rs.getString("reservation_id"));
        r.setGuestId(rs.getInt("guest_id"));
        int uId = rs.getInt("user_id");
        if (!rs.wasNull())
            r.setUserId(uId);
        r.setRoomId(rs.getInt("room_id"));
        r.setCheckInDate(rs.getString("check_in_date"));
        r.setCheckOutDate(rs.getString("check_out_date"));
        r.setTotalAmount(rs.getDouble("total_amount"));
        r.setStatus(rs.getString("status"));
        r.setCreatedAt(rs.getString("created_at"));
        r.setGuestName(rs.getString("name"));
        r.setGuestAddress(rs.getString("address"));
        r.setGuestContact(rs.getString("contact_number"));
        r.setRoomNumber(rs.getString("room_number"));
        r.setRoomType(rs.getString("room_type"));
        r.setRatePerNight(rs.getDouble("rate_per_night"));
        return r;
    }

    public java.util.Map<String, Object> getStatsByUserId(int userId) {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        String sql = "SELECT SUM(total_amount) as totalSpent, COUNT(*) as totalBookings, " +
                "SUM(TIMESTAMPDIFF(DAY, check_in_date, check_out_date)) as totalNights " +
                "FROM reservations WHERE user_id = ? AND status = 'CONFIRMED'";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                stats.put("totalSpent", rs.getBigDecimal("totalSpent") != null ? rs.getBigDecimal("totalSpent")
                        : java.math.BigDecimal.ZERO);
                stats.put("totalBookings", rs.getInt("totalBookings"));
                stats.put("totalNights", rs.getInt("totalNights"));
            }
        } catch (SQLException e) {
            System.err.println("ReservationDAO getStats error: " + e.getMessage());
        }
        return stats;
    }

    public boolean updateReservation(Reservation r) {
        String sql = "UPDATE reservations SET check_in_date = ?, check_out_date = ?, total_amount = ? WHERE reservation_id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, r.getCheckInDate());
            ps.setString(2, r.getCheckOutDate());
            ps.setDouble(3, r.getTotalAmount());
            ps.setString(4, r.getReservationId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ReservationDAO update error: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteReservation(String reservationId) {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, reservationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ReservationDAO delete error: " + e.getMessage());
        }
        return false;
    }
}
