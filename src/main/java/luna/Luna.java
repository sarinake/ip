package luna;

import java.time.LocalDate;
import java.util.ArrayList;

import luna.exception.LunaException;
import luna.parser.Parser;
import luna.storage.Storage;
import luna.task.Deadline;
import luna.task.Event;
import luna.task.Task;
import luna.task.TaskList;
import luna.task.Todo;
import luna.ui.Ui;

/**
 * Runs the Luna task manager.
 * Processes commands shared by the CLI and JavaFX GUI.
 *
 * <p>CLI and GUI follow these call paths:
 * <pre>{@code
 * CLI:
 * main()
 *   → run()
 *     → getResponse()
 *       → executeInput()
 *         → handleCommand()
 *           → addTodo(), markTask(), etc.
 *
 * GUI:
 * MainWindow.handleUserInput()
 *   → getResponse()
 *     → executeInput()
 *       → handleCommand()
 *         → addTodo(), markTask(), etc.
 * }</pre>
 */
public class Luna {
    private final TaskList tasks;
    private final Storage storage = new Storage();
    private final Ui ui = new Ui();
    private boolean shouldExit;

    /**
     * Creates a Luna instance and loads saved tasks from storage.
     */
    public Luna() {
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (LunaException e) {
            ui.showError("Warning: could not load saved tasks.");
            loadedTasks = new TaskList();
        }
        this.tasks = loadedTasks;
        shouldExit = false;
    }

    /**
     * Starts the application.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new Luna().run();
    }

    /**
     * Runs the main program loop: reads commands and executes them until "bye".
     */
    public void run() {
        ui.showWelcome();

        while (!shouldExit) {
            String input = ui.readCommand();
            String response = getResponse(input);
            ui.showResponse(response);

            if (!shouldExit) {
                ui.showLine();
            }
        }

        ui.close();
    }

    /**
     * Processes one command and returns Luna's response.
     *
     * @param input User command to process.
     * @return Response to display.
     */
    public String getResponse(String input) {
        try {
            // MainWindow.java calls the public getResponse() method
            // We hide the actual implementation as a private method executeInput()
            return executeInput(input);
        } catch (LunaException e) {
            return e.getMessage();
        }
    }

    /**
     * Parses and executes one user command.
     *
     * @param input User command to execute.
     * @return Response produced by the command.
     * @throws LunaException If the command is invalid or cannot be executed.
     */
    private String executeInput(String input) throws LunaException {
        String[] parts = Parser.parse(input);
        String command = parts[0];
        String rest = parts[1];

        if (command.equals("bye")) {
            shouldExit = true;
            return "Bye. Hope to see you again soon!";
        }

        return handleCommand(command, rest);
    }

    /**
     * Returns whether Luna has received the exit command.
     *
     * @return True if Luna should exit.
     */
    public boolean isExit() {
        return shouldExit;
    }

    /**
     * Executes a single user command.
     *
     * @param command Command word from user input (e.g. {@code todo}).
     * @param rest Rest of user input after command word.
     * @return Response produced by the command.
     * @throws LunaException If command is invalid or cannot be executed.
     */
    private String handleCommand(String command, String rest) throws LunaException {
        switch (command) {
            case "list":
                return showTaskList(tasks);
            case "mark":
                return markTask(rest);
            case "unmark":
                return unmarkTask(rest);
            case "delete":
                return deleteTask(rest);
            case "find":
                return findTasks(rest);
            case "todo":
                return addTodo(rest);
            case "deadline":
                return addDeadline(rest);
            case "event":
                return addEvent(rest);
            default:
                throw new LunaException("I'm sorry, I don't know what that means.");
        }
    }

