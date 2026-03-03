package com.oceanview.servlet;

import com.oceanview.service.DashboardService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/revenue")
public class RevenueReportServlet extends HttpServlet {

    private final DashboardService dashboardService = new DashboardService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String export = request.getParameter("export");
        if ("csv".equalsIgnoreCase(export)) {
            exportToCSV(response);
            return;
        }

        Map<String, Double> revenueData = dashboardService.getRevenueByRoomType();
        request.setAttribute("revenueData", revenueData);
        request.setAttribute("monthlyRevenue", dashboardService.getMonthlyRevenue());
        request.setAttribute("totalRevenue", dashboardService.getTotalRevenue());
        request.setAttribute("occupancyRate", dashboardService.getOccupancyRate());

        request.getRequestDispatcher("/revenue_reports.jsp").forward(request, response);
    }

    private void exportToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=Revenue_Report.csv");

        Map<String, Double> revenueData = dashboardService.getRevenueByRoomType();
        PrintWriter writer = response.getWriter();
        writer.println("Room Type,Revenue (LKR)");

        double total = 0;
        for (Map.Entry<String, Double> entry : revenueData.entrySet()) {
            writer.println(entry.getKey() + "," + entry.getValue());
            total += entry.getValue();
        }
        writer.println("TOTAL," + total);
        writer.flush();
    }
}
