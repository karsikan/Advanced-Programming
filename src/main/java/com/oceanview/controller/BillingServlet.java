package com.oceanview.controller;

import com.oceanview.model.Reservation;
import com.oceanview.service.BillingService;
import com.oceanview.service.ReservationService;
import com.oceanview.util.DateUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/billing")
public class BillingServlet extends HttpServlet {

    private ReservationService reservationService;
    private BillingService billingService;

    @Override
    public void init() throws ServletException {
        reservationService = new ReservationService();
        billingService = new BillingService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("search".equals(action)) {
            String reservationId = request.getParameter("reservationId");
            Reservation reservation = reservationService.getReservation(reservationId);

            if (reservation != null) {
                long nights = DateUtil.calculateNights(reservation.getCheckInDate(), reservation.getCheckOutDate());
                String billText = billingService.generateBillText(reservation, nights);

                request.setAttribute("reservation", reservation);
                request.setAttribute("billText", billText);
            } else {
                request.setAttribute("error", "Reservation ID not found.");
            }
        } else if ("my-bills".equals(action)) {
            // Forward to Customer bills view
            Object guestIdObj = request.getSession().getAttribute("guestId");
            if (guestIdObj != null) {
                int guestId = (Integer) guestIdObj;
                request.setAttribute("reservations", reservationService.getReservationsByGuest(guestId));
            }
            request.getRequestDispatcher("my_bills.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("generate_bill.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("checkout".equals(action)) {
            String reservationId = request.getParameter("reservationId");
            boolean success = reservationService.checkOutReservation(reservationId);

            if (success) {
                response.sendRedirect("billing?action=search&reservationId=" + reservationId
                        + "&success=Checkout complete! Payment received.");
            } else {
                response.sendRedirect("billing?error=Failed to process checkout.");
            }
        }
    }
}
