import java.util.Scanner;

public class Luna {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Task[] tasks = new Task[100];
        int count = 0;

        final String LINE = "____________________________________________________________\n";

        System.out.println("Hello! I'm Luna");
        System.out.println("What can I do for you?");

        while (true) {
            String input = sc.nextLine();

            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            } else if (input.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < count; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else if (input.startsWith("mark")) {
                int index = Integer.parseInt(input.substring(5)) - 1;
                tasks[index].markDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(tasks[index]);
            } else if (input.startsWith("unmark")) {
                int index = Integer.parseInt(input.substring(7)) - 1;
                tasks[index].markUndone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(tasks[index]);
            } else {
                tasks[count] = new Task(input);
                count++;
                System.out.println("added: " + input);
            }

            System.out.println(LINE);
        }

        sc.close();
    }
}
