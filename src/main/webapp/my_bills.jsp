<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Ocean View Resort - My Bills</title>
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
                            🧾 My Bills & Invoices
                        </h3>

                        <table>
                            <thead>
                                <tr>
                                    <th>Res ID</th>
                                    <th>Room No.</th>
                                    <th>Stay Dates</th>
                                    <th>Total (LKR)</th>
                                    <th>Payment Status</th>
                                    <th>Action</th>
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
                                            <c:out value="${r.checkInDate} to ${r.checkOutDate}" />
                                        </td>
                                        <td>
                                            <c:out value="${r.totalAmount}" />
                                        </td>
                                        <td>
                                            <span class="badge"
                                                style="${r.status == 'CHECKED_OUT' ? 'background: #10B981;' : 'background: var(--text-muted);'}">
                                                ${r.status == 'CHECKED_OUT' ? 'PAID' : 'PENDING'}
                                            </span>
                                        </td>
                                        <td>
                                            <a href="billing?action=search&reservationId=${r.reservationId}"
                                                class="btn btn-primary"
                                                style="padding: 0.3rem 0.5rem; font-size: 0.9em;">View Invoice</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty reservations}">
                                    <tr>
                                        <td colspan="6"
                                            style="text-align: center; padding: 2rem; color: var(--text-muted);">No
                                            billing history available.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </main>
            </div>
        </body>

        </html>