package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.InterviewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Duration;
import seedu.address.model.person.StartTime;

/**
 * Parses input arguments and creates a new InterviewCommand object
 */
public class InterviewCommandParser implements Parser<InterviewCommand> {

    private static final Logger logger = LogsCenter.getLogger(InterviewCommandParser.class);

    /**
     * Parses the given {@code String} of arguments in the context of the InterviewCommand
     * and returns an InterviewCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public InterviewCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();

        String[] parts = trimmedArgs.split("\\s+");

        if (parts.length < 4) {
            logger.info("Insufficient arguments provided for interview command");
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, InterviewCommand.MESSAGE_USAGE));
        }

        if (parts.length > 4) {
            logger.info("Over arguments provided for interview command");
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, InterviewCommand.MESSAGE_USAGE));
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(parts[0]);
            logger.fine("Parsed index: " + index.getOneBased());
        } catch (ParseException pe) {
            logger.info("Invalid index input: " + parts[0]);
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, InterviewCommand.MESSAGE_USAGE), pe);
        }

        // Reconstruct date and time string
        String startTimeString = parts[1] + " " + parts[2];
        String durationString = parts[3];

        if (!StartTime.isValidStartTime(startTimeString)) {
            logger.info("Invalid start time format: " + startTimeString);
            throw new ParseException(StartTime.MESSAGE_CONSTRAINTS);
        }

        if (!Duration.isValidDuration(durationString)) {
            logger.info("Invalid duration: " + durationString);
            throw new ParseException(Duration.MESSAGE_CONSTRAINTS);
        }

        StartTime startTime = new StartTime(startTimeString);
        Duration duration = new Duration(durationString);

        logger.fine("Parsed start time: " + startTime.value);
        logger.fine("Parsed duration: " + duration.getDurationInMinutes() + " minutes");

        return new InterviewCommand(index, startTime, duration);
    }
}
