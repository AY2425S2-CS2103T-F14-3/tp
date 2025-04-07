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
import seedu.address.model.person.Duration;
import seedu.address.model.person.Person;
import seedu.address.model.person.StartTime;

public class InterviewCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_setInterview_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        StartTime startTime = new StartTime("2025-04-01 10:00");
        Duration duration = new Duration("30");

        Person editedPerson = new Person(firstPerson.getName(), firstPerson.getPhone(), firstPerson.getEmail(),
                firstPerson.getAddress(), firstPerson.getJobPosition(), firstPerson.getTeam(),
                firstPerson.getTags(), firstPerson.getNotes(), startTime, duration);

        InterviewCommand command = new InterviewCommand(INDEX_FIRST_PERSON, startTime, duration);

        String expectedMessage = String.format(InterviewCommand.MESSAGE_SET_INTERVIEW_SUCCESS,
                INDEX_FIRST_PERSON.getOneBased(), startTime.value, duration.getDurationInMinutes());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        InterviewCommand interviewCommand = new InterviewCommand(outOfBoundIndex,
                new StartTime("2025-04-01 14:00"), new Duration("30"));

        assertCommandFailure(interviewCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        InterviewCommand interviewCommand = new InterviewCommand(outOfBoundIndex,
                new StartTime("2025-04-01 14:00"), new Duration("30"));

        assertCommandFailure(interviewCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        StartTime startTime = new StartTime("2025-04-01 10:00");
        Duration duration = new Duration("30");

        final InterviewCommand standardCommand = new InterviewCommand(INDEX_FIRST_PERSON, startTime, duration);

        // same values -> returns true
        InterviewCommand sameCommand = new InterviewCommand(INDEX_FIRST_PERSON,
                new StartTime("2025-04-01 10:00"), new Duration("30"));
        assertTrue(standardCommand.equals(sameCommand));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different type -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new InterviewCommand(INDEX_SECOND_PERSON, startTime, duration)));

        // different start time -> returns false
        assertFalse(standardCommand.equals(new InterviewCommand(INDEX_FIRST_PERSON,
                new StartTime("2025-04-01 11:00"), duration)));

        // different duration -> returns false
        assertFalse(standardCommand.equals(new InterviewCommand(INDEX_FIRST_PERSON,
                startTime, new Duration("45"))));
    }
}
