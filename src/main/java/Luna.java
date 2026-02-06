import java.util.Scanner;
import java.util.ArrayList;

public class Luna {
    private ArrayList<Task> tasks = new ArrayList<>(100);
    private static final String LINE = "____________________________________________________________\n";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Luna luna = new Luna();

        System.out.println("Hello! I'm Luna");
        System.out.println("What can I do for you?");

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

    public void handleCommand(String input) throws LunaException {
        if (input.equalsIgnoreCase("list")) {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        } else if (input.startsWith("mark")) {
            int index = parseIndex(input, "mark");
            Task task = tasks.get(index);
            if (task.isDone()) {
                throw new LunaException("This task is already marked as done.");
            }
            task.markDone();
            System.out.println("Nice! I've marked this task as done:");
            System.out.println(task);
        } else if (input.startsWith("unmark")) {
            int index = parseIndex(input, "unmark");
            Task task = tasks.get(index);
            if (!task.isDone()) {
                throw new LunaException("This task is not yet marked as done.");
            }
            task.markUndone();
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println(task);
        } else if (input.startsWith("delete")) {
            int index = parseIndex(input, "delete");
            Task task = tasks.get(index);
            tasks.remove(task);
            System.out.println("Noted. I've removed this task:");
            System.out.println(task);
            System.out.println("Now you have " + tasks.size() + " in the list.");
        } else if (input.startsWith("todo")) {
            String desc = input.substring("todo".length()).trim();
            if (desc.isEmpty()) {
                throw new LunaException("The description of a todo cannot be empty. Example: todo read book");
            }

            addTask(new Todo(desc));
        } else if (input.startsWith("deadline")) {
            String rest = input.substring("deadline".length()).trim();
            if (rest.isEmpty() || !rest.contains(" /by ")) {
                throw new LunaException("A deadline must include a description and end date in this format: " +
                        "deadline <desc> /by <end>\n" +
                        "Example: deadline return book /by Sunday");
            }

            int byPos = rest.indexOf(" /by ");
            String desc = rest.substring(0, byPos).trim();
            String by = rest.substring(byPos + " /by ".length()).trim();
            if (desc.isEmpty() || by.isEmpty()) {
                throw new LunaException("A deadline must include a description and end date in this format: " +
                        "deadline <desc> /by <end>\n" +
                        "Example: deadline return book /by Sunday");
            }

            addTask(new Deadline(desc, by));
        } else if (input.startsWith("event")) {
            String rest = input.substring("event".length()).trim();
            if (rest.isEmpty() || !rest.contains(" /from ") || !rest.contains(" /to ")) {
                throw new LunaException("An event must include a description, start date, and end date in this format: " +
                        "event <desc> /from <start> /to <end>\n" +
                        "Example: event project meeting /from Mon 2pm /to 4pm");
            }

            int fromPos = rest.indexOf(" /from ");
            int toPos = rest.indexOf(" /to ");

            String desc = rest.substring(0, fromPos).trim();
            String from = rest.substring(fromPos + " /from ".length(), toPos).trim();
            String to = rest.substring(toPos + " /to ".length()).trim();
            if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new LunaException("An event must include a description, start date, and end date in this format: " +
                        "event <desc> /from <start> /to <end>\n" +
                        "Example: event project meeting /from Mon 2pm /to 4pm");
            }

            addTask(new Event(desc, from, to));
        } else {
            throw new LunaException("I'm sorry, I don't know what that means.");
        }

        System.out.println(LINE);
    }

    // input is the overall String, command is "mark" or "unmark"
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

    public void addTask(Task task) throws LunaException {
        tasks.add(task);
        System.out.println("Got it. I've added this task:");
        System.out.println(tasks.get(tasks.size() - 1));
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }
}
