package com.oceanview.controller;

import com.oceanview.model.Reservation;
import com.oceanview.service.DashboardService;
import com.oceanview.service.ReservationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private DashboardService dashboardService;
    private ReservationService reservationService;

    @Override
    public void init() throws ServletException {
        dashboardService = new DashboardService();
        reservationService = new ReservationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String role = (String) session.getAttribute("role");

        if ("ADMIN".equals(role) || "STAFF".equals(role)) {
            // Load stats for Admin/Staff
            Map<String, Object> stats = dashboardService.getDashboardStats();
            request.setAttribute("stats", stats);

            // Just get recent reservations
            List<Reservation> allReservations = reservationService.getAllReservations();
            request.setAttribute("reservations", allReservations);
        } else if ("CUSTOMER".equals(role)) {
            // Load only customer's reservations
            Integer guestId = (Integer) session.getAttribute("guestId");
            if (guestId != null) {
                List<Reservation> guestReservations = reservationService.getReservationsByGuest(guestId);
                request.setAttribute("reservations", guestReservations);
            }
        }

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
