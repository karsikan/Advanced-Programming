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
    <title>${isEdit ? 'Update Booking' : 'Acquisition Flow'} - Ocean View Resort</title>
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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
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
        .input-focus {
            transition: all 0.3s ease;
        }
        .input-focus:focus {
            box-shadow: 0 0 0 4px rgba(14, 165, 233, 0.1);
            border-color: #0ea5e9;
            background: white;
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
                    <i class="fas ${isEdit ? 'fa-pen-nib' : 'fa-calendar-plus'} text-reception-600 mr-3"></i>
                    ${isEdit ? 'Modification Suite' : 'Operational Intake'}
                </h2>
                <c:if test="${isEdit}">
                    <p class="text-[10px] font-bold text-reception-600 uppercase tracking-widest mt-0.5">Adjusting Active Ledger: ${reservation.reservationId}</p>
                </c:if>
                <c:if test="${!isEdit}">
                    <p class="text-[10px] font-bold text-gray-400 uppercase tracking-widest mt-0.5">Provisioning new guest placement</p>
                </c:if>
            </div>
        </header>

        <main class="flex-1 overflow-auto bg-[radial-gradient(#e5e7eb_1px,transparent_1px)] [background-size:16px_16px] p-8 flex justify-center">

            <div class="w-full max-w-4xl">
                 <form action="${pageContext.request.contextPath}/reservation/${isEdit ? 'update' : 'new'}" method="POST" class="space-y-8">
                    
                    <c:if test="${isEdit}">
                        <input type="hidden" name="reservationId" value="${reservation.reservationId}">
                    </c:if>

                    <c:if test="${not empty errorMessage}">
                        <div class="p-6 rounded-3xl glass border-l-4 border-red-500 shadow-xl flex items-center animate-in fade-in slide-in-from-top-2 duration-300">
                            <div class="w-10 h-10 bg-red-50 rounded-xl flex items-center justify-center text-red-600 mr-4">
                                <i class="fas fa-exclamation-triangle"></i>
                            </div>
                            <p class="text-sm font-bold text-gray-800">${errorMessage}</p>
                        </div>
                    </c:if>

                    <div class="grid grid-cols-1 lg:grid-cols-12 gap-8">
                        
                        <!-- Left Pillar: Guest Intelligence -->
                        <div class="lg:col-span-7 space-y-8">
                            <div class="glass p-8 rounded-[2.5rem] shadow-2xl shadow-gray-200/50 border border-white">
                                <h3 class="text-xs font-black text-gray-400 uppercase tracking-[0.3em] mb-8 flex items-center">
                                    <div class="w-2 h-2 bg-reception-500 rounded-full mr-3"></div>
                                    Identity Profile
                                </h3>
                                
                                <div class="space-y-6">
                                    <div>
                                        <label class="block text-[10px] font-black text-gray-400 uppercase tracking-widest mb-2 ml-1">Guest Designation</label>
                                        <div class="relative">
                                            <i class="fas fa-user absolute left-4 top-1/2 -translate-y-1/2 text-gray-300"></i>
                                            <input type="text" name="guestName" required placeholder="Full Legal Name"
                                                value="${isEdit ? reservation.guestName : preFillGuest.name}" ${isEdit ? 'disabled' : ''}
                                                class="input-focus w-full pl-12 pr-4 py-3 bg-gray-50/50 border border-gray-100 rounded-2xl text-sm font-bold ${isEdit ? 'opacity-50 cursor-not-allowed' : ''}">
                                        </div>
                                    </div>

                                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                                        <div>
                                            <label class="block text-[10px] font-black text-gray-400 uppercase tracking-widest mb-2 ml-1">Communication Ref</label>
                                            <div class="relative">
                                                <i class="fas fa-phone absolute left-4 top-1/2 -translate-y-1/2 text-gray-300"></i>
                                                <input type="text" name="contact" required placeholder="10-Digit Secure Line"
                                                    value="${isEdit ? reservation.guestContact : preFillGuest.contactNumber}"
                                                    pattern="\d{10}" ${isEdit ? 'disabled' : ''}
                                                    class="input-focus w-full pl-12 pr-4 py-3 bg-gray-50/50 border border-gray-100 rounded-2xl text-sm font-bold ${isEdit ? 'opacity-50 cursor-not-allowed' : ''}">
                                            </div>
                                        </div>
                                        <div>
                                            <label class="block text-[10px] font-black text-gray-400 uppercase tracking-widest mb-2 ml-1">Domicile Proof</label>
                                            <div class="relative">
                                                <i class="fas fa-map-marker-alt absolute left-4 top-1/2 -translate-y-1/2 text-gray-300"></i>
                                                <input type="text" name="address" required value="${preFillGuest.address}" placeholder="Residential Address"
                                                    class="input-focus w-full pl-12 pr-4 py-3 bg-gray-50/50 border border-gray-100 rounded-2xl text-sm font-bold">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Timeline Module -->
                            <div class="glass p-8 rounded-[2.5rem] shadow-2xl shadow-gray-200/50 border border-white">
                                <h3 class="text-xs font-black text-gray-400 uppercase tracking-[0.3em] mb-8 flex items-center">
                                    <div class="w-2 h-2 bg-reception-500 rounded-full mr-3"></div>
                                    Temporal Parameters
                                </h3>
                                
                                <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
                                    <div>
                                        <label class="block text-[10px] font-black text-gray-400 uppercase tracking-widest mb-2 ml-1">Check-In Cycle</label>
                                        <div class="relative">
                                            <i class="fas fa-calendar-check absolute left-4 top-1/2 -translate-y-1/2 text-reception-500"></i>
                                            <input type="text" name="checkIn" id="checkIn" required placeholder="Select Date"
                                                value="${isEdit ? reservation.checkInDate : ''}"
                                                class="datepicker w-full pl-12 pr-4 py-3 bg-gray-50/50 border border-gray-100 rounded-2xl text-sm font-bold cursor-pointer hover:bg-white transition-all">
                                        </div>
                                    </div>
                                    <div>
                                        <label class="block text-[10px] font-black text-gray-400 uppercase tracking-widest mb-2 ml-1">Check-Out Cycle</label>
                                        <div class="relative">
                                            <i class="fas fa-calendar-day absolute left-4 top-1/2 -translate-y-1/2 text-red-500"></i>
                                            <input type="text" name="checkOut" id="checkOut" required placeholder="Select Date"
                                                value="${isEdit ? reservation.checkOutDate : ''}"
                                                class="datepicker w-full pl-12 pr-4 py-3 bg-gray-50/50 border border-gray-100 rounded-2xl text-sm font-bold cursor-pointer hover:bg-white transition-all">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Right Pillar: Placement & Amenities -->
                        <div class="lg:col-span-5 space-y-8">
                            <div class="glass p-8 rounded-[2.5rem] shadow-2xl shadow-gray-200/50 border border-white">
                                <h3 class="text-xs font-black text-gray-400 uppercase tracking-[0.3em] mb-8 flex items-center">
                                    <div class="w-2 h-2 bg-reception-500 rounded-full mr-3"></div>
                                    Asset Allocation
                                </h3>
                                
                                <div class="space-y-6">
                                    <label class="block text-[10px] font-black text-gray-400 uppercase tracking-widest mb-2 ml-1">Operational Unit (Room)</label>
                                    <select name="roomId" id="roomId" required onchange="updateAmenities()" ${isEdit ? 'disabled' : ''}
                                        class="w-full px-6 py-4 bg-gray-50/50 border border-gray-100 rounded-2xl text-sm font-black text-gray-900 appearance-none cursor-pointer focus:ring-2 focus:ring-reception-500 transition-all ${isEdit ? 'opacity-50' : ''}">
                                        <c:if test="${!isEdit}">
                                            <option value="" disabled selected>-- Initialize Selection --</option>
                                            <c:forEach var="room" items="${availableRooms}">
                                                <option value="${room.id}" data-type="${room.roomType}">
                                                    Room ${room.roomNumber} - ${room.roomType} (LKR ${room.ratePerNight})
                                                </option>
                                            </c:forEach>
                                        </c:if>
                                        <c:if test="${isEdit}">
                                            <option value="${reservation.roomId}" selected>
                                                Unit ${reservation.roomNumber} - ${reservation.roomType}
                                            </option>
                                        </c:if>
                                    </select>

                                    <div id="amenitiesBox" class="hidden animate-in fade-in duration-500 scale-95 origin-top">
                                        <div class="p-6 rounded-3xl bg-reception-900/5 border border-reception-100 relative overflow-hidden">
                                            <i class="fas fa-sparkles absolute -right-6 -bottom-6 text-7xl text-reception-200/20 rotate-12"></i>
                                            <p class="text-[9px] font-black text-reception-700 uppercase tracking-widest mb-4">Unit Features</p>
                                            <ul id="amenitiesList" class="space-y-2.5">
                                                <!-- Dynamic Content -->
                                            </ul>
                                        </div>
                                    </div>

                                    <c:if test="${empty availableRooms && !isEdit}">
                                        <div class="p-5 rounded-2xl bg-red-50 text-red-700 border border-red-100 flex items-center">
                                            <i class="fas fa-ban mr-3"></i>
                                            <p class="text-[10px] font-black uppercase tracking-widest">Inventory Depleted</p>
                                        </div>
                                    </c:if>
                                </div>
                            </div>

                            <!-- Execution Actions -->
                            <div class="flex flex-col space-y-4">
                                <button type="submit"
                                    class="w-full reception-gradient p-5 rounded-3xl text-sm font-black text-white uppercase tracking-[0.2em] shadow-xl shadow-reception-500/30 hover:shadow-reception-500/50 transition-all transform hover:-translate-y-1 flex items-center justify-center">
                                    <i class="fas ${isEdit ? 'fa-save' : 'fa-check-double'} mr-3"></i>
                                    ${isEdit ? 'Commit Changes' : 'Execute Acquisition'}
                                </button>
                                <a href="${pageContext.request.contextPath}/dashboard"
                                    class="w-full text-center p-5 rounded-3xl text-xs font-black text-gray-400 uppercase tracking-widest hover:text-gray-900 transition-colors">
                                    Abort Procedure
                                </a>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </main>
    </div>

    <script>
        flatpickr(".datepicker", {
            dateFormat: "Y-m-d",
            minDate: "today",
            theme: "material_blue"
        });

        const amenities = {
            'STANDARD': ['Single/Twin Configuration', 'Climate Control (AC/Fan)', 'Secured WiFi', 'En-suite Sanitation', 'Multi-channel Media'],
            'DELUXE': ['King Formation', 'Direct Ocean Trajectory', 'Premium Mini-Bar', 'Fiber-Optic Connectivity', '4K Smart Media Hub', 'Tactical Balcony'],
            'SUITE': ['Panoramic Living Zone', '360° View Horizon', 'Thermal Hydro-Unit (Jacuzzi)', 'Dedicated Butler Node', 'Artisan Toiletries', 'Specialty Brew Hub'],
            'OCEAN_VIEW': ['Direct Marine Horizon', 'High-Latitude Placement', 'Soundproof Encapsulation', 'Premium Mini-Bar']
        };

        function updateAmenities() {
            const select = document.getElementById('roomId');
            const box = document.getElementById('amenitiesBox');
            const list = document.getElementById('amenitiesList');
            const selectedOption = select.options[select.selectedIndex];

            if (!selectedOption || selectedOption.value === "") {
                box.classList.add('hidden');
                return;
            }

            let roomType = selectedOption.getAttribute('data-type');
            roomType = roomType ? roomType.trim().toUpperCase() : "";

            let match = null;
            for (let key in amenities) {
                if (roomType.includes(key)) {
                    match = amenities[key];
                    break;
                }
            }

            if (match) {
                box.classList.remove('hidden');
                list.innerHTML = match.map(item => `
                    <li class="flex items-center text-[10px] font-bold text-gray-700">
                        <div class="w-4 h-4 rounded-full bg-reception-500 flex items-center justify-center text-[8px] text-white mr-3 shadow-sm">
                            <i class="fas fa-check"></i>
                        </div>
                        <span class="uppercase tracking-tight">\${item}</span>
                    </li>
                `).join('');
            } else {
                box.classList.add('hidden');
            }
        }
        document.addEventListener('DOMContentLoaded', updateAmenities);
    </script>
</body>
</html>
