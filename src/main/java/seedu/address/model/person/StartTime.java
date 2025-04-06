package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.format.ResolverStyle;
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

    //private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);
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

        if (!test.matches(VALIDATION_REGEX)) {
            return false;
        }

        try {
            LocalDateTime parsed = LocalDateTime.parse(test, FORMATTER);

            // Manual check for invalid February 29
            int year = parsed.getYear();
            int month = parsed.getMonthValue();
            int day = parsed.getDayOfMonth();

            if (month == 2 && day >= 29 && !isLeapYear(year)) {
                return false;
            }

            if (month == 2 && day >= 30 && isLeapYear(year)) {
                return false;
            }

            return isMinuteMultipleOfFive(parsed);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
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
