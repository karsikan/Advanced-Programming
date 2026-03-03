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
    <title>Financial Statement - Ocean View Resort</title>
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
        @media print {
            .no-print { display: none !important; }
            body { background: white !important; padding: 0 !important; }
            .print-shadow { box-shadow: none !important; border: 1px solid #eee !important; }
            .glass { background: white !important; backdrop-filter: none !important; }
        }
    </style>
</head>
<body class="bg-gray-50 py-12 px-4 min-h-screen text-gray-800">

    <div class="max-w-4xl mx-auto">
        <!-- Actions -->
        <div class="no-print flex justify-between items-center mb-8 px-4">
            <button onclick="window.history.back()"
                class="text-xs font-black text-gray-400 hover:text-reception-600 uppercase tracking-widest transition-all flex items-center">
                <i class="fas fa-arrow-left mr-2"></i> Return to Command
            </button>
            <button onclick="window.print()"
                class="reception-gradient text-white px-8 py-3 rounded-2xl font-black shadow-xl shadow-reception-500/20 hover:shadow-reception-500/40 transition-all flex items-center text-xs uppercase tracking-widest">
                <i class="fas fa-print mr-2"></i> Print Document
            </button>
        </div>

        <!-- Main Invoice Container -->
        <div class="glass rounded-[3rem] shadow-2xl shadow-gray-200 overflow-hidden border border-white print-shadow relative">
            <!-- Decorative Header -->
            <div class="reception-gradient h-4 w-full"></div>

            <div class="p-12 md:p-20">
                <!-- Branding Header -->
                <div class="flex flex-col md:flex-row justify-between items-start mb-16 gap-8">
                    <div>
                        <div class="flex items-center gap-3 mb-2">
                             <div class="w-12 h-12 reception-gradient rounded-2xl flex items-center justify-center text-2xl text-white shadow-lg">
                                🌊
                             </div>
                             <h1 class="text-3xl font-black text-gray-900 tracking-tighter">OCEAN VIEW</h1>
                        </div>
                        <p class="text-[10px] font-black text-reception-600 uppercase tracking-[0.4em] ml-1">Galle Premium Hospitality</p>
                    </div>
                    <div class="text-right">
                        <h2 class="text-4xl font-black text-gray-100 absolute top-20 right-12 select-none print:hidden">STATEMENT</h2>
                        <div class="relative z-10">
                            <p class="text-[10px] font-black text-gray-400 uppercase tracking-widest mb-1">Financial Identifier</p>
                            <p class="text-xl font-mono font-black text-reception-700">#${reservation.reservationId}</p>
                        </div>
                    </div>
                </div>

                <!-- Stakeholder Grid -->
                <div class="grid grid-cols-1 md:grid-cols-2 gap-16 mb-16">
                    <div class="space-y-4">
                        <p class="text-[10px] font-black text-gray-400 uppercase tracking-widest flex items-center">
                            <i class="fas fa-user-circle mr-2 text-reception-500"></i> Primary Guest
                        </p>
                        <div class="pl-0">
                            <p class="text-2xl font-black text-gray-900 tracking-tight">${reservation.guestName}</p>
                            <p class="text-sm font-bold text-gray-500 mt-1">${reservation.guestContact}</p>
                            <p class="text-xs font-medium text-gray-400 mt-2 max-w-xs leading-relaxed capitalize">${reservation.guestAddress}</p>
                        </div>
                    </div>

                    <div class="space-y-4 md:text-right">
                        <p class="text-[10px] font-black text-gray-400 uppercase tracking-widest flex items-center justify-end">
                             Stay Parameters <i class="fas fa-bed ml-2 text-brand-500"></i>
                        </p>
                        <div class="md:pr-0">
                            <p class="text-lg font-black text-gray-800">${reservation.roomType} Suite</p>
                            <p class="text-sm font-bold text-gray-500 mt-1">Allocation: Room ${reservation.roomNumber}</p>
                            <p class="text-xs font-black text-brand-600 mt-2 uppercase tracking-tighter bg-brand-50 inline-block px-3 py-1 rounded-full">
                                Total Duration: ${nights} Nights
                            </p>
                        </div>
                    </div>
                </div>

                <!-- Ledger Table -->
                <div class="mb-12">
                    <table class="w-full text-left border-collapse">
                        <thead>
                            <tr class="text-[10px] font-black text-gray-400 uppercase tracking-[0.2em] border-b border-gray-100">
                                <th class="pb-6">Description of Services</th>
                                <th class="pb-6 text-center">Operational Interval</th>
                                <th class="pb-6 text-right">Net Value</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-50">
                            <tr>
                                <td class="py-10">
                                    <p class="text-lg font-black text-gray-800 tracking-tight">Accommodation Yield</p>
                                    <p class="text-[10px] text-gray-400 font-bold uppercase mt-2 tracking-widest">
                                        LKR ${reservation.ratePerNight} / Unit Night × ${nights} Intervals
                                    </p>
                                </td>
                                <td class="py-10 text-center">
                                    <div class="inline-flex items-center bg-gray-50 rounded-xl px-4 py-2 border border-gray-100 shadow-sm">
                                        <span class="text-[10px] font-black font-mono text-reception-600">${reservation.checkInDate}</span>
                                        <i class="fas fa-chevron-right mx-3 text-[8px] text-gray-300"></i>
                                        <span class="text-[10px] font-black font-mono text-red-500">${reservation.checkOutDate}</span>
                                    </div>
                                </td>
                                <td class="py-10 text-right">
                                    <p class="text-xl font-black text-gray-900 font-mono tracking-tighter">LKR ${subTotal}</p>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <!-- Financial Summary -->
                <div class="flex flex-col md:flex-row justify-end gap-12 pt-8 border-t border-gray-100">
                    <div class="w-full md:w-80 space-y-4">
                        <div class="flex justify-between items-center text-xs font-bold text-gray-500 uppercase tracking-widest">
                            <span>Transactional Subtotal</span>
                            <span class="text-gray-900 font-mono">LKR ${subTotal}</span>
                        </div>
                        <div class="flex justify-between items-center text-xs font-bold text-gray-500 uppercase tracking-widest">
                            <span>Statutory Tax (10%)</span>
                            <span class="text-gray-900 font-mono">+ LKR ${tax}</span>
                        </div>
                        <div class="flex justify-between items-center text-xs font-bold text-gray-500 uppercase tracking-widest">
                            <span>Service Surcharge (5%)</span>
                            <span class="text-gray-900 font-mono">+ LKR ${serviceCharge}</span>
                        </div>
                        <div class="pt-6 border-t border-gray-200 mt-4 h-24 reception-gradient rounded-3xl p-6 flex justify-between items-center text-white shadow-2xl shadow-reception-500/30">
                            <span class="text-xs font-black uppercase tracking-[0.2em] italic">Total Payable (Net)</span>
                            <span class="text-3xl font-black tracking-tighter font-mono italic">LKR ${totalAmount}</span>
                        </div>
                    </div>
                </div>

                <!-- Authentication Footer -->
                <div class="mt-24 text-center">
                    <div class="flex justify-center gap-12 mb-8 opacity-20 filter grayscale">
                         <div class="w-12 h-12 bg-gray-400 rounded-full"></div>
                         <div class="w-12 h-12 bg-gray-400 rounded-full"></div>
                    </div>
                    <p class="text-[10px] font-black text-gray-300 uppercase tracking-[0.5em] mb-2">Verified Ledger Release</p>
                    <p class="text-[9px] font-bold text-gray-400 max-w-sm mx-auto leading-relaxed">
                        This document serves as an official financial record of Ocean View Resort. 
                        Digital authenticity code: OVR-FIN-${reservation.reservationId}-SECURE
                    </p>
                </div>
            </div>
        </div>

        <p class="no-print text-center text-[10px] font-black text-gray-300 mt-12 uppercase tracking-[0.3em]">
            Generated by Intelligence Subsystem • Ocean View Resort Galle
        </p>
    </div>

</body>
</html>
