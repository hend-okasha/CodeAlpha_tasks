/**
 * Hotel Reservation System - Main Entry Point
 * A system for managing hotel room bookings
 *
 * @author Hend Okasha
 */
public class Main {
    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        PaymentService paymentService = new PaymentService();
        HotelMenu menu = new HotelMenu(hotel, paymentService);

        menu.start();
    }
}