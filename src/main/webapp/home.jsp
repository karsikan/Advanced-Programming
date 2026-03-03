<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ocean View Resort - Welcome</title>
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
<body class="bg-gradient-to-br from-brand-900 to-brand-700 min-h-screen flex flex-col justify-center items-center p-4 text-center text-white">

    <h1 class="text-5xl font-black mb-4">Welcome to Ocean View Resort</h1>
    <p class="mb-8 text-lg">Experience luxury hotel reservation at your fingertips.</p>

    <div class="space-x-4">
        <a href="${pageContext.request.contextPath}/login" class="bg-white/90 text-brand-900 font-bold py-3 px-6 rounded-lg shadow-lg hover:shadow-xl transition">Login</a>
        <a href="${pageContext.request.contextPath}/register" class="bg-brand-500 hover:bg-brand-700 text-white font-bold py-3 px-6 rounded-lg shadow-lg hover:shadow-xl transition">Register</a>
    </div>

    <footer class="mt-12 text-sm italic text-brand-50/70">&copy; 2026 Ocean View Resort, Galle, Sri Lanka</footer>
</body>
</html>