    /**
     * Formats the task list as a numbered, multi-line response.
     *
     * @param taskList Task list to format.
     * @return Formatted task-list response.
     */
    private String showTaskList(TaskList taskList) {
        StringBuilder response = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            response.append("\n")
                    .append(i + 1)
                    .append(". ")
                    .append(taskList.get(i));
        }
        return response.toString();
    }

    /**
     * Marks the task at the given task number as done.
     *
     * @param rest Rest of user input after command word (e.g. {@code mark}).
     * @return Confirmation that the task was marked.
     * @throws LunaException If task number is invalid or task is already marked as done.
     */
    private String markTask(String rest) throws LunaException {
        int index = Parser.parseIndex(rest, "mark", tasks.size());
        Task task = tasks.get(index);

        if (task.isDone()) {
            throw new LunaException("This task is already marked as done.");
        }

        task.markDone();
        storage.save(tasks.getUnmodifiableList());
        return "Nice! I've marked this task as done:\n" + task;
    }

    /**
     * Unmarks the task at the given task number (marks it as not done).
     *
     * @param rest Rest of user input after command word (e.g. {@code unmark}).
     * @return Confirmation that the task was unmarked.
     * @throws LunaException If the task number is invalid or the task is not yet marked as done.
     */
    private String unmarkTask(String rest) throws LunaException {
        int index = Parser.parseIndex(rest, "unmark", tasks.size());
        Task task = tasks.get(index);

        if (!task.isDone()) {
            throw new LunaException("This task is not yet marked as done.");
        }

        task.markUndone();
        storage.save(tasks.getUnmodifiableList());
        return "OK, I've marked this task as not done yet:\n" + task;
    }

    /**
     * Deletes the task at the given task number.
     *
     * @param rest Rest of user input after command word (e.g. {@code delete}).
     * @return Confirmation that the task was deleted.
     * @throws LunaException If the task number is invalid.
     */
    private String deleteTask(String rest) throws LunaException {
        int index = Parser.parseIndex(rest, "delete", tasks.size());
        Task removed = tasks.remove(index);
        storage.save(tasks.getUnmodifiableList());
        return "Noted. I've removed this task:\n" + removed
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Finds tasks whose descriptions contain the given keyword (case-insensitive).
     *
     * @param rest Rest of user input after command word {@code find}.
     * @return Matching tasks as a numbered, multi-line response.
     * @throws LunaException If the keyword is missing.
     */
    private String findTasks(String rest) throws LunaException {
        String keyword = Parser.parseKeyword(rest).toLowerCase();

        ArrayList<Task> matches = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String desc = task.getDescription().toLowerCase();
            if (desc.contains(keyword)) {
                matches.add(task);
            }
        }

        if (matches.isEmpty()) {
            return "(No matching tasks found.)";
        }

        StringBuilder response = new StringBuilder("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            response.append("\n")
                    .append(i + 1)
                    .append(". ")
                    .append(matches.get(i));
        }
        return response.toString();
    }

    /**
     * Adds a task to the task list and returns a confirmation message.
     *
     * @param task Task to add.
     * @return Confirmation that the task was added.
     * @throws LunaException If the task cannot be added.
     */
    public String addTask(Task task) throws LunaException {
        tasks.add(task);
        storage.save(tasks.getUnmodifiableList());
        return "Got it. I've added this task:\n" + task
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Adds a {@code Todo} task using the given user input.
     *
     * @param rest Rest of user input after command word (e.g. {@code todo}).
     * @return Confirmation that the todo was added.
     * @throws LunaException If the description is missing.
     */
    private String addTodo(String rest) throws LunaException {
        String desc = Parser.parseTodo(rest);
        return addTask(new Todo(desc));
    }

    /**
     * Adds a {@code Deadline} task using the given user input.
     *
     * @param rest Rest of user input after command word (e.g. {@code deadline}).
     * @return Confirmation that the deadline was added.
     * @throws LunaException If the input format is invalid.
     */
    private String addDeadline(String rest) throws LunaException {
        String[] parts = Parser.parseDeadline(rest); // [desc, by]
        String desc = parts[0];
        String by = parts[1];

        LocalDate byDate = Deadline.parseDate(by);
        return addTask(new Deadline(desc, byDate));
    }

    /**
     * Adds an {@code Event} task using the given user input.
     *
     * @param rest Rest of user input after command word (e.g. {@code event}).
     * @return Confirmation that the event was added.
     * @throws LunaException If the input format is invalid.
     */
    private String addEvent(String rest) throws LunaException {
        String[] parts = Parser.parseEvent(rest); // [desc, from, to]
        String desc = parts[0];
        String from = parts[1];
        String to = parts[2];

        LocalDate startDate = Event.parseDate(from);
        LocalDate endDate = Event.parseDate(to);
        if (startDate.isAfter(endDate)) {
            throw new LunaException("The start date must be before the end date");
        }

        return addTask(new Event(desc, startDate, endDate));
    }
}
