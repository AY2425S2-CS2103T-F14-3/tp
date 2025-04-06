package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TEAM;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.logic.commands.ClassifyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.JobPositionContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagsContainsKeywordsPredicate;
import seedu.address.model.person.TeamContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new ClassifyCommand object
 */
public class ClassifyCommandParser implements Parser<ClassifyCommand> {

    private static final String MESSAGE_EMPTY_TAG = "Tag value cannot be empty";
    private static final String MESSAGE_EMPTY_TEAM = "Team value cannot be empty";
    private static final String MESSAGE_EMPTY_JOB = "Job position value cannot be empty";

    /**
     * Parses the given {@code String} of arguments in the context of the ClassifyCommand
     * and returns a ClassifyCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public ClassifyCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = tokenizeArguments(args);
        validateAtLeastOnePrefix(argMultimap);
        validateSingleJobAndTeam(argMultimap);
        List<Predicate<Person>> predicates = createPredicates(argMultimap);
        return new ClassifyCommand(predicates);
    }

    /**
     * Validates that there is at most one job position and one team.
     * @throws ParseException if multiple job positions or teams are found
     */
    private void validateSingleJobAndTeam(ArgumentMultimap argMultimap) throws ParseException {
        if (argMultimap.getAllValues(PREFIX_JOB_POSITION).size() > 1) {
            throw new ParseException(ClassifyCommand.MESSAGE_MULTIPLE_JOB_POSITIONS);
        }
        if (argMultimap.getAllValues(PREFIX_TEAM).size() > 1) {
            throw new ParseException(ClassifyCommand.MESSAGE_MULTIPLE_TEAMS);
        }
    }

    /**
     * Tokenizes the command arguments with the required prefixes.
     */
    private ArgumentMultimap tokenizeArguments(String args) {
        return ArgumentTokenizer.tokenize(args, PREFIX_TAG, PREFIX_TEAM, PREFIX_JOB_POSITION);
    }

    /**
     * Validates that at least one prefix is present in the ArgumentMultimap.
     * @throws ParseException if no prefixes are present
     */
    private void validateAtLeastOnePrefix(ArgumentMultimap argMultimap) throws ParseException {
        if (!argMultimap.getValue(PREFIX_TAG).isPresent()
                && !argMultimap.getValue(PREFIX_TEAM).isPresent()
                && !argMultimap.getValue(PREFIX_JOB_POSITION).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClassifyCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Creates a list of predicates based on the provided arguments.
     * @throws ParseException if any of the provided values are empty
     */
    private List<Predicate<Person>> createPredicates(ArgumentMultimap argMultimap) throws ParseException {
        List<Predicate<Person>> predicates = new ArrayList<>();
        addTagPredicateIfPresent(argMultimap, predicates);
        addTeamPredicateIfPresent(argMultimap, predicates);
        addJobPredicateIfPresent(argMultimap, predicates);
        return predicates;
    }

    /**
     * Adds tag predicates to the list if tag prefix is present.
     * Each tag gets its own predicate to ensure AND logic between tags.
     */
    private void addTagPredicateIfPresent(ArgumentMultimap argMultimap, List<Predicate<Person>> predicates)
            throws ParseException {
        List<String> tagValues = argMultimap.getAllValues(PREFIX_TAG);
        for (String tagValue : tagValues) {
            String trimmedTag = tagValue.trim();
            if (trimmedTag.isEmpty()) {
                throw new ParseException(MESSAGE_EMPTY_TAG);
            }
            predicates.add(new TagsContainsKeywordsPredicate(List.of(trimmedTag)));
        }
    }

    /**
     * Adds a team predicate to the list if team prefix is present.
     */
    private void addTeamPredicateIfPresent(ArgumentMultimap argMultimap, List<Predicate<Person>> predicates)
            throws ParseException {
        Optional<String> teamValue = argMultimap.getValue(PREFIX_TEAM);
        if (teamValue.isPresent()) {
            String trimmedTeam = teamValue.get().trim();
            if (trimmedTeam.isEmpty()) {
                throw new ParseException(MESSAGE_EMPTY_TEAM);
            }
            predicates.add(new TeamContainsKeywordsPredicate(List.of(trimmedTeam)));
        }
    }

    /**
     * Adds a job position predicate to the list if job position prefix is present.
     */
    private void addJobPredicateIfPresent(ArgumentMultimap argMultimap, List<Predicate<Person>> predicates)
            throws ParseException {
        Optional<String> jobValue = argMultimap.getValue(PREFIX_JOB_POSITION);
        if (jobValue.isPresent()) {
            String trimmedJob = jobValue.get().trim();
            if (trimmedJob.isEmpty()) {
                throw new ParseException(MESSAGE_EMPTY_JOB);
            }
            predicates.add(new JobPositionContainsKeywordsPredicate(List.of(trimmedJob)));
        }
    }
}
