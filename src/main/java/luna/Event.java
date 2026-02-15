package luna;

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
}
