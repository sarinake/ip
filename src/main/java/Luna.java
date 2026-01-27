import java.util.Scanner;

public class Luna {
    public static void main(String[] args) {
        String[] tasks = new String[100];
        int count = 0;

        Scanner sc = new Scanner(System.in);
        final String LINE = "____________________________________________________________\n";

        System.out.println("Hello! I'm Luna");
        System.out.println("What can I do for you?");

        while (true) {
            String input = sc.nextLine();
            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            } else if (input.equals("list")) {
                for (int i = 0; i < count; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
                System.out.println(LINE);
                continue;
            }

            tasks[count] = input;
            count++;
            System.out.println("added: " + input);
            System.out.println(LINE);
        }
        sc.close();
    }
}
