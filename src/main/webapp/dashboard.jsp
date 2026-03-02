<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Ocean View Resort - Dashboard</title>
            <link rel="stylesheet" href="css/style.css">
        </head>

        <body>
            <header class="app-header">
                <h2>🌊 OCEAN VIEW RESORT</h2>
                <div>
                    <span style="margin-right: 1rem;">👤 Welcome, <strong>${sessionScope.username}</strong>
                        (${sessionScope.role})</span>
                    <a href="logout" class="btn btn-primary"
                        style="padding: 0.5rem 1rem; width: auto; background-color: var(--danger);">🚪 Logout</a>
                </div>
            </header>

            <div class="app-container">
                <aside class="app-sidebar">
                    <h4
                        style="color: rgba(255,255,255,0.6); margin-bottom: 1rem; font-size: 0.8rem; text-transform: uppercase; letter-spacing: 1px;">
                        Navigation</h4>

                    <a href="dashboard" class="active">📊 Dashboard Home</a>

                    <c:choose>
                        <c:when test="${sessionScope.role == 'ADMIN'}">
                            <a href="user_management.jsp">👥 User Management</a>
                            <a href="room_management.jsp">🛏️ Room Management</a>
                            <a href="all_reservations.jsp">📋 All Reservations</a>
                            <a href="revenue_reports.jsp">📈 Revenue Reports</a>
                        </c:when>

                        <c:when test="${sessionScope.role == 'STAFF'}">
                            <a href="reservation_form.jsp">📝 New Reservation</a>
                            <a href="search_guests.jsp">🔍 Search Guests</a>
                            <a href="generate_bill.jsp">🧾 Generate Bill</a>
                        </c:when>

                        <c:when test="${sessionScope.role == 'CUSTOMER'}">
                            <a href="book_room.jsp">🏨 Book a Room</a>
                            <a href="my_reservations.jsp">📋 My Reservations</a>
                            <a href="my_bills.jsp">🧾 My Bills</a>
                        </c:when>
                    </c:choose>
                </aside>

                <main class="app-content">
                    <c:if test="${sessionScope.role == 'ADMIN' || sessionScope.role == 'STAFF'}">
                        <div
                            style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 1.5rem; margin-bottom: 2rem;">
                            <div class="card" style="border-left: 4px solid var(--primary-light);">
                                <h4 style="color: var(--text-muted); margin-bottom: 0.5rem;">Total Revenue</h4>
                                <h2 style="color: var(--primary-color);">LKR ${stats != null ? stats['Total Revenue'] :
                                    '0.00'}</h2>
                            </div>
                            <div class="card" style="border-left: 4px solid var(--accent-color);">
                                <h4 style="color: var(--text-muted); margin-bottom: 0.5rem;">Occupied Rooms</h4>
                                <h2 style="color: var(--primary-color);">${stats != null ? stats['Occupied Rooms'] :
                                    '0'}</h2>
                            </div>
                        </div>
                    </c:if>

                    <div class="card">
                        <h3
                            style="margin-bottom: 1.5rem; color: var(--primary-color); border-bottom: 2px solid var(--border-color); padding-bottom: 0.5rem;">
                            <c:choose>
                                <c:when test="${sessionScope.role == 'CUSTOMER'}">My Reservations</c:when>
                                <c:otherwise>Recent Reservations</c:otherwise>
                            </c:choose>
                        </h3>

                        <c:if test="${empty reservations}">
                            <p style="color: var(--text-muted); text-align: center; padding: 2rem;">No reservations
                                found.</p>
                        </c:if>

                        <c:if test="${not empty reservations}">
                            <table>
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Guest Name</th>
                                        <th>Room ID</th>
                                        <th>Check-In</th>
                                        <th>Check-Out</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="res" items="${reservations}">
                                        <tr>
                                            <td>#${res.reservationId}</td>
                                            <td>${res.guestName}</td>
                                            <td>Room ${res.roomId}</td>
                                            <td>${res.checkInDate}</td>
                                            <td>${res.checkOutDate}</td>
                                            <td>
                                                <span
                                                    style="background-color: #ECFDF5; color: #059669; padding: 0.25rem 0.75rem; border-radius: 999px; font-size: 0.85rem; font-weight: 500;">
                                                    ${res.status}
                                                </span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                    </div>
                </main>
            </div>
        </body>

        </html>