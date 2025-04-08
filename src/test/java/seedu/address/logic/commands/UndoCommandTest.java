package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for UndoCommand.
 */
public class UndoCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_undoDeleteCommand_success() {
        Person firstPerson = model.getFilteredPersonList().get(6);

        DeleteCommand deleteCommand = new DeleteCommand(firstPerson);
        try {
            deleteCommand.execute(model);
        } catch (CommandException e) {
            e.printStackTrace();
        }

        UndoCommand undoCommand = new UndoCommand();

        try {
            undoCommand.execute(model);
        } catch (CommandException e) {
            e.printStackTrace();
        }

        assertEquals(model.getFilteredPersonList().size(), 7);
    }

    @Test
    public void execute_noCommandToUndo_throwsCommandException() {
        UndoCommand undoCommand = new UndoCommand();
        assertThrows(CommandException.class, () -> undoCommand.execute(model));
    }

    @Test
    public void execute_undoMultipleCommands_success() {
        Person firstPerson = model.getFilteredPersonList().get(6);
        Person secondPerson = model.getFilteredPersonList().get(5);

        DeleteCommand deleteFirstCommand = new DeleteCommand(firstPerson);
        DeleteCommand deleteSecondCommand = new DeleteCommand(secondPerson);

        try {
            deleteFirstCommand.execute(model);
            deleteSecondCommand.execute(model);
        } catch (CommandException e) {
            e.printStackTrace();
        }

        UndoCommand undoCommand = new UndoCommand();

        try {
            undoCommand.execute(model);
        } catch (CommandException e) {
            e.printStackTrace();
        }

        assertEquals(model.getFilteredPersonList().size(), 6);

        try {
            undoCommand.execute(model);
        } catch (CommandException e) {
            e.printStackTrace();
        }

        assertEquals(model.getFilteredPersonList().size(), 7);
    }

    @Test
    public void execute_undoAddCommand_success() {
        Person newPerson = model.getFilteredPersonList().get(0);

        AddCommand addCommand = new AddCommand(newPerson);
        DeleteCommand deleteCommand = new DeleteCommand(newPerson);

        try {
            deleteCommand.execute(model);
        } catch (CommandException e) {
            e.printStackTrace();
        }

        try {
            addCommand.execute(model);
        } catch (CommandException e) {
            e.printStackTrace();
        }

        UndoCommand undoCommand = new UndoCommand();

        try {
            undoCommand.execute(model);
            undoCommand.execute(model);
        } catch (CommandException e) {
            e.printStackTrace();
        }

        assertEquals(model.getFilteredPersonList().get(0), newPerson);
    }
}
