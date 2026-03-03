<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<c:set var="reqUri" value="${pageContext.request.requestURI}" />

<div class="w-72 bg-[#064e3b] text-white flex flex-col shadow-[10px_0_30px_rgba(0,0,0,0.1)] z-20 relative overflow-hidden">
    <!-- Sophisticated Background Pattern -->
    <div class="absolute inset-0 opacity-5 pointer-events-none" style="background-image: radial-gradient(circle at 2px 2px, white 1px, transparent 0); background-size: 24px 24px;"></div>
    
    <!-- Logo Section -->
    <div class="p-10 flex flex-col items-center relative">
        <div class="w-16 h-16 bg-white/10 backdrop-blur-md rounded-[2rem] flex items-center justify-center text-3xl shadow-2xl border border-white/20 mb-4 transform hover:rotate-6 transition-transform cursor-pointer">
            🌊
        </div>
        <h1 class="text-xl font-black tracking-[-0.05em] leading-tight text-center uppercase">Ocean View</h1>
        <div class="flex items-center space-x-2 mt-1">
            <div class="h-[1px] w-4 bg-brand-400"></div>
            <p class="text-[9px] font-black tracking-[0.4em] text-brand-300 uppercase">Resort & Spa</p>
            <div class="h-[1px] w-4 bg-brand-400"></div>
        </div>
    </div>

    <!-- Navigation -->
    <nav class="flex-1 overflow-y-auto px-4 py-6 scrollbar-hide">
        <ul class="space-y-2">
            
            <!-- Core Navigation -->
            <c:set var="isDash" value="${fn:contains(reqUri, '/dashboard')}" />
            <li>
                <c:set var="dashboardUrl" value="${sessionScope.loggedInRole == 'CUSTOMER' ? '/customer/dashboard' : '/dashboard'}" />
                <a href="${pageContext.request.contextPath}${dashboardUrl}"
                    class="group flex items-center px-5 py-3.5 rounded-2xl text-xs font-black uppercase tracking-widest transition-all
                    ${isDash ? 'bg-white text-brand-900 shadow-lg shadow-black/20' : 'text-brand-100/60 hover:bg-white/5 hover:text-white'}">
                    <i class="fas fa-grid-2 w-5 text-lg ${isDash ? 'text-brand-700' : 'text-brand-400/50 group-hover:text-brand-300'}"></i>
                    <span class="ml-4">Overview</span>
                </a>
            </li>

            <!-- Staff/Admin Functional Blocks -->
            <c:if test="${sessionScope.loggedInRole != 'CUSTOMER'}">
                <li class="pt-6 pb-2 px-5">
                    <span class="text-[9px] font-black text-brand-400/50 uppercase tracking-[0.3em]">Operational Desk</span>
                </li>

                <c:set var="isNewRes" value="${fn:contains(reqUri, '/reservation/new')}" />
                <li>
                    <a href="${pageContext.request.contextPath}/reservation/new"
                        class="group flex items-center px-5 py-3.5 rounded-2xl text-xs font-black uppercase tracking-widest transition-all
                        ${isNewRes ? 'bg-white text-brand-900 shadow-lg' : 'text-brand-100/60 hover:bg-white/5 hover:text-white'}">
                        <i class="fas fa-calendar-plus w-5 text-lg ${isNewRes ? 'text-brand-700' : 'text-brand-400/50 group-hover:text-brand-300'}"></i>
                        <span class="ml-4">New Booking</span>
                    </a>
                </li>

                <c:set var="isAllRes" value="${fn:endsWith(reqUri, '/reservations')}" />
                <li>
                    <a href="${pageContext.request.contextPath}/reservations"
                        class="group flex items-center px-5 py-3.5 rounded-2xl text-xs font-black uppercase tracking-widest transition-all
                        ${isAllRes ? 'bg-white text-brand-900 shadow-lg' : 'text-brand-100/60 hover:bg-white/5 hover:text-white'}">
                        <i class="fas fa-book-open w-5 text-lg ${isAllRes ? 'text-brand-700' : 'text-brand-400/50 group-hover:text-brand-300'}"></i>
                        <span class="ml-4">Ledger View</span>
                    </a>
                </li>
            </c:if>

            <!-- Admin Strategic Blocks -->
            <c:if test="${sessionScope.isAdmin}">
                <li class="pt-8 pb-2 px-5">
                    <span class="text-[9px] font-black text-brand-400/50 uppercase tracking-[0.3em]">Strategic Control</span>
                </li>

                <c:set var="isRooms" value="${fn:endsWith(reqUri, '/rooms')}" />
                <li>
                    <a href="${pageContext.request.contextPath}/rooms"
                        class="group flex items-center px-5 py-3.5 rounded-2xl text-xs font-black uppercase tracking-widest transition-all
                        ${isRooms ? 'bg-white text-brand-900 shadow-lg' : 'text-brand-100/60 hover:bg-white/5 hover:text-white'}">
                        <i class="fas fa-door-open w-5 text-lg ${isRooms ? 'text-brand-700' : 'text-brand-400/50 group-hover:text-brand-300'}"></i>
                        <span class="ml-4">Inventory</span>
                    </a>
                </li>

                <c:set var="isRev" value="${fn:endsWith(reqUri, '/revenue')}" />
                <li>
                    <a href="${pageContext.request.contextPath}/revenue"
                        class="group flex items-center px-5 py-3.5 rounded-2xl text-xs font-black uppercase tracking-widest transition-all
                        ${isRev ? 'bg-white text-brand-900 shadow-lg' : 'text-brand-100/60 hover:bg-white/5 hover:text-white'}">
                        <i class="fas fa-analytics w-5 text-lg ${isRev ? 'text-brand-700' : 'text-brand-400/50 group-hover:text-brand-300'}"></i>
                        <span class="ml-4">Intelligence</span>
                    </a>
                </li>

                <c:set var="isUsers" value="${fn:endsWith(reqUri, '/users')}" />
                <li>
                    <a href="${pageContext.request.contextPath}/users"
                        class="group flex items-center px-5 py-3.5 rounded-2xl text-xs font-black uppercase tracking-widest transition-all
                        ${isUsers ? 'bg-white text-brand-900 shadow-lg' : 'text-brand-100/60 hover:bg-white/5 hover:text-white'}">
                        <i class="fas fa-shield-check w-5 text-lg ${isUsers ? 'text-brand-700' : 'text-brand-400/50 group-hover:text-brand-300'}"></i>
                        <span class="ml-4">Identities</span>
                    </a>
                </li>
            </c:if>

            <!-- Customer Personal Blocks -->
            <c:if test="${sessionScope.loggedInRole == 'CUSTOMER'}">
                <li class="pt-6 pb-2 px-5">
                    <span class="text-[9px] font-black text-brand-400/50 uppercase tracking-[0.3em]">Personal Suite</span>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/reservation/new"
                        class="group flex items-center px-5 py-3.5 rounded-2xl text-xs font-black uppercase tracking-widest transition-all text-brand-100/60 hover:bg-white/5 hover:text-white">
                        <i class="fas fa-concierge-bell w-4 text-brand-400/50 group-hover:text-brand-300"></i>
                        <span class="ml-4">Instant Book</span>
                    </a>
                </li>
            </c:if>
        </ul>
    </nav>

    <!-- Profile & Session Termination -->
    <div class="p-6 bg-black/10 border-t border-white/5">
        <div class="flex items-center space-x-4 mb-6">
            <div class="w-12 h-12 rounded-2xl bg-white/10 flex items-center justify-center font-black text-brand-300 border border-white/5 shadow-inner">
                ${fn:substring(sessionScope.loggedInUser, 0, 1).toUpperCase()}
            </div>
            <div class="overflow-hidden">
                <p class="text-xs font-black tracking-tight truncate">${sessionScope.loggedInUser}</p>
                <div class="flex items-center space-x-1.5">
                    <div class="w-1.5 h-1.5 rounded-full bg-brand-400 animate-pulse"></div>
                    <p class="text-[9px] font-bold text-brand-300/60 uppercase tracking-widest truncate">${sessionScope.loggedInRole}</p>
                </div>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/logout"
            class="group w-full flex items-center justify-center px-4 py-3.5 bg-white/5 hover:bg-red-500 rounded-2xl transition-all duration-300">
            <i class="fas fa-power-off text-xs text-brand-400 group-hover:text-white transition-colors"></i>
            <span class="ml-3 text-[10px] font-black uppercase tracking-[0.2em] text-brand-100/60 group-hover:text-white transition-colors">Terminate Session</span>
        </a>
    </div>
</div>
