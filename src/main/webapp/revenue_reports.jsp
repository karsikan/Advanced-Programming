<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>Ocean View Resort - Revenue Reports</title>
                <link rel="stylesheet" href="css/style.css">
                <style>
                    .stats-grid {
                        display: grid;
                        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                        gap: 1.5rem;
                        margin-bottom: 2rem;
                    }

                    .stat-card {
                        background: white;
                        padding: 1.5rem;
                        border-radius: 8px;
                        box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
                        text-align: center;
                        border-top: 4px solid var(--primary-color);
                    }

                    .stat-value {
                        font-size: 2rem;
                        font-weight: 700;
                        color: var(--primary-dark);
                        margin: 0.5rem 0;
                    }

                    .stat-label {
                        color: var(--text-muted);
                        font-size: 0.9rem;
                        text-transform: uppercase;
                        letter-spacing: 0.05em;
                    }

                    .report-section {
                        background: white;
                        padding: 1.5rem;
                        border-radius: 8px;
                        box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
                        margin-bottom: 2rem;
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
                    }
                </style>
            </head>

            <body>
                <header class="app-header">
                    <h2>🌊 OCEAN VIEW RESORT</h2>
                    <div>
                        <span style="margin-right: 1rem;">👤 Welcome, <strong>${sessionScope.username}</strong>
                            (${sessionScope.role})</span>
                        <a href="dashboard" class="btn btn-primary" style="padding: 0.5rem 1rem; width: auto;">🔙 Back
                            to Dashboard</a>
                        <a href="logout" class="btn btn-primary"
                            style="padding: 0.5rem 1rem; width: auto; background-color: var(--danger);">🚪 Logout</a>
                    </div>
                </header>

                <div class="app-container">
                    <main class="app-content">
                        <h3
                            style="color: var(--primary-color); border-bottom: 2px solid var(--border-color); padding-bottom: 0.5rem; margin-bottom: 1.5rem;">
                            📈 Daily Revenue & Performance Reports
                        </h3>

                        <div class="stats-grid">
                            <div class="stat-card">
                                <div class="stat-label">Total Revenue</div>
                                <div class="stat-value">LKR
                                    <fmt:formatNumber value="${totalRevenue}" type="number" maxFractionDigits="2" />
                                </div>
                            </div>
                            <div class="stat-card" style="border-top-color: #10B981;">
                                <div class="stat-label">Occupancy Rate</div>
                                <div class="stat-value">
                                    <fmt:formatNumber value="${occupancyRate}" type="number" maxFractionDigits="1" />%
                                </div>
                            </div>
                            <div class="stat-card" style="border-top-color: #F59E0B;">
                                <div class="stat-label">Today's Check-ins</div>
                                <div class="stat-value">${todayCheckIns}</div>
                            </div>
                            <div class="stat-card" style="border-top-color: #3B82F6;">
                                <div class="stat-label">Total Reservations</div>
                                <div class="stat-value">${totalReservations}</div>
                            </div>
                        </div>

                        <div style="display: flex; gap: 2rem;">
                            <!-- Revenue By Room Type -->
                            <div class="report-section" style="flex: 1;">
                                <h4 style="color: var(--primary-color);">Revenue by Room Type</h4>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Room Type</th>
                                            <th style="text-align: right;">Revenue (LKR)</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="entry" items="${revenueByRoomType}">
                                            <tr>
                                                <td><strong>
                                                        <c:out value="${entry.key}" />
                                                    </strong></td>
                                                <td style="text-align: right;">
                                                    <fmt:formatNumber value="${entry.value}" type="number"
                                                        maxFractionDigits="2" />
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                            <!-- Monthly Reservations overview -->
                            <div class="report-section" style="flex: 1;">
                                <h4 style="color: var(--primary-color);">Monthly Bookings (Current Year)</h4>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Month</th>
                                            <th style="text-align: right;">Total Bookings</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="entry" items="${monthlyReservations}">
                                            <c:if test="${entry.value > 0}">
                                                <tr>
                                                    <td><strong>
                                                            <c:out value="${entry.key}" />
                                                        </strong></td>
                                                    <td style="text-align: right;">
                                                        <c:out value="${entry.value}" />
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                    </main>
                </div>
            </body>

            </html>