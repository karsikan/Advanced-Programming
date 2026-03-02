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

    public boolean insertReservation(Reservation r) {
        String sql = """
                INSERT INTO reservations (reservation_id, guest_id, room_id,
                check_in_date, check_out_date, total_amount, status, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, r.getReservationId());
            ps.setInt(2, r.getGuestId());
            ps.setInt(3, r.getRoomId());
            ps.setString(4, r.getCheckInDate());
            ps.setString(5, r.getCheckOutDate());
            ps.setDouble(6, r.getTotalAmount());
            ps.setString(7, r.getStatus());
            ps.setString(8, r.getCreatedAt());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ReservationDAO insert error: " + e.getMessage());
        }
        return false;
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

    public List<Reservation> findByGuestId(int guestId) {
        List<Reservation> list = new ArrayList<>();
        String sql = """
                SELECT r.*, g.name, g.address, g.contact_number,
                       rm.room_number, rm.room_type, rm.rate_per_night
                FROM reservations r
                JOIN guests g ON r.guest_id = g.id
                JOIN rooms rm ON r.room_id = rm.id
                WHERE r.guest_id = ?
                ORDER BY r.created_at DESC
                """;
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, guestId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("ReservationDAO findByGuestId error: " + e.getMessage());
        }
        return list;
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
}
