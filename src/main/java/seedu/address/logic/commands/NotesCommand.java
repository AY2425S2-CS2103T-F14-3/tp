package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Person;

/**
 * Adds interviewer notes to an existing candidate in RecruitIntel.
 */
public class NotesCommand extends Command {

    public static final String COMMAND_WORD = "note";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds interviewer notes to the candidate identified "
            + "by the index number used in the displayed candidate list.\n"
            + "Parameters: INDEX (must be a positive integer) NOTE_TEXT (maximum 450 characters)\n"
            + "Example: " + COMMAND_WORD + " 1 Strong backend experience, but lacks iOS exposure.";

    public static final String MESSAGE_ADD_NOTES_SUCCESS = "Note added to candidate #%1$d: \"%2$s\"";

    private final Index targetIndex;
    private Notes notes;
    private Person targetPerson;
    private Notes lastNotes;

    /**
     * @param targetIndex of the candidate in the filtered candidate list to add notes to
     * @param notes the notes to add to the candidate
     */
    public NotesCommand(Index targetIndex, Notes notes) {
        requireNonNull(targetIndex);
        requireNonNull(notes);

        this.targetIndex = targetIndex;
        this.notes = notes;
    }

    /**
     * Creates a new person with updated notes while preserving all other fields.
     */
    private Person createPersonWithNotes(Person basePerson, Notes newNotes) {
        return new Person(
                basePerson.getName(),
                basePerson.getPhone(),
                basePerson.getEmail(),
                basePerson.getAddress(),
                basePerson.getJobPosition(),
                basePerson.getTeam(),
                basePerson.getTags(),
                newNotes,
                basePerson.getStartTime(),
                basePerson.getDuration()
        );
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToAddNotesTo = lastShownList.get(targetIndex.getZeroBased());
        targetPerson = personToAddNotesTo;
        lastNotes = personToAddNotesTo.getNotes();

        Person updatedPerson = createPersonWithNotes(personToAddNotesTo, notes);
        model.setPerson(personToAddNotesTo, updatedPerson);
        targetPerson = updatedPerson;

        model.commit();

        return new CommandResult(String.format(MESSAGE_ADD_NOTES_SUCCESS, targetIndex.getOneBased(), notes.value));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NotesCommand)) {
            return false;
        }

        NotesCommand otherNotesCommand = (NotesCommand) other;
        return targetIndex.equals(otherNotesCommand.targetIndex)
                && notes.equals(otherNotesCommand.notes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("notes", notes)
                .toString();
    }
}
