<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
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
    <title>Ocean View Resort - Intelligence Dashboard</title>
    <script src="https://cdn.tailwindcss.com/3.4.1"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        brand: { 50: '#f0fdf4', 500: '#10b981', 700: '#047857', 900: '#064e3b' },
                        reception: { 50: '#f0f9ff', 500: '#0ea5e9', 700: '#0369a1', 900: '#0c4a6e' },
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
        .luxury-gradient {
            background: linear-gradient(135deg, #064e3b 0%, #047857 100%);
        }
        .reception-gradient {
            background: linear-gradient(135deg, #0c4a6e 0%, #0284c7 100%);
        }
    </style>
</head>
<body class="bg-gray-50 text-gray-800 flex h-screen overflow-hidden">

    <jsp:include page="/WEB-INF/components/sidebar.jsp" />

    <div class="flex-1 flex flex-col overflow-hidden">

        <!-- Header -->
        <header class="bg-white/80 backdrop-blur-md shadow-sm border-b border-gray-200 py-4 px-8 flex justify-between items-center z-10">
            <div>
                <h2 class="text-2xl font-black text-gray-900 tracking-tighter flex items-center">
                    <i class="fas ${sessionScope.isAdmin ? 'fa-shield-halved text-admin-600' : 'fa-concierge-bell text-reception-600'} mr-3"></i> 
                    ${sessionScope.isAdmin ? 'Command Center' : 'Receptionist Workstation'}
                </h2>
                <p class="text-[10px] font-bold text-gray-400 uppercase tracking-widest mt-0.5">Live operational telemetry & guest flow</p>
            </div>
            <div class="flex items-center space-x-6">
                <div class="text-right hidden md:block">
                    <p class="text-[10px] font-black text-gray-400 uppercase tracking-widest">Global Timestamp</p>
                    <p class="text-xs font-bold text-gray-900 font-mono tracking-tighter" id="datetime"></p>
                </div>
                <div class="h-8 w-[1px] bg-gray-200"></div>
                <div class="flex items-center gap-3">
                    <div class="text-right">
                        <p class="text-[9px] font-black text-gray-400 rotate-0">ID: ${sessionScope.loggedInUserId}</p>
                        <p class="text-[10px] font-black uppercase text-brand-700 tracking-widest">${sessionScope.loggedInRole}</p>
                    </div>
                    <div class="w-10 h-10 rounded-2xl bg-gray-100 flex items-center justify-center text-gray-400 hover:bg-brand-50 hover:text-brand-600 transition-all cursor-pointer" onclick="location.reload()">
                        <i class="fas fa-sync-alt text-xs animate-spin-hover"></i>
                    </div>
                </div>
            </div>
        </header>

        <main class="flex-1 overflow-auto bg-[radial-gradient(#e5e7eb_1px,transparent_1px)] [background-size:16px_16px] p-8">

            <div class="max-w-7xl mx-auto space-y-8">
                
                <!-- KPI Board -->
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                    <div class="glass p-6 rounded-[2rem] shadow-sm border border-white hover:shadow-xl transition-all group cursor-default">
                        <div class="flex justify-between items-start mb-4">
                            <div class="w-12 h-12 bg-blue-50 rounded-2xl flex items-center justify-center text-blue-600 group-hover:scale-110 transition-transform">
                                <i class="fas fa-book-bookmark text-xl"></i>
                            </div>
                            <i class="fas fa-ellipsis-h text-[10px] text-gray-300"></i>
                        </div>
                        <div class="text-[10px] font-black text-gray-400 uppercase tracking-[0.2em] mb-1">Total Ledger</div>
                        <div class="text-3xl font-black text-gray-900 tracking-tighter">${totalReservations}</div>
                    </div>

                    <div class="glass p-6 rounded-[2rem] shadow-sm border border-white hover:shadow-xl transition-all group cursor-default">
                        <div class="flex justify-between items-start mb-4">
                            <div class="w-12 h-12 bg-reception-50 rounded-2xl flex items-center justify-center text-reception-600 group-hover:scale-110 transition-transform">
                                <i class="fas fa-key text-xl"></i>
                            </div>
                            <span class="text-[9px] font-black text-reception-600 bg-reception-50 px-3 py-1 rounded-full uppercase tracking-tighter">Ready</span>
                        </div>
                        <div class="text-[10px] font-black text-gray-400 uppercase tracking-[0.2em] mb-1">Available Units</div>
                        <div class="text-3xl font-black text-gray-900 tracking-tighter">${availableRooms}</div>
                    </div>

                    <div class="glass p-6 rounded-[2rem] shadow-sm border border-white hover:shadow-xl transition-all group cursor-default">
                        <div class="flex justify-between items-start mb-4">
                            <div class="w-12 h-12 bg-brand-50 rounded-2xl flex items-center justify-center text-brand-600 group-hover:scale-110 transition-transform">
                                <i class="fas fa-user-check text-xl"></i>
                            </div>
                            <span class="text-[9px] font-black text-brand-600 bg-brand-50 px-3 py-1 rounded-full uppercase tracking-tighter">Active</span>
                        </div>
                        <div class="text-[10px] font-black text-gray-400 uppercase tracking-[0.2em] mb-1">Occupied Units</div>
                        <div class="text-3xl font-black text-gray-900 tracking-tighter">${occupiedRooms}</div>
                    </div>

                    <div class="reception-gradient p-6 rounded-[2rem] shadow-2xl shadow-reception-900/20 text-white group relative overflow-hidden cursor-default">
                        <i class="fas fa-chart-line absolute -right-6 -bottom-6 text-8xl text-white/10 rotate-12"></i>
                        <div class="relative z-10">
                            <div class="flex justify-between items-start mb-4">
                                <div class="w-12 h-12 bg-white/10 backdrop-blur-md rounded-2xl flex items-center justify-center text-white">
                                    <i class="fas fa-percent text-xl"></i>
                                </div>
                            </div>
                            <div class="text-[10px] font-black text-reception-100 uppercase tracking-[0.2em] mb-1">Yield Velocity</div>
                            <div class="text-3xl font-black tracking-tighter">${occupancyRate}%</div>
                        </div>
                    </div>
                </div>

                <!-- Strategic Operations Grid -->
                <div class="grid grid-cols-1 lg:grid-cols-12 gap-8">
                    
                    <!-- Left: Action Hub -->
                    <div class="lg:col-span-8 space-y-8">
                        
                        <div class="bg-white p-10 rounded-[3rem] shadow-xl shadow-gray-200/50 border border-gray-50 relative overflow-hidden group">
                             <div class="absolute -right-20 -bottom-20 p-8 opacity-[0.03] rotate-12 transition-transform group-hover:rotate-0 duration-1000">
                                <i class="fas fa-keyboard text-[20rem]"></i>
                            </div>
                            
                            <h3 class="text-xs font-black text-gray-400 uppercase tracking-[0.4em] mb-10 flex items-center">
                                <div class="w-2 h-3 bg-reception-500 rounded-full mr-4"></div>
                                Tactical Quick Actions
                            </h3>
                            
                            <div class="grid grid-cols-2 md:grid-cols-4 gap-6 relative z-10">
                                <a href="${pageContext.request.contextPath}/reservations" 
                                   class="group p-6 rounded-[2.5rem] bg-gray-50/50 hover:bg-white border border-transparent hover:border-reception-200 hover:shadow-2xl transition-all flex flex-col items-center text-center transform hover:-translate-y-2">
                                    <div class="w-16 h-16 bg-white rounded-3xl flex items-center justify-center text-reception-600 mb-4 shadow-sm group-hover:scale-110 transition-transform">
                                        <i class="fas fa-folder-open text-xl"></i>
                                    </div>
                                    <span class="text-[10px] font-black text-gray-500 uppercase tracking-widest">Ledger</span>
                                </a>
                                
                                <a href="${pageContext.request.contextPath}/reservation/new" 
                                   class="group p-6 rounded-[2.5rem] bg-gray-50/50 hover:bg-white border border-transparent hover:border-brand-200 hover:shadow-2xl transition-all flex flex-col items-center text-center transform hover:-translate-y-2">
                                    <div class="w-16 h-16 bg-white rounded-3xl flex items-center justify-center text-brand-600 mb-4 shadow-sm group-hover:scale-110 transition-transform">
                                        <i class="fas fa-calendar-plus text-xl"></i>
                                    </div>
                                    <span class="text-[10px] font-black text-gray-500 uppercase tracking-widest">Admission</span>
                                </a>

                                <a href="${pageContext.request.contextPath}/reservations" 
                                   class="group p-6 rounded-[2.5rem] bg-gray-50/50 hover:bg-white border border-transparent hover:border-orange-200 hover:shadow-2xl transition-all flex flex-col items-center text-center transform hover:-translate-y-2">
                                    <div class="w-16 h-16 bg-white rounded-3xl flex items-center justify-center text-orange-600 mb-4 shadow-sm group-hover:scale-110 transition-transform">
                                        <i class="fas fa-file-invoice text-xl"></i>
                                    </div>
                                    <span class="text-[10px] font-black text-gray-500 uppercase tracking-widest">Financials</span>
                                </a>

                                <c:if test="${sessionScope.isAdmin}">
                                    <a href="${pageContext.request.contextPath}/users" 
                                       class="group p-6 rounded-[2.5rem] bg-gray-50/50 hover:bg-white border border-transparent hover:border-admin-200 hover:shadow-2xl transition-all flex flex-col items-center text-center transform hover:-translate-y-2">
                                        <div class="w-16 h-16 bg-white rounded-3xl flex items-center justify-center text-admin-600 mb-4 shadow-sm group-hover:scale-110 transition-transform">
                                            <i class="fas fa-shield-halved text-xl"></i>
                                        </div>
                                        <span class="text-[10px] font-black text-gray-500 uppercase tracking-widest">Systems</span>
                                    </a>
                                </c:if>
                                <c:if test="${!sessionScope.isAdmin}">
                                    <a href="${pageContext.request.contextPath}/rooms" 
                                       class="group p-6 rounded-[2.5rem] bg-gray-50/50 hover:bg-white border border-transparent hover:border-blue-200 hover:shadow-2xl transition-all flex flex-col items-center text-center transform hover:-translate-y-2">
                                        <div class="w-16 h-16 bg-white rounded-3xl flex items-center justify-center text-blue-600 mb-4 shadow-sm group-hover:scale-110 transition-transform">
                                            <i class="fas fa-bed text-xl"></i>
                                        </div>
                                        <span class="text-[10px] font-black text-gray-500 uppercase tracking-widest">Inventory</span>
                                    </a>
                                </c:if>
                            </div>
                        </div>

                        <!-- Operational Flow Monitor -->
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
                            <div class="glass p-10 rounded-[3rem] border border-white text-center group hover:bg-white transition-all">
                                <div class="w-16 h-16 bg-green-50 rounded-3xl flex items-center justify-center text-green-600 mx-auto mb-6 group-hover:rotate-12 transition-transform">
                                    <i class="fas fa-sign-in-alt text-2xl"></i>
                                </div>
                                <div class="text-[10px] font-black text-gray-400 uppercase tracking-[0.4em] mb-2">Arrival Vector</div>
                                <div class="text-4xl font-black text-gray-900 tracking-tighter">${todayCheckIns}</div>
                                <p class="text-[9px] font-bold text-green-500 uppercase tracking-widest mt-3 underline decoration-2 underline-offset-4">Today's Check-ins</p>
                            </div>

                            <div class="glass p-10 rounded-[3rem] border border-white text-center group hover:bg-white transition-all">
                                <div class="w-16 h-16 bg-red-50 rounded-3xl flex items-center justify-center text-red-600 mx-auto mb-6 group-hover:-rotate-12 transition-transform">
                                    <i class="fas fa-sign-out-alt text-2xl"></i>
                                </div>
                                <div class="text-[10px] font-black text-gray-400 uppercase tracking-[0.4em] mb-2">Departure Vector</div>
                                <div class="text-4xl font-black text-gray-900 tracking-tighter">${todayCheckOuts}</div>
                                <p class="text-[9px] font-bold text-red-500 uppercase tracking-widest mt-3 underline decoration-2 underline-offset-4">Today's Check-outs</p>
                            </div>
                        </div>

                    </div>

                    <!-- Right: Audit Stream -->
                    <div class="lg:col-span-4 flex flex-col h-full">
                        <div class="bg-white/80 backdrop-blur-3xl rounded-[3rem] shadow-2xl shadow-gray-200/50 border border-white flex flex-col h-full relative overflow-hidden">
                            <div class="p-10 border-b border-gray-50">
                                <h3 class="text-xs font-black text-gray-900 uppercase tracking-[0.5em] flex items-center">
                                    <i class="fas fa-wave-square mr-4 text-reception-600 animate-pulse"></i> Operations Stream
                                </h3>
                            </div>
                            
                            <div class="flex-1 overflow-y-auto px-6 py-8 space-y-4 scrollbar-hide">
                                <c:forEach var="log" items="${recentLogs}">
                                    <div class="p-5 rounded-[2rem] bg-gray-50/50 border border-transparent hover:border-gray-100 transition-all hover:bg-white">
                                        <div class="flex justify-between items-start mb-2">
                                            <span class="px-3 py-1 bg-white rounded-full text-[8px] font-black text-reception-700 uppercase tracking-widest shadow-sm">
                                                ${log[1]}
                                            </span>
                                            <span class="text-[8px] font-black text-gray-300 font-mono">${log[0]}</span>
                                        </div>
                                        <p class="text-[11px] font-black text-gray-700 leading-tight">Unit Modification Ref: ${log[2]}</p>
                                        <p class="text-[9px] font-bold text-gray-400 mt-1 truncate">${log[3]}</p>
                                    </div>
                                </c:forEach>
                                <c:if test="${empty recentLogs}">
                                    <div class="py-32 text-center">
                                        <div class="w-20 h-20 bg-gray-50 rounded-full flex items-center justify-center mx-auto mb-6 text-gray-200">
                                            <i class="fas fa-shield text-3xl"></i>
                                        </div>
                                        <p class="text-[10px] font-black text-gray-400 uppercase tracking-[0.3em] italic">System Dormant</p>
                                    </div>
                                </c:if>
                            </div>
                            
                            <div class="p-8 bg-gray-50/50 border-t border-gray-100">
                                <div class="flex items-center justify-between">
                                    <div class="flex items-center gap-3">
                                        <div class="w-2 h-2 bg-green-500 rounded-full animate-ping"></div>
                                        <span class="text-[10px] font-black text-gray-400 uppercase tracking-widest">Network Active</span>
                                    </div>
                                    <i class="fas fa-terminal text-gray-200 text-xs"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </main>
    </div>

    <script>
        function updateTime() {
            const now = new Date();
            const options = { 
                weekday: 'short', 
                year: 'numeric', 
                month: 'short', 
                day: 'numeric', 
                hour: '2-digit', 
                minute: '2-digit', 
                second: '2-digit',
                hour12: true 
            };
            document.getElementById('datetime').textContent = now.toLocaleDateString('en-US', options);
        }
        setInterval(updateTime, 1000);
        updateTime();
    </script>
</body>
</html>
