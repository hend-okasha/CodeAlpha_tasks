import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * User Interface for Hotel Reservation System
 * Handles all user interactions and menu operations
 *
 * @author Hend Okasha
 */
public class HotelMenu {
    private final Hotel hotel;
    private final PaymentService paymentService;
    private final Scanner scanner;

    public HotelMenu(Hotel hotel, PaymentService paymentService) {
        this.hotel = hotel;
        this.paymentService = paymentService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            displayMainMenu();
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    searchRoomsFlow();
                    break;
                case "2":
                    bookRoomFlow();
                    break;
                case "3":
                    cancelBookingFlow();
                    break;
                case "4":
                    viewBookingFlow();
                    break;
                case "5":
                    exitProgram();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n─────────────────────────────────────────");
        System.out.println("   Hotel Reservation System");
        System.out.println("─────────────────────────────────────────");
        System.out.println("  1. Search Available Rooms");
        System.out.println("  2. Book a Room");
        System.out.println("  3. Cancel Booking");
        System.out.println("  4. View Booking Details");
        System.out.println("  5. Exit");
        System.out.println("─────────────────────────────────────────");
        System.out.print("Choose an option: ");
    }

    private void searchRoomsFlow() {
        try {
            System.out.println("\n--- Search Available Rooms ---");
            System.out.print("Enter category (Standard/Deluxe/Suite): ");
            String category = scanner.nextLine().trim();

            if (!category.equalsIgnoreCase("STANDARD") &&
                    !category.equalsIgnoreCase("DELUXE") &&
                    !category.equalsIgnoreCase("SUITE")) {
                System.out.println("Invalid category! Please choose: Standard, Deluxe, or Suite.");
                waitForEnter();
                return;
            }

            System.out.print("Enter check-in date (YYYY-MM-DD): ");
            LocalDate checkInDate = LocalDate.parse(scanner.nextLine().trim());

            System.out.print("Enter check-out date (YYYY-MM-DD): ");
            LocalDate checkOutDate = LocalDate.parse(scanner.nextLine().trim());

            if (!checkOutDate.isAfter(checkInDate)) {
                System.out.println("Check-out must be after check-in.");
                waitForEnter();
                return;
            }

            List<Room> availableRooms = hotel.searchAvailableRooms(category, checkInDate, checkOutDate);

            if (availableRooms.isEmpty()) {
                System.out.println("No available rooms found for that category and dates.");
            } else {
                displayRoomList(availableRooms);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        }
        waitForEnter();
    }

    private void bookRoomFlow() {
        try {
            System.out.println("\n--- Book a Room ---");

            System.out.print("Enter check-in date (YYYY-MM-DD): ");
            LocalDate checkInDate = LocalDate.parse(scanner.nextLine().trim());

            System.out.print("Enter check-out date (YYYY-MM-DD): ");
            LocalDate checkOutDate = LocalDate.parse(scanner.nextLine().trim());

            if (!checkOutDate.isAfter(checkInDate)) {
                System.out.println("Check-out must be after check-in.");
                waitForEnter();
                return;
            }

            System.out.print("Enter room category (Standard/Deluxe/Suite): ");
            String category = scanner.nextLine().trim();

            if (!category.equalsIgnoreCase("STANDARD") &&
                    !category.equalsIgnoreCase("DELUXE") &&
                    !category.equalsIgnoreCase("SUITE")) {
                System.out.println("Invalid category! Please choose: Standard, Deluxe, or Suite.");
                waitForEnter();
                return;
            }

            List<Room> availableRooms = hotel.searchAvailableRooms(category, checkInDate, checkOutDate);

            if (availableRooms.isEmpty()) {
                System.out.println("No available rooms found for that category and dates.");
                waitForEnter();
                return;
            }

            displayRoomList(availableRooms);

            System.out.print("\nEnter your name: ");
            String customerName = scanner.nextLine().trim();

            if (customerName.isEmpty()) {
                System.out.println("Name cannot be empty.");
                waitForEnter();
                return;
            }

            if (!customerName.matches("[a-zA-Z ]+")) {
                System.out.println("Invalid name! Only letters and spaces are allowed.");
                waitForEnter();
                return;
            }

            System.out.print("Enter room number to book: ");
            int roomNumber = Integer.parseInt(scanner.nextLine().trim());

            Room selectedRoom = hotel.getRoomByNumber(roomNumber);
            if (selectedRoom == null) {
                System.out.println("Invalid room number.");
                waitForEnter();
                return;
            }

            if (!isRoomInList(roomNumber, availableRooms)) {
                System.out.println("Please select a room from the available list.");
                waitForEnter();
                return;
            }

            long nights = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            double totalAmount = nights * selectedRoom.getPricePerNight();

            displayBookingSummary(roomNumber, selectedRoom.getCategory().name(), nights, totalAmount);

            System.out.print("Proceed to payment? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (!confirmation.equals("yes")) {
                System.out.println("Booking cancelled by user.");
                waitForEnter();
                return;
            }

            if (paymentService.processPayment(totalAmount)) {
                Booking booking = hotel.makeBooking(customerName, roomNumber, checkInDate, checkOutDate);
                if (booking != null) {
                    System.out.println("\nBooking successful!");
                    System.out.println("Your Booking ID: " + booking.getBookingId());
                    System.out.println("Please save this ID for future reference.");
                } else {
                    System.out.println("Failed to create booking.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number input.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        }
        waitForEnter();
    }

    private void cancelBookingFlow() {
        try {
            System.out.println("\n--- Cancel Booking ---");
            System.out.print("Enter booking ID to cancel: ");
            int bookingId = Integer.parseInt(scanner.nextLine().trim());

            boolean cancelled = hotel.cancelBooking(bookingId);
            if (cancelled) {
                System.out.println("Booking cancelled successfully.");
            } else {
                System.out.println("Booking ID not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid booking ID.");
        }
        waitForEnter();
    }

    private void viewBookingFlow() {
        try {
            System.out.println("\n--- View Booking Details ---");
            System.out.print("Enter booking ID: ");
            int bookingId = Integer.parseInt(scanner.nextLine().trim());

            Booking booking = hotel.viewBooking(bookingId);
            if (booking != null) {
                displayBookingDetails(booking);
            } else {
                System.out.println("Booking not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid booking ID.");
        }
        waitForEnter();
    }

    private void displayRoomList(List<Room> rooms) {
        System.out.println("\nAvailable rooms:");
        System.out.println("─────────────────────────────────────────");
        for (Room room : rooms) {
            System.out.printf("Room %d | %s | $%.2f per night%n",
                    room.getRoomNumber(),
                    room.getCategory(),
                    room.getPricePerNight());
        }
        System.out.println("─────────────────────────────────────────");
    }

    private void displayBookingSummary(int roomNumber, String category, long nights, double amount) {
        System.out.println("\n─────────────────────────────────────────");
        System.out.printf("Room: %d (%s)%n", roomNumber, category);
        System.out.printf("Duration: %d night(s)%n", nights);
        System.out.printf("Total amount: $%.2f%n", amount);
        System.out.println("─────────────────────────────────────────");
    }

    private void displayBookingDetails(Booking booking) {
        System.out.println("\n─────────────────────────────────────────");
        System.out.println("         Booking Details");
        System.out.println("─────────────────────────────────────────");
        System.out.printf("Booking ID:     %d%n", booking.getBookingId());
        System.out.printf("Customer:       %s%n", booking.getCustomerName());
        System.out.printf("Room Number:    %d%n", booking.getRoomNumber());
        System.out.printf("Category:       %s%n", booking.getCategory());
        System.out.printf("Check-in:       %s%n", booking.getCheckInDate());
        System.out.printf("Check-out:      %s%n", booking.getCheckOutDate());
        System.out.printf("Amount Paid:    $%.2f%n", booking.getAmountPaid());
        System.out.println("─────────────────────────────────────────");
    }

    private boolean isRoomInList(int roomNumber, List<Room> roomList) {
        for (Room room : roomList) {
            if (room.getRoomNumber() == roomNumber) {
                return true;
            }
        }
        return false;
    }

    private void waitForEnter() {
        System.out.print("\nPress Enter to return to menu...");
        scanner.nextLine();
    }

    private void exitProgram() {
        System.out.println("\nThank you for using Hotel Reservation System!");
        System.out.println("Goodbye!");
        scanner.close();
        System.exit(0);
    }
}