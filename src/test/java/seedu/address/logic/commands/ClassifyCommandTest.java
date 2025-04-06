package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.JobPositionContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagsContainsKeywordsPredicate;
import seedu.address.model.person.TeamContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code ClassifyCommand}.
 */
public class ClassifyCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        TagsContainsKeywordsPredicate firstPredicate =
                new TagsContainsKeywordsPredicate(Collections.singletonList("first"));
        TagsContainsKeywordsPredicate secondPredicate =
                new TagsContainsKeywordsPredicate(Collections.singletonList("second"));

        List<Predicate<Person>> firstPredicates = new ArrayList<>();
        firstPredicates.add(firstPredicate);
        List<Predicate<Person>> secondPredicates = new ArrayList<>();
        secondPredicates.add(secondPredicate);

        ClassifyCommand classifyFirstCommand = new ClassifyCommand(firstPredicates);
        ClassifyCommand classifySecondCommand = new ClassifyCommand(secondPredicates);

        // same object -> returns true
        assertTrue(classifyFirstCommand.equals(classifyFirstCommand));

        // same values -> returns true
        ClassifyCommand classifyFirstCommandCopy = new ClassifyCommand(firstPredicates);
        assertTrue(classifyFirstCommand.equals(classifyFirstCommandCopy));

        // different types -> returns false
        assertFalse(classifyFirstCommand.equals(1));

        // null -> returns false
        assertFalse(classifyFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(classifyFirstCommand.equals(classifySecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        TagsContainsKeywordsPredicate predicate = preparePredicate(" ");
        List<Predicate<Person>> predicates = new ArrayList<>();
        predicates.add(predicate);
        ClassifyCommand command = new ClassifyCommand(predicates);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void classifyCommand_singleTagFriend_threePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        List<Predicate<Person>> predicates = new ArrayList<>();
        predicates.add(new TagsContainsKeywordsPredicate(List.of("friend")));
        ClassifyCommand command = new ClassifyCommand(predicates);
        expectedModel.updateFilteredPersonList(person -> predicates.stream().allMatch(pred -> pred.test(person)));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL), model.getFilteredPersonList());
    }

    @Test
    public void classifyCommand_twoTagsWithAndLogic_onePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        List<Predicate<Person>> predicates = new ArrayList<>();
        predicates.add(new TagsContainsKeywordsPredicate(List.of("friends")));
        predicates.add(new TagsContainsKeywordsPredicate(List.of("owesMoney")));
        ClassifyCommand command = new ClassifyCommand(predicates);
        expectedModel.updateFilteredPersonList(person -> predicates.stream().allMatch(pred -> pred.test(person)));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void classifyCommand_tagsWithJobAndTeam_onePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        List<Predicate<Person>> predicates = new ArrayList<>();
        predicates.add(new TagsContainsKeywordsPredicate(Arrays.asList("friends", "owesMoney")));
        predicates.add(new JobPositionContainsKeywordsPredicate(Arrays.asList("Product Manager")));
        predicates.add(new TeamContainsKeywordsPredicate(Arrays.asList("Product")));
        ClassifyCommand command = new ClassifyCommand(predicates);
        expectedModel.updateFilteredPersonList(person -> predicates.stream().allMatch(pred -> pred.test(person)));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        TagsContainsKeywordsPredicate predicate = new TagsContainsKeywordsPredicate(Arrays.asList("keyword"));
        List<Predicate<Person>> predicates = new ArrayList<>();
        predicates.add(predicate);
        ClassifyCommand classifyCommand = new ClassifyCommand(predicates);
        String expected = ClassifyCommand.class.getCanonicalName() + "{predicates=" + predicates + "}";
        assertEquals(expected, classifyCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code TagsContainsKeywordsPredicate}.
     */
    private TagsContainsKeywordsPredicate preparePredicate(String userInput) {
        return new TagsContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
