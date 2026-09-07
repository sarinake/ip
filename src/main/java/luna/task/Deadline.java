package luna.task;

import java.time.LocalDate; // Represents a date without the time
import java.time.format.DateTimeFormatter; // Formatter for printing and parsing date-time objects
import java.time.format.DateTimeParseException; // An exception thrown when an error occurs during parsing

import luna.exception.LunaException;

/**
 * Represents a {@code Task} with an associated deadline.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DATE_INPUT_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd
    private static final DateTimeFormatter DATE_OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private LocalDate deadline;

    /**
     * Creates a deadline task.
     *
     * @param description Task description.
     * @param deadline Deadline as a {@link LocalDate}.
     */
    public Deadline(String description, LocalDate deadline) {
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), deadline.format(DATE_OUTPUT_FORMAT));
    }

    @Override
    public String toFileString() {
        return "D | " + formatDoneFlag(isDone) + " | " + description + " | "
                + deadline.format(DATE_INPUT_FORMAT);
    }

    /**
     * Creates a {@link Deadline} from saved parts of a file line.
     *
     * @param parts Split components of a saved line.
     * @return Parsed deadline.
     * @throws LunaException If the line format is invalid.
     */
    protected static Deadline fromFileParts(String[] parts) throws LunaException {
        if (parts.length < 4) {
            throw new LunaException("Invalid deadline line in data file");
        }

        LocalDate date = parseDate(parts[3]);
        Deadline deadline = new Deadline(parts[2], date);
        if (parseDoneFlag(parts[1])) {
            deadline.markDone();
        }
        return deadline;
    }

    /**
     * Parses a user-provided date string into {@link LocalDate}.
     *
     * @param raw User input date string (expected yyyy-MM-dd).
     * @return Parsed date.
     * @throws LunaException If the date format is invalid.
     */
    public static LocalDate parseDate(String raw) throws LunaException {
        try {
            return LocalDate.parse(raw, DATE_INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new LunaException("Invalid date format. Use yyyy-MM-dd (e.g., 2019-10-15)");
        }
    }
}
