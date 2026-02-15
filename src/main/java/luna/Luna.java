package luna;

import java.util.Scanner;
import java.util.ArrayList;

/**
 * Runs the Luna task manager.
 * Reads commands from standard input and prints responses to standard output.
 */
public class Luna {
    private final ArrayList<Task> tasks = new ArrayList<>(100);
    private static final String LINE = "____________________________________________________________\n";

    private static final String DEADLINE_FORMAT_MESSAGE = "A deadline must include a description and end date in "
            + "this format: deadline <desc> /by <end>\n"
            + "Example: deadline return book /by Sunday";

    private static final String EVENT_FORMAT_MESSAGE = "An event must include a description, start date, and end date in "
            + "this format: event <desc> /from <start> /to <end>\n"
            + "Example: event project meeting /from Mon 2pm /to 4pm";
    
    /**
     * Starts the application.
     *
     * @param args command-line arguments (unused).
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Luna luna = new Luna();

        System.out.println("Hello! I'm Luna");
        System.out.println("What can I do for you?\n");

        while (true) {
            String input = sc.nextLine().trim();

            try {
                if (input.equalsIgnoreCase("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    break;
                }

                luna.handleCommand(input);
            } catch (LunaException e) {
                System.out.println(e.getMessage());
                System.out.println(LINE);
            }
        }

        sc.close();
    }

    /**
     * Executes a single user command.
     *
     * @param input raw user input
     * @throws LunaException if the command is invalid or cannot be executed
     */
    public void handleCommand(String input) throws LunaException {
        if (input.equalsIgnoreCase("list")) {
            listTasks();
        } else if (input.startsWith("mark")) {
            markTask(input);
        } else if (input.startsWith("unmark")) {
            unmarkTask(input);
        } else if (input.startsWith("delete")) {
            deleteTask(input);
        } else if (input.startsWith("todo")) {
            addTodo(input);
        } else if (input.startsWith("deadline")) {
            addDeadline(input);
        } else if (input.startsWith("event")) {
            addEvent(input);
        } else {
            throw new LunaException("I'm sorry, I don't know what that means.");
        }

        System.out.println(LINE);
    }

    /**
     * Prints all tasks currently in the task list.
     */
    private void listTasks() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Marks the task at the given task number as done.
     *
     * @param input raw user input
     * @throws LunaException if the task number is invalid or the task is already marked as done
     */
    private void markTask(String input) throws LunaException {
        int index = parseIndex(input, "mark");
        Task task = tasks.get(index);

        if (task.isDone()) {
            throw new LunaException("This task is already marked as done.");
        }

        task.markDone();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    /**
     * Unmarks the task at the given task number (marks it as not done).
     *
     * @param input raw user input
     * @throws LunaException if the task number is invalid or the task is not yet marked as done
     */
    private void unmarkTask(String input) throws LunaException {
        int index = parseIndex(input, "unmark");
        Task task = tasks.get(index);

        if (!task.isDone()) {
            throw new LunaException("This task is not yet marked as done.");
        }

        task.markUndone();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    /**
     * Deletes the task at the given task number.
     *
     * @param input raw user input
     * @throws LunaException if the task number is invalid
     */
    private void deleteTask(String input) throws LunaException {
        int index = parseIndex(input, "delete");
        Task task = tasks.remove(index);

        System.out.println("Noted. I've removed this task:");
        System.out.println(task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Adds a {@code Todo} task using the given user input.
     *
     * @param input raw user input
     * @throws LunaException if the description is missing
     */
    private void addTodo(String input) throws LunaException {
        String desc = input.substring("todo".length()).trim();
        if (desc.isEmpty()) {
            throw new LunaException("The description of a todo cannot be empty. Example: todo read book");
        }

        addTask(new Todo(desc));
    }

    /**
     * Adds a {@code Deadline} task using the given user input.
     *
     * @param input raw user input
     * @throws LunaException if the input format is invalid
     */
    private void addDeadline(String input) throws LunaException {
        String rest = input.substring("deadline".length()).trim();

        if (rest.isEmpty() || !rest.contains(" /by ")) {
            throw new LunaException(DEADLINE_FORMAT_MESSAGE);
        }

        int byPos = rest.indexOf(" /by ");
        String desc = rest.substring(0, byPos).trim();
        String by = rest.substring(byPos + " /by ".length()).trim();

        if (desc.isEmpty() || by.isEmpty()) {
            throw new LunaException(DEADLINE_FORMAT_MESSAGE);
        }

        addTask(new Deadline(desc, by));
    }

    /**
     * Adds an {@code Event} task using the given user input.
     *
     * @param input raw user input
     * @throws LunaException if the input format is invalid
     */
    private void addEvent(String input) throws LunaException {
        String rest = input.substring("event".length()).trim();

        if (rest.isEmpty() || !rest.contains(" /from ") || !rest.contains(" /to ")) {
            throw new LunaException(EVENT_FORMAT_MESSAGE);
        }

        int fromPos = rest.indexOf(" /from ");
        int toPos = rest.indexOf(" /to ");

        if (fromPos >= toPos) {
            throw new LunaException(EVENT_FORMAT_MESSAGE);
        }

        String desc = rest.substring(0, fromPos).trim();
        String from = rest.substring(fromPos + " /from ".length(), toPos).trim();
        String to = rest.substring(toPos + " /to ".length()).trim();

        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new LunaException(EVENT_FORMAT_MESSAGE);
        }

        addTask(new Event(desc, from, to));
    }

    /**
     * Extracts and parses the task index from a user input for {@code mark} / {@code unmark}.
     * Converts the user-provided task number (1-based) into a 0-based index for internal use.
     *
     * @param input raw user input (e.g. {@code "mark 2"} or {@code "unmark 3"}).
     * @param command command keyword (e.g. {@code "mark"}).
     * @return 0-based index corresponding to the task number in the input.
     * @throws LunaException if the task number is missing, not an integer, or out of range.
     */
    private int parseIndex(String input, String command) throws LunaException {
        String rest = input.substring(command.length()).trim();
        if (rest.isEmpty()) {
            throw new LunaException("Please provide a task number. Example: " + command + " 2");
        }

        int num = 0;
        try {
            num = Integer.parseInt(rest);
        } catch (NumberFormatException e) {
            throw new LunaException("Task number must be an integer. Example: " + command + " 2");
        }

        if (num < 1 || num > tasks.size()) {
            throw new LunaException("Task number is out of range. Use 1 to " + tasks.size() + ".");
        }
        
        return num - 1;
    }

    /**
     * Adds a task to the task list and prints a confirmation message.
     *
     * @param task task to add.
     * @throws LunaException if the task cannot be added.
     */
    public void addTask(Task task) throws LunaException {
        tasks.add(task);
        System.out.println("Got it. I've added this task:");
        System.out.println(tasks.get(tasks.size() - 1));
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }
}
