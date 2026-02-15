package luna.task;

/**
 * Represents a {@code Task} with no additional time-related information.
 */
public class Todo extends Task {

    /**
     * Creates a to-do task.
     *
     * @param description task description
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
