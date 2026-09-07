package luna.parser;

import luna.exception.LunaException;

/**
 * Parses raw user input into command words and arguments.
 * Validates input format before Luna tries to execute anything.
 */
public class Parser {
    private static final String FORMAT_MESSAGE_DEADLINE = "A deadline must include a description and end date in "
            + "this format: deadline <desc> /by <end>\n"
            + "Example: deadline return book /by 2019-10-15";

    private static final String FORMAT_MESSAGE_EVENT = "An event must include a description, start date, and "
            + "end date in "
            + "this format: event <desc> /from <start> /to <end>\n"
            + "Example: event project meeting /from 2019-10-15 /to 2019-10-16";

    /**
     * Splits raw input into [commandWord, rest of arguments].
     * Example: [todo, description].
     *
     * @param input Raw user input.
     * @return String array of size 2: [commandWord, rest of arguments].
     * @throws LunaException If user input is empty.
     */
    public static String[] parse(String input) throws LunaException {
        input = input.trim();
        if (input.isEmpty()) {
            throw new LunaException("Please enter a command");
        }

        String[] parts = input.split("\\s+", 2); // Splits input by one or more whitespaces
        String command = parts[0].toLowerCase();
        String rest = (parts.length == 2) ? parts[1].trim() : "";

        return new String[] {command, rest};
    }

    /**
     * Extracts and parses the task index from {@code rest} for {@code mark} / {@code unmark}.
     * Converts the user-provided task number (1-based) into a 0-based index for internal use.
     *
     * @param rest Rest of user input after {@code mark} or {@code unmark}.
     * @param command Command word (e.g. {@code mark}).
     * @param listSize Current number of tasks.
     * @return 0-based index.
     * @throws LunaException If the task number is missing/not a number/out of range.
     */
    public static int parseIndex(String rest, String command, int listSize) throws LunaException {
        rest = (rest == null) ? "" : rest.trim();
        if (rest.isEmpty()) {
            throw new LunaException("Please provide a task number. Example: " + command + " 2");
        }

        int num;
        try {
            num = Integer.parseInt(rest);
        } catch (NumberFormatException e) {
            throw new LunaException("Task number must be an integer. Example: " + command + " 2");
        }

        if (num < 1 || num > listSize) {
            throw new LunaException("Task number is out of range. Use 1 to " + listSize + ".");
        }

        return num - 1;
    }

    /**
     * Parses the keyword for the user command {@code find}.
     *
     * @param rest Rest of user input after {@code find}.
     * @return Keyword to search for.
     * @throws LunaException If the keyword is missing.
     */
    public static String parseKeyword(String rest) throws LunaException {
        String keyword = (rest == null) ? "" : rest.trim();
        if (keyword.isEmpty()) {
            throw new LunaException("Please provide a keyword to search for. Example: find book");
        }
        return keyword;
    }

    /**
     * Parses the description of a todo.
     *
     * @param rest Rest of user input after {@code todo}.
     * @return Description.
     * @throws LunaException If rest of the user input is empty.
     */
    public static String parseTodo(String rest) throws LunaException {
        String desc = (rest == null) ? "" : rest.trim();
        if (desc.isEmpty()) {
            throw new LunaException("The description of a todo cannot be empty. Example: todo read book");
        }
        return desc;
    }

    /**
     * Parses the description and deadline of a todo.
     *
     * @param rest Rest of user input after {@code deadline}.
     * @return String array of size 2: [description, by].
     * @throws LunaException If input format is invalid.
     */
    public static String[] parseDeadline(String rest) throws LunaException {
        rest = (rest == null) ? "" : rest.trim();
        if (rest.isEmpty() || !rest.contains(" /by ")) {
            throw new LunaException(FORMAT_MESSAGE_DEADLINE);
        }

        int byPos = rest.indexOf(" /by ");
        String desc = rest.substring(0, byPos).trim();
        String by = rest.substring(byPos + " /by ".length()).trim();
        if (desc.isEmpty() || by.isEmpty()) {
            throw new LunaException(FORMAT_MESSAGE_DEADLINE);
        }

        return new String[] {desc, by};
    }

    /**
     * Parses the description, startDate, and endDate of an event.
     *
     * @param rest Rest of user input after {@code event}.
     * @return String array of size 3: [description, startDate, endDate].
     * @throws LunaException If input format is invalid.
     */
    public static String[] parseEvent(String rest) throws LunaException {
        rest = (rest == null) ? "" : rest.trim();
        if (rest.isEmpty() || !rest.contains(" /from ") || !rest.contains(" /to ")) {
            throw new LunaException(FORMAT_MESSAGE_EVENT);
        }

        int fromPos = rest.indexOf(" /from ");
        int toPos = rest.indexOf(" /to ");
        if (fromPos >= toPos) {
            throw new LunaException(FORMAT_MESSAGE_EVENT);
        }

        String desc = rest.substring(0, fromPos).trim();
        String from = rest.substring(fromPos + " /from ".length(), toPos).trim();
        String to = rest.substring(toPos + " /to ".length()).trim();
        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new LunaException(FORMAT_MESSAGE_EVENT);
        }

        return new String[] {desc, from, to};
    }
}
