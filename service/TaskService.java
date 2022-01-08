package service;

import model.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface TaskService {
    void addTask(String name, String duration);

    void editTask(String id, String name, String duration);

    Task getTask(String id);

    List<Task> getTasks();

    void deleteTask(String id);

    void addSubTask(String id, String subTaskId);

    void deleteSubTask(String id, String subTaskId);

    Set<Task> getSortedAndCalculatedTasks(LocalDate startDate);
}