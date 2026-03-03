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
    private final AvailabilityService availabilityService;

    public ReservationService() {
        this.reservationDAO = new ReservationDAO();
        this.guestDAO = new GuestDAO();
        this.roomDAO = new RoomDAO();
        this.billingService = new BillingService();
        this.availabilityService = new AvailabilityService();
    }

    /**
     * Creates a new reservation with full validation.
     * 
     * @return result message (success or validation error)
     */
    public String createReservation(String guestName, String address, String contact,
            int roomId, String checkIn, String checkOut, Integer userId) {
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

        // Intelligent Availability Check (Date overlap logic)
        if (!availabilityService.isRoomAvailable(roomId, checkIn, checkOut)) {
            return "Room " + room.getRoomNumber() + " is already booked for these dates.";
        }

        // Create guest
        Guest guest = new Guest(guestName.trim(), address.trim(), contact.trim());
        int guestId = guestDAO.insertGuest(guest);
        if (guestId == -1)
            return "Failed to save guest details.";

        // Generate reservation ID
        String reservationId = generateReservationId();
        long nights = DateUtil.calculateNights(checkIn, checkOut);
        double total = billingService.calculateTotal(room.getRatePerNight(), nights);

        // Build reservation
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Reservation reservation = new Reservation(reservationId, guestId, roomId, checkIn, checkOut, now);
        reservation.setTotalAmount(total);
        reservation.setUserId(userId);

        String dbResult = reservationDAO.insertReservation(reservation);
        if (!"SUCCESS".equals(dbResult))
            return dbResult;

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

    public List<Reservation> getReservationsByUserId(int userId) {
        return reservationDAO.getReservationsByUserId(userId);
    }

    /**
     * For REST API, returns list without sensitive guest data if needed.
     */
    public List<Reservation> getAllSafeReservations() {
        return reservationDAO.getAllReservations();
    }

    public List<Room> getAvailableRooms() {
        return roomDAO.getAllAvailableRooms();
    }

    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    public boolean cancelReservation(String reservationId, int roomId) {
        return reservationDAO.updateStatus(reservationId, "CANCELLED");
    }

    /**
     * Secure cancellation for Customers - verifies ownership.
     */
    public boolean cancelReservationForCustomer(String reservationId, int roomId, int userId) {
        Reservation res = reservationDAO.findById(reservationId);
        if (res != null && res.getUserId() != null && res.getUserId() == userId) {
            return cancelReservation(reservationId, roomId);
        }
        return false;
    }

    /**
     * Checks out a guest. Room is automatically marked AVAILABLE by
     * SQLite trigger trg_room_release_on_checkout.
     */
    public boolean checkOutReservation(String reservationId) {
        return reservationDAO.updateStatus(reservationId, "CHECKED_OUT");
    }

    public boolean updateReservation(String reservationId, String checkIn, String checkOut) {
        Reservation res = reservationDAO.findById(reservationId);
        if (res == null)
            return false;

        if (!Validator.isValidDate(checkIn) || !Validator.isValidDate(checkOut))
            return false;
        if (!DateUtil.isCheckOutAfterCheckIn(checkIn, checkOut))
            return false;

        res.setCheckInDate(checkIn);
        res.setCheckOutDate(checkOut);

        long nights = DateUtil.calculateNights(checkIn, checkOut);
        double total = billingService.calculateTotal(res.getRatePerNight(), nights);
        res.setTotalAmount(total);

        return reservationDAO.updateReservation(res);
    }

    public boolean deleteReservation(String reservationId) {
        return reservationDAO.deleteReservation(reservationId);
    }

    private String generateReservationId() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String rand = String.valueOf((int) (Math.random() * 9000) + 1000);
        return "OVR-" + date + "-" + rand;
    }

    public java.util.Map<String, Object> getCustomerStats(int userId) {
        return reservationDAO.getStatsByUserId(userId);
    }
}
