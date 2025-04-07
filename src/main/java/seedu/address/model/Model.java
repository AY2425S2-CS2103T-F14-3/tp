package seedu.address.model;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' RecruitIntel file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' RecruitIntel file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces RecruitIntel data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the RecruitIntel.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given candidate.
     * The person must exist in the RecruitIntel.
     */
    void deletePerson(Person target);

    /**
     * Adds the given candidate.
     * {@code person} must not already exist in the RecruitIntel.
     */
    void addPerson(Person person);

    /**
     * Replaces the given candidate {@code target} with {@code editedPerson}.
     * {@code target} must exist in the RecruitIntel.
     * The candidate identity of {@code editedPerson} must not be the same as
     * another existing candidate in the RecruitIntel.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered candidate list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered candidate list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    void sortFilteredPersonList(Comparator<Person> comparing);

    void commit();

    void undo() throws ModelManager.NoUndoableStateException;

    void redo() throws ModelManager.NoRedoableStateException;
}
