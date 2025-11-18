import java.util.Scanner;

/**
 * Student Grade Tracker - Main Entry Point
 * A program to input and manage student grades
 *
 * @author Hend Okasha
 */
public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        StudentMenu menu = new StudentMenu(scanner, manager);
        menu.start();
    }
}