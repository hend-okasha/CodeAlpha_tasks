import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Hotel Management System
 * Handles room inventory and booking operations
 *
 * @author Hend Okasha
 */
public class Hotel {
    private List<Room> rooms;
    private List<Booking> bookings;
    private final String roomsFile = "rooms.txt";
    private final String bookingsFile = "bookings.txt";

    public Hotel() {
        rooms = new ArrayList<>();
        bookings = new ArrayList<>();
        loadRooms();
        loadBookings();
    }

    public void addRoom(Room room) {
        rooms.add(room);
        saveRooms();
    }

    public List<Room> searchAvailableRooms(String category, LocalDate checkIn, LocalDate checkOut) {
        List<Room> availableRooms = new ArrayList<>();
        LocalDate today = LocalDate.now();

        if (checkIn.isBefore(today)) {
            System.out.println("Cannot search rooms in the past!");
            return availableRooms;
        }

        if (!checkOut.isAfter(checkIn)) {
            System.out.println("Check-out must be after check-in.");
            return availableRooms;
        }

        for (Room room : rooms) {
            if (!room.getCategory().name().equalsIgnoreCase(category)) {
                continue;
            }
            if (isRoomAvailableForDates(room.getRoomNumber(), checkIn, checkOut)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public Room getRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    public boolean isRoomAvailableForDates(int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        for (Booking booking : bookings) {
            if (booking.getRoomNumber() != roomNumber) {
                continue;
            }

            LocalDate bookingCheckIn = booking.getCheckInDate();
            LocalDate bookingCheckOut = booking.getCheckOutDate();

            if (checkIn.isBefore(bookingCheckOut) && checkOut.isAfter(bookingCheckIn)) {
                return false;
            }
        }
        return true;
    }

    public Booking makeBooking(String customerName, int roomNumber, LocalDate checkInDate, LocalDate checkOutDate) {
        Room room = getRoomByNumber(roomNumber);
        LocalDate today = LocalDate.now();

        if (checkInDate.isBefore(today)) {
            System.out.println("Check-in date cannot be in the past!");
            return null;
        }

        if (room == null) {
            System.out.println("Room " + roomNumber + " not found.");
            return null;
        }

        if (!checkOutDate.isAfter(checkInDate)) {
            System.out.println("Check-out must be after check-in.");
            return null;
        }

        if (!isRoomAvailableForDates(roomNumber, checkInDate, checkOutDate)) {
            System.out.println("Room " + roomNumber + " is not available for those dates.");
            return null;
        }

        int bookingId = bookings.stream()
                .map(Booking::getBookingId)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;

        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        double amount = nights * room.getPricePerNight();

        Booking booking = new Booking(bookingId, customerName, roomNumber,
                room.getCategory(), checkInDate, checkOutDate, amount);
        bookings.add(booking);

        saveBookings();
        saveRooms();
        return booking;
    }

    public boolean cancelBooking(int bookingId) {
        Booking foundBooking = null;

        for (Booking booking : bookings) {
            if (booking.getBookingId() == bookingId) {
                foundBooking = booking;
                break;
            }
        }

        if (foundBooking == null) {
            return false;
        }

        bookings.remove(foundBooking);

        Room room = getRoomByNumber(foundBooking.getRoomNumber());
        if (room != null) {
            boolean stillBookedToday = false;
            LocalDate today = LocalDate.now();

            for (Booking booking : bookings) {
                if (booking.getRoomNumber() == room.getRoomNumber()) {
                    LocalDate bookingCheckIn = booking.getCheckInDate();
                    LocalDate bookingCheckOut = booking.getCheckOutDate();

                    if (!today.isBefore(bookingCheckIn) && today.isBefore(bookingCheckOut)) {
                        stillBookedToday = true;
                        break;
                    }
                }
            }

            if (!stillBookedToday) {
                room.setAvailable(true);
            }
        }

        saveBookings();
        saveRooms();
        return true;
    }

    public Booking viewBooking(int bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId() == bookingId) {
                return booking;
            }
        }
        return null;
    }

    private void loadRooms() {
        rooms.clear();
        File file = new File(roomsFile);

        if (!file.exists()) {
            System.out.println("rooms.txt not found — starting with empty rooms list.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 4) {
                    continue;
                }

                int roomNumber = Integer.parseInt(parts[0].trim());
                RoomCategory category = RoomCategory.valueOf(parts[1].trim().toUpperCase());
                double pricePerNight = Double.parseDouble(parts[2].trim());

                rooms.add(new Room(roomNumber, category, pricePerNight));
            }
        } catch (IOException e) {
            System.out.println("Error loading rooms: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid room category in file: " + e.getMessage());
        }
    }

    private void saveRooms() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(roomsFile))) {
            for (Room room : rooms) {
                pw.println(room.getRoomNumber() + "," +
                        room.getCategory() + "," +
                        room.getPricePerNight() + "," +
                        room.isAvailable());
            }
        } catch (IOException e) {
            System.out.println("Error saving rooms: " + e.getMessage());
        }
    }

    private void loadBookings() {
        bookings.clear();
        File file = new File(bookingsFile);

        if (!file.exists()) {
            System.out.println("bookings.txt not found — starting with empty bookings list.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 7) {
                    continue;
                }

                int bookingId = Integer.parseInt(parts[0].trim());
                String customerName = parts[1].trim();
                int roomNumber = Integer.parseInt(parts[2].trim());
                RoomCategory category = RoomCategory.valueOf(parts[3].trim().toUpperCase());
                LocalDate checkInDate = LocalDate.parse(parts[4].trim());
                LocalDate checkOutDate = LocalDate.parse(parts[5].trim());
                double amountPaid = Double.parseDouble(parts[6].trim());

                bookings.add(new Booking(bookingId, customerName, roomNumber,
                        category, checkInDate, checkOutDate, amountPaid));
            }
        } catch (IOException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date in bookings file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid room category in bookings file: " + e.getMessage());
        }
    }

    private void saveBookings() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(bookingsFile))) {
            for (Booking booking : bookings) {
                pw.println(booking.getBookingId() + "," +
                        booking.getCustomerName() + "," +
                        booking.getRoomNumber() + "," +
                        booking.getCategory() + "," +
                        booking.getCheckInDate() + "," +
                        booking.getCheckOutDate() + "," +
                        booking.getAmountPaid());
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }
}