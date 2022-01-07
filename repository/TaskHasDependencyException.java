package repository;

public class TaskHasDependencyException extends Exception {
    public TaskHasDependencyException(String message) {
        super(message);
    }
}
