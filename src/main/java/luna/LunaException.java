package luna;

/**
 * Represents exceptions thrown by Luna.
 * 
 * <p>Typical error scenarios include:
 * <ul>
 *   <li>User enters an unrecognized command</li>
 *   <li>Task description, {@code /from}, or {@code /to} fields are empty</li>
 *   <li>Attempt to mark/unmark a task that does not exist or has an invalid index</li>
 * </ul>
 *
 * <p>Ignored error scenarios (as of now):
 * <ul>
 *   <li>Event where start date is after end date</li>
 *   <li>Invalid calendar values such as impossible dates or times</li>
 * </ul>
 */
public class LunaException extends Exception {
    /**
     * Creates an exception with the given message.
     *
     * @param message error message.
     */
    public LunaException(String message) {
        super(message);
    }
}
