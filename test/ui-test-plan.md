# UI Test Plan

This is the canonical record of scripted command-line UI test cases for Luna.
Run the cases and commands in the order shown. The semicolons in the deadline
and event inputs are literal characters. Parenthetical notes from the requested
input list describe checks and are not entered into the program.

## Test environment

- Java version: Java 25
- Entry point: `luna.Luna`
- Working directory: one isolated temporary directory shared by all three
  sessions, initially without `data/luna.txt`
- Comparison: exact text, punctuation, line order, and blank lines; only LF/CRLF
  differences and one final newline are ignored
- Session 3 continues from the persisted data created by session 2

## UI-001: Unknown command and exit

**Aim:** Verify that an unknown command is rejected and that `bye` exits the
application cleanly.

**Preconditions:** No `data/luna.txt` file.

**Inputs and expected outputs:**

### Command 1

**Input:**

```text
asdkjgsd
```

**Expected output:**

```text
Hello! I'm Luna
What can I do for you?

I'm sorry, I don't know what that means.
____________________________________________________________

```

### Command 2

**Input:**

```text
bye
```

**Expected output:**

```text
Bye. Hope to see you again soon!
```

## UI-002: Task commands and validation

**Aim:** Verify validation for todo, deadline, event, mark, and unmark commands;
verify adding, listing, marking, unmarking, and deleting tasks; and leave the
resulting task list saved for the persistence test.

**Preconditions:** Start a new process in the same isolated working directory
used for UI-001. UI-001 must not have created `data/luna.txt`.

**Inputs and expected outputs:**

### Command 1

**Input:**

```text
todo
```

**Expected output:**

```text
Hello! I'm Luna
What can I do for you?

The description of a todo cannot be empty. Example: todo read book
____________________________________________________________

```

### Command 2

**Input:**

```text
todo read book
```

**Expected output:**

```text
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________

```

### Command 3

**Input:**

```text
deadline
```

**Expected output:**

```text
A deadline must include a description and end date in this format: deadline <desc> /by <end>
Example: deadline return book /by Sunday
____________________________________________________________

```

### Command 4

**Input:**

```text
deadline asdkjgsd
```

**Expected output:**

```text
A deadline must include a description and end date in this format: deadline <desc> /by <end>
Example: deadline return book /by Sunday
____________________________________________________________

```

### Command 5

**Input:**

```text
deadline return book /by Sunday;
```

**Expected output:**

```text
Got it. I've added this task:
[D][ ] return book (by: Sunday;)
Now you have 2 tasks in the list.
____________________________________________________________

```

### Command 6

**Input:**

```text
event
```

**Expected output:**

```text
An event must include a description, start date, and end date in this format: event <desc> /from <start> /to <end>
Example: event project meeting /from Mon 2pm /to 4pm
____________________________________________________________

```

### Command 7

**Input:**

```text
event asdkjgsd
```

**Expected output:**

```text
An event must include a description, start date, and end date in this format: event <desc> /from <start> /to <end>
Example: event project meeting /from Mon 2pm /to 4pm
____________________________________________________________

```

### Command 8

**Input:**

```text
event project meeting /from Mon 2pm /to 4pm;
```

**Expected output:**

```text
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm;)
Now you have 3 tasks in the list.
____________________________________________________________

```

### Command 9 — Check the three added tasks

**Input:**

```text
list
```

**Expected output:**

```text
Here are the tasks in your list:
1. [T][ ] read book
2. [D][ ] return book (by: Sunday;)
3. [E][ ] project meeting (from: Mon 2pm to: 4pm;)
____________________________________________________________

```

### Command 10

**Input:**

```text
mark
```

**Expected output:**

```text
Please provide a task number. Example: mark 2
____________________________________________________________

```

### Command 11

**Input:**

```text
mark asdkjgsd
```

**Expected output:**

```text
Task number must be an integer. Example: mark 2
____________________________________________________________

```

### Command 12

**Input:**

```text
mark 0
```

**Expected output:**

```text
Task number is out of range. Use 1 to 3.
____________________________________________________________

```

### Command 13

**Input:**

```text
mark 4
```

**Expected output:**

```text
Task number is out of range. Use 1 to 3.
____________________________________________________________

```

### Command 14

**Input:**

```text
unmark
```

**Expected output:**

```text
Please provide a task number. Example: unmark 2
____________________________________________________________

```

### Command 15

**Input:**

```text
unmark asdkjgsd
```

**Expected output:**

```text
Task number must be an integer. Example: unmark 2
____________________________________________________________

```

### Command 16

**Input:**

```text
unmark 0
```

**Expected output:**

```text
Task number is out of range. Use 1 to 3.
____________________________________________________________

```

### Command 17

**Input:**

```text
unmark 4
```

**Expected output:**

```text
Task number is out of range. Use 1 to 3.
____________________________________________________________

```

### Command 18

**Input:**

```text
unmark 1
```

**Expected output:**

```text
This task is not yet marked as done.
____________________________________________________________

```

### Command 19

**Input:**

```text
mark 1
```

**Expected output:**

```text
Nice! I've marked this task as done:
[T][X] read book
____________________________________________________________

```

### Command 20

**Input:**

```text
mark 2
```

**Expected output:**

```text
Nice! I've marked this task as done:
[D][X] return book (by: Sunday;)
____________________________________________________________

```

### Command 21

**Input:**

```text
unmark 2
```

**Expected output:**

```text
OK, I've marked this task as not done yet:
[D][ ] return book (by: Sunday;)
____________________________________________________________

```

### Command 22 — Check task statuses

**Input:**

```text
list
```

**Expected output:**

```text
Here are the tasks in your list:
1. [T][X] read book
2. [D][ ] return book (by: Sunday;)
3. [E][ ] project meeting (from: Mon 2pm to: 4pm;)
____________________________________________________________

```

### Command 23

**Input:**

```text
delete 3
```

**Expected output:**

```text
Noted. I've removed this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm;)
Now you have 2 tasks in the list.
____________________________________________________________

```

### Command 24 — Check deletion

**Input:**

```text
list
```

**Expected output:**

```text
Here are the tasks in your list:
1. [T][X] read book
2. [D][ ] return book (by: Sunday;)
____________________________________________________________

```

### Command 25

**Input:**

```text
bye
```

**Expected output:**

```text
Bye. Hope to see you again soon!
```

## UI-003: Reload persisted tasks

**Aim:** Reopen Luna after UI-002 exits and verify that the remaining tasks and
their completion states were stored and restored.

**Preconditions:** Start a new process in the same isolated working directory
immediately after UI-002. Its `data/luna.txt` must contain the state saved by
UI-002.

**Inputs and expected outputs:**

### Command 1 — Check restored tasks

**Input:**

```text
list
```

**Expected output:**

```text
Hello! I'm Luna
What can I do for you?

Here are the tasks in your list:
1. [T][X] read book
2. [D][ ] return book (by: Sunday;)
____________________________________________________________

```

After this expected output is observed, the test harness terminates the process
without sending another application command. This cleanup action is not part of
the ordered input list.
