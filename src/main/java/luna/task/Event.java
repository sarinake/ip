package luna.task;

import luna.exception.LunaException;

/**
 * Represents a {@code Task} with an associated start and end date.
 */
public class Event extends Task {
    protected String startDate;
    protected String endDate;

    /**
     * Creates an event task.
     *
     * @param description task description
     * @param startDate event start time in the user-provided format
     * @param endDate event end time in the user-provided format
     */
    public Event(String description, String startDate, String endDate) {
        super(description);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(), this.startDate, this.endDate);
    }

    @Override
    public String toFileString() {
        return "E | " + doneFlag(isDone) + " | " + description + " | " + startDate + " | " + endDate;
    }

    /**
     * Creates an {@link Event} from saved parts of a file line.
     *
     * @param parts split components of a saved line
     * @return parsed Event
     * @throws LunaException if the saved line format is invalid
     */
    public static Event fromFileParts(String[] parts) {
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid event line.");
        }

        Event event = new Event(parts[2], parts[3], parts[4]);
        if (parseDoneFlag(parts[1])) {
            event.markDone();
        }
        return event;
    }
}