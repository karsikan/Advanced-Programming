package com.oceanview.controller;

import com.oceanview.model.Room;
import com.oceanview.service.ReservationService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/reservations")
public class ReservationServlet extends HttpServlet {

    private ReservationService reservationService;

    @Override
    public void init() throws ServletException {
        reservationService = new ReservationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "all":
                    listAll(request, response);
                    break;
                case "my":
                    listMyReservations(request, response);
                    break;
                default:
                    showNewForm(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                addReservation(request, response);
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Room> availableRooms = reservationService.getAvailableRooms();
        request.setAttribute("rooms", availableRooms);
        request.getRequestDispatcher("reservation_form.jsp").forward(request, response);
    }

    private void listAll(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("reservations", reservationService.getAllReservations());
        // For Admin Dashboard (All Reservations)
        request.getRequestDispatcher("all_reservations.jsp").forward(request, response);
    }

    private void listMyReservations(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Object guestIdObj = request.getSession().getAttribute("guestId");
        if (guestIdObj != null) {
            int guestId = (Integer) guestIdObj;
            request.setAttribute("reservations", reservationService.getReservationsByGuest(guestId));
        }
        // For Customer Dashboard (My Reservations)
        request.getRequestDispatcher("my_reservations.jsp").forward(request, response);
    }

    private void addReservation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String guestName = request.getParameter("guestName");
        String address = request.getParameter("address");
        String contact = request.getParameter("contact");
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        String checkInDate = request.getParameter("checkInDate");
        String checkOutDate = request.getParameter("checkOutDate");

        // existingGuestId might come from Customer's own session, or 0 if new guest via
        // Staff
        Integer guestId = null;
        Object sessionGuestId = request.getSession().getAttribute("guestId");
        if (sessionGuestId != null && (Integer) sessionGuestId > 0) {
            guestId = (Integer) sessionGuestId;
        }

        String result = reservationService.createReservation(guestName, address, contact, roomId, checkInDate,
                checkOutDate, guestId);

        if (result.startsWith("SUCCESS:")) {
            String resId = result.split(":")[1];
            response.sendRedirect("reservations?action=new&success=Reservation created successfully! ID: " + resId);
        } else {
            response.sendRedirect("reservations?action=new&error=" + java.net.URLEncoder.encode(result, "UTF-8"));
        }
    }
}
