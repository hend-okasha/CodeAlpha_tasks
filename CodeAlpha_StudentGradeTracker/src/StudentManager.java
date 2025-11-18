import java.util.ArrayList;
import java.util.Comparator;

/**
 * Student Management System
 * Handles student records and grade operations
 *
 * @author Hend Okasha
 */
public class StudentManager {
    private ArrayList<Student> students;

    /**
     * Constructor - initializes the student management system
     */
    public StudentManager() {
        students = new ArrayList<>();
    }

    public void addStudent(String name) {
        // Check if student already exists
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                System.out.println("Student with this name already exists.");
                return;
            }
        }

        int studentId = students.stream()
                .map(Student::getId)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;

        Student newStudent = new Student(studentId, name);
        students.add(newStudent);

        System.out.println("Student added successfully!");
        System.out.println("Student ID: " + studentId);
    }

    public Student findStudentByName(String name) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                return student;
            }
        }
        return null;
    }

    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("\n─────────────────────────────────────────");
        System.out.println("          All Students Report");
        System.out.println("─────────────────────────────────────────");

        for (Student student : students) {
            displayStudentDetails(student);
            System.out.println("─────────────────────────────────────────");
        }
    }

    public void displayStudentDetails(Student student) {
        System.out.printf("ID: %d%n", student.getId());
        System.out.printf("Name: %s%n", student.getName());

        if (student.getGrades().isEmpty()) {
            System.out.println("Grades: No grades recorded yet");
        } else {
            System.out.print("Grades: ");
            for (int i = 0; i < student.getGrades().size(); i++) {
                System.out.printf("%.2f", student.getGrades().get(i));
                if (i < student.getGrades().size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
            System.out.printf("Average: %.2f%n", student.getAverageScore());
            System.out.printf("Highest: %.2f%n", student.getHighestGrade());
            System.out.printf("Lowest: %.2f%n", student.getLowestGrade());
        }
    }

    public String generateSummaryReport() {
        if (students.isEmpty()) {
            return "No students to generate report.";
        }

        double totalAverage = 0;
        double overallHighest = 0;
        double overallLowest = 100;
        int studentsWithGrades = 0;

        for (Student student : students) {
            if (!student.getGrades().isEmpty()) {
                studentsWithGrades++;
                totalAverage += student.getAverageScore();

                if (student.getHighestGrade() > overallHighest) {
                    overallHighest = student.getHighestGrade();
                }

                if (student.getLowestGrade() < overallLowest) {
                    overallLowest = student.getLowestGrade();
                }
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("\n─────────────────────────────────────────\n");
        report.append("           Summary Report\n");
        report.append("─────────────────────────────────────────\n");
        report.append(String.format("Total Students: %d%n", students.size()));
        report.append(String.format("Students with Grades: %d%n", studentsWithGrades));

        if (studentsWithGrades > 0) {
            double classAverage = totalAverage / studentsWithGrades;
            report.append(String.format("Class Average: %.2f%n", classAverage));
            report.append(String.format("Highest Grade Overall: %.2f%n", overallHighest));
            report.append(String.format("Lowest Grade Overall: %.2f%n", overallLowest));
        } else {
            report.append("No grades recorded yet.\n");
        }

        report.append("─────────────────────────────────────────");

        return report.toString();
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}