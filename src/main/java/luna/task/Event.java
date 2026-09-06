package luna.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import luna.exception.LunaException;

/**
 * Represents a {@code Task} with an associated start and end date.
 */
public class Event extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    protected LocalDate startDate;
    protected LocalDate endDate;

    /**
     * Creates an event task.
     *
     * @param description task description
     * @param startDate event start date as a {@link LocalDate}.
     * @param endDate event end date as a a {@link LocalDate}.
     */
    public Event(String description, LocalDate startDate, LocalDate endDate) {
        super(description);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(), 
                startDate.format(OUTPUT_FORMAT), endDate.format(OUTPUT_FORMAT));
    }

    @Override
    public String toFileString() {
        return "E | " + doneFlag(isDone) + " | " + description + " | "
                + startDate.format(INPUT_FORMAT) + " | " + endDate.format(INPUT_FORMAT);
    }

    /**
     * Creates an {@link Event} from saved parts of a file line.
     *
     * @param parts split components of a saved line
     * @return parsed Event
     * @throws LunaException if the line format is invalid
     */
    protected static Event fromFileParts(String[] parts) throws LunaException {
        if (parts.length < 5) {
            throw new LunaException("Invalid event line in data file.");
        }

        LocalDate from = parseDate(parts[3]);
        LocalDate to = parseDate(parts[4]);
        Event event = new Event(parts[2], from, to);
        if (parseDoneFlag(parts[1])) {
            event.markDone();
        }
        return event;
    }

    /**
     * Parses a user-provided date string into {@link LocalDate}.
     *
     * @param raw user input date string (expected yyyy-MM-dd)
     * @return parsed LocalDate
     * @throws LunaException if the date format is invalid
     */
    public static LocalDate parseDate(String raw) throws LunaException {
        try {
            return LocalDate.parse(raw, INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new LunaException("Invalid date format. Use yyyy-MM-dd (e.g., 2019-10-15)");
        }
    }
}