package luna.ui;

import java.util.Scanner;

import luna.task.Task;
import luna.task.TaskList;

/**
 * Handles all interactions with the user (I/O).
 * Prints messages to standard output and reads commands from standard input.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________\n";
    private final Scanner scanner;

    /**
     * Creates a Ui instance that reads from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints the horizontal separator line.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Shows the welcome message.
     */
    public void showWelcome() {
        System.out.println("Hello! I'm Luna");
        System.out.println("What can I do for you?\n");
    }

    /**
     * Shows the exit message.
     */
    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Shows an error message.
     *
     * @param message error message to show.
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Reads the next line of user input (trimmed).
     *
     * @return user input as a trimmed string
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Prints all tasks currently in the task list.
     *
     * @param tasks task list to display
     */
    public void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Shows confirmation that a task was added.
     *
     * @param task the task that was added
     * @param totalTasks updated number of tasks in the list
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Shows confirmation that a task was removed.
     *
     * @param task the task that was removed
     * @param totalTasks updated number of tasks in the list
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println(task);
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Shows confirmation that a task was marked as done.
     *
     * @param task the task that was marked
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    /**
     * Shows confirmation that a task was marked as not done.
     *
     * @param task the task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    /**
     * Closes the underlying scanner.
     * Call this when the program is exiting.
     */
    public void close() {
        scanner.close();
    }
}
