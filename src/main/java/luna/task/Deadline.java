package luna.task;

import java.time.LocalDate; // Represents a date without the time
import java.time.format.DateTimeFormatter; // Formatter for printing and parsing date-time objects
import java.time.format.DateTimeParseException; // An exception thrown when an error occurs during parsing

import luna.exception.LunaException;

/**
 * Represents a {@code Task} with an associated deadline.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");
    protected LocalDate deadline;

    /**
     * Creates a deadline task.
     *
     * @param description task description.
     * @param deadline deadline as a {@link LocalDate}.
     */
    public Deadline(String description, LocalDate deadline) {
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), deadline.format(OUTPUT_FORMAT));
    }

    @Override
    public String toFileString() {
        return "D | " + doneFlag(isDone) + " | " + description + " | " + deadline.format(INPUT_FORMAT);
    }

    /**
     * Creates a {@link Deadline} from saved parts of a file line.
     *
     * @param parts split components of a saved line
     * @return parsed Deadline
     * @throws LunaException if the line format is invalid
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