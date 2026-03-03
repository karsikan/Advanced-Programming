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
    <title>Revenue Intelligence - Ocean View Resort</title>
    <script src="https://cdn.tailwindcss.com/3.4.1"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        brand: { 50: '#f0fdf4', 500: '#10b981', 700: '#047857', 900: '#064e3b' },
                        gold: { 500: '#D4AF37', 600: '#B8860B' }
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
        .stats-gradient {
            background: linear-gradient(135deg, #064e3b 0%, #047857 100%);
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
                    <i class="fas fa-chart-line text-brand-600 mr-3"></i> Intelligence Hub
                </h2>
                <p class="text-[10px] font-bold text-gray-400 uppercase tracking-widest mt-0.5">Real-time financial performance analytics</p>
            </div>
            <a href="${pageContext.request.contextPath}/revenue?export=csv"
                class="stats-gradient hover:opacity-90 text-white font-black py-2.5 px-6 rounded-xl shadow-lg shadow-brand-500/20 transition-all text-xs flex items-center uppercase tracking-wider">
                <i class="fas fa-file-export mr-2"></i> Export Dataset
            </a>
        </header>

        <main class="flex-1 overflow-auto bg-[radial-gradient(#e5e7eb_1px,transparent_1px)] [background-size:16px_16px] p-8">

            <div class="max-w-6xl mx-auto space-y-8">
                
                <!-- KPI Grid -->
                <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <div class="stats-gradient p-8 rounded-[2.5rem] shadow-2xl shadow-brand-900/20 text-white relative overflow-hidden group">
                        <i class="fas fa-wallet absolute -right-6 -bottom-6 text-9xl text-white/5 group-hover:rotate-12 transition-transform duration-500"></i>
                        <div class="relative z-10">
                            <div class="text-[10px] font-black text-brand-200 uppercase tracking-[0.2em] mb-1">Gross Yield</div>
                            <div class="text-4xl font-black tracking-tighter mb-2">${totalRevenue}</div>
                            <div class="text-[10px] font-bold text-brand-300 uppercase tracking-widest flex items-center">
                                <i class="fas fa-arrow-up mr-2"></i> Total LKR Realized
                            </div>
                        </div>
                    </div>

                    <div class="bg-white p-8 rounded-[2.5rem] shadow-sm border border-gray-100 flex flex-col justify-between group">
                        <div class="flex justify-between items-center mb-4">
                            <div class="w-12 h-12 bg-blue-50 rounded-2xl flex items-center justify-center text-blue-600">
                                <i class="fas fa-percent text-xl"></i>
                            </div>
                            <span class="text-[10px] font-black text-blue-600 bg-blue-50 px-3 py-1 rounded-lg uppercase tracking-widest">Active</span>
                        </div>
                        <div>
                            <div class="text-[10px] font-black text-gray-400 uppercase tracking-[0.2em] mb-1">Occupancy Rate</div>
                            <div class="text-3xl font-black text-gray-900">${fn:substringBefore(occupancyRate, '.').concat('%')}</div>
                        </div>
                    </div>

                    <div class="bg-white p-8 rounded-[2.5rem] shadow-sm border border-gray-100 flex flex-col justify-between group">
                        <div class="flex justify-between items-center mb-4">
                            <div class="w-12 h-12 bg-orange-50 rounded-2xl flex items-center justify-center text-orange-600">
                                <i class="fas fa-coins text-xl"></i>
                            </div>
                        </div>
                        <div>
                            <div class="text-[10px] font-black text-gray-400 uppercase tracking-[0.2em] mb-1">Portfolio Value</div>
                            <div class="text-3xl font-black text-gray-900">PREMIUM</div>
                        </div>
                    </div>
                </div>

                <!-- Detailed Breakdown -->
                <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
                    
                    <!-- Distribution Chart Card -->
                    <div class="glass rounded-[2.5rem] p-8 shadow-2xl shadow-gray-200/50 border border-white">
                        <div class="flex items-center justify-between mb-8">
                            <div>
                                <h3 class="text-lg font-black text-gray-900 tracking-tighter">Category Distribution</h3>
                                <p class="text-[10px] font-bold text-gray-400 uppercase tracking-widest">Revenue by Room Classification</p>
                            </div>
                            <div class="w-10 h-10 bg-gray-50 rounded-xl flex items-center justify-center text-gray-400">
                                <i class="fas fa-sliders-h"></i>
                            </div>
                        </div>

                        <div class="space-y-6">
                            <c:forEach var="entry" items="${revenueData}">
                                <div class="group">
                                    <div class="flex justify-between items-end mb-2">
                                        <span class="text-sm font-black text-gray-700 group-hover:text-brand-700 transition-colors uppercase tracking-tight">${entry.key}</span>
                                        <span class="text-brand-700 font-mono font-black text-xs">LKR ${entry.value}</span>
                                    </div>
                                    <div class="w-full bg-gray-100 rounded-full h-2.5 overflow-hidden">
                                        <div class="stats-gradient h-full rounded-full transition-all duration-1000 group-hover:shadow-[0_0_15px_rgba(16,185,129,0.5)]"
                                            style="width: 75%"></div>
                                    </div>
                                </div>
                            </c:forEach>
                            <c:if test="${empty revenueData}">
                                <div class="py-12 text-center">
                                    <div class="w-16 h-16 bg-gray-50 rounded-full flex items-center justify-center text-gray-200 mx-auto mb-4">
                                        <i class="fas fa-folder-open text-2xl"></i>
                                    </div>
                                    <p class="text-[10px] font-black text-gray-400 uppercase tracking-widest italic">Awaiting Financial Cycles</p>
                                </div>
                            </c:if>
                        </div>
                    </div>

                    <!-- Monthly Performance -->
                    <div class="glass rounded-[2.5rem] p-8 shadow-2xl shadow-gray-200/50 border border-white">
                        <div class="flex items-center justify-between mb-8">
                            <div>
                                <h3 class="text-lg font-black text-gray-900 tracking-tighter">Velocity Report</h3>
                                <p class="text-[10px] font-bold text-gray-400 uppercase tracking-widest">Monthly Growth trajectory</p>
                            </div>
                            <div class="w-10 h-10 bg-brand-50 rounded-xl flex items-center justify-center text-brand-600">
                                <i class="fas fa-calendar-alt text-xs"></i>
                            </div>
                        </div>

                        <div class="overflow-hidden">
                            <table class="min-w-full">
                                <thead>
                                    <tr class="border-b border-gray-100">
                                        <th class="text-left text-[10px] font-black text-gray-400 uppercase tracking-widest pb-4">Cycle</th>
                                        <th class="text-right text-[10px] font-black text-gray-400 uppercase tracking-widest pb-4">Yield (LKR)</th>
                                    </tr>
                                </thead>
                                <tbody class="divide-y divide-gray-50">
                                    <c:forEach var="entry" items="${monthlyRevenue}">
                                        <tr class="group hover:bg-brand-50/20 transition-colors">
                                            <td class="py-4">
                                                <div class="text-sm font-black text-gray-800 uppercase tracking-tight">${entry.key}</div>
                                            </td>
                                            <td class="py-4 text-right">
                                                <div class="text-xs font-black text-brand-700 font-mono">LKR ${entry.value}</div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- Intelligence Alert -->
                <div class="bg-blue-600 rounded-[2rem] p-8 shadow-xl shadow-blue-500/20 text-white flex items-center justify-between group overflow-hidden relative">
                    <i class="fas fa-info-circle absolute -left-4 -bottom-4 text-8xl text-white/10 group-hover:scale-110 transition-transform duration-500"></i>
                    <div class="flex items-center space-x-6 relative z-10">
                        <div class="w-16 h-16 bg-white/20 backdrop-blur-md rounded-2xl flex items-center justify-center text-2xl">
                            <i class="fas fa-database"></i>
                        </div>
                        <div>
                            <h4 class="text-lg font-black tracking-tighter">Raw Data Export Available</h4>
                            <p class="text-xs font-medium text-blue-100/80 mt-1">Need deeper analysis? Our CSV export includes T+0 real-time reservation data inclusive of taxes and service fees.</p>
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}/revenue?export=csv" class="hidden md:flex bg-white text-blue-600 px-6 py-3 rounded-2xl text-[10px] font-black uppercase tracking-widest shadow-lg hover:bg-blue-50 transition-all relative z-10">
                        Initiate Download
                    </a>
                </div>

            </div>

        </main>
    </div>
</body>
</html>
