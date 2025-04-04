package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Undo the most recent command that modifies the data.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undo the most recent command that modifies the data.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Undo successful!";

    public static final String MESSAGE_UNDO_NOT_AVAILABLE = "No command to undo!";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        try {
            model.undo();
        } catch (ModelManager.NoUndoableStateException e) {
            throw new CommandException(MESSAGE_UNDO_NOT_AVAILABLE);
        }

        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || other instanceof UndoCommand; // instanceof handles nulls
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }

}
