package com.oceanview.servlet;

import com.oceanview.model.Reservation;
import com.oceanview.service.BillingService;
import com.oceanview.service.ReservationService;
import com.oceanview.util.DateUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/billing")
public class BillingServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();
    private final BillingService billingService = new BillingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String resId = request.getParameter("reservationId");
        if (resId != null && !resId.trim().isEmpty()) {
            Reservation res = reservationService.getReservation(resId);
            if (res != null) {
                long nights = DateUtil.calculateNights(res.getCheckInDate(), res.getCheckOutDate());
                request.setAttribute("reservation", res);
                request.setAttribute("nights", nights);
                request.setAttribute("subTotal", billingService.getSubTotal(res.getRatePerNight(), nights));
                request.setAttribute("tax", billingService.getTax(res.getRatePerNight(), nights));
                request.setAttribute("serviceCharge", billingService.getServiceCharge(res.getRatePerNight(), nights));
                request.setAttribute("totalAmount", billingService.calculateTotal(res.getRatePerNight(), nights));
            } else {
                request.setAttribute("error", "Reservation code not found.");
            }
        }

        request.getRequestDispatcher("/generate_bill.jsp").forward(request, response);
    }
}
