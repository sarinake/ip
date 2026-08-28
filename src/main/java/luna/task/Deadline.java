package luna.task;

import luna.exception.LunaException;

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

    @Override
    public String toFileString() {
        return "D | " + doneFlag(isDone) + " | " + description + " | " + deadline;
    }

    /**
     * Creates a {@link Deadline} from saved parts of a file line.
     *
     * @param parts split components of a saved line
     * @return parsed Deadline
     * @throws LunaException if the saved line format is invalid
     */
    public static Deadline fromFileParts(String[] parts) {
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid deadline line.");
        }

        Deadline deadline = new Deadline(parts[2], parts[3]);
        if (parseDoneFlag(parts[1])) {
            deadline.markDone();
        }
        return deadline;
    }
}