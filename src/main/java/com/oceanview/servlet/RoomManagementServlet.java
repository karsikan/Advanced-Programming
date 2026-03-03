package com.oceanview.servlet;

import com.oceanview.model.Room;
import com.oceanview.service.AdminService;
import com.oceanview.service.ReservationService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/rooms")
public class RoomManagementServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();
    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Room> rooms = reservationService.getAllRooms();
        long availCount = rooms.stream().filter(r -> "AVAILABLE".equals(r.getStatus())).count();
        long occCount = rooms.stream().filter(r -> "OCCUPIED".equals(r.getStatus())).count();

        request.setAttribute("roomsList", rooms);
        request.setAttribute("availCount", availCount);
        request.setAttribute("occCount", occCount);
        request.setAttribute("totalCount", rooms.size());

        request.getRequestDispatcher("/room_management.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("loggedInRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return; // Only admins can do POST actions here
        }

        String action = request.getParameter("action");
        String message = "";
        String msgType = "error";

        try {
            if ("add".equals(action) || "edit".equals(action)) {
                String idStr = request.getParameter("roomId");
                Integer id = (idStr != null && !idStr.isEmpty()) ? Integer.parseInt(idStr) : null;
                String roomNo = request.getParameter("roomNumber");
                String type = request.getParameter("roomType");
                double rate = Double.parseDouble(request.getParameter("ratePerNight"));

                String res = adminService.saveRoom(id, roomNo, type, rate);
                if ("SUCCESS".equals(res)) {
                    message = id == null ? "Room added successfully." : "Room updated successfully.";
                    msgType = "success";
                } else {
                    message = res;
                }

            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("roomId"));
                if (adminService.deleteRoom(id)) {
                    message = "Room deleted successfully.";
                    msgType = "success";
                } else {
                    message = "Failed to delete room. It might be linked to a reservation.";
                }
            }
        } catch (NumberFormatException e) {
            message = "Invalid number format provided.";
        } catch (Exception e) {
            message = "An unexpected error occurred.";
        }

        // Set flash message into session (so it survives redirect)
        session.setAttribute("flashMessage", message);
        session.setAttribute("flashType", msgType);

        response.sendRedirect(request.getContextPath() + "/rooms");
    }
}
