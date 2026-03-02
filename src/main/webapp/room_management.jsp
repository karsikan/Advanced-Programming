<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Ocean View Resort - Room Management</title>
            <link rel="stylesheet" href="css/style.css">
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
                    <div class="card">
                        <h3
                            style="margin-bottom: 1.5rem; color: var(--primary-color); border-bottom: 2px solid var(--border-color); padding-bottom: 0.5rem;">
                            🛏️ Room Management
                        </h3>
                        <div style="text-align: center; padding: 3rem;">
                            <span style="font-size: 4rem;">🚧</span>
                            <h2 style="color: var(--text-muted); margin-top: 1rem;">This feature is under development.
                            </h2>
                            <p style="color: var(--text-muted); margin-top: 0.5rem;">Please check back later as we
                                complete the web migration!</p>
                        </div>
                    </div>
                </main>
            </div>
        </body>

        </html>