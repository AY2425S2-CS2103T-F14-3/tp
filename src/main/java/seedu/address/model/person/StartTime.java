package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents the start time of an interview in the format "yyyy-MM-dd HH:mm".
 */
public class StartTime implements Comparable<StartTime> {

    public static final String MESSAGE_CONSTRAINTS = "Start time must follow the \"yyyy-MM-dd HH:mm\" format, "
            + "and the minutes must be in multiples of 5 (e.g., 10, 15, 20).\n"
            + "Example: \"2025-04-01 10:15\"";

    public static final String VALIDATION_REGEX = "^$|^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public final String value;
    private final LocalDateTime parsedStartTime;

    /**
     * Constructs a {@code StartTime} from the given string.
     * If input is null or blank, no value is parsed.
     *
     * @param startTime A string in "yy-MM-dd HH-mm" format or null
     */
    public StartTime(String startTime) {
        if (startTime == null || startTime.isBlank()) {
            this.value = "";
            this.parsedStartTime = null;
            return;
        }

        checkArgument(isValidStartTime(startTime), MESSAGE_CONSTRAINTS);

        this.value = startTime;
        this.parsedStartTime = LocalDateTime.parse(startTime, FORMATTER);
    }


    /**
     * Returns true if {@code test} is a valid StartTime string or null.
     */
    public static boolean isValidStartTime(String test) {
        if (test == null || test.isBlank()) {
            return true;
        }

        try {
            if (!test.matches(VALIDATION_REGEX)) {
                return false;
            }
            LocalDateTime parsed = LocalDateTime.parse(test, FORMATTER);
            return isMinuteMultipleOfFive(parsed);
        } catch (DateTimeParseException e) {
            return false;
        }
    }



    public static boolean isMinuteMultipleOfFive(LocalDateTime dateTime) {
        return dateTime.getMinute() % 5 == 0;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof StartTime
                && value.equals(((StartTime) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public LocalDateTime getParsedStartTime() {
        return this.parsedStartTime;
    }

    @Override
    public int compareTo(StartTime other) {
        if (this.parsedStartTime == null && other.parsedStartTime == null) {
            return 0;
        }
        if (this.parsedStartTime == null) {
            return 1; // treat nulls as later
        }
        if (other.parsedStartTime == null) {
            return -1;
        }
        return this.parsedStartTime.compareTo(other.parsedStartTime);
    }

}
