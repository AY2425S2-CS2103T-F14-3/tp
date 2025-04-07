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

public class RedoCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_noUndoToRedo_throwsCommandException() {
        RedoCommand redoCommand = new RedoCommand();
        assertThrows(CommandException.class, () -> redoCommand.execute(model));
    }

    @Test
    public void execute_redoAfterUndo_success() {
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

        RedoCommand redoCommand = new RedoCommand();
        try {
            redoCommand.execute(model);
        } catch (CommandException e) {
            e.printStackTrace();
        }

        assertEquals(model.getFilteredPersonList().size(), 6);
    }

    @Test
    public void execute_redoMultipleUndos_success() {
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
            undoCommand.execute(model);
        } catch (CommandException e) {
            e.printStackTrace();
        }

        RedoCommand redoCommand = new RedoCommand();
        try {
            redoCommand.execute(model);
        } catch (CommandException e) {
            e.printStackTrace();
        }

        assertEquals(model.getFilteredPersonList().size(), 6);

        try {
            redoCommand.execute(model);
        } catch (CommandException e) {
            e.printStackTrace();
        }

        assertEquals(model.getFilteredPersonList().size(), 5);
    }

    @Test
    public void execute_noRedoAfterModifyingCommand_throwsCommandException() {
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

        Person thirdPerson = model.getFilteredPersonList().get(4);
        DeleteCommand deleteThirdCommand = new DeleteCommand(thirdPerson);

        try {
            deleteThirdCommand.execute(model);
        } catch (CommandException e) {
            e.printStackTrace();
        }

        RedoCommand redoCommand = new RedoCommand();
        assertThrows(CommandException.class, () -> redoCommand.execute(model));
    }

    @Test
    public void execute_redoNonModifyingCommand_noChange() {
        Person newPerson = model.getFilteredPersonList().get(0);

        DeleteCommand deleteCommand = new DeleteCommand(newPerson);

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

        ListCommand listCommand = new ListCommand();
        listCommand.execute(model);

        SortCommand sortCommand = new SortCommand();
        sortCommand.execute(model);

        RedoCommand redoCommand = new RedoCommand();

        try {
            redoCommand.execute(model);
        } catch (CommandException e) {
            e.printStackTrace();
        }

        assertEquals(model.getFilteredPersonList().size(), 6);
    }
}
