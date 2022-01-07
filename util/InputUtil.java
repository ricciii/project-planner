package util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtil {

    private static Scanner scanner = new Scanner(System.in);

    public static LocalDate getProjectStartDate() {
        boolean done = false;
        LocalDate startDate = null;
        do {
            System.out.print("Please enter project start date (mm-dd-yyyy format): ");
            String input = scanner.nextLine();
            try {
                if (!"cancel".equalsIgnoreCase(input)) {
                    startDate = DateUtil.stringToDate(input);
                }
                done = true;
            } catch (DateTimeParseException e) {
                System.out.println("Error in parsing date format. Please try again or type cancel to exit.");
            }
        } while (!done);

        return startDate;
    }

    public static String getTaskId() {
        System.out.print("Please enter task ID: ");
        return scanner.nextLine();
    }

    public static String getSubTaskId() {
        System.out.print("Please enter sub task ID: ");
        return scanner.nextLine();
    }

    public static String getTaskName() {
        System.out.print("Please enter task name: ");
        return scanner.nextLine();
    }

    public static String getTaskDuration() {
        System.out.print("Please enter task day duration: ");
        return scanner.nextLine();
    }

    public static String getSubTaskIds() {
        System.out.print("Please enter task dependency id/s. (Separate by space for multiple. Enter to skip.): ");
        return scanner.nextLine();
    }

    public static String getMainMenuInput() {
        System.out.print("Enter the number of the task that you want to do: ");
        return scanner.nextLine();
    }

}
