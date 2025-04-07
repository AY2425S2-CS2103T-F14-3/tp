package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX =
            "The candidate index provided is out of bounds. Please provide a valid index.";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d candidates listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
            "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_NOTES_CHARACTER_LIMIT_EXCEEDED =
            "Notes cannot exceed 450 characters. Current length: %1$d characters.";
    public static final String MESSAGE_NO_COMMAND_TO_UNDO = "There is no command to undo.";

    // Constants for person formatting
    private static final String FORMAT_PHONE_PREFIX = "; Phone: ";
    private static final String FORMAT_EMAIL_PREFIX = "; Email: ";
    private static final String FORMAT_ADDRESS_PREFIX = "; Address: ";
    private static final String FORMAT_JOB_POSITION_PREFIX = "; Job Position: ";
    private static final String FORMAT_TEAM_PREFIX = "; Team: ";
    private static final String FORMAT_TAGS_PREFIX = "; Tags: ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code candidate} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append(FORMAT_PHONE_PREFIX)
                .append(person.getPhone())
                .append(FORMAT_EMAIL_PREFIX)
                .append(person.getEmail())
                .append(FORMAT_ADDRESS_PREFIX)
                .append(person.getAddress())
                .append(FORMAT_JOB_POSITION_PREFIX)
                .append(person.getJobPosition())
                .append(FORMAT_TEAM_PREFIX)
                .append(person.getTeam())
                .append(FORMAT_TAGS_PREFIX);
        person.getTags().forEach(builder::append);
        return builder.toString();
    }
}
