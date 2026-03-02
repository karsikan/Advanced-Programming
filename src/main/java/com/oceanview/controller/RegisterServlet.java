package com.oceanview.controller;

import com.oceanview.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        authService = new AuthService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String contact = request.getParameter("contact");
        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        String result = authService.registerCustomer(name, address, contact, user, pass);

        if ("SUCCESS".equals(result)) {
            request.setAttribute("message", "✅ Registration successful! Please login.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "❌ " + result);
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
