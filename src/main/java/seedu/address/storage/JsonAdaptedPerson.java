package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Duration;
import seedu.address.model.person.Email;
import seedu.address.model.person.JobPosition;
import seedu.address.model.person.Name;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StartTime;
import seedu.address.model.person.Team;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Candidate's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String jobPosition;
    private final String team;
    private final String notes;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final String startTime;
    private final String duration;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email, @JsonProperty("address") String address,
                             @JsonProperty("jobPosition") String jobPosition, @JsonProperty("team") String team,
                             @JsonProperty("notes") String notes, @JsonProperty("tags") List<JsonAdaptedTag> tags,
                             @JsonProperty("startTime") String startTime, @JsonProperty("duration") String duration) {

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.jobPosition = jobPosition;
        this.team = team;
        this.notes = notes;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.startTime = startTime;
        this.duration = duration;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        jobPosition = source.getJobPosition().value;
        team = source.getTeam().value;
        notes = source.getNotes().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        startTime = source.getStartTime().value;
        duration = source.getDuration().value;
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = convertTags();

        final Name modelName = validateAndCreateName();
        final Phone modelPhone = validateAndCreatePhone();
        final Email modelEmail = validateAndCreateEmail();
        final Address modelAddress = validateAndCreateAddress();
        final JobPosition modelJobPosition = validateAndCreateJobPosition();
        final Team modelTeam = validateAndCreateTeam();

        // Optional fields with default empty values
        final StartTime modelStartTime = validateAndCreateStartTime();
        final Duration modelDuration = validateAndCreateDuration();
        final Notes modelNotes = new Notes(notes != null ? notes : "");
        final Set<Tag> modelTags = new HashSet<>(personTags);

        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelJobPosition,
                modelTeam, modelTags, modelNotes, modelStartTime, modelDuration);
    }

    /**
     * Converts the stored tags into a List of Tag objects.
     */
    private List<Tag> convertTags() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }
        return personTags;
    }

    /**
     * Validates and creates a Name object.
     */
    private Name validateAndCreateName() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(name);
    }

    /**
     * Validates and creates a Phone object.
     */
    private Phone validateAndCreatePhone() throws IllegalValueException {
        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(phone);
    }

    /**
     * Validates and creates an Email object.
     */
    private Email validateAndCreateEmail() throws IllegalValueException {
        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(email);
    }

    /**
     * Validates and creates an Address object.
     */
    private Address validateAndCreateAddress() throws IllegalValueException {
        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(address);
    }

    /**
     * Validates and creates a JobPosition object.
     */
    private JobPosition validateAndCreateJobPosition() throws IllegalValueException {
        if (jobPosition == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    JobPosition.class.getSimpleName()));
        }
        if (!JobPosition.isValidJobPosition(jobPosition)) {
            throw new IllegalValueException(JobPosition.MESSAGE_CONSTRAINTS);
        }
        return new JobPosition(jobPosition);
    }

    /**
     * Validates and creates a Team object.
     */
    private Team validateAndCreateTeam() throws IllegalValueException {
        if (team == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Team.class.getSimpleName()));
        }
        if (!Team.isValidTeam(team)) {
            throw new IllegalValueException(Team.MESSAGE_CONSTRAINTS);
        }
        return new Team(team);
    }

    private StartTime validateAndCreateStartTime() throws IllegalValueException {
        if (startTime == null) {
            return new StartTime("");
        }
        if (!StartTime.isValidStartTime(startTime)) {
            throw new IllegalValueException(StartTime.MESSAGE_CONSTRAINTS);
        }
        return new StartTime(startTime);
    }

    private Duration validateAndCreateDuration() throws IllegalValueException {
        if (duration == null) {
            return new Duration("");
        }
        if (!Duration.isValidDuration(duration)) {
            throw new IllegalValueException(Duration.MESSAGE_CONSTRAINTS);
        }
        return new Duration(duration);
    }
}
