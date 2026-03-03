<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ocean View Resort - Register</title>
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
<body class="bg-gradient-to-br from-brand-900 to-brand-700 min-h-screen flex flex-col justify-center items-center p-4">

<div class="text-center mb-8">
    <div class="text-6xl mb-4">🌊</div>
    <h1 class="text-3xl font-bold text-white tracking-wide">OCEAN VIEW RESORT</h1>
    <p class="text-brand-50 mt-1">Hotel Management System</p>
</div>

<div class="bg-white/95 backdrop-blur-sm rounded-xl shadow-2xl p-8 max-w-md w-full border border-gray-100">
    <form action="${pageContext.request.contextPath}/register" method="POST" class="space-y-6">
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="bg-red-50 border-l-4 border-red-500 p-4 mb-6 rounded">
                <p class="text-sm text-red-700 font-medium"><%= request.getAttribute("errorMessage") %></p>
            </div>
        <% } %>

        <div>
            <label for="name" class="block text-sm font-bold text-gray-700 mb-2">Full Name</label>
            <input type="text" id="name" name="name" required
                   class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-brand-500 focus:border-brand-500 bg-brand-50/50 transition-colors"
                   placeholder="e.g. John Doe">
        </div>
        <div>
            <label for="username" class="block text-sm font-bold text-gray-700 mb-2">Username</label>
            <input type="text" id="username" name="username" required
                   class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-brand-500 focus:border-brand-500 bg-brand-50/50 transition-colors"
                   placeholder="Desired username">
        </div>
        <div>
            <label for="email" class="block text-sm font-bold text-gray-700 mb-2">Email</label>
            <input type="email" id="email" name="email" required
                   class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-brand-500 focus:border-brand-500 bg-brand-50/50 transition-colors"
                   placeholder="you@example.com">
        </div>
        <div>
            <label for="phone" class="block text-sm font-bold text-gray-700 mb-2">Phone</label>
            <input type="text" id="phone" name="phone" required
                   class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-brand-500 focus:border-brand-500 bg-brand-50/50 transition-colors"
                   placeholder="+94 77 123 4567">
        </div>
        <div>
            <label for="password" class="block text-sm font-bold text-gray-700 mb-2">Password</label>
            <input type="password" id="password" name="password" required
                   class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-brand-500 focus:border-brand-500 bg-brand-50/50 transition-colors"
                   placeholder="Choose a strong password">
        </div>
        <div>
            <label for="confirmPassword" class="block text-sm font-bold text-gray-700 mb-2">Confirm Password</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required
                   class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-brand-500 focus:border-brand-500 bg-brand-50/50 transition-colors"
                   placeholder="Repeat password">
        </div>

        <button type="submit" class="w-full bg-brand-700 hover:bg-brand-900 text-white font-bold py-3 px-4 rounded-lg shadow-lg hover:shadow-xl transition-all duration-200 transform hover:-translate-y-0.5 mt-4">
            REGISTER
        </button>
        <p class="text-center text-sm mt-2">Already have an account? <a href="${pageContext.request.contextPath}/login" class="text-brand-700 font-bold hover:underline">Login here</a></p>
    </form>
</div>

<div class="mt-12 text-center text-brand-50/70 text-sm italic">
    &copy; 2026 Ocean View Resort, Galle, Sri Lanka
</div>

</body>
</html>