package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Redoes the most recent command that was undone.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Redo successful!";
    public static final String MESSAGE_REDO_NOT_AVAILABLE = "No command to redo!";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Redoes the last command that was undone.\n"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        try {
            model.redo();
        } catch (ModelManager.NoRedoableStateException e) {
            throw new CommandException(MESSAGE_REDO_NOT_AVAILABLE);
        }

        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || other instanceof RedoCommand; // instanceof handles nulls
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
