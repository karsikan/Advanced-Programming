# 🌊 Ocean View Resort – Hotel Reservation System

[![GitHub](https://img.shields.io/badge/GitHub-Repository-blue?logo=github)](https://github.com/karsikan/Advanced-Programming)

A high-fidelity Java Swing desktop application for managing hotel reservations at Ocean View Resort, Galle, Sri Lanka. This version features a **Triple-Dashboard System** (Admin, Staff, Customer) and a robust **MySQL Backend**.

## 🚀 Key Features (v2.1)
- **Triple-Dashboard Architecture**: Dedicated interfaces for Admin, Staff, and Customers.
- **Customer Self-Registration**: New guests can create accounts directly from the login screen.
- **MySQL Automation**: 4 custom database triggers for room status updates and audit logging.
- **RESTful Web Service**: Built-in HTTP server for JSON report generation (Distributed Component).
- **Premium UI Overhaul**: Consistent "Ocean Blue" theme with micro-animations and responsive validation.
- **Data Isolation**: Strict RBAC (Role-Based Access Control) for guest privacy.

## 🏢 3-Tier Architecture
- **Presentation**: Java Swing (com.oceanview.ui)
- **Business Logic**: Service Layer (com.oceanview.service)
- **Data Access**: DAO Layer (com.oceanview.dao) + MySQL

---

## 🖥 How to Use the 3 Dashboards

### 1. Admin Dashboard (`admin` / `admin123`)
- **Login**: Use the admin credentials to gain full system access.
- **User Management**: Add, update, or delete staff and customer accounts.
- **Room Management**: Edit room rates, amenities, and inventory.
- **Revenue Analytics**: View live charts and export revenue reports to CSV.

### 2. Staff Dashboard (`staff` / `staff123`)
- **Reservations**: Create new bookings for walk-in guests.
- **Search**: Find reservations by ID or Name.
- **Billing**: Generate and print professional invoices with tax calculations.

### 3. Customer Dashboard (Register New Account)
- **Registration**: Click "Register Here" on the login screen to create a personal profile.
- **Book Room**: Browse available rooms and book directly (details are pre-filled).
- **My Reservations**: View personal booking history and current status.
- **Check Bill**: View and download personal stay invoices.

---

## 🏃 Getting Started (Web Application)

### Prerequisites
- **Java 17+** (JDK)
- **Apache Tomcat 9.0+**
- **MySQL** (via XAMPP or standalone)
- **Apache NetBeans IDE**

### Database Setup
1. Start **MySQL** in XAMPP.
2. Create `oceanview_db` in PHPMyAdmin.
3. Import the SQL script provided in the `verification_guide.md`.

### Run via NetBeans & Tomcat
1. File -> Open Project -> Select `OceanViewResort`.
2. Right-click the project -> **Properties** -> **Run**.
3. Set the Server to **Apache Tomcat**.
4. Right-click the project -> **Run**. NetBeans will automatically build the WAR file, deploy it to Tomcat, and open the system in your default browser at `http://localhost:8080/OceanViewResort/`.

## 📂 Project Structure
```
OceanViewResort/
├── .github/             # GitHub workflow configs
├── src/main/java/com/oceanview/
│   ├── ui/              # Screen navigation & UI components
│   ├── service/         # Business logic & Web services
│   ├── dao/             # Data Access Objects (SQL)
│   ├── model/           # Entity classes (User, Guest, Room)
│   └── Main.java        # Main App Launcher
└── revenue_report.csv   # Auto-generated CSV reports
```

## 📜 Academic Reference
GitHub Repository: [https://github.com/karsikan/Advanced-Programming](https://github.com/karsikan/Advanced-Programming)
Developed for **Ocean View Resort, Galle, Sri Lanka** © 2026.
