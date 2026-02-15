package luna;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task with the given description.
     *
     * @param description task description.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks the task as done.
     */
    public void markDone() {
        isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markUndone() {
        isDone = false;
    }

    /**
     * Returns whether the task is done.
     *
     * @return {@code true} if done, otherwise {@code false}.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns a status icon for the task.
     *
     * @return {@code "X"} if done, otherwise {@code " "}.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }
}
