package com.oceanview.service;

import com.oceanview.dao.GuestDAO;
import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.util.DateUtil;
import com.oceanview.util.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationService {

    private final ReservationDAO reservationDAO;
    private final GuestDAO guestDAO;
    private final RoomDAO roomDAO;
    private final BillingService billingService;

    public ReservationService() {
        this.reservationDAO = new ReservationDAO();
        this.guestDAO = new GuestDAO();
        this.roomDAO = new RoomDAO();
        this.billingService = new BillingService();
    }

    /**
     * Creates a new reservation with full validation.
     * 
     * @return result message (success or validation error)
     */
    public String createReservation(String guestName, String address, String contact,
            int roomId, String checkIn, String checkOut, Integer existingGuestId) {
        // Validate inputs
        if (!Validator.isNotEmpty(guestName))
            return "Guest name is required.";
        if (!Validator.isNotEmpty(address))
            return "Address is required.";
        if (!Validator.isValidPhone(contact))
            return "Invalid contact number. Use 10 digits.";
        if (!Validator.isValidDate(checkIn))
            return "Invalid check-in date. Use YYYY-MM-DD.";
        if (!Validator.isValidDate(checkOut))
            return "Invalid check-out date. Use YYYY-MM-DD.";
        if (!DateUtil.isCheckOutAfterCheckIn(checkIn, checkOut))
            return "Check-out date must be after check-in date.";

        Room room = roomDAO.findById(roomId);
        if (room == null)
            return "Room not found.";
        if (!"AVAILABLE".equals(room.getStatus()))
            return "Room " + room.getRoomNumber() + " is not available.";

        // Handle guest
        int guestId;
        if (existingGuestId != null && existingGuestId > 0) {
            guestId = existingGuestId;
        } else {
            Guest guest = new Guest(guestName.trim(), address.trim(), contact.trim());
            guestId = guestDAO.insertGuest(guest);
            if (guestId == -1)
                return "Failed to save guest details.";
        }

        // Generate reservation ID
        String reservationId = generateReservationId();
        long nights = DateUtil.calculateNights(checkIn, checkOut);
        double total = billingService.calculateTotal(room.getRatePerNight(), nights);

        // Build reservation
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Reservation reservation = new Reservation(reservationId, guestId, roomId, checkIn, checkOut, now);
        reservation.setTotalAmount(total);

        boolean saved = reservationDAO.insertReservation(reservation);
        if (!saved)
            return "Failed to save reservation.";

        // Mark room as OCCUPIED
        roomDAO.updateRoomStatus(roomId, "OCCUPIED");
        return "SUCCESS:" + reservationId;
    }

    public Reservation getReservation(String reservationId) {
        if (!Validator.isNotEmpty(reservationId))
            return null;
        return reservationDAO.findById(reservationId.trim().toUpperCase());
    }

    public List<Reservation> getAllReservations() {
        return reservationDAO.getAllReservations();
    }

    public List<Reservation> getReservationsByGuest(int guestId) {
        return reservationDAO.findByGuestId(guestId);
    }

    public List<Room> getAvailableRooms() {
        return roomDAO.getAllAvailableRooms();
    }

    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    public com.oceanview.model.Guest getGuestById(int id) {
        return guestDAO.findById(id);
    }

    public boolean cancelReservation(String reservationId, int roomId) {
        boolean updated = reservationDAO.updateStatus(reservationId, "CANCELLED");
        // NOTE: Room release is handled automatically by SQLite trigger
        // trg_room_release_on_cancel
        return updated;
    }

    /**
     * Checks out a guest. Room is automatically marked AVAILABLE by
     * SQLite trigger trg_room_release_on_checkout.
     */
    public boolean checkOutReservation(String reservationId) {
        return reservationDAO.updateStatus(reservationId, "CHECKED_OUT");
    }

    private String generateReservationId() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String rand = String.valueOf((int) (Math.random() * 9000) + 1000);
        return "OVR-" + date + "-" + rand;
    }
}
