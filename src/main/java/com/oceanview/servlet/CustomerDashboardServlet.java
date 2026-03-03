package com.oceanview.servlet;

import com.oceanview.model.Reservation;
import com.oceanview.service.ReservationService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * CustomerDashboardServlet - Personalized view for Customer role.
 */
@WebServlet("/customer/dashboard")
public class CustomerDashboardServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("loggedInRole");
        Integer userId = (Integer) session.getAttribute("loggedInUserId");

        if (userId == null || !"CUSTOMER".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Fetch only reservations belonging to this customer
        List<Reservation> myReservations = reservationService.getReservationsByUserId(userId);
        request.setAttribute("myReservations", myReservations);

        // Fetch loyalty stats
        java.util.Map<String, Object> stats = reservationService.getCustomerStats(userId);
        request.setAttribute("customerStats", stats);

        // Forward to the customer dashboard JSP (created next)
        request.getRequestDispatcher("/customer_dashboard.jsp").forward(request, response);
    }
}
