package com.oceanview.servlet.api;

import com.google.gson.Gson;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.service.DashboardService;
import com.oceanview.service.ReservationService;
import com.oceanview.service.AvailabilityService;
import com.oceanview.service.BillingService;
import com.oceanview.util.DateUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RestApiController - Provides JSON web services for the distributed
 * application. Supports strategic data exchange and external platform integration.
 */
@WebServlet("/api/*")
public class RestApiController extends HttpServlet {

    private final Gson gson = new Gson();
    private final DashboardService dashboardService = new DashboardService();
    private final ReservationService reservationService = new ReservationService();
    private final AvailabilityService availabilityService = new AvailabilityService();
    private final BillingService billingService = new BillingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                Map<String, Object> welcome = new HashMap<>();
                welcome.put("message", "Ocean View Resort Intelligence Hub API");
                welcome.put("version", "v1.5 (Strategic)");
                welcome.put("endpoints", new String[] { "/api/dashboard", "/api/reservations", "/api/rooms", "/api/bill?resId=..." });
                response.getWriter().write(gson.toJson(welcome));
            } else if (pathInfo.equals("/dashboard")) {
                handleDashboard(response);
            } else if (pathInfo.equals("/reservations")) {
                handleReservations(response);
            } else if (pathInfo.equals("/rooms")) {
                handleRooms(request, response);
            } else if (pathInfo.equals("/rooms/available")) {
                handleAvailability(request, response);
            } else if (pathInfo.equals("/bill")) {
                handleBill(request, response);
            } else {
                sendError(response, "Endpoint not found", 404);
            }
        } catch (Exception e) {
            sendError(response, "Intelligence Subsystem Failure: " + e.getMessage(), 500);
        }
    }

    private void handleDashboard(HttpServletResponse response) throws IOException {
        Map<String, Object> stats = dashboardService.getDashboardStats();
        response.getWriter().write(gson.toJson(stats));
    }

    private void handleReservations(HttpServletResponse response) throws IOException {
        List<Reservation> reservations = reservationService.getAllSafeReservations();
        response.getWriter().write(gson.toJson(reservations));
    }

    private void handleRooms(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Room> rooms = reservationService.getAllRooms();
        response.getWriter().write(gson.toJson(rooms));
    }

    private void handleAvailability(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roomIdStr = request.getParameter("roomId");
        String checkIn = request.getParameter("checkIn");
        String checkOut = request.getParameter("checkOut");

        if (roomIdStr == null || checkIn == null || checkOut == null) {
            sendError(response, "Incomplete parameters: [roomId, checkIn, checkOut] required", 400);
            return;
        }

        int roomId = Integer.parseInt(roomIdStr);
        boolean available = availabilityService.isRoomAvailable(roomId, checkIn, checkOut);

        Map<String, Object> result = new HashMap<>();
        result.put("roomId", roomId);
        result.put("isAvailable", available);
        result.put("requestedPeriod", checkIn + " to " + checkOut);
        
        response.getWriter().write(gson.toJson(result));
    }

    private void handleBill(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resId = request.getParameter("resId");
        if (resId == null || resId.isEmpty()) {
            sendError(response, "Parameter 'resId' is required for financial statements", 400);
            return;
        }

        Reservation r = reservationService.getReservation(resId);
        if (r == null) {
            sendError(response, "Statement not found for Identifier: " + resId, 410);
            return;
        }

        long nights = DateUtil.calculateNights(r.getCheckInDate(), r.getCheckOutDate());
        Map<String, Object> billData = new HashMap<>();
        billData.put("reservationId", r.getReservationId());
        billData.put("guest", r.getGuestName());
        billData.put("stayNights", nights);
        billData.put("subTotal", billingService.getSubTotal(r.getRatePerNight(), nights));
        billData.put("taxAmount", billingService.getTax(r.getRatePerNight(), nights));
        billData.put("serviceCharge", billingService.getServiceCharge(r.getRatePerNight(), nights));
        billData.put("totalPayable", r.getTotalAmount());
        billData.put("currency", "LKR");

        response.getWriter().write(gson.toJson(billData));
    }

    private void sendError(HttpServletResponse response, String message, int code) throws IOException {
        response.setStatus(code);
        Map<String, String> error = new HashMap<>();
        error.put("status", "FAILURE");
        error.put("code", String.valueOf(code));
        error.put("details", message);
        response.getWriter().write(gson.toJson(error));
    }
}


