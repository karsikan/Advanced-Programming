<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Ocean View Resort - Login</title>
        <!-- Include Tailwind CSS via CDN for rapid UI styling -->
        <script src="https://cdn.tailwindcss.com/3.4.1"></script>
        <script>
            tailwind.config = {
                theme: {
                    extend: {
                        colors: {
                            brand: {
                                50: '#f0fdf4',
                                500: '#10b981',
                                700: '#047857',
                                900: '#064e3b',
                            }
                        }
                    }
                }
            }
        </script>
    </head>

    <body
        class="bg-gradient-to-br from-brand-900 to-brand-700 min-h-screen flex flex-col justify-center items-center p-4">

        <!-- Header / Logo -->
        <div class="text-center mb-8">
            <div class="text-6xl mb-4">🌊</div>
            <h1 class="text-3xl font-bold text-white tracking-wide">OCEAN VIEW RESORT</h1>
            <p class="text-brand-50 mt-1">Hotel Management System</p>
        </div>

        <!-- Login Card -->
        <div class="bg-white/95 backdrop-blur-sm rounded-xl shadow-2xl p-8 max-w-md w-full border border-gray-100">

            <form action="${pageContext.request.contextPath}/login" method="POST" class="space-y-6">

                <%-- Success/Error Message Display --%>
                    <% if (request.getAttribute("successMessage") != null) { %>
                        <div class="bg-green-50 border-l-4 border-green-500 p-4 mb-6 rounded">
                            <p class="text-sm text-green-700 font-medium"><%= request.getAttribute("successMessage") %></p>
                        </div>
                    <% } %>
                    <% if (request.getAttribute("errorMessage") !=null) { %>
                        <div class="bg-red-50 border-l-4 border-red-500 p-4 mb-6 rounded">
                            <div class="flex">
                                <div class="flex-shrink-0">
                                    <svg class="h-5 w-5 text-red-400" xmlns="http://www.w3.org/2000/svg"
                                        viewBox="0 0 20 20" fill="currentColor">
                                        <path fill-rule="evenodd"
                                            d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                                            clip-rule="evenodd" />
                                    </svg>
                                </div>
                                <div class="ml-3">
                                    <p class="text-sm text-red-700 font-medium">
                                        <%= request.getAttribute("errorMessage") %>
                                    </p>
                                </div>
                            </div>
                        </div>
                        <% } %>

                            <!-- Username Field -->
                            <div>
                                <label for="username" class="block text-sm font-bold text-gray-700 mb-2">👤
                                    Username</label>
                                <input type="text" id="username" name="username" required
                                    class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-brand-500 focus:border-brand-500 bg-brand-50/50 transition-colors"
                                    placeholder="Enter your username">
                            </div>

                            <!-- Password Field -->
                            <div>
                                <label for="password" class="block text-sm font-bold text-gray-700 mb-2">🔒
                                    Password</label>
                                <input type="password" id="password" name="password" required
                                    class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-brand-500 focus:border-brand-500 bg-brand-50/50 transition-colors"
                                    placeholder="Enter your password">
                            </div>

                            <!-- Submit Button -->
                            <button type="submit"
                                class="w-full bg-brand-700 hover:bg-brand-900 text-white font-bold py-3 px-4 rounded-lg shadow-lg hover:shadow-xl transition-all duration-200 transform hover:-translate-y-0.5 mt-4">
                                LOGIN
                            </button>

                            <!-- Register Link -->
                            <p class="mt-6 text-center text-sm text-gray-600">Don't have an account? <a href="${pageContext.request.contextPath}/register" class="text-brand-700 font-bold hover:underline">Register here</a></p>
            </form>
        </div>

        <!-- Footer -->
        <div class="mt-12 text-center text-brand-50/70 text-sm italic">
            &copy; 2026 Ocean View Resort, Galle, Sri Lanka
        </div>

    </body>

    </html>