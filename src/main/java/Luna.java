import java.util.Scanner;

public class Luna {
    private Task[] tasks = new Task[100];
    private int count = 0;

    private static final String LINE = "____________________________________________________________\n";

    public static void main(String[] args) {
        new Luna().run();
    }

    public void run() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Hello! I'm Luna");
        System.out.println("What can I do for you?");

        while (true) {
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            } else if (input.equalsIgnoreCase("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < count; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else if (input.startsWith("mark")) {
                int index = Integer.parseInt(input.substring("mark ".length()).trim()) - 1;
                tasks[index].markDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(tasks[index]);
            } else if (input.startsWith("unmark")) {
                int index = Integer.parseInt(input.substring("unmark ".length()).trim()) - 1;
                tasks[index].markUndone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(tasks[index]);
            } else if (input.startsWith("todo")) {
                String desc = input.substring("todo ".length()).trim();
                addTask(new Todo(desc));
            } else if (input.startsWith("deadline")) {
                String[] arr = input.substring("deadline ".length()).trim().split(" /by ");
                String desc = arr[0];
                String deadline = arr[1];
                addTask(new Deadline(desc, deadline));
            } else if (input.startsWith("event")) {
                String rest = input.substring("event ".length()).trim();
                int fromIndex = rest.indexOf(" /from ");
                int toIndex = rest.indexOf(" /to ");

                String desc = rest.substring(0, fromIndex);
                String startDate = rest.substring(fromIndex + " /from ".length(), toIndex);
                String endDate = rest.substring(toIndex + " /to ".length());
                addTask(new Event(desc, startDate, endDate));
            }

            System.out.println(LINE);
        }

        sc.close();
    }

    public void addTask(Task task) {
        tasks[count] = task;
        count++;
        System.out.println("Got it. I've added this task:");
        System.out.println(tasks[count - 1]);
        System.out.println("Now you have " + count + " tasks in the list.");
    }
}
