# Hotel Reservation System

A Java console application that manages hotel rooms and reservations.  
It supports multiple room categories, booking, cancellation, and viewing reservation details.

---

## Features

### Room Management
- Standard, Deluxe, and Suite categories  
- Track availability for each room  

### Reservation Management
- Create new reservations with customer details  
- Cancel existing reservations  
- Prevent double-booking  
- Save and load booking data using File I/O  

### Payment Simulation
- Basic confirmation flow to finalize bookings  

*Example:*  
- Search for Deluxe rooms → Room #310 available  
- Book for 2 nights → Total cost calculated  
- Reservation saved in file for next program run  

---
## System Architecture / Classes

- **Main.java**  
  Entry point, system initialization  

- **Hotel.java**  
  Core logic: rooms, bookings, availability, file I/O  

- **HotelMenu.java**  
  User interface, menu navigation, input/output handling  

- **Room.java**  
  Room entity and properties  

- **RoomCategory.java**  
  Room types enum (Standard, Deluxe, Suite)  

- **Booking.java**  
  Booking records: customer & reservation info  

- **PaymentService.java**  
  Payment simulation & transaction processing


---

## Technologies
- Java  
- OOP  
- File I/O  
- ArrayList  
- Console interface  

---

## How to Run
1. Open the project folder  
2. Run `Main.java`  
3. Choose room type, book, cancel, or view reservations  


