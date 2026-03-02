<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Ocean View Resort - Search Guests</title>
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

                table {
                    width: 100%;
                    border-collapse: collapse;
                    background: white;
                    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
                    border-radius: 8px;
                    overflow: hidden;
                }

                th,
                td {
                    padding: 1rem;
                    text-align: left;
                    border-bottom: 1px solid var(--border-color);
                }

                th {
                    background-color: var(--bg-page);
                    color: var(--text-main);
                    font-weight: 600;
                }

                tbody tr:hover {
                    background-color: #F9FAFB;
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
                        🔍 Search Guests
                    </h3>

                    <form action="guests" method="get" class="search-container">
                        <input type="text" name="query" placeholder="Search by name or contact number..."
                            value="<c:out value='${query}' />" />
                        <button type="submit" class="btn btn-primary">Search</button>
                        <a href="guests" class="btn"
                            style="background: var(--bg-page); color: var(--text-main);">Clear</a>
                    </form>

                    <table>
                        <thead>
                            <tr>
                                <th>Guest ID</th>
                                <th>Name</th>
                                <th>Contact Number</th>
                                <th>Address</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty guests}">
                                    <tr>
                                        <td colspan="4"
                                            style="text-align: center; color: var(--text-muted); padding: 2rem;">No
                                            guests found.</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="g" items="${guests}">
                                        <tr>
                                            <td><strong>
                                                    <c:out value="${g.id}" />
                                                </strong></td>
                                            <td>
                                                <c:out value="${g.name}" />
                                            </td>
                                            <td>
                                                <c:out value="${g.contactNumber}" />
                                            </td>
                                            <td>
                                                <c:out value="${g.address}" />
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </main>
            </div>
        </body>

        </html>