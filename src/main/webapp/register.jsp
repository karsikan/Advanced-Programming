<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Ocean View Resort - Register</title>
        <link rel="stylesheet" href="css/style.css">
    </head>

    <body>
        <div class="auth-wrapper">
            <div class="auth-card" style="max-width: 500px;">
                <div class="auth-logo">📝</div>
                <h2 class="auth-title">Guest Registration</h2>
                <p class="auth-subtitle">Create your account to start booking</p>

                <% String error=(String) request.getAttribute("error"); if (error !=null) { %>
                    <div class="alert alert-error">
                        <%= error %>
                    </div>
                    <% } %>

                        <form action="register" method="post">
                            <div class="form-group">
                                <label>Full Name</label>
                                <input type="text" name="name" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label>Address</label>
                                <input type="text" name="address" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label>Contact Number</label>
                                <input type="text" name="contact" class="form-control" required>
                            </div>
                            <hr style="border: 0; border-top: 1px solid #E5E7EB; margin: 1.5rem 0;">
                            <div class="form-group">
                                <label>Choose Username</label>
                                <input type="text" name="username" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label>Choose Password</label>
                                <input type="password" name="password" class="form-control" required>
                            </div>

                            <button type="submit" class="btn btn-primary" style="margin-bottom: 0.5rem;">✅ Register
                                Now</button>
                        </form>

                        <div style="text-align: center;">
                            <a href="login.jsp" class="btn-link" style="color: #6B7280;">⬅ Back to Login</a>
                        </div>
            </div>
        </div>
    </body>

    </html>