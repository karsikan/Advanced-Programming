package com.oceanview.model;

public class Room {

    public enum RoomType {
        STANDARD, DELUXE, SUITE, OCEAN_VIEW
    }

    public enum RoomStatus {
        AVAILABLE, OCCUPIED, MAINTENANCE
    }

    private int id;
    private String roomNumber;
    private String roomType;
    private double ratePerNight;
    private String status;

    public Room() {}

    public Room(int id, String roomNumber, String roomType, double ratePerNight, String status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.ratePerNight = ratePerNight;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public double getRatePerNight() { return ratePerNight; }
    public void setRatePerNight(double ratePerNight) { this.ratePerNight = ratePerNight; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    /** Returns amenities based on room type */
    public String getAmenities() {
        return switch (roomType) {
            case "STANDARD"   -> "Air Conditioning, Wi-Fi, TV";
            case "DELUXE"     -> "Air Conditioning, Wi-Fi, TV, Mini-Bar, Balcony";
            case "SUITE"      -> "Air Conditioning, Wi-Fi, TV, Mini-Bar, Balcony, Jacuzzi, Living Room";
            case "OCEAN_VIEW" -> "Air Conditioning, Wi-Fi, TV, Mini-Bar, Balcony, Jacuzzi, Living Room, Ocean View";
            default -> "Standard Amenities";
        };
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " [" + roomType + "] - LKR " + ratePerNight + "/night - " + status;
    }
}
