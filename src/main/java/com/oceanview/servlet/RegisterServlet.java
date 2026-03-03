package com.oceanview.servlet;

import com.oceanview.dao.UserDAO;
import com.oceanview.model.User;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles customer self-registration and basic validation.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirmPassword");

        // basic validation
        if (name == null || name.isBlank() ||
            username == null || username.isBlank() ||
            email == null || email.isBlank() ||
            phone == null || phone.isBlank() ||
            password == null || password.isBlank() ||
            confirm == null || confirm.isBlank()) {
            request.setAttribute("errorMessage", "All fields are required.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirm)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // unique username check
        if (userDAO.findByUsername(username.trim()) != null) {
            request.setAttribute("errorMessage", "Username already exists.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        String hashed = BCrypt.hashpw(password.trim(), BCrypt.gensalt());
        User newUser = new User(0, name.trim(), username.trim(), email.trim(), phone.trim(), hashed, "CUSTOMER", "ACTIVE", null, null);
        boolean inserted = userDAO.insertUser(newUser);
        if (inserted) {
            request.setAttribute("successMessage", "Registration successful! You may now login.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Unable to register. Please try again later.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}