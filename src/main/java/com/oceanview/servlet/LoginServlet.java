package com.oceanview.servlet;

import com.oceanview.model.User;
import com.oceanview.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to the login JSP
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        User authenticatedUser = authService.authenticate(user, pass);

        if (authenticatedUser != null) {
            // Create session and set user attributes
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", authenticatedUser.getUsername());
            session.setAttribute("loggedInUserId", authenticatedUser.getId());
            session.setAttribute("loggedInRole", authenticatedUser.getRole());
            session.setAttribute("isAdmin", "ADMIN".equalsIgnoreCase(authenticatedUser.getRole()));

            // Redirect based on role
            if ("CUSTOMER".equalsIgnoreCase(authenticatedUser.getRole())) {
                response.sendRedirect(request.getContextPath() + "/customer/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else {
            // Forward back to login with error message
            request.setAttribute("errorMessage", "Invalid username or password!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
