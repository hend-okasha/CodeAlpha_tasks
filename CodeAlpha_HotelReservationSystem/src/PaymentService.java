/**
 * Simulates payment processing for the hotel system
 *
 * @author Hend Okasha
 */
public class PaymentService {

    public boolean processPayment(double amount) {
        System.out.println("Processing payment of $" + String.format("%.2f", amount) + "...");
        System.out.println("Payment successful!");
        return true;
    }
}