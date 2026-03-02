<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Ocean View Resort - Login</title>
        <link rel="stylesheet" href="css/style.css">
    </head>

    <body>
        <div class="auth-wrapper">
            <div class="auth-card">
                <div class="auth-logo">🌊</div>
                <h2 class="auth-title">OCEAN VIEW RESORT</h2>
                <p class="auth-subtitle">Hotel Management System</p>

                <% String error=(String) request.getAttribute("error"); String message=(String)
                    request.getAttribute("message"); if (error !=null) { %>
                    <div class="alert alert-error">
                        <%= error %>
                    </div>
                    <% } else if (message !=null) { %>
                        <div class="alert alert-success">
                            <%= message %>
                        </div>
                        <% } %>

                            <form action="login" method="post">
                                <div class="form-group">
                                    <label>👤 Username</label>
                                    <input type="text" name="username" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label>🔒 Password</label>
                                    <input type="password" name="password" class="form-control" required>
                                </div>
                                <button type="submit" class="btn btn-primary">🔐 LOGIN</button>
                            </form>

                            <div style="text-align: center; margin-top: 1rem;">
                                <a href="register.jsp" class="btn-link">Don't have an account? Register Here</a>
                            </div>

                            <p style="text-align: center; margin-top: 2rem; font-size: 0.8rem; color: #9CA3AF;">
                                © 2026 Ocean View Resort, Galle, Sri Lanka
                            </p>
            </div>
        </div>
    </body>

    </html>