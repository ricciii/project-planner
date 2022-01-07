package service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import model.Task;
import model.dto.TaskDto;
import repository.TaskHasDependencyException;
import repository.TaskNotFoundException;
import repository.TaskRepository;
import service.TaskService;
import util.StringUtil;

public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;

    public TaskServiceImpl() {
        taskRepository = new TaskRepository();
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
            taskRepository.addTask(task);
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
    public List<TaskDto> getTasks() {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepository.getTasks()) {
            taskDtos.add(taskToDto(task));
        }
        return taskDtos;
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
    public Set<TaskDto> getSortedAndCalculatedTasks(LocalDate startDate) {
        return calculateAndSortTasks(taskRepository.getTasks(), startDate);
    }

    private Set<TaskDto> calculateAndSortTasks(List<Task> tasks, LocalDate startDate) {
        Set<TaskDto> sortedTasks = new LinkedHashSet<TaskDto>();
        for (Task task : tasks) {
            traverseTaskList(task, task, sortedTasks, startDate);
        }

        return sortedTasks;
    }

    private void traverseTaskList(Task task, Task previousTask, Set<TaskDto> sortedTasks, LocalDate startDate) {
        Task nextTask = null;
        if (task.getSubTaskIds() != null) {
            for (Long subTaskId : task.getSubTaskIds()) {
                nextTask = taskRepository.getTask(subTaskId);
                traverseTaskList(nextTask, task, sortedTasks, startDate);
            }
        }

        TaskDto taskDto = taskToDto(task);

        if (nextTask == null || previousTask == null) {
            taskDto.setStartDate(startDate);
            taskDto.setEndDate(startDate.plusDays(taskDto.getDuration()));
            if (previousTask != null) {
                if (previousTask.getStartDate() == null
                        || (previousTask.getStartDate() != null
                                && taskDto.getEndDate().compareTo(previousTask.getStartDate()) > 0)) {
                    previousTask.setStartDate(taskDto.getEndDate());
                }
            }
        } else {
            taskDto.setStartDate(task.getStartDate().plusDays(1));
            taskDto.setEndDate(taskDto.getStartDate().plusDays(taskDto.getDuration()));
            previousTask.setStartDate(taskDto.getEndDate());
            task.setStartDate(startDate);
        }

        sortedTasks.add(taskDto);
    }

    private TaskDto taskToDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setName(task.getName());
        taskDto.setDuration(task.getDuration());
        taskDto.setSubTaskIds(task.getSubTaskIds());
        return taskDto;
    }
}
