package com.oceanview.servlet;

import com.oceanview.service.DashboardService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final DashboardService dashboardService = new DashboardService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Fetch statistics
        request.setAttribute("totalReservations", dashboardService.getTotalReservations());
        request.setAttribute("availableRooms", dashboardService.getAvailableRooms());
        request.setAttribute("occupiedRooms", dashboardService.getOccupiedRooms());
        request.setAttribute("totalRevenue", dashboardService.getTotalRevenue());
        request.setAttribute("occupancyRate", dashboardService.getOccupancyRate());
        request.setAttribute("todayCheckIns", dashboardService.getTodayCheckIns());
        request.setAttribute("todayCheckOuts", dashboardService.getTodayCheckOuts());
        request.setAttribute("revenueByRoomType", dashboardService.getRevenueByRoomType());
        request.setAttribute("recentLogs", dashboardService.getRecentAuditLog(8));

        // Forward to the dashboard JSP
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}
