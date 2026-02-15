package luna.task;

/**
 * Represents a {@code Task} with an associated deadline.
 */
public class Deadline extends Task {
    protected String deadline;

    /**
     * Creates a deadline task.
     *
     * @param description task description.
     * @param deadline deadline in the user-provided format.
     */
    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.deadline);
    }
}
