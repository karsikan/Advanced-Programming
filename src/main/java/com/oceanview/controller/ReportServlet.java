package com.oceanview.controller;

import com.oceanview.service.DashboardService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/reports")
public class ReportServlet extends HttpServlet {

    private DashboardService dashboardService;

    @Override
    public void init() throws ServletException {
        dashboardService = new DashboardService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("totalRevenue", dashboardService.getTotalRevenue());
        request.setAttribute("occupancyRate", dashboardService.getOccupancyRate());
        request.setAttribute("totalReservations", dashboardService.getTotalReservations());

        request.setAttribute("revenueByRoomType", dashboardService.getRevenueByRoomType());
        request.setAttribute("monthlyReservations", dashboardService.getMonthlyReservations());

        request.setAttribute("todayCheckIns", dashboardService.getTodayCheckIns());
        request.setAttribute("todayCheckOuts", dashboardService.getTodayCheckOuts());
        request.setAttribute("occupiedRooms", dashboardService.getOccupiedRooms());
        request.setAttribute("availableRooms", dashboardService.getAvailableRooms());

        request.getRequestDispatcher("revenue_reports.jsp").forward(request, response);
    }
}
