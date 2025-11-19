# Student Grade Tracker

The Student Grade Tracker is a Java console application used to manage student information and evaluate their performance.  
The system follows a simple menu-based structure that allows users to add students, enter grades, and generate performance reports.

---

## Features
- Add new students  
- Record multiple grades per student  
- Automatically calculate average, highest, and lowest grade  
- Display all student records  
- Search for a specific student by name  
- Input validation for names and score ranges  

*Example:*  
If a student named "Omar" has grades [70, 82, 95], the system calculates:  
- Average: 82.33  
- Highest: 95  
- Lowest: 70  

---

## System Design
The project uses a clean OOP structure:
- **Student** – stores student info and grade calculations  
- **StudentManager** – adds students, manages grade lists  
- **Menu** – handles user interaction  
- **Main** – program entry point  

---

## Technologies
- Java  
- OOP  
- ArrayList  
- Console I/O  

---

## How to Run
1. Open the project folder  
2. Compile and run `Main.java`  
3. Follow the menu options to add students and view reports  
