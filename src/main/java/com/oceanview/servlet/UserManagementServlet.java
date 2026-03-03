package com.oceanview.servlet;

import com.oceanview.model.User;
import com.oceanview.service.AdminService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserManagementServlet extends HttpServlet {

    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Only ADMIN can access User Management
        if (session == null || !"ADMIN".equals(session.getAttribute("loggedInRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        List<User> users = adminService.getAllUsers();
        request.setAttribute("usersList", users);
        request.getRequestDispatcher("/user_management.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("loggedInRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        String action = request.getParameter("action");
        String message = "";
        String msgType = "error";

        try {
            if ("add".equals(action) || "edit".equals(action)) {
                String idStr = request.getParameter("userId");
                Integer id = (idStr != null && !idStr.isEmpty()) ? Integer.parseInt(idStr) : null;
                String username = request.getParameter("username");
                String password = request.getParameter("password"); // Could be blank on edit
                String role = request.getParameter("role");

                String res = adminService.saveUser(id, username, password, role);
                if ("SUCCESS".equals(res)) {
                    message = id == null ? "User created successfully." : "User updated successfully.";
                    msgType = "success";
                } else {
                    message = res;
                }

            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("userId"));
                String currentUsername = (String) session.getAttribute("loggedInUser");

                String res = adminService.deleteUser(id, currentUsername);
                if ("SUCCESS".equals(res)) {
                    message = "User deleted successfully.";
                    msgType = "success";
                } else {
                    message = res;
                }
            }
        } catch (NumberFormatException e) {
            message = "Invalid number format provided.";
        } catch (Exception e) {
            message = "An unexpected error occurred.";
        }

        session.setAttribute("flashMessage", message);
        session.setAttribute("flashType", msgType);

        response.sendRedirect(request.getContextPath() + "/users");
    }
}