package luna.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the tasks managed by Luna and provides operations for accessing them.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>(100);
    }

    /**
     * Creates a task list containing the tasks in the given list.
     * Changes to the given list after construction do not affect this task list.
     *
     * @param tasks tasks to include in the new task list
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified zero-based index.
     *
     * @param index zero-based index of the task to remove
     * @return removed task
     * @throws IndexOutOfBoundsException if the index is outside the list
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index zero-based index of the task to retrieve
     * @return task at the specified index
     * @throws IndexOutOfBoundsException if the index is outside the list
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an unmodifiable snapshot of the tasks currently in the list.
     * Later changes to this task list are not reflected in the returned list.
     *
     * @return unmodifiable snapshot of the tasks
     */
    public List<Task> getUnmodifiableList() {
        return List.copyOf(tasks);
    }
}
