package com.oceanview.factory;

import com.oceanview.model.Room;

/**
 * Factory Design Pattern – creates Room objects based on room type.
 * This centralizes room creation logic and makes it easy to add new types.
 */
public class RoomFactory {

    public static Room createRoom(String roomNumber, String roomType) {
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType.toUpperCase());

        switch (roomType.toUpperCase()) {
            case "STANDARD" -> {
                room.setRatePerNight(5000.00);
                room.setStatus("AVAILABLE");
            }
            case "DELUXE" -> {
                room.setRatePerNight(8500.00);
                room.setStatus("AVAILABLE");
            }
            case "SUITE" -> {
                room.setRatePerNight(15000.00);
                room.setStatus("AVAILABLE");
            }
            case "OCEAN_VIEW" -> {
                room.setRatePerNight(20000.00);
                room.setStatus("AVAILABLE");
            }
            default -> throw new IllegalArgumentException("Unknown room type: " + roomType);
        }
        return room;
    }

    public static double getRateForType(String roomType) {
        return switch (roomType.toUpperCase()) {
            case "STANDARD" -> 5000.00;
            case "DELUXE" -> 8500.00;
            case "SUITE" -> 15000.00;
            case "OCEAN_VIEW" -> 20000.00;
            default -> 0.0;
        };
    }
}
