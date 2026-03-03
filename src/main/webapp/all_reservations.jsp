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
    <title>Reservation Ledger - Ocean View Resort</title>
    <script src="https://cdn.tailwindcss.com/3.4.1"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        brand: { 50: '#f0fdf4', 500: '#10b981', 700: '#047857', 900: '#064e3b' },
                        reception: { 50: '#f0f9ff', 500: '#0ea5e9', 700: '#0369a1', 900: '#0c4a6e' }
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
        .reception-gradient {
            background: linear-gradient(135deg, #0c4a6e 0%, #0284c7 100%);
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
                    <i class="fas fa-book-open text-reception-600 mr-3"></i> Reservation Ledger
                </h2>
                <p class="text-[10px] font-bold text-gray-400 uppercase tracking-widest mt-0.5">Central booking repository & guest management</p>
            </div>
            <a href="${pageContext.request.contextPath}/reservation/new"
                class="reception-gradient hover:opacity-90 text-white font-black py-2.5 px-6 rounded-xl shadow-lg shadow-reception-500/20 transition-all text-xs flex items-center uppercase tracking-wider">
                <i class="fas fa-plus-circle mr-2"></i> New Acquisition
            </a>
        </header>

        <main class="flex-1 overflow-auto bg-[radial-gradient(#e5e7eb_1px,transparent_1px)] [background-size:16px_16px] p-8">

            <div class="max-w-7xl mx-auto">
                
                <!-- Feedback Messages -->
                <c:if test="${not empty sessionScope.flashMessage}">
                    <div class="mb-8 p-5 rounded-2xl glass border-l-4 ${sessionScope.flashType == 'success' ? 'border-brand-500' : 'border-red-500'} flex items-center shadow-xl animate-in fade-in slide-in-from-top-4 duration-500">
                        <div class="w-10 h-10 rounded-xl ${sessionScope.flashType == 'success' ? 'bg-brand-50 text-brand-600' : 'bg-red-50 text-red-600'} flex items-center justify-center mr-4">
                            <i class="fas ${sessionScope.flashType == 'success' ? 'fa-check' : 'fa-exclamation'} text-sm"></i>
                        </div>
                        <div>
                            <p class="text-[10px] font-black uppercase tracking-widest text-gray-400">${sessionScope.flashType == 'success' ? 'Ledger Updated' : 'System Alert'}</p>
                            <p class="text-sm font-bold text-gray-800">${sessionScope.flashMessage}</p>
                        </div>
                    </div>
                    <c:remove var="flashMessage" scope="session" />
                    <c:remove var="flashType" scope="session" />
                </c:if>

                <!-- Data Table -->
                <div class="glass rounded-[2.5rem] shadow-2xl shadow-gray-200/50 overflow-hidden border border-white">
                    <table class="min-w-full divide-y divide-gray-100">
                        <thead class="bg-gray-50/50">
                            <tr>
                                <th class="px-8 py-5 text-left text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Identifier</th>
                                <th class="px-8 py-5 text-left text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Guest Reference</th>
                                <th class="px-8 py-5 text-left text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Placement</th>
                                <th class="px-8 py-5 text-left text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Timeline</th>
                                <th class="px-8 py-5 text-center text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Status</th>
                                <th class="px-8 py-5 text-right text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-50 bg-white/50">
                            <c:forEach var="res" items="${reservationsList}">
                                <tr class="hover:bg-reception-50/30 transition-colors group">
                                    <td class="px-8 py-6 whitespace-nowrap">
                                        <div class="text-xs font-black text-gray-900 font-mono tracking-tighter">${res.reservationId}</div>
                                    </td>
                                    <td class="px-8 py-6 whitespace-nowrap">
                                        <div class="flex items-center">
                                            <div class="w-8 h-8 rounded-full bg-gray-100 flex items-center justify-center text-[10px] font-black text-gray-500 mr-3">
                                                ID
                                            </div>
                                            <span class="text-sm font-bold text-gray-700">Account Ref: ${res.guestId}</span>
                                        </div>
                                    </td>
                                    <td class="px-8 py-6 whitespace-nowrap">
                                        <span class="px-3 py-1 bg-white border border-gray-100 rounded-lg text-[10px] font-black text-reception-700 uppercase tracking-widest shadow-sm">
                                            Room ${res.roomId}
                                        </span>
                                    </td>
                                    <td class="px-8 py-6 whitespace-nowrap">
                                        <div class="text-[10px] font-bold text-gray-500 flex flex-col space-y-1">
                                            <span class="flex items-center"><i class="fas fa-sign-in-alt text-brand-500 mr-2 w-3"></i> ${res.checkInDate}</span>
                                            <span class="flex items-center"><i class="fas fa-sign-out-alt text-red-500 mr-2 w-3"></i> ${res.checkOutDate}</span>
                                        </div>
                                    </td>
                                    <td class="px-8 py-6 whitespace-nowrap text-center">
                                        <span class="px-4 py-1.5 inline-flex text-[10px] leading-5 font-black rounded-xl uppercase tracking-[0.15em]
                                            ${res.status == 'CONFIRMED' ? 'bg-reception-50 text-reception-700 border border-reception-100' : 
                                              (res.status == 'CHECKED_OUT' ? 'bg-gray-50 text-gray-500 border border-gray-100' : 'bg-red-50 text-red-700 border border-red-100')}">
                                            ${res.status}
                                        </span>
                                    </td>
                                    <td class="px-8 py-6 whitespace-nowrap text-right">
                                        <div class="flex justify-end space-x-2">
                                            <a href="${pageContext.request.contextPath}/billing?reservationId=${res.reservationId}"
                                                title="Generate Invoice"
                                                class="w-10 h-10 rounded-xl bg-orange-50 text-orange-600 flex items-center justify-center shadow-sm hover:bg-orange-600 hover:text-white transition-all transform hover:-translate-y-0.5">
                                                <i class="fas fa-file-invoice-dollar text-xs"></i>
                                            </a>

                                            <a href="${pageContext.request.contextPath}/reservation/edit?id=${res.reservationId}"
                                                title="Tactical Edit"
                                                class="w-10 h-10 rounded-xl bg-reception-50 text-reception-600 flex items-center justify-center shadow-sm hover:bg-reception-600 hover:text-white transition-all transform hover:-translate-y-0.5">
                                                <i class="fas fa-pencil-alt text-xs"></i>
                                            </a>

                                            <c:if test="${res.status == 'CONFIRMED'}">
                                                <form action="${pageContext.request.contextPath}/reservation/cancel" method="POST" class="inline"
                                                    onsubmit="return confirm('Terminate reservation ${res.reservationId}? This action will update system occupancy.');">
                                                    <input type="hidden" name="reservationId" value="${res.reservationId}">
                                                    <input type="hidden" name="roomId" value="${res.roomId}">
                                                    <button type="submit" title="Cancellation Flow"
                                                        class="w-10 h-10 rounded-xl bg-red-50 text-red-600 flex items-center justify-center shadow-sm hover:bg-red-600 hover:text-white transition-all transform hover:-translate-y-0.5">
                                                        <i class="fas fa-trash-alt text-xs"></i>
                                                    </button>
                                                </form>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty reservationsList}">
                                <tr>
                                    <td colspan="6" class="px-8 py-12 text-center">
                                        <div class="w-16 h-16 bg-gray-50 rounded-full flex items-center justify-center text-gray-200 mx-auto mb-4">
                                            <i class="fas fa-inbox text-2xl"></i>
                                        </div>
                                        <p class="text-[10px] font-black text-gray-400 uppercase tracking-widest italic">Ledger Empty - Awaiting Bookings</p>
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
