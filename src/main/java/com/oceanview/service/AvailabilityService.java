package com.oceanview.service;

import com.oceanview.db.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;

/**
 * AvailabilityService - Logic to prevent booking overlaps and conflicts.
 */
public class AvailabilityService {

    /**
     * Checks if a room is available for the given date range.
     * Logic: Overlap occurs if (RequestedCheckIn < ExistingCheckOut) AND
     * (RequestedCheckOut > ExistingCheckIn)
     *
     * @param roomId   The ID of the room to check
     * @param checkIn  The requested check-in date (YYYY-MM-DD)
     * @param checkOut The requested check-out date (YYYY-MM-DD)
     * @return true if available, false if overlapping
     */
    public boolean isRoomAvailable(int roomId, String checkIn, String checkOut) {
        String sql = """
                SELECT COUNT(*) FROM reservations
                WHERE room_id = ?
                AND status = 'CONFIRMED'
                AND (
                    (check_in_date < ? AND check_out_date > ?)
                )
                """;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ps.setString(2, checkOut); // End of requested range
            ps.setString(3, checkIn); // Start of requested range

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            System.err.println("Availability check error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Validates that check-out is after check-in.
     */
    public boolean isValidDateRange(String checkIn, String checkOut) {
        try {
            LocalDate in = LocalDate.parse(checkIn);
            LocalDate out = LocalDate.parse(checkOut);
            return out.isAfter(in);
        } catch (Exception e) {
            return false;
        }
    }
}
