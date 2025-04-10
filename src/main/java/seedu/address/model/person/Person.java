package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Candidate in RecruitIntel.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields - used to uniquely identify a candidate
    private final Name name;
    private final Email email;
    private final Phone phone;

    // Professional fields - related to job application
    private final JobPosition jobPosition;
    private final Team team;
    private final Set<Tag> tags = new HashSet<>();

    // Interview fields - related to interview scheduling
    private final StartTime startTime;
    private final Duration duration;

    // Additional fields
    private final Address address;
    private final Notes notes;

    /**
     * Creates a Person with default empty values for optional fields.
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, JobPosition jobPosition,
                  Team team, Set<Tag> tags) {
        this(name, phone, email, address, jobPosition, team, tags,
                new Notes(""), new StartTime(""), new Duration(""));
    }

    /**
     * Creates a Person with all fields specified.
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, JobPosition jobPosition,
                  Team team, Set<Tag> tags, Notes notes, StartTime startTime, Duration duration) {
        requireAllNonNull(name, phone, email, address, jobPosition, team, tags, notes, startTime, duration);

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.jobPosition = jobPosition;
        this.team = team;
        this.tags.addAll(tags);
        this.startTime = startTime;
        this.duration = duration;
        this.notes = notes;
    }

    // Identity field accessors
    public Name getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Phone getPhone() {
        return phone;
    }

    // Professional field accessors
    public JobPosition getJobPosition() {
        return jobPosition;
    }

    public Team getTeam() {
        return team;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    // Interview field accessors
    public StartTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    // Additional field accessors
    public Address getAddress() {
        return address;
    }

    public Notes getNotes() {
        return notes;
    }

    /**
     * Returns true if both candidates have the same email.
     * This defines a weaker notion of equality between two candidates.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getEmail().equals(getEmail());
    }

    /**
     * Returns true if both candidates have the same identity and data fields.
     * This defines a stronger notion of equality between two candidates.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && jobPosition.equals(otherPerson.jobPosition)
                && team.equals(otherPerson.team)
                && tags.equals(otherPerson.tags)
                && startTime.equals(otherPerson.startTime)
                && duration.equals(otherPerson.duration)
                && notes.equals(otherPerson.notes);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, jobPosition, team, tags, notes, startTime, duration);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("jobPosition", jobPosition)
                .add("team", team)
                .add("tags", tags)
                .add("notes", notes)
                .add("interview time", startTime)
                .add("duration", duration)
                .toString();
    }
}
