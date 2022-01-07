package service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import model.Task;
import model.dto.TaskDto;

public interface TaskService {
    void addTask(String name, String duration);

    void editTask(String id, String name, String duration);

    Task getTask(String id);

    List<TaskDto> getTasks();

    void deleteTask(String id);

    void addSubTask(String id, String subTaskId);

    void deleteSubTask(String id, String subTaskId);

    Set<TaskDto> getSortedAndCalculatedTasks(LocalDate startDate);
}