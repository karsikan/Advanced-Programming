package com.oceanview.dao;

import com.oceanview.db.DatabaseConnection;
import com.oceanview.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    public List<Room> getAllAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE status = 'AVAILABLE' ORDER BY room_number";
        try (Statement stmt = getConn().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("RoomDAO error: " + e.getMessage());
        }
        return rooms;
    }

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY room_number";
        try (Statement stmt = getConn().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("RoomDAO getAllRooms error: " + e.getMessage());
        }
        return rooms;
    }

    public Room findById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("RoomDAO findById error: " + e.getMessage());
        }
        return null;
    }

    public boolean updateRoomStatus(int roomId, String status) {
        String sql = "UPDATE rooms SET status = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, roomId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("RoomDAO updateStatus error: " + e.getMessage());
        }
        return false;
    }

    public boolean insertRoom(Room room) {
        String sql = "INSERT INTO rooms (room_number, room_type, rate_per_night) VALUES (?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType());
            ps.setDouble(3, room.getRatePerNight());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("RoomDAO insert error: " + e.getMessage());
        }
        return false;
    }

    public boolean updateRoom(Room room) {
        String sql = "UPDATE rooms SET room_number = ?, room_type = ?, rate_per_night = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType());
            ps.setDouble(3, room.getRatePerNight());
            ps.setInt(4, room.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("RoomDAO update error: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteRoom(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("RoomDAO delete error: " + e.getMessage());
        }
        return false;
    }

    private Room mapRow(ResultSet rs) throws SQLException {
        return new Room(
                rs.getInt("id"),
                rs.getString("room_number"),
                rs.getString("room_type"),
                rs.getDouble("rate_per_night"),
                rs.getString("status"));
    }
}
