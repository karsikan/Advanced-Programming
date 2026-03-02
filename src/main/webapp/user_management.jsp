<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Ocean View Resort - User Management</title>
            <link rel="stylesheet" href="css/style.css">
            <style>
                .management-container {
                    display: flex;
                    gap: 2rem;
                    align-items: flex-start;
                }

                .form-section {
                    flex: 1;
                    background: white;
                    padding: 1.5rem;
                    border-radius: 8px;
                    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
                }

                .table-section {
                    flex: 2;
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

                .btn-small {
                    padding: 0.25rem 0.5rem;
                    font-size: 0.875rem;
                    margin-right: 0.25rem;
                }

                .action-links a {
                    text-decoration: none;
                    color: white;
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
                <!-- Render Sidebar if needed, or directly content -->
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

                    <div class="management-container">

                        <!-- Form Section -->
                        <div class="form-section">
                            <h3
                                style="color: var(--primary-color); border-bottom: 2px solid var(--border-color); padding-bottom: 0.5rem; margin-bottom: 1rem;">
                                <c:choose>
                                    <c:when test="${user != null}">Edit User</c:when>
                                    <c:otherwise>Add New User</c:otherwise>
                                </c:choose>
                            </h3>

                            <form action="users" method="post">
                                <c:if test="${user != null}">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="id" value="${user.id}">
                                </c:if>
                                <c:if test="${user == null}">
                                    <input type="hidden" name="action" value="add">
                                </c:if>

                                <div class="form-group">
                                    <label>Username:</label>
                                    <input type="text" name="username" class="form-control"
                                        value="<c:out value='${user.username}' />" required ${user !=null ? 'readonly'
                                        : '' } />
                                    <c:if test="${user != null}">
                                        <small style="color: var(--text-muted);">Username cannot be changed.</small>
                                    </c:if>
                                </div>

                                <div class="form-group">
                                    <label>Password:</label>
                                    <input type="text" name="password" class="form-control"
                                        value="<c:out value='${user.password}' />" required />
                                </div>

                                <div class="form-group">
                                    <label>Role:</label>
                                    <select name="role" class="form-control" required>
                                        <option value="STAFF" ${user !=null && user.role=='STAFF' ? 'selected' : '' }>
                                            Staff</option>
                                        <option value="ADMIN" ${user !=null && user.role=='ADMIN' ? 'selected' : '' }>
                                            Admin</option>
                                    </select>
                                </div>

                                <button type="submit" class="btn btn-primary" style="width: 100%;">
                                    ${user != null ? 'Update User' : 'Add User'}
                                </button>

                                <c:if test="${user != null}">
                                    <a href="users" class="btn"
                                        style="display: block; text-align: center; margin-top: 0.5rem; background: var(--bg-page); color: var(--text-main);">Cancel
                                        Edit</a>
                                </c:if>
                            </form>
                        </div>

                        <!-- Table Section -->
                        <div class="table-section">
                            <h3
                                style="color: var(--primary-color); border-bottom: 2px solid var(--border-color); padding-bottom: 0.5rem; margin-bottom: 1rem;">
                                👥 User List
                            </h3>

                            <table>
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Username</th>
                                        <th>Role</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="u" items="${listUser}">
                                        <tr>
                                            <td>
                                                <c:out value="${u.id}" />
                                            </td>
                                            <td>
                                                <c:out value="${u.username}" />
                                            </td>
                                            <td>
                                                <span class="badge"
                                                    style="${u.role == 'ADMIN' ? 'background: var(--danger);' : 'background: var(--primary-light);'} color: white; padding: 0.2rem 0.5rem; border-radius: 4px; font-size: 0.8rem;">
                                                    <c:out value="${u.role}" />
                                                </span>
                                            </td>
                                            <td class="action-links">
                                                <a href="users?action=edit&username=<c:out value='${u.username}' />"
                                                    class="btn btn-small btn-primary"
                                                    style="background: var(--accent-color); color: #000;">✎ Edit</a>
                                                <c:if test="${u.username != sessionScope.username}">
                                                    <a href="users?action=delete&id=<c:out value='${u.id}' />"
                                                        class="btn btn-small btn-primary"
                                                        style="background: var(--danger);"
                                                        onclick="return confirm('Are you sure you want to delete this user?');">🗑
                                                        Delete</a>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                    </div>
                </main>
            </div>
        </body>

        </html>