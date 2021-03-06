package duke.command;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import duke.DateTime;
import duke.TaskList;
import duke.Ui;
import duke.exception.InvalidInputException;
import duke.exception.InvalidInputException.InputExceptionType;
import duke.exception.SaveException;
import duke.task.Deadline;

/**
 * Insert a deadline task into the task list
 */
public class DeadlineCommand extends Command {
    public DeadlineCommand(Ui ui, TaskList tasks, HashMap<String, String> arguments) {
        super(ui, tasks, arguments);
    }

    @Override
    public void execute() throws InvalidInputException, IOException, SaveException {
        String byDateTime = arguments.get("by");
        if (byDateTime == null || byDateTime.length() == 0) {
            // Either /by is not found at all, or no dates are following /by
            throw new InvalidInputException(InputExceptionType.NO_BY_DATE);
        }
        try {
            tasks.addTask(new Deadline(arguments.get("payload"), new DateTime(byDateTime)));
            ui.printNewTask(tasks);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException(InputExceptionType.MALFORMED_DATE);
        }
    }
}
