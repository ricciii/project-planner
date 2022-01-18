package service.impl;

import model.Task;
import repository.TaskHasDependencyException;
import repository.TaskNotFoundException;
import repository.TaskRepository;
import service.TaskService;
import util.StringUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl() {
        taskRepository = new TaskRepository();
    }

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void addTask(String name, String duration) {
        if (areTaskFieldsValid(name, duration, true)) {
            Task task = new Task();
            task.setName(name);
            task.setDuration(Integer.parseInt(duration));
            taskRepository.addTask(task);
        }
    }

    @Override
    public void editTask(String id, String name, String duration) {
        Task task = getTask(id);
        if (task == null) {
            System.out.println("No Task with ID found");
            return;
        }

        if (areTaskFieldsValid(name, duration, false)) {
            if (!StringUtil.isStringEmpty(name)) {
                task.setName(name);
            }

            if (!StringUtil.isStringEmpty(name)) {
                task.setDuration(Integer.parseInt(duration));
            }
        }
    }

    private boolean areTaskFieldsValid(String name,
            String duration, boolean isNew) {
        boolean result = true;

        if (isNew && StringUtil.isStringEmpty(name)) {
            System.out.println("Name is blank.");
            result = false;
        }

        if (isNew && StringUtil.isStringEmpty(duration)) {
            System.out.println("Duration is blank.");
            result = false;
        } else if (!StringUtil.isStringEmpty(duration)) {
            try {
                Long.parseLong(duration);
            } catch (NumberFormatException e) {
                System.out.println("Invalid duration.");
                result = false;
            }
        }

        return result;
    }

    @Override
    public void addSubTask(String id, String subTaskId) {
        Task task = getTask(id);
        if (task == null) {
            System.out.println("Invalid ID");
            return;
        }

        Task subTask = getTask(subTaskId);
        if (subTask == null) {
            System.out.println("Invalid sub task ID");
            return;
        }

        if (isTaskDependencyValid(task, subTask)) {
            task.getSubTaskIds().add(subTask.getId());
        }
    }

    private boolean isTaskDependencyValid(Task task, Task subTask) {
        boolean result = true;

        for (Long id : task.getSubTaskIds()) {
            if (id.equals(subTask.getId())) {
                result = false;
                System.out.println("Already has sub task");
                break;
            }
        }

        if (result && searchTaskForCircularDependency(task, subTask) != null) {
            result = false;
            System.out.println("Invalid Task Dependency. May cause circular dependency.");
        }

        return result;
    }

    private Task searchTaskForCircularDependency(Task task, Task subTask) {
        Task result = null;
        if (task.getId().equals(subTask.getId())) {
            return task;
        } else {
            for (Long subTaskId : subTask.getSubTaskIds()) {
                result = searchTaskForCircularDependency(task, taskRepository.getTask(subTaskId));
                if (result != null) {
                    return result;
                }
            }

        }
        return result;
    }

    @Override
    public void deleteSubTask(String id, String subTaskId) {
        Task task = getTask(id);
        if (task == null) {
            System.out.println("Invalid ID");
            return;
        }

        Task subTask = getTask(subTaskId);
        if (subTask == null) {
            System.out.println("Invalid sub task ID");
            return;
        }

        int index = -1;
        boolean found = false;
        for (Long longId : task.getSubTaskIds()) {
            index++;
            if (longId.equals(subTask.getId())) {
                found = true;
                break;
            }
        }

        if (found) {
            task.getSubTaskIds().remove(index);
        } else {
            System.out.println("Task has no sub task with ID");
        }

    }

    @Override
    public Task getTask(String id) {
        Task task = null;
        try {
            task = taskRepository.getTask(Long.parseLong(id));
        } catch (NumberFormatException e) {
            System.out.println("Invalid id.");
        }

        return task;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(taskRepository.getTasks());
    }

    @Override
    public void deleteTask(String id) {
        try {
            taskRepository.deleteTask(Long.parseLong(id));
        } catch (NumberFormatException e) {
            System.out.println("Invalid id.");
        } catch (TaskNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (TaskHasDependencyException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Set<Task> getSortedAndCalculatedTasks(LocalDate startDate) {
        return calculateAndSortTasks(taskRepository.getTasks(), startDate);
    }

    private Set<Task> calculateAndSortTasks(List<Task> tasks, LocalDate startDate) {
        Set<Task> sortedTasks = new LinkedHashSet<>();
        for (Task task : tasks) {
            traverseTaskList(task, null, sortedTasks, startDate);
        }

        return sortedTasks;
    }

    private void traverseTaskList(Task task, Task previousTask, Set<Task> sortedTasks, LocalDate startDate) {
        Task nextTask = null;
        if (task.getSubTaskIds() != null) {
            for (Long subTaskId : task.getSubTaskIds()) {
                nextTask = taskRepository.getTask(subTaskId);
                traverseTaskList(nextTask, task, sortedTasks, startDate);
            }
        }

        if (nextTask == null) {
            task.setStartDate(startDate);
            task.setEndDate(startDate.plusDays(task.getDuration()));
        } else {
            task.setEndDate(task.getStartDate().plusDays(task.getDuration()));
        }

        if (previousTask != null && (previousTask.getStartDate() == null
                || (previousTask.getStartDate() != null
                        && task.getEndDate().plusDays(1).compareTo(previousTask.getStartDate()) > 0))) {
            previousTask.setStartDate(task.getEndDate().plusDays(1));
        }

        sortedTasks.add(task);
    }
}
