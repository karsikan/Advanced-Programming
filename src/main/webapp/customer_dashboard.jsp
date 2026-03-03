<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
            <% if (session.getAttribute("loggedInUser")==null) { response.sendRedirect(request.getContextPath()
                + "/login" ); return; } %>
                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Customer Dashboard - Ocean View Resort</title>
                    <script src="https://cdn.tailwindcss.com/3.4.1"></script>
                    <script>
                        tailwind.config = {
                            theme: {
                                extend: {
                                    colors: {
                                        brand: { 50: '#f0fdf4', 500: '#10b981', 700: '#047857', 900: '#064e3b' }
                                    }
                                }
                            }
                        }
                    </script>
                    <link rel="stylesheet"
                        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
                </head>

                <body class="bg-gray-50 flex h-screen overflow-hidden">

                    <jsp:include page="/WEB-INF/components/sidebar.jsp" />

                    <div class="flex-1 flex flex-col overflow-hidden">
                        <header
                            class="bg-white shadow-sm border-b border-gray-200 py-4 px-6 flex justify-between items-center z-10">
                            <h2 class="text-xl font-bold text-gray-800 flex items-center">
                                <i class="fas fa-user-circle text-brand-600 mr-3"></i> Welcome,
                                ${sessionScope.loggedInUser}!
                            </h2>
                            <div class="flex items-center space-x-4">
                                <span
                                    class="text-xs font-bold px-3 py-1 bg-brand-100 text-brand-700 rounded-full uppercase tracking-widest">
                                    Customer Account
                                </span>
                            </div>
                        </header>

                        <main class="flex-1 overflow-auto bg-gray-50 p-6">

                            <!-- Flash Messages -->
                            <c:if test="${not empty sessionScope.flashMessage}">
                                <div
                                    class="max-w-5xl mx-auto mb-6 p-4 rounded-xl shadow-sm flex items-center 
                                            ${sessionScope.flashType == 'success' ? 'bg-green-50 text-green-800 border border-green-200' : 'bg-red-50 text-red-800 border border-red-200'}">
                                    <i
                                        class="fas ${sessionScope.flashType == 'success' ? 'fa-check-circle text-green-500' : 'fa-exclamation-circle text-red-500'} mr-3"></i>
                                    <span class="font-medium">${sessionScope.flashMessage}</span>
                                </div>
                                <c:remove var="flashMessage" scope="session" />
                                <c:remove var="flashType" scope="session" />
                            </c:if>

                            <div class="max-w-5xl mx-auto space-y-6">

                                <!-- Welcome Card with Tier -->
                                <div
                                    class="bg-gradient-to-r from-brand-800 to-brand-600 rounded-3xl p-8 text-white shadow-xl relative overflow-hidden">
                                    <div
                                        class="relative z-10 flex flex-col md:flex-row justify-between items-start md:items-center">
                                        <div class="mb-6 md:mb-0">
                                            <div class="flex items-center space-x-2 mb-3">
                                                <c:choose>
                                                    <c:when test="${customerStats.totalNights >= 10}">
                                                        <span
                                                            class="px-3 py-1 bg-white/20 backdrop-blur-md rounded-full text-[10px] font-black uppercase tracking-widest border border-white/30 flex items-center">
                                                            <i class="fas fa-crown text-yellow-400 mr-2"></i> PLATINUM
                                                            PREMIER
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${customerStats.totalNights >= 6}">
                                                        <span
                                                            class="px-3 py-1 bg-white/20 backdrop-blur-md rounded-full text-[10px] font-black uppercase tracking-widest border border-white/30 flex items-center">
                                                            <i class="fas fa-medal text-yellow-500 mr-2"></i> GOLD VIP
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${customerStats.totalNights >= 3}">
                                                        <span
                                                            class="px-3 py-1 bg-white/20 backdrop-blur-md rounded-full text-[10px] font-black uppercase tracking-widest border border-white/30 flex items-center">
                                                            <i class="fas fa-award text-gray-300 mr-2"></i> SILVER ELITE
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span
                                                            class="px-3 py-1 bg-white/20 backdrop-blur-md rounded-full text-[10px] font-black uppercase tracking-widest border border-white/30 flex items-center">
                                                            <i class="fas fa-user-check text-brand-200 mr-2"></i> RESORT
                                                            MEMBER
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <h3 class="text-3xl font-black mb-2">Welcome back,
                                                ${sessionScope.loggedInUser}! 🌊</h3>
                                            <p class="text-brand-100 max-w-md text-sm leading-relaxed">
                                                Thank you for being a loyal guest. You've stayed <span
                                                    class="text-white font-bold">${customerStats.totalNights != null ?
                                                    customerStats.totalNights : 0} nights</span> with us.
                                            </p>
                                        </div>
                                        <div
                                            class="bg-white/10 backdrop-blur-lg border border-white/20 p-5 rounded-2xl md:w-64">
                                            <div class="flex justify-between items-end mb-2">
                                                <p class="text-[10px] font-bold uppercase text-brand-100">Loyalty
                                                    Progress</p>
                                                <p class="text-xs font-black">
                                                    <c:choose>
                                                        <c:when test="${customerStats.totalNights >= 10}">MAX</c:when>
                                                        <c:when test="${customerStats.totalNights >= 6}">
                                                            ${customerStats.totalNights}/10</c:when>
                                                        <c:when test="${customerStats.totalNights >= 3}">
                                                            ${customerStats.totalNights}/6</c:when>
                                                        <c:otherwise>${customerStats.totalNights}/3</c:otherwise>
                                                    </c:choose>
                                                </p>
                                            </div>
                                            <div class="h-2 w-full bg-white/20 rounded-full overflow-hidden">
                                                <c:set var="width" value="0" />
                                                <c:choose>
                                                    <c:when test="${customerStats.totalNights >= 10}">
                                                        <c:set var="width" value="100" />
                                                    </c:when>
                                                    <c:when test="${customerStats.totalNights >= 6}">
                                                        <c:set var="width"
                                                            value="${(customerStats.totalNights / 10) * 100}" />
                                                    </c:when>
                                                    <c:when test="${customerStats.totalNights >= 3}">
                                                        <c:set var="width"
                                                            value="${(customerStats.totalNights / 6) * 100}" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="width"
                                                            value="${(customerStats.totalNights / 3) * 100}" />
                                                    </c:otherwise>
                                                </c:choose>
                                                <div class="h-full bg-white transition-all duration-1000"
                                                    style="width: ${width}%"></div>
                                            </div>
                                            <p
                                                class="text-[9px] mt-2 text-brand-50/70 italic uppercase tracking-tighter">
                                                <c:choose>
                                                    <c:when test="${customerStats.totalNights >= 10}">Enjoy VIP Platinum
                                                        status!</c:when>
                                                    <c:when test="${customerStats.totalNights >= 6}">${10 -
                                                        customerStats.totalNights} more nights to Platinum</c:when>
                                                    <c:when test="${customerStats.totalNights >= 3}">${6 -
                                                        customerStats.totalNights} more nights to Gold</c:when>
                                                    <c:otherwise>${3 - customerStats.totalNights} more nights to Silver
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="absolute -right-10 -bottom-10 opacity-20 text-[180px] rotate-12">
                                        <i class="fas fa-umbrella-beach"></i>
                                    </div>
                                </div>

                                <!-- Quick Actions Grid (MODIFIED as requested) -->
                                <div class="grid grid-cols-2 lg:grid-cols-4 gap-4">
                                    <a href="${pageContext.request.contextPath}/reservation/new"
                                        class="bg-white p-6 rounded-2xl shadow-sm border border-brand-100 hover:border-brand-500 hover:shadow-md transition-all group flex flex-col items-center text-center">
                                        <div
                                            class="w-12 h-12 bg-brand-50 rounded-xl flex items-center justify-center text-brand-600 mb-4 group-hover:scale-110 transition-transform">
                                            <i class="fas fa-calendar-plus text-xl"></i>
                                        </div>
                                        <span class="text-sm font-bold text-gray-800 uppercase tracking-wider">Book
                                            Room</span>
                                    </a>
                                    <a href="#my-reservations"
                                        class="bg-white p-6 rounded-2xl shadow-sm border border-blue-100 hover:border-blue-500 hover:shadow-md transition-all group flex flex-col items-center text-center">
                                        <div
                                            class="w-12 h-12 bg-blue-50 rounded-xl flex items-center justify-center text-blue-600 mb-4 group-hover:scale-110 transition-transform">
                                            <i class="fas fa-history text-xl"></i>
                                        </div>
                                        <span class="text-sm font-bold text-gray-800 uppercase tracking-wider">View My
                                            Reservation</span>
                                    </a>
                                    <a href="#my-reservations"
                                        class="bg-white p-6 rounded-2xl shadow-sm border border-orange-100 hover:border-orange-500 hover:shadow-md transition-all group flex flex-col items-center text-center">
                                        <div
                                            class="w-12 h-12 bg-orange-50 rounded-xl flex items-center justify-center text-orange-600 mb-4 group-hover:scale-110 transition-transform">
                                            <i class="fas fa-file-invoice-dollar text-xl"></i>
                                        </div>
                                        <span class="text-sm font-bold text-gray-800 uppercase tracking-wider">Check
                                            Bill</span>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/logout"
                                        class="bg-white p-6 rounded-2xl shadow-sm border border-red-100 hover:border-red-500 hover:shadow-md transition-all group flex flex-col items-center text-center">
                                        <div
                                            class="w-12 h-12 bg-red-50 rounded-xl flex items-center justify-center text-red-600 mb-4 group-hover:scale-110 transition-transform">
                                            <i class="fas fa-sign-out-alt text-xl"></i>
                                        </div>
                                        <span
                                            class="text-sm font-bold text-gray-800 uppercase tracking-wider">Logout</span>
                                    </a>
                                </div>

                                <!-- My Bookings Section -->
                                <div id="my-reservations">
                                    <h4 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
                                        <i class="fas fa-calendar-check text-brand-500 mr-2"></i> My Recent Bookings
                                    </h4>

                                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                        <c:forEach var="res" items="${myReservations}">
                                            <div
                                                class="bg-white rounded-xl shadow-sm border border-gray-100 p-5 hover:shadow-md transition-shadow">
                                                <div class="flex justify-between items-start mb-4">
                                                    <div>
                                                        <span
                                                            class="text-xs font-bold text-gray-400 uppercase tracking-tighter">Booking
                                                            ID</span>
                                                        <p class="font-bold text-brand-900">${res.reservationId}</p>
                                                    </div>
                                                    <span
                                                        class="px-3 py-1 rounded-full text-[10px] font-black uppercase tracking-widest 
                                        ${res.status == 'CONFIRMED' ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-500'}">
                                                        ${res.status}
                                                    </span>
                                                </div>

                                                <div class="grid grid-cols-2 gap-4 mb-4">
                                                    <div class="flex items-center">
                                                        <div
                                                            class="w-8 h-8 rounded-lg bg-blue-50 flex items-center justify-center text-blue-500 mr-3">
                                                            <i class="fas fa-sign-in-alt text-xs"></i>
                                                        </div>
                                                        <div>
                                                            <p class="text-[10px] text-gray-400 uppercase font-bold">
                                                                Check-In</p>
                                                            <p class="text-sm font-bold text-gray-700">
                                                                ${res.checkInDate}</p>
                                                        </div>
                                                    </div>
                                                    <div class="flex items-center">
                                                        <div
                                                            class="w-8 h-8 rounded-lg bg-red-50 flex items-center justify-center text-red-500 mr-3">
                                                            <i class="fas fa-sign-out-alt text-xs"></i>
                                                        </div>
                                                        <div>
                                                            <p class="text-[10px] text-gray-400 uppercase font-bold">
                                                                Check-Out</p>
                                                            <p class="text-sm font-bold text-gray-700">
                                                                ${res.checkOutDate}</p>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div
                                                    class="border-t border-gray-50 pt-4 flex justify-between items-center">
                                                    <p class="text-sm text-gray-500">
                                                        <i class="fas fa-bed mr-1"></i> ${res.roomType}
                                                    </p>
                                                    <p class="font-black text-brand-700">LKR ${res.totalAmount}</p>
                                                </div>

                                                <c:if test="${res.status == 'CONFIRMED'}">
                                                    <div class="mt-4 flex flex-wrap gap-2 justify-end">
                                                        <a href="${pageContext.request.contextPath}/billing?reservationId=${res.reservationId}"
                                                            class="text-xs font-bold text-brand-600 hover:text-brand-800 border border-brand-200 hover:border-brand-300 px-3 py-1.5 rounded-lg transition-all flex items-center">
                                                            <i class="fas fa-file-invoice mr-1.5"></i> View Invoice
                                                        </a>
                                                        <form
                                                            action="${pageContext.request.contextPath}/reservation/cancel"
                                                            method="POST"
                                                            onsubmit="return confirm('Are you sure you want to cancel this booking?');">
                                                            <input type="hidden" name="reservationId"
                                                                value="${res.reservationId}">
                                                            <input type="hidden" name="roomId" value="${res.roomId}">
                                                            <button type="submit"
                                                                class="text-xs font-bold text-red-500 hover:text-red-700 border border-red-200 hover:border-red-300 px-3 py-1.5 rounded-lg transition-all flex items-center">
                                                                <i class="fas fa-times-circle mr-1.5"></i> Cancel
                                                            </button>
                                                        </form>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:forEach>

                                        <c:if test="${empty myReservations}">
                                            <div
                                                class="col-span-full py-12 bg-white rounded-xl border-2 border-dashed border-gray-200 flex flex-col items-center justify-center grayscale opacity-60">
                                                <div class="text-5xl mb-4 text-gray-300">📅</div>
                                                <p class="text-gray-500 font-medium">No active reservations found.</p>
                                                <p class="text-gray-400 text-sm">Start by booking your first room!</p>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>

                                <!-- Loyalty Stats -->
                                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                                    <div
                                        class="bg-white rounded-xl shadow-sm border border-gray-100 p-6 flex items-center">
                                        <div
                                            class="w-12 h-12 bg-orange-50 rounded-2xl flex items-center justify-center text-orange-500 mr-4 shadow-inner">
                                            <i class="fas fa-moon text-xl"></i>
                                        </div>
                                        <div>
                                            <p class="text-[10px] font-black text-gray-400 uppercase tracking-widest">
                                                Total Nights Stayed</p>
                                            <p class="text-2xl font-black text-gray-800">${customerStats.totalNights !=
                                                null ? customerStats.totalNights : 0}</p>
                                        </div>
                                    </div>
                                    <div
                                        class="bg-white rounded-xl shadow-sm border border-gray-100 p-6 flex items-center">
                                        <div
                                            class="w-12 h-12 bg-brand-50 rounded-2xl flex items-center justify-center text-brand-600 mr-4 shadow-inner">
                                            <i class="fas fa-wallet text-xl"></i>
                                        </div>
                                        <div>
                                            <p class="text-[10px] font-black text-gray-400 uppercase tracking-widest">
                                                Total Loyalty Spend</p>
                                            <p class="text-2xl font-black text-gray-800">LKR ${customerStats.totalSpent
                                                != null ? customerStats.totalSpent : '0.00'}</p>
                                        </div>
                                    </div>
                                </div>

                                <!-- Account Overview -->
                                <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                                    <div class="px-6 py-4 border-b border-gray-50 bg-gray-50/50">
                                        <h5
                                            class="text-sm font-bold text-gray-700 uppercase tracking-widest flex items-center">
                                            <i class="fas fa-id-card mr-2 text-brand-500"></i> Account Overview
                                        </h5>
                                    </div>
                                    <div class="p-6 grid grid-cols-1 md:grid-cols-3 gap-6">
                                        <div class="space-y-1">
                                            <p class="text-[10px] font-black text-gray-400 uppercase">Username</p>
                                            <p class="text-sm font-bold text-gray-800">${sessionScope.loggedInUser}</p>
                                        </div>
                                        <div class="space-y-1">
                                            <p class="text-[10px] font-black text-gray-400 uppercase">Guest Profile</p>
                                            <p class="text-sm font-bold text-gray-800">
                                                Linked <i class="fas fa-link text-brand-500 ml-1"></i>
                                            </p>
                                        </div>
                                        <div class="space-y-1">
                                            <p class="text-[10px] font-black text-gray-400 uppercase">Membership Role
                                            </p>
                                            <span
                                                class="inline-flex items-center px-2 py-0.5 rounded text-xs font-bold bg-brand-100 text-brand-700">
                                                ${sessionScope.loggedInRole}
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <!-- Support Card -->
                                <div class="bg-gray-800 rounded-xl p-6 text-white flex items-center justify-between">
                                    <div class="flex items-center">
                                        <div class="bg-brand-500 p-3 rounded-lg mr-4">
                                            <i class="fas fa-headset text-xl"></i>
                                        </div>
                                        <div>
                                            <h5 class="font-bold">Need Assistance?</h5>
                                            <p class="text-sm text-gray-400">Our support team is available 24/7 for our
                                                valued guests.</p>
                                        </div>
                                    </div>
                                    <div class="flex gap-3">
                                        <button
                                            onclick="alert('Late Check-out request sent! We will notify you via Dashboard.')"
                                            class="px-4 py-2 border border-brand-400 bg-brand-700/30 text-white rounded-lg text-sm font-bold hover:bg-brand-600 transition-colors flex items-center">
                                            <i class="fas fa-clock mr-2"></i> Request Late Check-out
                                        </button>
                                        <button
                                            class="px-4 py-2 border border-gray-600 rounded-lg text-sm hover:bg-gray-700 transition-colors">
                                            Contact Support
                                        </button>
                                    </div>
                                </div>

                            </div>

                        </main>
                    </div>

                </body>

                </html>