package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Candidate's name in the RecruitIntel.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should:\n"
                    + "1. Start with a letter or number\n"
                    + "2. Can contain letters (including accented letters), numbers, spaces\n"
                    + "3. Can contain hyphens (-), apostrophes ('), periods (.), and forward slashes (/)\n"
                    + "4. Cannot be blank";

    /*
     * Name validation rules:
     * 1. Must start with alphanumeric: ^[\\p{Alnum}]
     * 2. Can contain:
     *    - alphanumeric characters (including accented letters): \\p{Alnum}
     *    - spaces: \\s
     *    - hyphens, apostrophes, periods, forward slashes: [\\-\\'\\.\\/ ]
     * 3. Can have any number of these characters after the first: *
     */
    public static final String VALIDATION_REGEX = "^[\\p{Alnum}\\p{L}][\\p{L}\\p{Alnum}\\s\\-\\'\\.\\/ ]*";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
