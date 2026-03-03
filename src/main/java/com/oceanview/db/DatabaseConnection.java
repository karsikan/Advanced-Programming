package com.oceanview.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton Design Pattern – ensures only ONE database connection exists.
 *
 * Database: MySQL (via phpMyAdmin / WAMP)
 * Advanced Features:
 * - MySQL TRIGGERS for automated business rule enforcement
 * - Audit log table for change tracking
 * - Database VIEW for reporting
 *
 * ===== CONFIGURATION (WAMP Default) =====
 * DB Host : localhost:3306
 * DB Name : oceanview_db
 * DB User : root
 * DB Pass : (empty for WAMP default)
 * =========================
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    // ── MySQL Connection Settings ─────────────────────────────────────────────
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "oceanview_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = ""; // Change if you set a MySQL password

    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
            + "?createDatabaseIfNotExist=true"
            + "&useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=Asia/Colombo"
            + "&characterEncoding=UTF-8";

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            connection.setAutoCommit(true);
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Cannot connect to MySQL.\n" +
                            "Make sure WAMP Server is running, MySQL is started on port 3306,\n" +
                            "and database 'oceanview_db' is created in phpMyAdmin.\n" +
                            "Error: " + e.getMessage(),
                    e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error: " + e.getMessage(), e);
        }
        return connection;
    }

    private void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {

            // ── TABLES ────────────────────────────────────────────────────────
            // users table now includes personal details and status/created_at for auditing
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100),
                        username VARCHAR(50) NOT NULL UNIQUE,
                        email VARCHAR(100),
                        phone VARCHAR(15),
                        password VARCHAR(100) NOT NULL,
                        role VARCHAR(20) NOT NULL DEFAULT 'STAFF',
                        status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                    """);
            // make sure any older installations acquire new columns
            try { stmt.execute("ALTER TABLE users ADD COLUMN name VARCHAR(100)"); } catch (SQLException ignored) {}
            try { stmt.execute("ALTER TABLE users ADD COLUMN email VARCHAR(100)"); } catch (SQLException ignored) {}
            try { stmt.execute("ALTER TABLE users ADD COLUMN phone VARCHAR(15)"); } catch (SQLException ignored) {}
            try { stmt.execute("ALTER TABLE users ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'"); } catch (SQLException ignored) {}
            try { stmt.execute("ALTER TABLE users ADD COLUMN created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP"); } catch (SQLException ignored) {}

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS guests (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        address VARCHAR(200) NOT NULL,
                        contact_number VARCHAR(15) NOT NULL
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS rooms (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        room_number VARCHAR(10) NOT NULL UNIQUE,
                        room_type VARCHAR(30) NOT NULL,
                        rate_per_night DECIMAL(10,2) NOT NULL,
                        status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE'
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS reservations (
                        reservation_id VARCHAR(30) PRIMARY KEY,
                        guest_id INT NOT NULL,
                        user_id INT,
                        room_id INT NOT NULL,
                        check_in_date DATE NOT NULL,
                        check_out_date DATE NOT NULL,
                        total_amount DECIMAL(12,2) DEFAULT 0.00,
                        status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
                        created_at DATETIME NOT NULL,
                        FOREIGN KEY (guest_id) REFERENCES guests(id),
                        FOREIGN KEY (room_id)  REFERENCES rooms(id),
                        FOREIGN KEY (user_id)  REFERENCES users(id)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                    """);

            // Migration: Add user_id column if it doesn't exist
            try {
                stmt.execute("ALTER TABLE reservations ADD COLUMN user_id INT AFTER guest_id");
            } catch (SQLException e) {
                // Ignore if column already exists
            }

            try {
                stmt.execute(
                        "ALTER TABLE reservations ADD CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES users(id)");
            } catch (SQLException e) {
                // Ignore if constraint already exists or column wasn't added
            }

            // Audit log table – tracks all system state changes
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS audit_log (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        action VARCHAR(50) NOT NULL,
                        table_name VARCHAR(30) NOT NULL,
                        record_id VARCHAR(50) NOT NULL,
                        changed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        details TEXT
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                    """);

            // ── MYSQL TRIGGERS ─────────────────────────────────────────────────
            // TRIGGER 1: Auto-mark room OCCUPIED when reservation is created
            stmt.execute("DROP TRIGGER IF EXISTS trg_room_occupy");
            stmt.execute("""
                    CREATE TRIGGER trg_room_occupy
                    AFTER INSERT ON reservations
                    FOR EACH ROW
                    BEGIN
                        UPDATE rooms SET status = 'OCCUPIED' WHERE id = NEW.room_id;
                        INSERT INTO audit_log (action, table_name, record_id, details)
                        VALUES ('ROOM_OCCUPIED', 'rooms', CAST(NEW.room_id AS CHAR),
                                CONCAT('Auto-occupied by reservation ', NEW.reservation_id));
                    END
                    """);

            // TRIGGER 2: Auto-release room when reservation is CANCELLED
            stmt.execute("DROP TRIGGER IF EXISTS trg_room_release_cancel");
            stmt.execute("""
                    CREATE TRIGGER trg_room_release_cancel
                    AFTER UPDATE ON reservations
                    FOR EACH ROW
                    BEGIN
                        IF NEW.status = 'CANCELLED' AND OLD.status != 'CANCELLED' THEN
                            UPDATE rooms SET status = 'AVAILABLE' WHERE id = NEW.room_id;
                            INSERT INTO audit_log (action, table_name, record_id, details)
                            VALUES ('ROOM_RELEASED', 'rooms', CAST(NEW.room_id AS CHAR),
                                    CONCAT('Released by cancellation of ', NEW.reservation_id));
                        END IF;
                    END
                    """);

            // TRIGGER 3: Auto-release room when guest CHECKS OUT
            stmt.execute("DROP TRIGGER IF EXISTS trg_room_release_checkout");
            stmt.execute("""
                    CREATE TRIGGER trg_room_release_checkout
                    AFTER UPDATE ON reservations
                    FOR EACH ROW
                    BEGIN
                        IF NEW.status = 'CHECKED_OUT' AND OLD.status != 'CHECKED_OUT' THEN
                            UPDATE rooms SET status = 'AVAILABLE' WHERE id = NEW.room_id;
                            INSERT INTO audit_log (action, table_name, record_id, details)
                            VALUES ('GUEST_CHECKED_OUT', 'rooms', CAST(NEW.room_id AS CHAR),
                                    CONCAT('Room freed after checkout: ', NEW.reservation_id));
                        END IF;
                    END
                    """);

            // TRIGGER 4: Audit new reservations
            stmt.execute("DROP TRIGGER IF EXISTS trg_audit_new_reservation");
            stmt.execute("""
                    CREATE TRIGGER trg_audit_new_reservation
                    AFTER INSERT ON reservations
                    FOR EACH ROW
                    BEGIN
                        INSERT INTO audit_log (action, table_name, record_id, details)
                        VALUES ('NEW_RESERVATION', 'reservations', NEW.reservation_id,
                                CONCAT('Guest ID: ', NEW.guest_id, ' | Room ID: ', NEW.room_id));
                    END
                    """);

            // ── DATABASE VIEW for reporting ────────────────────────────────────
            stmt.execute("DROP VIEW IF EXISTS v_reservation_report");
            stmt.execute("""
                    CREATE VIEW v_reservation_report AS
                    SELECT r.reservation_id,
                           g.name         AS guest_name,
                           g.contact_number,
                           rm.room_number,
                           rm.room_type,
                           rm.rate_per_night,
                           r.check_in_date,
                           r.check_out_date,
                           DATEDIFF(r.check_out_date, r.check_in_date) AS nights,
                           r.total_amount,
                           r.status,
                           r.created_at
                    FROM reservations r
                    JOIN guests g  ON r.guest_id = g.id
                    JOIN rooms  rm ON r.room_id  = rm.id
                    """);

            // ── SEED DATA ──────────────────────────────────────────────────────
            // seed default accounts with hashed passwords
            String adminHash = org.mindrot.jbcrypt.BCrypt.hashpw("admin123", org.mindrot.jbcrypt.BCrypt.gensalt());
            String staffHash = org.mindrot.jbcrypt.BCrypt.hashpw("staff123", org.mindrot.jbcrypt.BCrypt.gensalt());
            String managerHash = org.mindrot.jbcrypt.BCrypt.hashpw("manager123", org.mindrot.jbcrypt.BCrypt.gensalt());
            String customerHash = org.mindrot.jbcrypt.BCrypt.hashpw("customer123", org.mindrot.jbcrypt.BCrypt.gensalt());
            String seedSql = "INSERT IGNORE INTO users (username, password, role, name, status) VALUES "
                    + "('admin','" + adminHash + "','ADMIN','Administrator','ACTIVE')," 
                    + "('staff','" + staffHash + "','STAFF','Frontdesk Staff','ACTIVE')," 
                    + "('manager','" + managerHash + "','ADMIN','General Manager','ACTIVE')," 
                    + "('customer','" + customerHash + "','CUSTOMER','Demo Customer','ACTIVE')";
            stmt.execute(seedSql);

            stmt.execute("""
                    INSERT IGNORE INTO rooms (room_number, room_type, rate_per_night) VALUES
                    ('101', 'STANDARD',   5000.00), ('102', 'STANDARD',   5000.00), ('103', 'STANDARD',   5000.00),
                    ('104', 'STANDARD',   5000.00), ('105', 'STANDARD',   5000.00), ('106', 'STANDARD',   5000.00),
                    ('107', 'STANDARD',   5000.00), ('108', 'STANDARD',   5000.00), ('109', 'STANDARD',   5000.00),
                    ('110', 'STANDARD',   5000.00),
                    ('201', 'DELUXE',     8500.00), ('202', 'DELUXE',     8500.00), ('203', 'DELUXE',     8500.00),
                    ('204', 'DELUXE',     8500.00), ('205', 'DELUXE',     8500.00), ('206', 'DELUXE',     8500.00),
                    ('207', 'DELUXE',     8500.00), ('208', 'DELUXE',     8500.00), ('209', 'DELUXE',     8500.00),
                    ('210', 'DELUXE',     8500.00),
                    ('301', 'SUITE',     15000.00), ('302', 'SUITE',     15000.00), ('303', 'SUITE',     15000.00),
                    ('304', 'SUITE',     15000.00), ('305', 'SUITE',     15000.00), ('306', 'SUITE',     15000.00),
                    ('307', 'SUITE',     15000.00), ('308', 'SUITE',     15000.00), ('309', 'SUITE',     15000.00),
                    ('310', 'SUITE',     15000.00),
                    ('401', 'OCEAN_VIEW',20000.00), ('402', 'OCEAN_VIEW',20000.00), ('403', 'OCEAN_VIEW',20000.00),
                    ('404', 'OCEAN_VIEW',20000.00), ('405', 'OCEAN_VIEW',20000.00), ('406', 'OCEAN_VIEW',20000.00),
                    ('407', 'OCEAN_VIEW',20000.00), ('408', 'OCEAN_VIEW',20000.00), ('409', 'OCEAN_VIEW',20000.00),
                    ('410', 'OCEAN_VIEW',20000.00)
                    """);

            stmt.execute("""
                    INSERT IGNORE INTO guests (name, address, contact_number) VALUES
                    ('Arjun Krishnan', 'No 45, Sea Street, Colombo', '0771234567'),
                    ('Vethuran Selvaraj', 'No 12, Main Road, Jaffna', '0779876543'),
                    ('Karan Sivakumar', 'Green Lane, Galle', '0714567890'),
                    ('Tharun Rajesh', 'Beach Road, Trincomalee', '0765432109'),
                    ('Prithvi Murugan', 'Temple Road, Kandy', '0701112223')
                    """);

        } catch (SQLException e) {
            throw new RuntimeException("DB initialization error: " + e.getMessage(), e);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing DB: " + e.getMessage());
        }
    }
}
