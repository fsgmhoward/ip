package duke;

import java.io.IOException;
import java.util.Arrays;

import duke.exception.InvalidInputException;
import duke.exception.InvalidInputException.InputExceptionType;
import duke.exception.SaveException;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.ToDo;

public class ActionHandler {
    // Print a greeting message when the program is invoked
    protected static void greetingHandler() {
        Helper.printlnWithIndent("Hello! I'm Duke.");
        Helper.printlnWithIndent("What can I do for you?");
    }

    // Print a goodbye message before the program exits
    protected static void byeHandler() {
        Helper.printlnWithIndent("Bye. Hope to see you again soon!");
    }

    // Print out everything in the list, index starts from 1
    protected static void listHandler(TaskList tasks) {
        if (tasks.size() == 0) {
            Helper.printlnWithIndent("You don't have a task in your list!");
            return;
        }
        Helper.printlnWithIndent("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i += 1) {
            Helper.printlnWithIndent(String.format("%d.\t%s", i + 1, tasks.get(i)));
        }
    }

    // Mark a task to be done with index specified in arguments[1]
    protected static void doneHandler(TaskList tasks, String[] arguments)
            throws InvalidInputException, IOException, SaveException {
        if (arguments.length < 2) {
            // An index must be provided for the task to be marked "done"
            Helper.printlnWithIndent("You will need to give me an index, like this: `done 2`.");
        } else {
            try {
                int index = Integer.parseInt(arguments[1]);
                if (index > tasks.size() || index < 1) {
                    // This index is out of the boundary of our database
                    throw new InvalidInputException(InputExceptionType.INDEX_OUT_OF_BOUND);
                }

                Task task = tasks.get(index - 1);
                task.markAsDone();
                tasks.setTask(index - 1, task);

                Helper.printlnWithIndent("Nice! I've marked this task as done:");
                Helper.printlnWithIndent("\t" + task);
            } catch (NumberFormatException e) {
                throw new InvalidInputException(InputExceptionType.NOT_INTEGER, e);
            }
        }
    }

    // Delete a task from the list
    protected static void deleteHandler(TaskList tasks, String[] arguments)
            throws InvalidInputException, IOException, SaveException {
        if (arguments.length < 2) {
            // An index must be provided for the task to be marked "done"
            Helper.printlnWithIndent("You will need to give me an index, like this: `delete 2`.");
        } else {
            try {
                int index = Integer.parseInt(arguments[1]);
                if (index > tasks.size() || index < 1) {
                    // This index is out of the boundary of our database
                    throw new InvalidInputException(InputExceptionType.INDEX_OUT_OF_BOUND);
                }

                Task task = tasks.remove(index - 1);
                Helper.printlnWithIndent("Sure! I've removed this task:");
                Helper.printlnWithIndent("\t" + task);
            } catch (NumberFormatException e) {
                throw new InvalidInputException(InputExceptionType.NOT_INTEGER, e);
            }
        }
    }

    // Create a deadline task
    protected static void deadlineHandler(TaskList tasks, String[] arguments)
            throws InvalidInputException, IOException, SaveException {
        int i = Helper.findIndex(arguments, "/by");
        if (i != -1 && i + 1 != arguments.length) {
            String description = String.join(" ", Arrays.copyOfRange(arguments, 1, i));
            String by = String.join(" ", Arrays.copyOfRange(arguments, i + 1, arguments.length));
            tasks.addTask(new Deadline(description, by));
            Helper.printNewTask(tasks);
        } else {
            // Either /by is not found at all, or no dates are following /by
            throw new InvalidInputException(InputExceptionType.NO_BY_DATE);
        }
    }

    // Create an event task
    protected static void eventHandler(TaskList tasks, String[] arguments)
            throws InvalidInputException, IOException, SaveException {
        int i = Helper.findIndex(arguments, "/at");
        if (i != -1 && i + 1 != arguments.length) {
            String description = String.join(" ", Arrays.copyOfRange(arguments, 1, i));
            String at = String.join(" ", Arrays.copyOfRange(arguments, i + 1, arguments.length));
            tasks.addTask(new Event(description, at));
            Helper.printNewTask(tasks);
        } else {
            // Either /at is not found at all, or no dates are following /at
            throw new InvalidInputException(InputExceptionType.NO_AT_DATE);
        }
    }

    // Create a todo task
    protected static void todoHandler(TaskList tasks, String[] arguments)
            throws InvalidInputException, IOException, SaveException {
        String description = String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length));
        tasks.addTask(new ToDo(description));
        Helper.printNewTask(tasks);
    }
}
