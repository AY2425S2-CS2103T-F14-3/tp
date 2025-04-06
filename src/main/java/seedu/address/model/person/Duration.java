package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the duration of an interview, in multiples of 5 minutes.
 */
public class Duration {

    public static final String MESSAGE_CONSTRAINTS = "Duration must be a positive integer multiple of 5, "
            + "and cannot exceed 1440 (24 hours).\n"
            + "Example: \"30\" means 30 minutes.";

    public final String value;
    private final int durationInMinutes;

    /**
     * Creates a Duration from a string.
     * If the string is empty, it represents no duration set.
     * Otherwise, the value must be a positive multiple of 5.
     */
    public Duration(String duration) {

        if (duration.isBlank()) {
            this.value = "";
            this.durationInMinutes = 0; // You can also use OptionalInt or -1 if you want to represent "not set"
            return;
        }

        checkArgument(isValidDuration(duration), MESSAGE_CONSTRAINTS);
        int parsed = Integer.parseInt(duration);
        this.value = duration;
        this.durationInMinutes = parsed;
    }

    /**
     * Returns true if {@code test} is empty (meaning no duration),
     * or can be parsed into a positive multiple of 5.
     */
    public static boolean isValidDuration(String test) {
        if (test == null || test.isBlank()) {
            return true; // Accept empty as valid input
        }

        try {
            int durationMinutes = Integer.parseInt(test);
            return durationMinutes > 0 && durationMinutes % 5 == 0 && durationMinutes <= 1440;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    @Override
    public String toString() {
        return value + " min";
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Duration
                && value.equals(((Duration) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }
}
