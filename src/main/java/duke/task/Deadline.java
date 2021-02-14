package duke.task;

import duke.exception.InvalidInputException;

public class Deadline extends Task {
    // This value indicates the version of this class when doing serialization
    public static final long serialVersionUID = 01L;

    protected String by;

    public Deadline(String description, String by) throws InvalidInputException {
        super(description);
        this.by = by;
        typeIcon = "D";
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" (by: %s)", by);
    }
}
