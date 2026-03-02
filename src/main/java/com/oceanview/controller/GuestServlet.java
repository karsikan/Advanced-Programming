package com.oceanview.controller;

import com.oceanview.dao.GuestDAO;
import com.oceanview.model.Guest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/guests")
public class GuestServlet extends HttpServlet {

    private GuestDAO guestDAO;

    @Override
    public void init() throws ServletException {
        guestDAO = new GuestDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String query = request.getParameter("query");

        List<Guest> guests;
        if (query != null && !query.trim().isEmpty()) {
            guests = guestDAO.searchGuests(query);
            request.setAttribute("query", query);
        } else {
            guests = guestDAO.getAllGuests();
        }

        request.setAttribute("guests", guests);
        request.getRequestDispatcher("search_guests.jsp").forward(request, response);
    }
}
