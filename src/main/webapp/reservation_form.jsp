<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Ocean View Resort - New Reservation</title>
            <link rel="stylesheet" href="css/style.css">
            <style>
                .form-container {
                    background: white;
                    padding: 2rem;
                    border-radius: 8px;
                    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
                    max-width: 600px;
                    margin: 0 auto;
                }

                .form-row {
                    display: flex;
                    gap: 1rem;
                }

                .form-row .form-group {
                    flex: 1;
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

                    <div class="form-container">
                        <h3
                            style="color: var(--primary-color); border-bottom: 2px solid var(--border-color); padding-bottom: 0.5rem; margin-bottom: 1.5rem;">
                            📝 Create New Reservation
                        </h3>

                        <form action="reservations" method="post">
                            <input type="hidden" name="action" value="add">

                            <div class="form-group">
                                <label>Guest Name:</label>
                                <input type="text" name="guestName" class="form-control" placeholder="Enter full name"
                                    required <c:if test="${sessionScope.role == 'CUSTOMER'}">
                                value="${sessionScope.username}" readonly
                                </c:if>
                                />
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label>Contact Number (10 digits):</label>
                                    <input type="text" name="contact" class="form-control" placeholder="07XXXXXXXX"
                                        required />
                                </div>
                                <div class="form-group">
                                    <label>Address:</label>
                                    <input type="text" name="address" class="form-control" placeholder="City, Country"
                                        required />
                                </div>
                            </div>

                            <div class="form-group">
                                <label>Select Available Room:</label>
                                <select name="roomId" class="form-control" required>
                                    <option value="">-- Choose a Room --</option>
                                    <c:forEach var="room" items="${rooms}">
                                        <option value="${room.id}">
                                            Room ${room.roomNumber} (${room.roomType}) - LKR ${room.ratePerNight}/night
                                        </option>
                                    </c:forEach>
                                </select>
                                <c:if test="${empty rooms}">
                                    <small style="color: red;">No available rooms found!</small>
                                </c:if>
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label>Check-in Date:</label>
                                    <input type="date" name="checkInDate" class="form-control" required />
                                </div>
                                <div class="form-group">
                                    <label>Check-out Date:</label>
                                    <input type="date" name="checkOutDate" class="form-control" required />
                                </div>
                            </div>

                            <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 1rem;" ${empty
                                rooms ? 'disabled' : '' }>
                                Confirm Booking
                            </button>
                        </form>
                    </div>
                </main>
            </div>
        </body>

        </html>