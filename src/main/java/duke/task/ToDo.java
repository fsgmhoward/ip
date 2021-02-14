package duke.task;

import duke.exception.InvalidInputException;

public class ToDo extends Task {
    // This value indicates the version of this class when doing serialization
    public static final long serialVersionUID = 01L;

    public ToDo(String description) throws InvalidInputException {
        super(description);
        typeIcon = "T";
    }
}