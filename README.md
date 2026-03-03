# 🌊 Ocean View Resort – Hotel Reservation System

A Java Swing desktop application for managing hotel reservations at Ocean View Resort, Galle, Sri Lanka.

## Features
- 🔐 User Authentication (Admin & Staff roles)
- 📋 Add New Reservations with full validation
- 🔍 View & Search Reservation Details
- 🧾 Calculate & Print Bill (with 10% tax + 5% service charge)
- 📊 All Reservations Report
- ❓ Help Section

## Architecture
- **3-Tier**: Presentation (Swing UI) → Business Logic (Services) → Data Access (DAO + SQLite)
- **Design Patterns**: Singleton (DB), DAO, Factory (Rooms), Observer
- **Database**: SQLite (embedded, no server required)

## Room Types & Rates
| Room Type  | Rate/Night (LKR) | Amenities |
|-----------|-----------------|-----------|
| Standard   | 5,000           | AC, Wi-Fi, TV |
| Deluxe     | 8,500           | + Mini-Bar, Balcony |
| Suite      | 15,000          | + Jacuzzi, Living Room |
| Ocean View | 20,000          | + Panoramic Ocean View |

## Default Login Credentials
| Username | Password | Role  |
|----------|----------|-------|
| admin    | admin123 | ADMIN |
| staff    | staff123 | STAFF |

## How to Run

### Prerequisites
- Java 17+
- Apache Maven 3.8+
- Apache NetBeans 23

### Run in NetBeans
1. Open NetBeans → File → Open Project
2. Select `OceanViewResort` folder
3. Right-click project → Run

### Run via Maven
```bash
mvn compile exec:java -Dexec.mainClass="com.oceanview.Main"
```

### Run Tests
```bash
mvn test
```

## Project Structure
```
OceanViewResort/
├── pom.xml
├── src/
│   ├── main/java/com/oceanview/
│   │   ├── Main.java                   # Entry point
│   │   ├── db/DatabaseConnection.java  # Singleton DB
│   │   ├── model/                      # Domain models
│   │   ├── dao/                        # Data Access Layer
│   │   ├── service/                    # Business Logic
│   │   ├── ui/                         # Java Swing frames
│   │   ├── factory/RoomFactory.java    # Factory pattern
│   │   └── util/                       # Validator, DateUtil
│   └── test/java/                      # JUnit 5 tests
└── hotel.db                            # SQLite database (auto-created)
```

## Version History
- **v1.0** – Initial release: Login, Add Reservation, View, Bill
- **v1.1** – Added All Reservations Report & Help Section
- **v1.2** – Improved UI styling, input validation, and test coverage

## License
Academic project – Ocean View Resort, Galle, Sri Lanka © 2024
