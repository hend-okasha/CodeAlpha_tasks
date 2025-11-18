import java.util.Scanner;

/**
 * User Interface for Student Grade Tracker
 * Handles all user interactions and menu operations
 *
 * @author Hend Okasha
 */
public class StudentMenu {
    private final Scanner scanner;
    private final StudentManager manager;

    public StudentMenu(Scanner scanner, StudentManager manager) {
        this.scanner = scanner;
        this.manager = manager;
    }

    public void start() {
        System.out.println("    Welcome to Student Grade Tracker");

        while (true) {
            displayMainMenu();
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    addStudentFlow();
                    break;
                case "2":
                    addGradesFlow();
                    break;
                case "3":
                    viewAllStudentsFlow();
                    break;
                case "4":
                    viewSpecificStudentFlow();
                    break;
                case "5":
                    viewSummaryReportFlow();
                    break;
                case "6":
                    exitProgram();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n─────────────────────────────────────────");
        System.out.println("   Student Grade Tracker");
        System.out.println("─────────────────────────────────────────");
        System.out.println("  1. Add Student");
        System.out.println("  2. Add Grades");
        System.out.println("  3. Display All Students");
        System.out.println("  4. Display Specific Student");
        System.out.println("  5. View Summary Report");
        System.out.println("  6. Exit");
        System.out.println("─────────────────────────────────────────");
        System.out.print("Choose an option: ");
    }

    private void addStudentFlow() {
        System.out.println("\n--- Add Student ---");
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            waitForEnter();
            return;
        }

        if (!name.matches("[a-zA-Z ]+")) {
            System.out.println("Invalid name! Only letters and spaces are allowed.");
            waitForEnter();
            return;
        }

        manager.addStudent(name);
        waitForEnter();
    }

    private void addGradesFlow() {
        System.out.println("\n--- Add Grades ---");

        if (manager.getStudents().isEmpty()) {
            System.out.println("No students found. Please add a student first.");
            waitForEnter();
            return;
        }

        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();

        Student student = manager.findStudentByName(name);
        if (student == null) {
            System.out.println("Student not found!");
            waitForEnter();
            return;
        }

        System.out.print("Enter grade(s) (0-100) separated by spaces: ");
        String gradesLine = scanner.nextLine().trim();

        if (gradesLine.isEmpty()) {
            System.out.println("No grades entered!");
            waitForEnter();
            return;
        }

        String[] parts = gradesLine.split("\\s+");
        boolean anyAdded = false;

        for (String part : parts) {
            try {
                double grade = Double.parseDouble(part);

                if (grade >= 0 && grade <= 100) {
                    student.addGrade(grade);
                    anyAdded = true;
                } else {
                    System.out.println("Invalid grade: " + part + " (must be 0-100)");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid grade: " + part + " (skipped)");
            }
        }

        if (anyAdded) {
            System.out.println("Grades added successfully!");
        } else {
            System.out.println("No valid grades were added.");
        }

        waitForEnter();
    }

    private void viewAllStudentsFlow() {
        manager.displayAllStudents();
        waitForEnter();
    }

    private void viewSpecificStudentFlow() {
        System.out.println("\n--- View Student Details ---");

        if (manager.getStudents().isEmpty()) {
            System.out.println("No students found.");
            waitForEnter();
            return;
        }

        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();

        Student student = manager.findStudentByName(name);
        if (student == null) {
            System.out.println("Student not found!");
            waitForEnter();
            return;
        }

        System.out.println("\n─────────────────────────────────────────");
        manager.displayStudentDetails(student);
        System.out.println("─────────────────────────────────────────");

        waitForEnter();
    }

    private void viewSummaryReportFlow() {
        String report = manager.generateSummaryReport();
        System.out.println(report);
        waitForEnter();
    }

    private void waitForEnter() {
        System.out.print("\nPress Enter to return to menu...");
        scanner.nextLine();
    }
    
    private void exitProgram() {
        System.out.println("\nThank you for using Student Grade Tracker!");
        System.out.println("Goodbye!");
        scanner.close();
        System.exit(0);
    }
}