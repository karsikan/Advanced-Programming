package com.oceanview.service;

import com.oceanview.model.Reservation;

/**
 * Billing Service – calculates total bill with taxes and generates bill text.
 */
public class BillingService {

    private static final double TAX_RATE = 0.10; // 10% government tax
    private static final double SERVICE_CHARGE = 0.05; // 5% service charge

    public double calculateTotal(double ratePerNight, long nights) {
        double subTotal = ratePerNight * nights;
        double tax = subTotal * TAX_RATE;
        double service = subTotal * SERVICE_CHARGE;
        return subTotal + tax + service;
    }

    public double getSubTotal(double ratePerNight, long nights) {
        return ratePerNight * nights;
    }

    public double getTax(double ratePerNight, long nights) {
        return getSubTotal(ratePerNight, nights) * TAX_RATE;
    }

    public double getServiceCharge(double ratePerNight, long nights) {
        return getSubTotal(ratePerNight, nights) * SERVICE_CHARGE;
    }

    /**
     * Generates a formatted bill as a string for display/print.
     */
    public String generateBillText(Reservation r, long nights) {
        double subTotal = r.getRatePerNight() * nights;
        double tax = subTotal * TAX_RATE;
        double service = subTotal * SERVICE_CHARGE;
        double total = subTotal + tax + service;

        return String.format("""
                ╔══════════════════════════════════════════╗
                ║       OCEAN VIEW RESORT - GALLE          ║
                ║         HOTEL INVOICE / BILL             ║
                ╚══════════════════════════════════════════╝

                Reservation ID  : %s
                Guest Name      : %s
                Address         : %s
                Contact         : %s

                Room Number     : %s
                Room Type       : %s
                Check-In Date   : %s
                Check-Out Date  : %s
                Total Nights    : %d

                ──────────────────────────────────────────
                Room Rate/Night : LKR %,.2f
                Sub Total       : LKR %,.2f
                Tax (10%%)       : LKR %,.2f
                Service Charge  : LKR %,.2f
                ──────────────────────────────────────────
                TOTAL AMOUNT    : LKR %,.2f
                ══════════════════════════════════════════

                  Thank you for staying at Ocean View Resort!
                      Enjoy your stay — Galle, Sri Lanka

                """,
                r.getReservationId(),
                r.getGuestName(),
                r.getGuestAddress(),
                r.getGuestContact(),
                r.getRoomNumber(),
                r.getRoomType(),
                r.getCheckInDate(),
                r.getCheckOutDate(),
                nights,
                r.getRatePerNight(),
                subTotal, tax, service, total);
    }
}
