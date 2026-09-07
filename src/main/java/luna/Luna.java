package luna;

import java.time.LocalDate;

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
 * Reads commands from standard input and prints responses to standard output.
 */
public class Luna {
    private final TaskList tasks;
    private final Storage storage = new Storage();
    private final Ui ui = new Ui();

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
    }

    /**
     * Starts the application.
     *
     * @param args command-line arguments (unused).
     */
    public static void main(String[] args) {
        new Luna().run();
    }

    /**
     * Runs the main program loop: reads commands and executes them until "bye".
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();

            try {
                String[] parts = Parser.parse(input);
                String command = parts[0];
                String rest = parts[1];

                if (command.equals("bye")) {
                    ui.showBye();
                    break;
                }
                handleCommand(command, rest);
            } catch (LunaException e) {
                ui.showError(e.getMessage());
            }

            ui.showLine();
        }

        ui.close();
    }

    /**
     * Executes a single user command.
     *
     * @param command Command word from user input (e.g. {@code todo}).
     * @param rest Rest of user input after command word.
     * @throws LunaException If command is invalid or cannot be executed.
     */
    public void handleCommand(String command, String rest) throws LunaException {
        switch (command) {
            case "list":
                ui.showTaskList(tasks);
                break;
            case "mark":
                markTask(rest);
                break;
            case "unmark":
                unmarkTask(rest);
                break;
            case "delete":
                deleteTask(rest);
                break;
            case "todo":
                addTodo(rest);
                break;
            case "deadline":
                addDeadline(rest);
                break;
            case "event":
                addEvent(rest);
                break;
            default:
                throw new LunaException("I'm sorry, I don't know what that means.");
        }
    }

    /**
     * Marks the task at the given task number as done.
     *
     * @param rest Rest of user input after command word (e.g. {@code todo}).
     * @throws LunaException If task number is invalid or task is already marked as done.
     */
    private void markTask(String rest) throws LunaException {
        int index = Parser.parseIndex(rest, "mark", tasks.size());
        Task task = tasks.get(index);

        if (task.isDone()) {
            throw new LunaException("This task is already marked as done.");
        }

        task.markDone();
        storage.save(tasks.getUnmodifiableList());
        ui.showTaskMarked(task);
    }

    /**
     * Unmarks the task at the given task number (marks it as not done).
     *
     * @param rest Rest of user input after command word (e.g. {@code todo}).
     * @throws LunaException If the task number is invalid or the task is not yet marked as done.
     */
    private void unmarkTask(String rest) throws LunaException {
        int index = Parser.parseIndex(rest, "unmark", tasks.size());
        Task task = tasks.get(index);

        if (!task.isDone()) {
            throw new LunaException("This task is not yet marked as done.");
        }

        task.markUndone();
        storage.save(tasks.getUnmodifiableList());
        ui.showTaskUnmarked(task);
    }

    /**
     * Deletes the task at the given task number.
     *
     * @param rest Rest of user input after command word (e.g. {@code todo}).
     * @throws LunaException If the task number is invalid.
     */
    private void deleteTask(String rest) throws LunaException {
        int index = Parser.parseIndex(rest, "delete", tasks.size());
        Task removed = tasks.remove(index);
        storage.save(tasks.getUnmodifiableList());
        ui.showTaskDeleted(removed, tasks.size());
    }

    /**
     * Adds a {@code Todo} task using the given user input.
     *
     * @param rest Rest of user input after command word (e.g. {@code todo}).
     * @throws LunaException If the description is missing.
     */
    private void addTodo(String rest) throws LunaException {
        String desc = Parser.parseTodo(rest);
        addTask(new Todo(desc));
    }

    /**
     * Adds a {@code Deadline} task using the given user input.
     *
     * @param rest Rest of user input after command word (e.g. {@code todo}).
     * @throws LunaException If the input format is invalid.
     */
    private void addDeadline(String rest) throws LunaException {
        String[] parts = Parser.parseDeadline(rest); // [desc, by]
        String desc = parts[0];
        String by = parts[1];

        LocalDate byDate = Deadline.parseDate(by);
        addTask(new Deadline(desc, byDate));
    }

    /**
     * Adds an {@code Event} task using the given user input.
     *
     * @param rest Rest of user input after command word (e.g. {@code todo}).
     * @throws LunaException If the input format is invalid.
     */
    private void addEvent(String rest) throws LunaException {
        String[] parts = Parser.parseEvent(rest); // [desc, from, to]
        String desc = parts[0];
        String from = parts[1];
        String to = parts[2];

        LocalDate startDate = Event.parseDate(from);
        LocalDate endDate = Event.parseDate(to);
        if (startDate.isAfter(endDate)) {
            throw new LunaException("The start date must be before the end date");
        }

        addTask(new Event(desc, startDate, endDate));
    }

    /**
     * Adds a task to the task list and prints a confirmation message.
     *
     * @param task Task to add.
     * @throws LunaException If the task cannot be added.
     */
    public void addTask(Task task) throws LunaException {
        tasks.add(task);
        storage.save(tasks.getUnmodifiableList());
        ui.showTaskAdded(task, tasks.size());
    }
}
