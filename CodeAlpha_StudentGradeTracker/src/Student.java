import java.util.ArrayList;

/**
 * Represents a student with their grades and academic information
 *
 * @author Hend Okasha
 */
public class Student {
    private int id;
    private String name;
    private ArrayList<Double> grades;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
        this.grades = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Double> getGrades() {
        return grades;
    }

    public void addGrade(double grade) {
        if (grade >= 0 && grade <= 100) {
            grades.add(grade);
        } else {
            System.out.println("Invalid grade! Grade must be between 0 and 100.");
        }
    }

    public double getAverageScore() {
        if (grades.isEmpty()) {
            return 0;
        }

        double sum = 0;
        for (double grade : grades) {
            sum += grade;
        }

        return sum / grades.size();
    }

    public double getHighestGrade() {
        if (grades.isEmpty()) {
            return 0;
        }

        double max = grades.get(0);
        for (double grade : grades) {
            if (grade > max) {
                max = grade;
            }
        }

        return max;
    }

    public double getLowestGrade() {
        if (grades.isEmpty()) {
            return 0;
        }

        double min = grades.get(0);
        for (double grade : grades) {
            if (grade < min) {
                min = grade;
            }
        }

        return min;
    }

    @Override
    public String toString() {
        return "Student{" +
                "ID: " + id +
                ", Name: '" + name + '\'' +
                ", Grades: " + grades +
                ", Average: " + String.format("%.2f", getAverageScore()) +
                ", Highest: " + getHighestGrade() +
                ", Lowest: " + getLowestGrade() +
                '}';
    }
}