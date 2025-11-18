import java.time.LocalDate;

/**
 * Represents a hotel booking record
 * Contains all information about a customer's reservation
 *
 * @author Hend Okasha
 */
public class Booking {
    private int bookingId;
    private String customerName;
    private int roomNumber;
    private RoomCategory category;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double amountPaid;

    public Booking(int bookingId, String customerName, int roomNumber, RoomCategory category,
                   LocalDate checkInDate, LocalDate checkOutDate, double amountPaid) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.amountPaid = amountPaid;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomCategory getCategory() {
        return category;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", customerName='" + customerName + '\'' +
                ", roomNumber=" + roomNumber +
                ", category=" + category +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", amountPaid=" + amountPaid +
                '}';
    }
}