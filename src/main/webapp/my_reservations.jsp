<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Ocean View Resort - My Reservations</title>
            <link rel="stylesheet" href="css/style.css">
            <style>
                .table-section {
                    background: white;
                    padding: 1.5rem;
                    border-radius: 8px;
                    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
                }

                table {
                    width: 100%;
                    border-collapse: collapse;
                    margin-top: 1rem;
                }

                th,
                td {
                    padding: 0.75rem;
                    text-align: left;
                    border-bottom: 1px solid var(--border-color);
                }

                th {
                    background-color: var(--bg-page);
                    color: var(--text-main);
                    font-weight: 600;
                }

                .badge {
                    padding: 0.2rem 0.5rem;
                    border-radius: 4px;
                    font-size: 0.8rem;
                    color: white;
                    display: inline-block;
                }
            </style>
        </head>

        <body>
            <header class="app-header">
                <h2>🌊 OCEAN VIEW RESORT</h2>
                <div>
                    <span style="margin-right: 1rem;">👤 Welcome, <strong>${sessionScope.username}</strong>
                        (${sessionScope.role})</span>
                    <a href="dashboard" class="btn btn-primary" style="padding: 0.5rem 1rem; width: auto;">🔙 Back to
                        Dashboard</a>
                    <a href="logout" class="btn btn-primary"
                        style="padding: 0.5rem 1rem; width: auto; background-color: var(--danger);">🚪 Logout</a>
                </div>
            </header>

            <div class="app-container">
                <main class="app-content">
                    <div class="table-section">
                        <h3
                            style="color: var(--primary-color); border-bottom: 2px solid var(--border-color); padding-bottom: 0.5rem; margin-bottom: 1rem;">
                            📋 My Reservations
                        </h3>

                        <table>
                            <thead>
                                <tr>
                                    <th>Res ID</th>
                                    <th>Room No.</th>
                                    <th>Type</th>
                                    <th>Check-In</th>
                                    <th>Check-Out</th>
                                    <th>Total (LKR)</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="r" items="${reservations}">
                                    <tr>
                                        <td><strong>
                                                <c:out value="${r.reservationId}" />
                                            </strong></td>
                                        <td>
                                            <c:out value="${r.roomNumber}" />
                                        </td>
                                        <td>
                                            <c:out value="${r.roomType}" />
                                        </td>
                                        <td>
                                            <c:out value="${r.checkInDate}" />
                                        </td>
                                        <td>
                                            <c:out value="${r.checkOutDate}" />
                                        </td>
                                        <td>
                                            <c:out value="${r.totalAmount}" />
                                        </td>
                                        <td>
                                            <span class="badge" style="
                                        ${r.status == 'ACTIVE' ? 'background: var(--primary-light);' : 
                                          r.status == 'CANCELLED' ? 'background: var(--danger);' : 
                                          'background: #10B981;'}">
                                                <c:out value="${r.status}" />
                                            </span>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty reservations}">
                                    <tr>
                                        <td colspan="7"
                                            style="text-align: center; padding: 2rem; color: var(--text-muted);">You
                                            have no reservations yet.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                        <div style="margin-top: 2rem;">
                            <a href="reservations?action=new" class="btn btn-primary">Book Another Room</a>
                        </div>
                    </div>
                </main>
            </div>
        </body>

        </html>