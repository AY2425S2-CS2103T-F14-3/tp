package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ClassifyCommand;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagsContainsKeywordsPredicate;

public class ClassifyCommandParserTest {

    private ClassifyCommandParser parser = new ClassifyCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClassifyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_singleTag_returnsClassifyCommand() {
        List<Predicate<Person>> singleTagPredicates = new ArrayList<>();
        singleTagPredicates.add(new TagsContainsKeywordsPredicate(List.of("python")));
        ClassifyCommand expectedCommand = new ClassifyCommand(singleTagPredicates);
        assertParseSuccess(parser, " t/python", expectedCommand);
        assertParseSuccess(parser, " t/python   ", expectedCommand);
    }

    @Test
    public void parse_multipleTagsWithAndLogic_returnsClassifyCommand() {
        List<Predicate<Person>> multipleTagPredicates = new ArrayList<>();
        multipleTagPredicates.add(new TagsContainsKeywordsPredicate(List.of("python")));
        multipleTagPredicates.add(new TagsContainsKeywordsPredicate(List.of("java")));
        ClassifyCommand expectedCommand = new ClassifyCommand(multipleTagPredicates);
        assertParseSuccess(parser, " t/python t/java", expectedCommand);
    }

    @Test
    public void parse_multipleJobPositions_throwsParseException() {
        assertParseFailure(parser, " j/Engineer j/Designer",
                ClassifyCommand.MESSAGE_MULTIPLE_JOB_POSITIONS);
    }

    @Test
    public void parse_multipleTeams_throwsParseException() {
        assertParseFailure(parser, " tm/Engineering tm/Design",
                ClassifyCommand.MESSAGE_MULTIPLE_TEAMS);
    }
}
