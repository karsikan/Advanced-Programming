package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * Task B Requirement: Distributed Application Component.
 * This embedded web service provides a REST-like API to retrieve system reports
 * via HTTP.
 */
public class ReportWebService {

    private static final int PORT = 8080;
    private final ReservationDAO reservationDAO;

    public ReportWebService() {
        this.reservationDAO = new ReservationDAO();
    }

    public void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

            // Endpoint: http://localhost:8080/api/reports/reservations
            server.createContext("/api/reports/reservations", new ReservationHandler());

            server.setExecutor(null); // default executor
            server.start();
            System.out.println(">>> Web Service started at http://localhost:" + PORT + "/api/reports/reservations");
        } catch (IOException e) {
            System.err.println("Web Service failed to start: " + e.getMessage());
        }
    }

    private class ReservationHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Reservation> reservations = reservationDAO.getAllReservations();

                // Construct basic JSON response manually
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < reservations.size(); i++) {
                    Reservation r = reservations.get(i);
                    json.append("{")
                            .append("\"id\":\"").append(r.getReservationId()).append("\",")
                            .append("\"guestId\":").append(r.getGuestId()).append(",")
                            .append("\"roomId\":").append(r.getRoomId()).append(",")
                            .append("\"status\":\"").append(r.getStatus()).append("\",")
                            .append("\"amount\":").append(r.getTotalAmount())
                            .append("}");
                    if (i < reservations.size() - 1)
                        json.append(",");
                }
                json.append("]");

                byte[] response = json.toString().getBytes();
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }
}
