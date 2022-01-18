package controller;

import model.Task;
import service.TaskService;
import service.impl.TaskServiceImpl;
import util.InputUtil;
import util.MenuUtil;
import util.OutputUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class PlannerController {

    private final TaskService taskService;
    private LocalDate projectStartDate;

    public PlannerController() {
        taskService = new TaskServiceImpl();
    }

    public PlannerController(TaskService taskService) {
        this.taskService = taskService;
    }

    public void start() {
        projectStartDate = InputUtil.getProjectStartDate();
        if (projectStartDate != null) {
            promptPlannerMenu();
        }
    }

    private void promptPlannerMenu() {
        boolean done = false;
        do {
            MenuUtil.displayMainMenu();
            switch (InputUtil.getMainMenuInput()) {
                case "1":
                    addTask();
                    break;
                case "2":
                    viewTask();
                    break;
                case "3":
                    editTask();
                    break;
                case "4":
                    addSubTask();
                    break;
                case "5":
                    deleteSubTask();
                    break;
                case "6":
                    deleteTask();
                    break;
                case "7":
                    viewTasks();
                    break;
                case "8":
                    viewPlanner();
                    break;
                case "9":
                    done = true;
                    break;
                default:
                    System.out.println("Option not available, please try again.");
            }
        } while (!done);
    }

    private void addTask() {
        String name = InputUtil.getTaskName();
        String duration = InputUtil.getTaskDuration();
        taskService.addTask(name, duration);
    }

    private void editTask() {
        String id = InputUtil.getTaskId();
        String name = InputUtil.getTaskName();
        String duration = InputUtil.getTaskDuration();
        taskService.editTask(id, name, duration);
    }

    private void viewTask() {
        String id = InputUtil.getTaskId();
        Task task = taskService.getTask(id);
        OutputUtil.showTaskDetails(task);
    }

    private void deleteTask() {
        String id = InputUtil.getTaskId();
        taskService.deleteTask(id);
    }

    private void viewTasks() {
        List<Task> tasks = taskService.getTasks();
        OutputUtil.showTasksDetails(tasks);
    }

    private void viewPlanner() {
        Set<Task> tasks = taskService.getSortedAndCalculatedTasks(projectStartDate);
        OutputUtil.showPlanner(tasks);
    }

    private void addSubTask() {
        String id = InputUtil.getTaskId();
        String subTaskId = InputUtil.getSubTaskId();
        taskService.addSubTask(id, subTaskId);
    }

    private void deleteSubTask() {
        String id = InputUtil.getTaskId();
        String subTaskId = InputUtil.getSubTaskId();
        taskService.deleteSubTask(id, subTaskId);
    }
}
