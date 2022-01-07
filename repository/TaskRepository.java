package repository;

import java.util.ArrayList;
import java.util.List;

import model.Task;

public class TaskRepository {
    private List<Task> tasks;
    private long idGenerator;

    public TaskRepository() {
        tasks = new ArrayList<>();
        idGenerator = 0;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void deleteTask(long id) throws TaskNotFoundException, TaskHasDependencyException {
        List<Task> tasks = getTasks();
        int index = -1;
        Task task = null;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(id)) {
                index = i;
                task = tasks.get(i);
            }
        }

        if (index > -1) {
            if (task.getSubTaskIds() != null && !task.getSubTaskIds().isEmpty()) {
                throw new TaskHasDependencyException("Can't delete. Has an existing dependency.");
            } else {
                tasks.remove(index);
            }
        } else {
            throw new TaskNotFoundException("No task found.");
        }
    }

    public Task getTask(long id) {
        Task result = null;
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                result = task;
                break;
            }
        }

        return result;
    }

    public void addTask(Task task) {
        if (task.getId() == null) {
            task.setId(idGenerator++);
        }
        tasks.add(task);
    }
}
