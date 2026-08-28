package luna.task;

import luna.exception.LunaException;

/**
 * Represents a task with a description and completion status.
 */
public abstract class Task {
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

    /**
     * Returns the file format representation of this task.
     *
     * @return one-line string for saving in storage
     */
    public abstract String toFileString();

    /**
     * Parses a saved line and creates the corresponding {@code Task} object.
     *
     * @param line a line from the data file
     * @return parsed Task
     * @throws LunaException if the line is invalid
     */
    public static Task fromFileLine(String line) throws LunaException {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            throw new LunaException("Invalid saved line: " + line);
        }

        String type = parts[0];
        switch (type) {
        case "T":
            return Todo.fromFileParts(parts);
        case "D":
            return Deadline.fromFileParts(parts);
        case "E":
            return Event.fromFileParts(parts);
        default:
            throw new LunaException("Unknown task type: " + type);
        }
    }

    /**
     * Converts a done flag read from file into a boolean value,
     * indicating whether a task is marked as done.
     *
     * @param doneFlag done flag from file (expected {@code "0"} or {@code "1"})
     * @return true if the flag is {@code "1"}, otherwise false
     */
    protected static boolean parseDoneFlag(String doneFlag) {
        return "1".equals(doneFlag);
    }

    /**
     * Converts the done status into the string flag used in the data file.
     *
     * @param isDone whether the task is done
     * @return {@code "1"} if done, otherwise {@code "0"}
     */
    protected static String doneFlag(boolean isDone) {
        return isDone ? "1" : "0";
    }
}