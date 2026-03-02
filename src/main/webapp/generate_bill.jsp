<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Ocean View Resort - Generate Bill</title>
            <link rel="stylesheet" href="css/style.css">
            <style>
                .search-container {
                    background: white;
                    padding: 1.5rem;
                    border-radius: 8px;
                    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
                    margin-bottom: 2rem;
                    display: flex;
                    gap: 1rem;
                    align-items: center;
                }

                .search-container input {
                    flex: 1;
                    padding: 0.75rem;
                    font-size: 1rem;
                }

                .search-container button {
                    padding: 0.75rem 2rem;
                    font-size: 1rem;
                }

                .bill-container {
                    background: white;
                    padding: 2rem;
                    border-radius: 8px;
                    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
                }

                pre.bill-text {
                    background-color: #F3F4F6;
                    padding: 1rem;
                    border-radius: 4px;
                    font-family: monospace;
                    white-space: pre-wrap;
                    color: #111827;
                    font-size: 1.1rem;
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
                    <h3
                        style="color: var(--primary-color); border-bottom: 2px solid var(--border-color); padding-bottom: 0.5rem; margin-bottom: 1.5rem;">
                        🧾 Generate Bill & Checkout
                    </h3>

                    <c:if test="${not empty param.success}">
                        <div
                            style="background-color: #D1FAE5; color: #065F46; padding: 1rem; border-radius: 4px; margin-bottom: 1rem;">
                            ✅ ${param.success}
                        </div>
                    </c:if>
                    <c:if test="${not empty param.error}">
                        <div
                            style="background-color: #FEE2E2; color: #991B1B; padding: 1rem; border-radius: 4px; margin-bottom: 1rem;">
                            ❌ ${param.error}
                        </div>
                    </c:if>
                    <c:if test="${not empty requestScope.error}">
                        <div
                            style="background-color: #FEE2E2; color: #991B1B; padding: 1rem; border-radius: 4px; margin-bottom: 1rem;">
                            ❌ ${requestScope.error}
                        </div>
                    </c:if>

                    <form action="billing" method="get" class="search-container">
                        <input type="hidden" name="action" value="search">
                        <input type="text" name="reservationId"
                            placeholder="Enter Reservation ID (e.g., OVR-20231015-1234)" value="${param.reservationId}"
                            required />
                        <button type="submit" class="btn btn-primary">Find Bill</button>
                    </form>

                    <c:if test="${not empty billText}">
                        <div class="bill-container">
                            <h4 style="margin-bottom: 1rem;">Invoice Preview:</h4>
                            <pre class="bill-text">${billText}</pre>

                            <c:if test="${reservation.status != 'CHECKED_OUT' && reservation.status != 'CANCELLED'}">
                                <form action="billing" method="post" style="margin-top: 2rem;">
                                    <input type="hidden" name="action" value="checkout">
                                    <input type="hidden" name="reservationId" value="${reservation.reservationId}">
                                    <button type="submit" class="btn btn-primary"
                                        style="background-color: #10B981; font-size: 1.2rem; padding: 1rem 2rem; width: 100%;">
                                        Confirm Payment & Check-out Room
                                    </button>
                                </form>
                            </c:if>
                            <c:if test="${reservation.status == 'CHECKED_OUT'}">
                                <div
                                    style="margin-top: 1rem; padding: 1rem; background: #E5E7EB; color: #374151; font-weight: bold; text-align: center; border-radius: 4px;">
                                    This reservation is already Checked Out and Paid.
                                </div>
                            </c:if>
                        </div>
                    </c:if>

                </main>
            </div>
        </body>

        </html>