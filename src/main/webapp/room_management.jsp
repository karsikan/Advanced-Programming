<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<% 
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Room Management - Ocean View Resort</title>
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
        .brand-gradient {
            background: linear-gradient(135deg, #047857 0%, #10b981 100%);
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
                    <i class="fas fa-layer-group text-brand-600 mr-3"></i> Inventory Explorer
                </h2>
                <p class="text-[10px] font-bold text-gray-400 uppercase tracking-widest mt-0.5">Real-time room status & configuration</p>
            </div>
            <c:if test="${sessionScope.isAdmin}">
                <button onclick="openModal('add')"
                    class="brand-gradient hover:opacity-90 text-white font-black py-2.5 px-6 rounded-xl shadow-lg shadow-brand-500/20 transition-all text-xs flex items-center uppercase tracking-wider">
                    <i class="fas fa-plus-circle mr-2"></i> Add Inventory
                </button>
            </c:if>
        </header>

        <main class="flex-1 overflow-auto bg-[radial-gradient(#e5e7eb_1px,transparent_1px)] [background-size:16px_16px] p-8">

            <div class="max-w-6xl mx-auto">
                <!-- Stats Row -->
                <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
                    <div class="bg-white p-6 rounded-3xl shadow-sm border border-gray-100">
                        <div class="flex justify-between items-start mb-2">
                            <div class="w-10 h-10 bg-green-50 rounded-xl flex items-center justify-center text-green-600">
                                <i class="fas fa-check-circle text-lg"></i>
                            </div>
                            <span class="text-[10px] font-black text-green-600 bg-green-50 px-2 py-0.5 rounded-md uppercase tracking-tighter">Ready</span>
                        </div>
                        <div class="text-[10px] font-black text-gray-400 uppercase tracking-widest">Available</div>
                        <div class="text-2xl font-black text-gray-900">${availCount} Units</div>
                    </div>
                    <div class="bg-white p-6 rounded-3xl shadow-sm border border-gray-100">
                        <div class="flex justify-between items-start mb-2">
                            <div class="w-10 h-10 bg-red-50 rounded-xl flex items-center justify-center text-red-600">
                                <i class="fas fa-user-lock text-lg"></i>
                            </div>
                            <span class="text-[10px] font-black text-red-600 bg-red-50 px-2 py-0.5 rounded-md uppercase tracking-tighter">Live</span>
                        </div>
                        <div class="text-[10px] font-black text-gray-400 uppercase tracking-widest">Occupied</div>
                        <div class="text-2xl font-black text-gray-900">${occCount} Units</div>
                    </div>
                    <div class="bg-white p-6 rounded-3xl shadow-sm border border-gray-100">
                        <div class="flex justify-between items-start mb-2">
                            <div class="w-10 h-10 bg-brand-50 rounded-xl flex items-center justify-center text-brand-600">
                                <i class="fas fa-hotel text-lg"></i>
                            </div>
                        </div>
                        <div class="text-[10px] font-black text-gray-400 uppercase tracking-widest">Total Rooms</div>
                        <div class="text-2xl font-black text-gray-900">${totalCount} Units</div>
                    </div>
                    <div class="bg-white p-6 rounded-3xl shadow-sm border border-gray-100">
                        <div class="flex justify-between items-start mb-2">
                            <div class="w-10 h-10 bg-blue-50 rounded-xl flex items-center justify-center text-blue-600">
                                <i class="fas fa-chart-pie text-lg"></i>
                            </div>
                        </div>
                        <div class="text-[10px] font-black text-gray-400 uppercase tracking-widest">Occupancy</div>
                        <div class="text-2xl font-black text-gray-900">
                            <c:if test="${totalCount > 0}">
                                ${fn:substring((occCount * 100 / totalCount), 0, 4)}%
                            </c:if>
                            <c:if test="${totalCount == 0}">0%</c:if>
                        </div>
                    </div>
                </div>

                <!-- Room Table -->
                <div class="glass rounded-[2.5rem] shadow-2xl shadow-gray-200/50 overflow-hidden border border-white">
                    <table class="min-w-full divide-y divide-gray-100">
                        <thead class="bg-gray-50/50">
                            <tr>
                                <th class="px-8 py-5 text-left text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Identifier</th>
                                <th class="px-8 py-5 text-left text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Category</th>
                                <th class="px-8 py-5 text-center text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Rate/Night</th>
                                <th class="px-8 py-5 text-center text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Live Status</th>
                                <th class="px-8 py-5 text-right text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Management</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-50 bg-white/50">
                            <c:forEach var="r" items="${roomsList}">
                                <tr class="hover:bg-brand-50/30 transition-colors group">
                                    <td class="px-8 py-6 whitespace-nowrap">
                                        <div class="flex items-center">
                                            <div class="w-10 h-10 rounded-xl brand-gradient flex items-center justify-center text-white font-black text-xs shadow-md shadow-brand-500/20 mr-4">
                                                ${r.roomNumber}
                                            </div>
                                            <span class="text-sm font-black text-gray-900">Unit ${r.roomNumber}</span>
                                        </div>
                                    </td>
                                    <td class="px-8 py-6 whitespace-nowrap">
                                        <span class="text-xs font-bold text-gray-600 uppercase tracking-wider">${r.roomType}</span>
                                    </td>
                                    <td class="px-8 py-6 whitespace-nowrap text-center font-mono text-sm font-bold text-gray-700">
                                        LKR ${r.ratePerNight}
                                    </td>
                                    <td class="px-8 py-6 whitespace-nowrap text-center">
                                        <span class="px-4 py-1.5 inline-flex text-[10px] leading-5 font-black rounded-xl uppercase tracking-widest
                                            ${r.status == 'AVAILABLE' ? 'bg-green-50 text-green-700 border border-green-100' : 
                                              (r.status == 'OCCUPIED' ? 'bg-admin-50 text-admin-700 border border-admin-100' : 'bg-orange-50 text-orange-700 border border-orange-100')}">
                                            <i class="fas ${r.status == 'AVAILABLE' ? 'fa-check' : 'fa-clock'} mr-2"></i> ${r.status}
                                        </span>
                                    </td>
                                    <td class="px-8 py-6 whitespace-nowrap text-right text-sm">
                                        <c:if test="${sessionScope.isAdmin}">
                                            <div class="flex justify-end space-x-2">
                                                <button onclick="openModal('edit', ${r.id}, '${r.roomNumber}', '${r.roomType}', ${r.ratePerNight})"
                                                    class="w-10 h-10 rounded-xl bg-blue-50 text-blue-600 flex items-center justify-center shadow-sm hover:bg-blue-600 hover:text-white transition-all transform hover:-translate-y-0.5">
                                                    <i class="fas fa-pencil-alt text-xs"></i>
                                                </button>
                                                <form action="${pageContext.request.contextPath}/rooms" method="POST" class="inline"
                                                    onsubmit="return confirm('CRITICAL: Delete Room ${r.roomNumber}? This cannot be undone.');">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="roomId" value="${r.id}">
                                                    <button type="submit"
                                                        class="w-10 h-10 rounded-xl bg-admin-50 text-admin-600 flex items-center justify-center shadow-sm hover:bg-admin-600 hover:text-white transition-all transform hover:-translate-y-0.5">
                                                        <i class="fas fa-trash-alt text-xs"></i>
                                                    </button>
                                                </form>
                                            </div>
                                        </c:if>
                                        <c:if test="${not sessionScope.isAdmin}">
                                            <span class="text-[9px] font-black text-gray-400 uppercase tracking-widest italic">View Only</span>
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

    <!-- Modern Room Modal -->
    <div id="roomModal" class="fixed inset-0 bg-gray-900/60 backdrop-blur-md hidden flex items-center justify-center z-50 p-4 animate-in fade-in duration-300">
        <div class="bg-white rounded-[2rem] shadow-2xl w-full max-w-md overflow-hidden transform transition-all scale-100 border border-white">
            <div class="px-8 py-6 brand-gradient relative overflow-hidden">
                <i class="fas fa-bed absolute -right-4 -bottom-4 text-7xl text-white/10 rotate-12"></i>
                <h3 id="modalTitle" class="text-xl font-black text-white tracking-tighter">Inventory Configuration</h3>
                <p class="text-[10px] font-bold text-white/60 uppercase tracking-widest mt-1">Define room properties & pricing</p>
            </div>
            
            <form action="${pageContext.request.contextPath}/rooms" method="POST" class="p-8 space-y-6">
                <input type="hidden" name="action" id="modalAction" value="add">
                <input type="hidden" name="roomId" id="modalRoomId" value="">

                <div class="space-y-4">
                    <div>
                        <label class="block text-[10px] font-black text-gray-400 uppercase tracking-widest mb-2 ml-1">Room Identifier (Number)</label>
                        <div class="relative">
                            <i class="fas fa-hashtag absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"></i>
                            <input type="text" name="roomNumber" id="modalRoomNo" required
                                class="w-full pl-12 pr-4 py-3 bg-gray-50 border border-gray-100 rounded-2xl shadow-inner focus:ring-2 focus:ring-brand-500 focus:bg-white transition-all text-sm font-bold">
                        </div>
                    </div>

                    <div>
                        <label class="block text-[10px] font-black text-gray-400 uppercase tracking-widest mb-2 ml-1">Room Category</label>
                        <select name="roomType" id="modalType"
                            class="w-full px-4 py-3 bg-gray-50 border border-gray-100 rounded-2xl shadow-inner focus:ring-2 focus:ring-brand-500 focus:bg-white transition-all text-sm font-bold appearance-none">
                            <option value="STANDARD">STANDARD</option>
                            <option value="DELUXE">DELUXE</option>
                            <option value="SUITE">SUITE</option>
                            <option value="OCEAN_VIEW">OCEAN_VIEW</option>
                        </select>
                    </div>

                    <div>
                        <label class="block text-[10px] font-black text-gray-400 uppercase tracking-widest mb-2 ml-1">Nightly Rate (LKR)</label>
                        <div class="relative">
                            <i class="fas fa-coins absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"></i>
                            <input type="number" step="0.01" name="ratePerNight" id="modalRate" required
                                class="w-full pl-12 pr-4 py-3 bg-gray-50 border border-gray-100 rounded-2xl shadow-inner focus:ring-2 focus:ring-brand-500 focus:bg-white transition-all text-sm font-bold">
                        </div>
                    </div>
                </div>

                <div class="pt-4 flex space-x-3">
                    <button type="button" onclick="closeModal()"
                        class="flex-1 px-6 py-3 border border-gray-100 rounded-2xl text-[10px] font-black uppercase tracking-widest text-gray-500 hover:bg-gray-50 transition-all">
                        Cancel
                    </button>
                    <button type="submit"
                        class="flex-1 brand-gradient px-6 py-3 rounded-2xl text-[10px] font-black uppercase tracking-widest text-white shadow-lg shadow-brand-500/30 hover:shadow-brand-500/50 transition-all">
                        Commit Configuration
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openModal(mode, id = '', roomNo = '', type = 'STANDARD', rate = '') {
            document.getElementById('modalAction').value = mode;
            document.getElementById('modalTitle').innerText = mode === 'add' ? 'Add Inventory' : 'Update Inventory';
            document.getElementById('modalRoomId').value = id;
            document.getElementById('modalRoomNo').value = roomNo;
            document.getElementById('modalType').value = type;
            document.getElementById('modalRate').value = rate;
            
            document.getElementById('roomModal').classList.remove('hidden');
            document.getElementById('roomModal').classList.add('flex');
        }
        function closeModal() {
            document.getElementById('roomModal').classList.add('hidden');
            document.getElementById('roomModal').classList.remove('flex');
        }
    </script>
</body>
</html>
