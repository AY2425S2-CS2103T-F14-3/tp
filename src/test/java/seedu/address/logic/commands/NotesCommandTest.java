package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Person;


public class NotesCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void executeNotesCommand_validIndex_addsNotesSuccessfully() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new Person(firstPerson.getName(), firstPerson.getPhone(), firstPerson.getEmail(),
                firstPerson.getAddress(), firstPerson.getJobPosition(), firstPerson.getTeam(),
                firstPerson.getTags(), new Notes("Test notes"), firstPerson.getStartTime(),
                firstPerson.getDuration());

        NotesCommand notesCommand = new NotesCommand(INDEX_FIRST_PERSON, new Notes("Test notes"));

        String expectedMessage = String.format(NotesCommand.MESSAGE_ADD_NOTES_SUCCESS,
                INDEX_FIRST_PERSON.getOneBased(), "Test notes");

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(notesCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void executeNotesCommand_invalidIndexUnfiltered_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        NotesCommand notesCommand = new NotesCommand(outOfBoundIndex, new Notes("Test notes"));

        assertCommandFailure(notesCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeNotesCommand_invalidIndexFiltered_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        NotesCommand notesCommand = new NotesCommand(outOfBoundIndex, new Notes("Test notes"));

        assertCommandFailure(notesCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equalsMethod_variousScenarios_returnsExpectedResults() {
        final Notes notes = new Notes("Test notes");
        final NotesCommand standardCommand = new NotesCommand(INDEX_FIRST_PERSON, notes);

        // same values -> returns true
        NotesCommand commandWithSameValues = new NotesCommand(INDEX_FIRST_PERSON, notes);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new NotesCommand(INDEX_SECOND_PERSON, notes)));

        // different notes -> returns false
        Notes differentNotes = new Notes("Different notes");
        assertFalse(standardCommand.equals(new NotesCommand(INDEX_FIRST_PERSON, differentNotes)));
    }
}
