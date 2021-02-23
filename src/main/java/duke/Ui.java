package duke;

import java.util.Scanner;

import duke.locale.English;

/**
 * This Ui class handles all user input/output and has the necessary formatting functionality.
 * It should be closed properly at the end of the program using close().
 */
public class Ui implements AutoCloseable {
    public static final String LONG_LINE = "------------------------------------------------------------";

    // Default indentation is 8 whitespaces
    public static final char DEFAULT_INDENT_CHARACTER = '\t';
    public static final int DEFAULT_INDENT_COUNT = 1;
    // Used for internal 2nd-level indentation (e.g. print task)
    public static final String INTERNAL_INDENT = "\t";

    public static final String LOCALE_CLASS_PREFIX = "duke.locale";
    public static final String DEFAULT_LOCALE = "English";
    public static final Class<?> DEFAULT_LOCALE_CLASS = English.class;

    protected String indent;
    protected Scanner scanner;
    protected Class<?> locale;

    /**
     * Constructor of Ui class
     * When no arguments are supplied, default indent and locale setting are used
     */
    public Ui() {
        this(DEFAULT_INDENT_CHARACTER, DEFAULT_INDENT_COUNT, DEFAULT_LOCALE);
    }

    /**
     * Constructor of Ui class
     * @param indentCharacter The specific character for indentation
     * @param indentCount Number of `indentCharacter` to be used for indentation
     * @param locale Locale of all output messages (fallback option: English)
     * @see English
     */
    public Ui(char indentCharacter, int indentCount, String locale) {
        indent = new String(new char[indentCount]).replace('\0', indentCharacter);
        scanner = new Scanner(System.in);

        // Load locale from class
        String className = LOCALE_CLASS_PREFIX + locale;
        try {
            this.locale = Class.forName(className);
        } catch (ClassNotFoundException e) {
            // Cannot load a locale class, fall back to English
            this.locale = DEFAULT_LOCALE_CLASS;
        }
    }

    protected String getLocaleText(String key, Object ... args) {
        try {
            return String.format((String) locale.getDeclaredField(key).get(null), args);
        } catch (Exception e) {
            // If it fails, fall back to default locale
            try {
                return String.format((String) DEFAULT_LOCALE_CLASS.getDeclaredField(key).get(null), args);
            } catch (Exception ex) {
                // Critical error - we cannot even get this key from the default locale
                // Print out the error and return an empty string
                printException(ex);
                return "";
            }
        }
    }

    /**
     * Close the scanner instance initialized in constructor
     */
    @Override
    public void close() {
        scanner.close();
        scanner = null;
    }

    /**
     * Read a line (delimited by EOL characters)
     * @return the line in the format of a string
     */
    public String read() {
        if (scanner == null || !scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine();
    }

    /**
     * Print lines of text (lines are broken by \n)
     * @param text Text to print, format specifiers can be used
     * @param args If format specifiers are used in `text`, objects need to be provided
     */
    public void print(String text, Object ... args) {
        // Pass to String.format to format the text using varargs provided
        String[] lines = String.format(text, args).split("\n");
        for (String line : lines) {
            System.out.println(indent + line);
        }
    }

    /**
     * Print a message for a successful insertion of task
     * @param tasks The full task list (after insertion)
     */
    public void printNewTask(TaskList tasks) {
        int size = tasks.size();
        print(getLocaleText("NEW_TASK", INTERNAL_INDENT + tasks.get(size - 1), size));
    }

    /**
     * Print out an ordinary task list (with no additional information)
     * @param tasks The task list to be printed
     */
    public void printTaskList(TaskList tasks) {
        printTaskList(tasks, "");
    }

    /**
     * Print out a task list, showing the at/by time specified
     * @param tasks The task list (result) to be printed
     * @param dateTime The at/by time specified by the user for search
     */
    public void printTaskList(TaskList tasks, DateTime dateTime) {
        printTaskList(tasks, getLocaleText("TASK_LIST_AT_BY", dateTime));
    }

    /**
     * Print out a task list, showing additional text
     * @param tasks The task list to be printed
     * @param additionalText Additional text which will be appended at the end of the message
     */
    public void printTaskList(TaskList tasks, String additionalText) {
        if (tasks.size() == 0) {
            print(getLocaleText("TASK_LIST_EMPTY", additionalText));
            return;
        }
        print(getLocaleText("TASK_LIST", additionalText));
        for (int i = 0; i < tasks.size(); i += 1) {
            print("%d.%s%s", i + 1, INTERNAL_INDENT, tasks.get(i));
        }
    }

    /**
     * Print a long horizontal line
     */
    public void printLine() {
        print(LONG_LINE);
    }

    /**
     * Print an exception related to save-file loading exception
     * @param filepath Path of the save-file
     * @param e The exception object
     */
    public void printSaveException(String filepath, Exception e) {
        printException(e);
        print(getLocaleText("SAVE_EXCEPTION", filepath));
        printLine();
    }

    /**
     * Print a general exception encountered
     * @param e The exception object
     */
    public void printException(Exception e) {
        print(getLocaleText("EXCEPTION", e));
    }

    /**
     * Print a welcome message
     */
    public void printWelcome() {
        print(getLocaleText("GREETING", new DateTime("31/01/2021 23:59")));
        printLine();
    }

    /**
     * Print a farewell message
     */
    public void printGoodbye() {
        print(getLocaleText("FAREWELL"));
    }
}
