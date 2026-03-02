package com.oceanview.controller;

import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Room;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/rooms")
public class RoomServlet extends HttpServlet {

    private RoomDAO roomDAO;

    @Override
    public void init() throws ServletException {
        roomDAO = new RoomDAO();
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
                case "delete":
                    deleteRoom(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                default:
                    listRooms(request, response);
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
                addRoom(request, response);
            } else if ("update".equals(action)) {
                updateRoom(request, response);
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void listRooms(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Room> listRoom = roomDAO.getAllRooms();
        request.setAttribute("listRoom", listRoom);
        request.getRequestDispatcher("room_management.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Room existingRoom = roomDAO.findById(id);
        request.setAttribute("room", existingRoom);
        request.getRequestDispatcher("room_management.jsp").forward(request, response);
    }

    private void addRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roomNumber = request.getParameter("roomNumber");
        String roomType = request.getParameter("roomType");
        double ratePerNight = Double.parseDouble(request.getParameter("ratePerNight"));

        Room newRoom = new Room(0, roomNumber, roomType, ratePerNight, "AVAILABLE");
        boolean success = roomDAO.insertRoom(newRoom);

        if (success) {
            response.sendRedirect("rooms?success=Room added successfully");
        } else {
            response.sendRedirect("rooms?error=Could not add room. Number might exist.");
        }
    }

    private void updateRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String roomNumber = request.getParameter("roomNumber");
        String roomType = request.getParameter("roomType");
        double ratePerNight = Double.parseDouble(request.getParameter("ratePerNight"));

        Room room = new Room(id, roomNumber, roomType, ratePerNight, null); // Status isn't modified by this DAO method
        boolean success = roomDAO.updateRoom(room);

        if (success) {
            response.sendRedirect("rooms?success=Room updated successfully");
        } else {
            response.sendRedirect("rooms?error=Could not update room.");
        }
    }

    private void deleteRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        roomDAO.deleteRoom(id);
        response.sendRedirect("rooms?success=Room deleted successfully");
    }
}
