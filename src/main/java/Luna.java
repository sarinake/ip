import java.util.Scanner;

public class Luna {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        final String LINE = "____________________________________________________________\n";

        System.out.println("Hello! I'm Luna\n");
        System.out.println("What can I do for you?\n");

        while (true) {
            String input = sc.nextLine();
            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }
            System.out.println(input);
            System.out.println(LINE);
        }
        sc.close();
    }
}
