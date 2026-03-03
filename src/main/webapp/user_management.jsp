<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<% 
    if (session.getAttribute("loggedInUser") == null || !"ADMIN".equals(session.getAttribute("loggedInRole"))) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Management - Ocean View Resort</title>
    <script src="https://cdn.tailwindcss.com/3.4.1"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        brand: { 50: '#f0fdf4', 500: '#10b981', 700: '#047857', 900: '#064e3b' },
                        admin: { 50: '#fef2f2', 500: '#ef4444', 700: '#b91c1c', 900: '#7f1d1d' }
                    }
                }
            }
        }
    </script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .glass {
            background: rgba(255, 255, 255, 0.7);
            backdrop-filter: blur(10px);
            -webkit-backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.3);
        }
        .admin-gradient {
            background: linear-gradient(135deg, #7f1d1d 0%, #b91c1c 100%);
        }
    </style>
</head>
<body class="bg-gray-50 flex h-screen overflow-hidden text-gray-800">

    <jsp:include page="/WEB-INF/components/sidebar.jsp" />

    <div class="flex-1 flex flex-col overflow-hidden">
        <!-- Header -->
        <header class="bg-white/80 backdrop-blur-md shadow-sm border-b border-gray-200 py-4 px-8 flex justify-between items-center z-10">
            <div>
                <h2 class="text-2xl font-black text-gray-900 tracking-tighter flex items-center">
                    <i class="fas fa-users-cog text-admin-600 mr-3"></i> User Control Center
                </h2>
                <p class="text-[10px] font-bold text-gray-400 uppercase tracking-widest mt-0.5">Manage staff access and permissions</p>
            </div>
            <button onclick="openModal('add')"
                class="admin-gradient hover:opacity-90 text-white font-black py-2.5 px-6 rounded-xl shadow-lg shadow-admin-500/20 transition-all text-xs flex items-center uppercase tracking-wider">
                <i class="fas fa-user-plus mr-2"></i> Create New User
            </button>
        </header>

        <main class="flex-1 overflow-auto bg-[radial-gradient(#e5e7eb_1px,transparent_1px)] [background-size:16px_16px] p-8">

            <div class="max-w-5xl mx-auto">
                <!-- User Table Card -->
                <div class="glass rounded-[2.5rem] shadow-2xl shadow-gray-200/50 overflow-hidden border border-white">
                    <table class="min-w-full divide-y divide-gray-100">
                        <thead class="bg-gray-50/50">
                            <tr>
                                <th class="px-8 py-5 text-left text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Profile</th>
                                <th class="px-8 py-5 text-center text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Permission Level</th>
                                <th class="px-8 py-5 text-right text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Management Actions</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-50 bg-white/50">
                            <c:forEach var="u" items="${usersList}">
                                <tr class="hover:bg-brand-50/30 transition-colors group">
                                    <td class="px-8 py-6 whitespace-nowrap">
                                        <div class="flex items-center">
                                            <div class="w-10 h-10 rounded-full admin-gradient flex items-center justify-center text-white font-black text-xs shadow-md shadow-admin-500/20 mr-4">
                                                ${u.username.substring(0, 1).toUpperCase()}
                                            </div>
                                            <div>
                                                <div class="text-sm font-black text-gray-900">${u.username}</div>
                                                <c:if test="${u.username == sessionScope.loggedInUser}">
                                                    <span class="text-[9px] font-black text-brand-600 uppercase bg-brand-50 px-1.5 py-0.5 rounded-md tracking-tighter">Current Session</span>
                                                </c:if>
                                            </div>
                                        </div>
                                    </td>
                                    <td class="px-8 py-6 whitespace-nowrap text-center">
                                        <span class="px-4 py-1.5 inline-flex text-[10px] leading-5 font-black rounded-xl uppercase tracking-widest
                                            ${u.role == 'ADMIN' ? 'bg-admin-50 text-admin-700 border border-admin-100' : 'bg-blue-50 text-blue-700 border border-blue-100'}">
                                            <i class="fas ${u.role == 'ADMIN' ? 'fa-crown' : 'fa-user-tag'} mr-2"></i> ${u.role}
                                        </span>
                                    </td>
                                    <td class="px-8 py-6 whitespace-nowrap text-right text-sm">
                                        <div class="flex justify-end space-x-2">
                                            <button onclick="openModal('edit', '${u.id}', '${u.username}', '${u.role}')"
                                                class="w-10 h-10 rounded-xl bg-blue-50 text-blue-600 flex items-center justify-center shadow-sm hover:bg-blue-600 hover:text-white transition-all transform hover:-translate-y-0.5"
                                                title="Edit Permissions">
                                                <i class="fas fa-pencil-alt text-xs"></i>
                                            </button>

                                            <c:if test="${u.username != sessionScope.loggedInUser}">
                                                <form action="${pageContext.request.contextPath}/users" method="POST" class="inline"
                                                    onsubmit="return confirm('CRITICAL: Are you sure you want to revoke access for ${u.username}?');">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="userId" value="${u.id}">
                                                    <button type="submit"
                                                        class="w-10 h-10 rounded-xl bg-admin-50 text-admin-600 flex items-center justify-center shadow-sm hover:bg-admin-600 hover:text-white transition-all transform hover:-translate-y-0.5"
                                                        title="Revoke Access">
                                                        <i class="fas fa-trash-alt text-xs"></i>
                                                    </button>
                                                </form>
                                            </c:if>
                                            <c:if test="${u.username == sessionScope.loggedInUser}">
                                                <div class="w-10 h-10 rounded-xl bg-gray-50 text-gray-300 flex items-center justify-center cursor-not-allowed" title="Security Locked">
                                                    <i class="fas fa-lock text-xs"></i>
                                                </div>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>

    <div id="userModal" class="fixed inset-0 bg-gray-900/60 backdrop-blur-md hidden flex items-center justify-center z-50 p-4 animate-in fade-in duration-300">
        <div class="bg-white rounded-[2rem] shadow-2xl w-full max-w-md overflow-hidden transform transition-all scale-100 border border-white">
            <div class="px-8 py-6 admin-gradient relative overflow-hidden">
                <i class="fas fa-users-cog absolute -right-4 -bottom-4 text-7xl text-white/10 rotate-12"></i>
                <h3 id="modalTitle" class="text-xl font-black text-white tracking-tighter">Identity Management</h3>
                <p class="text-[10px] font-bold text-white/60 uppercase tracking-widest mt-1">Configure user access credentials</p>
            </div>
            
            <form action="${pageContext.request.contextPath}/users" method="POST" class="p-8 space-y-6">
                <input type="hidden" name="action" id="modalAction" value="add">
                <input type="hidden" name="userId" id="modalUserId" value="">

                <div class="space-y-4">
                    <div>
                        <label class="block text-[10px] font-black text-gray-400 uppercase tracking-widest mb-2 ml-1">Account Username</label>
                        <div class="relative">
                            <i class="fas fa-user absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"></i>
                            <input type="text" name="username" id="modalUsername" required
                                class="w-full pl-12 pr-4 py-3 bg-gray-50 border border-gray-100 rounded-2xl shadow-inner focus:ring-2 focus:ring-admin-500 focus:bg-white transition-all text-sm font-bold">
                        </div>
                    </div>

                    <div>
                        <label class="block text-[10px] font-black text-gray-400 uppercase tracking-widest mb-2 ml-1">Secure Password</label>
                        <div class="relative">
                            <i class="fas fa-key absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"></i>
                            <input type="password" name="password" id="modalPassword"
                                class="w-full pl-12 pr-4 py-3 bg-gray-50 border border-gray-100 rounded-2xl shadow-inner focus:ring-2 focus:ring-admin-500 focus:bg-white transition-all text-sm font-bold"
                                placeholder="••••••••">
                        </div>
                        <p class="text-[9px] text-gray-400 mt-2 ml-1 italic" id="pwHint">Leave empty to keep current password in Edit mode.</p>
                    </div>

                    <div>
                        <label class="block text-[10px] font-black text-gray-400 uppercase tracking-widest mb-2 ml-1">Authorization Tier</label>
                        <div class="grid grid-cols-2 gap-3">
                            <label class="relative block cursor-pointer">
                                <input type="radio" name="role" value="STAFF" id="roleStaff" class="peer sr-only" checked>
                                <div class="px-4 py-3 rounded-2xl border-2 border-gray-100 bg-gray-50 text-center peer-checked:border-admin-500 peer-checked:bg-admin-50 peer-checked:text-admin-700 transition-all">
                                    <i class="fas fa-user-tag text-xs mb-1 block"></i>
                                    <span class="text-[10px] font-black uppercase tracking-wider">Staff</span>
                                </div>
                            </label>
                            <label class="relative block cursor-pointer">
                                <input type="radio" name="role" value="ADMIN" id="roleAdmin" class="peer sr-only">
                                <div class="px-4 py-3 rounded-2xl border-2 border-gray-100 bg-gray-50 text-center peer-checked:border-admin-500 peer-checked:bg-admin-50 peer-checked:text-admin-700 transition-all">
                                    <i class="fas fa-crown text-xs mb-1 block"></i>
                                    <span class="text-[10px] font-black uppercase tracking-wider">Admin</span>
                                </div>
                            </label>
                        </div>
                    </div>
                </div>

                <div class="pt-4 flex space-x-3">
                    <button type="button" onclick="closeModal()"
                        class="flex-1 px-6 py-3 border border-gray-100 rounded-2xl text-[10px] font-black uppercase tracking-widest text-gray-500 hover:bg-gray-50 transition-all">
                        Cancel
                    </button>
                    <button type="submit"
                        class="flex-1 admin-gradient px-6 py-3 rounded-2xl text-[10px] font-black uppercase tracking-widest text-white shadow-lg shadow-admin-500/30 hover:shadow-admin-500/50 transition-all">
                        Commit Changes
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openModal(mode, id = '', username = '', role = 'STAFF') {
            document.getElementById('modalAction').value = mode;
            document.getElementById('modalTitle').innerText = mode === 'add' ? 'Provision Account' : 'Identity Override';
            document.getElementById('modalUserId').value = id;
            document.getElementById('modalUsername').value = username;
            
            if (role === 'ADMIN') {
                document.getElementById('roleAdmin').checked = true;
            } else {
                document.getElementById('roleStaff').checked = true;
            }

            const pwInput = document.getElementById('modalPassword');
            const pwHint = document.getElementById('pwHint');
            if (mode === 'add') {
                pwInput.required = true;
                pwHint.style.display = 'none';
            } else {
                pwInput.required = false;
                pwHint.style.display = 'block';
            }

            document.getElementById('userModal').classList.remove('hidden');
            document.getElementById('userModal').classList.add('flex');
        }
        function closeModal() {
            document.getElementById('userModal').classList.add('hidden');
            document.getElementById('userModal').classList.remove('flex');
        }
    </script>
</body>
</html>
