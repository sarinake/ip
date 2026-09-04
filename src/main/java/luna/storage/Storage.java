package luna.storage;

import java.io.File;
import java.io.FileReader; // read characters one by one
import java.io.FileWriter; // writes characters one by one
import java.io.BufferedReader; // reads whole lines efficiently
import java.io.BufferedWriter; // writes whole lines efficiently
import java.io.IOException;

import java.util.ArrayList;

import luna.task.Task;
import luna.exception.LunaException;

/**
 * Saves and loads tasks to/from a local text file.
 *
 * <p>File format (one task per line):
 * <pre>
 * T | 1 | read book
 * D | 0 | return book | Sunday
 * E | 0 | project meeting | Mon 2pm | 4pm
 * </pre>
 */
public class Storage {
    private static final String FILE_PATH = "data/luna.txt"; // OS-independent relative path
    private final File file;

    /**
     * Creates a Storage that reads/writes to {@code data/luna.txt}.
     *
     * <p>Note: {@link File} does not create the file on disk. It only represents a path.
     * The folder/file is created when {@link #save(ArrayList)} is called.
     */
    public Storage() {
        this.file = new File(FILE_PATH);
    }

    /**
     * Loads tasks from the data file.
     *
     * <p>If the data file does not exist (e.g. first run), an empty list is returned.
     *
     * @return tasks loaded from disk
     * @throws LunaException if reading fails or if the file contains an invalid line
     */
    public ArrayList<Task> load() throws LunaException {
        ArrayList<Task> tasks = new ArrayList<>();

        /** 
         * If there's no data file, simply return the empty list of tasks.
         * No need to create the file just yet.
         */
        if (!file.exists()) {
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    tasks.add(Task.fromFileLine(line)); // OOP parsing
                }
            }
        } catch (IOException e) {
            throw new LunaException("Unable to load tasks: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Saves all tasks to the data file.
     *
     * <p>Creates the {@code data/} folder and the data file if they do not exist.
     *
     * @param tasks tasks to save
     * @throws LunaException if writing fails or the folder/file cannot be created
     */
    public void save(ArrayList<Task> tasks) throws LunaException {
        ensureFileExists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : tasks) {
                writer.write(task.toFileString()); // OOP serialising (convert object into file storable format)
                writer.newLine();
            }
        } catch (IOException e) {
            throw new LunaException("Unable to save tasks: " + e.getMessage());
        }
    }

    /**
     * Ensures the data file exists and is ready for reading/writing.
     *
     * @throws LunaException if the folder/file cannot be created
     */
    private void ensureFileExists() throws LunaException {
        try {
            file.getParentFile().mkdir(); // create ./data folder if missing
            file.createNewFile(); // create data file if missing
        } catch (IOException e) {
            throw new LunaException("Unable to create folder or file:" + e.getMessage());
        }
    }
}