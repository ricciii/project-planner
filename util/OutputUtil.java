package util;

import java.util.List;
import java.util.Set;

import model.Task;
import model.dto.TaskDto;

public class OutputUtil {

    public static void showTaskDetails(Task task) {
        if (task != null) {
            System.out.println("ID: " + task.getId());
            System.out.println("Task Name: " + task.getName());
            System.out.println("Task Duration: " + task.getDuration() + " day/s");
        } else {
            System.out.println("No task with ID found.");
        }
    }

    public static void showTasksDetails(List<TaskDto> tasks) {
        if (!tasks.isEmpty()) {
            for (TaskDto task : tasks) {
                System.out.println();
                System.out.println("Task ID: " + task.getId());
                System.out.println("Task Name: " + task.getName());
                System.out.println("Task Duration: " + task.getDuration() + " day/s");
                System.out.println("Sub Task/s: " + String.join(",", task.getSubTaskIdStrings()));
            }
            System.out.println();
        } else {
            System.out.println("No task/s stored.");
        }
    }

    public static void showPlanner(Set<TaskDto> tasks) {
        if (!tasks.isEmpty()) {
            for (TaskDto task : tasks) {
                System.out.println();
                System.out.println("Task ID: " + task.getId());
                System.out.println("Task Name: " + task.getName());
                System.out.println("Start Date: " + task.getStartDate());
                System.out.println("End Date: " + task.getEndDate());
                System.out.println("Task Duration: " + task.getDuration() + " day/s");
                System.out.println("Sub Task/s: " + String.join(",", task.getSubTaskIdStrings()));
            }
            System.out.println();
        } else {
            System.out.println("No task/s stored.");
        }
    }
}
