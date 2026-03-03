package com.oceanview.servlet;

import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.service.ReservationService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = { "/reservations", "/reservation/new", "/reservation/cancel", "/reservation/edit",
        "/reservation/update" })
public class ReservationServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String path = request.getServletPath();

        if ("/reservations".equals(path)) {
            // View all reservations
            List<Reservation> reservations = reservationService.getAllReservations();
            request.setAttribute("reservationsList", reservations);
            request.getRequestDispatcher("/all_reservations.jsp").forward(request, response);

        } else if ("/reservation/new".equals(path)) {
            // Show new reservation form
            String role = (String) session.getAttribute("loggedInRole");
            if ("CUSTOMER".equalsIgnoreCase(role)) {
                String username = (String) session.getAttribute("loggedInUser");
                com.oceanview.dao.UserDAO userDAO = new com.oceanview.dao.UserDAO();
                com.oceanview.model.User user = userDAO.findByUsername(username);
                if (user != null && user.getGuestId() != null) {
                    com.oceanview.dao.GuestDAO guestDAO = new com.oceanview.dao.GuestDAO();
                    request.setAttribute("preFillGuest", guestDAO.findById(user.getGuestId()));
                }
            }
            List<Room> availableRooms = reservationService.getAvailableRooms();
            request.setAttribute("availableRooms", availableRooms);
            request.getRequestDispatcher("/reservation_form.jsp").forward(request, response);
        } else if ("/reservation/edit".equals(path)) {
            String resId = request.getParameter("id");
            Reservation res = reservationService.getReservation(resId);
            if (res != null) {
                request.setAttribute("reservation", res);
                request.setAttribute("isEdit", true);
                List<Room> availableRooms = reservationService.getAllRooms(); // Show all for edit
                request.setAttribute("availableRooms", availableRooms);
                request.getRequestDispatcher("/reservation_form.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/reservations");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        String path = request.getServletPath();
        String message = "";
        String msgType = "error";

        if ("/reservation/new".equals(path)) {
            // Handle form submission
            try {
                String guestName = request.getParameter("guestName");
                String address = request.getParameter("address");
                String contact = request.getParameter("contact");
                int roomId = Integer.parseInt(request.getParameter("roomId"));
                String checkIn = request.getParameter("checkIn");
                String checkOut = request.getParameter("checkOut");

                Integer loggedInUserId = (Integer) session.getAttribute("loggedInUserId");
                String result = reservationService.createReservation(guestName, address, contact, roomId, checkIn,
                        checkOut, loggedInUserId);

                if (result.startsWith("SUCCESS:")) {
                    String resId = result.split(":")[1];
                    message = "Reservation Created Successfully! ID: " + resId;
                    msgType = "success";
                    session.setAttribute("flashMessage", message);
                    session.setAttribute("flashType", msgType);

                    String role = (String) session.getAttribute("loggedInRole");
                    if ("CUSTOMER".equalsIgnoreCase(role)) {
                        response.sendRedirect(request.getContextPath() + "/customer/dashboard");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/reservations");
                    }
                    return;
                } else {
                    message = result;
                }
            } catch (Exception e) {
                message = "Invalid data provided. Please check your inputs.";
            }

            // If error, go back to form
            request.setAttribute("errorMessage", message);
            List<Room> availableRooms = reservationService.getAvailableRooms();
            request.setAttribute("availableRooms", availableRooms);
            request.getRequestDispatcher("/reservation_form.jsp").forward(request, response);

        } else if ("/reservation/update".equals(path)) {
            try {
                String resId = request.getParameter("reservationId");
                String checkIn = request.getParameter("checkIn");
                String checkOut = request.getParameter("checkOut");

                boolean success = reservationService.updateReservation(resId, checkIn, checkOut);
                if (success) {
                    session.setAttribute("flashMessage", "Reservation Updated Successfully!");
                    session.setAttribute("flashType", "success");
                } else {
                    session.setAttribute("flashMessage", "Failed to update reservation. Check dates.");
                    session.setAttribute("flashType", "error");
                }
            } catch (Exception e) {
                session.setAttribute("flashMessage", "System error during update.");
                session.setAttribute("flashType", "error");
            }
            response.sendRedirect(request.getContextPath() + "/reservations");

        } else if ("/reservation/cancel".equals(path)) {
            String resId = request.getParameter("reservationId");
            try {
                int roomId = Integer.parseInt(request.getParameter("roomId"));
                String role = (String) session.getAttribute("loggedInRole");
                Integer userId = (Integer) session.getAttribute("loggedInUserId");

                boolean success;
                if ("CUSTOMER".equalsIgnoreCase(role)) {
                    success = reservationService.cancelReservationForCustomer(resId, roomId, userId);
                } else {
                    success = reservationService.cancelReservation(resId, roomId);
                }

                if (success) {
                    message = "Reservation " + resId + " cancelled successfully.";
                    msgType = "success";
                } else {
                    message = "Failed to cancel reservation. You may not have permission or it is already cancelled.";
                }
            } catch (Exception e) {
                message = "Invalid request.";
            }
            session.setAttribute("flashMessage", message);
            session.setAttribute("flashType", msgType);

            // Role-based redirect
            if ("CUSTOMER".equalsIgnoreCase((String) session.getAttribute("loggedInRole"))) {
                response.sendRedirect(request.getContextPath() + "/customer/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/reservations");
            }
        }
    }
}
