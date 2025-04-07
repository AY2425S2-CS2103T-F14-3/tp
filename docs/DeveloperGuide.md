---
layout: page
title: Developer Guide
---
- [Acknowledgements](#acknowledgements)
- [Setting up, getting started](#setting-up-getting-started)
- [Design](#design)
    - [Architecture](#architecture)
    - [UI component](#ui-component)
    - [Logic component](#logic-component)
    - [Model component](#model-component)
    - [Storage component](#storage-component)
    - [Common classes](#common-classes)
- [Implementation](#implementation)
    - [Proposed Undo/redo feature](#proposed-undoredo-feature)
- [Documentation, logging, testing, configuration, dev-ops](#documentation-logging-testing-configuration-dev-ops)
- [Appendix: Requirements](#appendix-requirements)
    - [Product scope](#product-scope)
    - [User stories](#user-stories)
    - [Use cases](#use-cases)
    - [Non-Functional Requirements](#non-functional-requirements)
    - [Glossary](#glossary)
- [Appendix: Instructions for manual testing](#appendix-instructions-for-manual-testing)
    - [Launch and shutdown](#launch-and-shutdown)
    - [Deleting a person](#deleting-a-person)
    - [Editing a person](#editing-a-persons-information)
    - [Finding a person](#finding-a-person)

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* Code and idea for Undo and Redo were 
    inspired by the original AB3 Developer Guide.


--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2425S2-CS2103T-F14-3/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2425S2-CS2103T-F14-3/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S2-CS2103T-F14-3/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2425S2-CS2103T-F14-3/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2425S2-CS2103T-F14-3/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S2-CS2103T-F14-3/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S2-CS2103T-F14-3/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the RecruitIntel data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S2-CS2103T-F14-3/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both RecruitIntel data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

The following is an example JSON output for a person:
```json
{
    "name" : "David Li",
    "phone" : "91031282",
    "email" : "lidavid@example.com",
    "address" : "Blk 436 Serangoon Gardens Street 26, #16-43",
    "jobPosition" : "Security Specialist",
    "team" : "Apple Store",
    "notes" : "Expert in iOS security protocols and penetration testing. Previously led security at a major tech firm. Identified critical vulnerabilities in App Store submission process. Strong security mindset. Published research on mobile app security. Excellent communicator when explaining complex security concepts.",
    "tags" : [ "what", "shw" ],
    "startTime" : "2025-01-01 13:45",
    "duration" : "15"
  }
```
### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current RecruitIntel state in its history.
* `VersionedAddressBook#undo()` — Restores the previous RecruitIntel state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone RecruitIntel state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial RecruitIntel state, and the `currentStatePointer` pointing to that single RecruitIntel state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the RecruitIntel. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the RecruitIntel after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted RecruitIntel state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified RecruitIntel state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the RecruitIntel state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous RecruitIntel state, and restores the RecruitIntel to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the RecruitIntel to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest RecruitIntel state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the RecruitIntel, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all RecruitIntel states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire RecruitIntel.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Manages a high volume of candidates across multiple hiring roles.
* Prefers fast, efficient tools over traditional GUI-based applicant tracking systems (ATS).
* Comfortable using CLI-based applications and prefers keyboard-driven workflows.
* Needs quick access to candidate details, notes, and filtering functions for better decision-making.
* Requires an organized and structured way to track candidate interactions, evaluations, and hiring progress.

**Value proposition**:

A highly efficient, CLI-driven contact management system that enables Apple’s HR recruiters to quickly add, retrieve, and evaluate candidates, reducing manual screening time and improving hiring accuracy.



### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​   | I want to …​                                                   | So that I can…​                                                                       |
|--------|-----------|----------------------------------------------------------------|---------------------------------------------------------------------------------------|
| `* * *` | new user  | see usage instructions                                         | refer to instructions when I forget how to use the App or when I start using the app. |
| `* * *` | recruiter | add a candidate’s name, contact details, and role applied for  | keep track of them easily.                                                            |
| `* * *` | recruiter | list all candidates in the system                              | see at a glance who is currently in the database.                                     |
| `* * *` | recruiter | remove outdated or irrelevant candidate records                | maintain a clean list.                                                                |
| `* * *` | recruiter | find candidates by their names or skills                       | quickly locate specific individuals.                                                  |
| `* * *` | recruiter | Record the candidate's interview performance                   | facilitate subsequent admission evaluation.                                           |
| `* * *` | recruiter | Add candidates' interview time                                 | schedule an interview.                                                                |
| `* *`  | recruiter | sort candidates by increasing interview time                   | who is the fisrt to be interviewed.                                                   |
| `* *`  | recruiter | edit a candidate's details (e.g. phone, email)                 | correct mistakes and keep data accurate conveniently.                                 |
| `* *`  | recruiter | classify candidates by a combination of attributes             | filter candidates based on specific criteria.                                         |
### Use cases

(For all use cases below, the **System** is the `RecruitIntel` and the **Actor** is the `user`, unless specified otherwise)


## Use Case: UC01 - Add New Candidate

**MSS**
1. HR Recruiter adds a new candidate.
2. **System** validates the details.
3. **System** creates a new candidate record and displays a success message.  
   Use case ends.

**Extensions**

- **1a.** HR Recruiter omits a mandatory detail.
    - **1a1.** **System** shows an error message and requests correction.
    - **1a2.** HR Recruiter enters the required detail.
    - Steps 1a1–1a2 are repeated until all mandatory details are provided.  
      Use case resumes from step 2.

- **2a.** **System** detects invalid data (e.g., phone is non-numeric or email is malformed).
    - **2a1.** **System** shows an error message and requests correction.
    - **2a2.** HR Recruiter enters valid data.
    - Steps 2a1–2a2 are repeated until all data are valid.  
      Use case resumes from step 3.

- **2b.** **System** detects a duplicate candidate (same email).
    - **2b1.** **System** notifies the recruiter that the candidate already exists.
    - Steps 2b1–2b2 are repeated until all data are valid.  
      Use case resumes from step 3.
  
      Use case ends.

## Use Case: UC02 - List All Candidate

**MSS**
1. HR Recruiter chooses to list all candidates.
2. **System** retrieves all candidates.
3. **System** displays the list of candidates.  
   Use case ends.

## Use Case: UC03 - Edit Candidate Information

**MSS**
1. HR Recruiter chooses to edit a candidate.
2. **System** requests the candidate to be edited.
3. HR Recruiter enters the candidate to be edited.
4. **System** requests the new details for the candidate.
5. HR Recruiter enters the new details for the candidate.
6. **System** updates the candidate’s details and display a success message.
  
Use case ends.

**Extension**
- **3a.** **System** detects an invalid candidate.
    - **3a1.** **System** requests the recruiter to enter a valid candidate.
    - **3a2.** HR Recruiter enters a new valid candidate.
    - Steps 3a1–3a2 are repeated until the input is valid.  
      Use case resumes from step 4.
- **5a.** **System** detects an invalid detail.
  - **5a1.** **System** requests the recruiter to enter a valid detail.
  - **5a2.** HR Recruiter enters a new valid detail.
  - Steps 5a1–5a2 are repeated until the input is valid.  
    Use case resumes from step 6.

Use case ends.

## Use Case: UC04 - Classify Candidate
**MSS**
1. HR Recruiter chooses to classify some candidates based on tags.
2. **System** requests the tags to be used to classify.
3. **System** displays the candidates classified by tags.

Use case ends.

## Use Case: UC05 - Find Candidate
**MSS**
1. HR Recruiter chooses to find a candidate.
2. **System** requests the keywords to be used to find candidate.
3. HR Recruiter enters the keywords to find.
4. **System** finds the candidate and displays the candidate.

   Use case ends.

## Use Case: UC06 - Enter Note for Candidate
**MSS**
1. HR Recruiter chooses to enter a note for a candidate.
2. **System** requests the candidate that to be added a note.
3. HR Recruiter enters the chosen candidate.
4. **System** requests the note to be added for the candidate.
5. HR Recruiter enters the note for the candidate.
6. **System** displays the note for the candidate.  
   Use case ends.

**Extension**
- **3a.** **System** detects an invalid candidate.
    - **3a1.** **System** requests the recruiter to enter a valid candidate.
    - **3a2.** HR Recruiter enters a new valid candidate.
    - Steps 3a1–3a2 are repeated until the input is valid.  
      Use case resumes from step 4.
- **5a.** **System** detects an invalid note.
    - **5a1.** **System** requests the recruiter to enter a valid note.
    - **5a2.** HR Recruiter enters a new valid note.
    - Steps 5a1–5a2 are repeated until the input is valid.  
      Use case resumes from step 6.
Use case ends.

## Use Case: UC07 - Delete Candidate

**MSS**
1. HR Recruiter chooses to delete a candidate.
2. **System** requests the candidate to be deleted.
3. HR Recruiter enters the candidate to be deleted.
4. **System** deletes the candidate and displays a success message.

Use case ends.

**Extensions**
- **3a.** **System** detects an invalid candidate.
    - **3a1.** **System** requests the recruiter to enter a valid candidate.
    - **3a2.** HR Recruiter enters a new valid candidate.
    - Steps 3a1–3a2 are repeated until the input is valid.  
      Use case resumes from step 4.

Use case ends.

## Use Case: UC08 - Add Interview Time Information for Candidate
**MSS**
1. HR Recruiter chooses to add interview time information for a candidate.
2. **System** requests the candidate to be added interview time information.
3. HR Recruiter enters the candidate to be added interview time information.
4. **System** requests the interview time information for the candidate.
5. HR Recruiter enters the interview time information for the candidate.
6. **System** displays the interview time information for the candidate.  
   Use case ends.

**Extensions**
- **3a.** **System** detects an invalid candidate.
    - **3a1.** **System** requests the recruiter to enter a valid candidate.
    - **3a2.** HR Recruiter enters a new valid candidate.
    - Steps 3a1–3a2 are repeated until the input is valid.  
      Use case resumes from step 4.
- **5a.** **System** detects an invalid interview time information.
    - **5a1.** **System** requests the recruiter to enter a valid interview time information.
    - **5a2.** HR Recruiter enters a new valid interview time information.
    - Steps 5a1–5a2 are repeated until the input is valid.  
      Use case resumes from step 6.

Use case ends.

## Use Case: UC09 - Sort Candidates

**MSS**
1. HR Recruiter chooses to sort candidates.
2. **System** sorts the candidates based on the interview time.
3. **System** displays the sorted list.  
   Use case ends.

## Use Case: UC10 - Undo Actions
**MSS**
1. HR Recruiter chooses to undo the last action.
2. **System** undoes the last action.  
   Use case ends.

**Extensions**
- **1a.** **System** detects that there are no actions to undo.
    - **1a1.** **System** shows an error message and requests the recruiter to enter a valid action.  

Use case ends.

## Use Case: UC11 - Redo Actions
**MSS**
1. HR Recruiter chooses to redo the last action.
2. **System** redoes the last action.  
   Use case ends.

**Extensions**
- **1a.** **System** detects that there are no actions to redo.
    - **1a1.** **System** shows an error message and requests the recruiter to enter a valid action.
    
Use case ends.

## Use Case: UC12 - Clear All Candidates
**MSS**
1. HR Recruiter chooses to clear all candidates.
2. **System** clears all candidates.  
   Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Should ensure that all data entries are processed within 5 seconds under normal operating conditions, providing quick feedback to user interactions.
5.  Application should maintain user data privacy and security, adhering to the latest data protection regulations.
6.  The system should be user-friendly, with an interface that requires no more than 20 minutes of training for new users to perform basic operations.
7.  The system should store candidate information securely.
8.  The system should not crash or freeze when an invalid command is entered.
9.  The system should be self-contained and should not require an internet connection or external databases.

### Glossary
* **AB3**: The codebase provided as a starting point for this project. RecruitIntel extends and modifies AB3.
* **CLI (Command Line Interface)**: A user interface navigated by typing commands into a terminal or console window.
* **GUI (Graphical User Interface)**: An interface allowing users to interact with electronic devices through graphical icons and visual indicators.
* **JSON (JavaScript Object Notation)**: A lightweight data-interchange format, easy for humans to read and write, and easy for machines to parse and generate.
* **Mainstream OS**: Operating systems that are widely used and supported, such as Windows, macOS, Linux, and UNIX.
* **Parser**: A component that interprets text data within a file according to predefined rules or specifications.
* **PlantUML**: A tool for quickly writing and sharing visual representations of programs, algorithms, and systems.
* **Sequence Diagram**: A type of UML diagram showing how objects operate with one another and in what order.
* **State Pointer**: A reference point tracking the current state or position in a sequence of states.
* **UML (Unified Modeling Language)**: A standardized modeling language consisting of an integrated set of diagrams.
* **XML (eXtensible Markup Language)**: A markup language defining rules for encoding documents in a format that is both human-readable and machine-readable.
* **Tag**: A keyword or term assigned to a piece of information, making it easier to search for and locate.
* **Candidate**: A person who applies for a job or position, often undergoing an interview process.
* **Interview**: A formal meeting in which a candidate is evaluated for a position.
* **Recruiter**: A person responsible for finding and hiring candidates for job positions.
--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

3. Exit the App

Enter exit in the command box. This will exit the app.

### Adding a person

Adding a person in the person list:

   1. Test case `add n/Byran high p/33665544 e/byranh@example.com a/123, Clementi Rd, 335544 j/Hardware Engineer tm/Machine Learning System t/C t/AI` <br>
   Assumption: We assume there is no person with duplicate email. <br>
   Excepted：Person added successfully and displayed in the list.
   2. Test Case (duplicate email): `add n/Anna Doe p/12345678 e/byranh@example.com a/789, Pasir Panjang Rd, 112233 j/Software Engineer tm/Frontend` <br>
   Assumption: We assume there is a person with duplicate email in the list. <br>
   Excepted：Error message shown indicating email duplication.
   3. Test Case : `add n/Anna Doe e/bryanh@ab.com a/789, Pasir Panjang Rd, 112233 j/Software Engineer ` <br>
   Excepted：No person is added. Error message shown indicating wrong format.

### Deleting a person

Deleting a person while all persons are being shown:

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   2. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   3. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   4. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

### Editing a person's information
Editing a person in the person list:
   1. Assumption: We assume the index here is valid if it is a positive integer <br>
   Test case: `edit 1 t/Java` <br>
   Expected: The Tag of the first person in the list is changed to Java
   2. Test case: `edit 0 t/Java` <br>
   Expected: No person is edited, an error message will be displayed.
   3. Other invalid edit command to try : <br>
   `edit -1 p/00` <br>
   `edit 1 e/ab@cd` <br>
   Expected: No person is edited, an error message will be displayed.
   
   4. Other valid edit command to try : <br>
   `edit 1 n/Robin gen` <br>
   `edit 1 p/96754328` <br>
   Expected: the field indicated by the prefix is edit to the information given.

### Finding a person
Finding a person by name:
   1. Test case: `find alice` <br>
      Expected: All persons with the name that contains `alice` are shown in the list. Other persons are hidden.

### Classifying Persons
Classifying Persons by some of the tags, team and jobPosition:
   1. Test case: `classify t/Python tm/Design j/Software Engineer` <br>
     Expected: Displays persons matching all given filters.

   2. Test case : `classify t/Python` <br>
     Expected: Displays persons have tag Python.

### Clearing All Persons
Clearing All Persons in the list:
1. Test Case: `clear` <br> Expected: All entries are deleted. Application shows an empty list.

### Scheduling an Interview
Scheduling an interview with duration for a person with positive integer indexes:
1. Assumption: The minutes of StartTime and Duration must be multiple of 5.
2. Test case: `interview 1 2025-04-01 10:00 40` <br>
  Expected: Interview scheduled with startTime 2025-04-01 10:00 and Duration 40;
3. Test case: `interview 0 2025-04-12 14:00 20` <br>
   Expected: No interview is scheduled, an error message will be displayed.
4. Other invalid interview command to try: <br>
   `interview 1 2025-04-12 14:00 23` <br>
   `interview 1 2025-04-12 14:12 20` <br>
   `interview 1 2025 04-12 14:12 15` <br>
   Expected: No interview is scheduled, an error message will be displayed.

### Adding a Note
Adding a note for a person:
1. Assumption: The index here is valid if it is a positive integer <br>
2. Test case: `note 1 Excellent technical skills observed during the interview.` <br>
   Expected: Note successfully added to the person's details.

### Sorting the List
Sort the persons by interview startTime in the current list by ascending order:
1. Test case: `sort` <br>
       Expected: List sorted by person's interview time. For persons without interviews,they are displayed at the end of the list.

### Viewing Help
Viewing help for the commands:
1. Test case: `help` <br> 
        Expected: Help window opens showing the link of User Guide.

### Undo
Restores the previous state:
1. Assumption: Last command belongs to modifying commands (add, delete, edit, etc.)
2. Test case: undo <br>
   Expected: Last operation is reversed. The person list is restored to the previous state.

### Redo
Reapplies the last undone action:
1. Assumption: Last command is undo.
2. Test case: redo <br>
   Expected: Previously undone action reapplied. The person list information is changed.


## **Planned Enhancements**

Team size: 4
1. **Support Multiple Job Applications per Candidate**: Currently, candidates can only apply for one job position and one team at a time, which doesn't reflect real-world scenarios where candidates might be able to apply for multiple roles. We plan to enhance this by:
    * Modifying the `add` command to accept multiple job positions and teams:
      ```
      add n/NAME p/PHONE e/EMAIL a/ADDRESS j/[JOB_POSITION_1, JOB_POSITION_2] 
      tm/[TEAM_1, TEAM_2] [t/TAG]...
      ```
    * Example usage:
      ```
      add n/John Doe p/98765432 e/johnd@example.com a/123 Apple Park Way 
      j/[Software Engineer, Product Manager] tm/[iOS, Product] t/Swift 
      t/UIKit t/Agile
      ```
    * Updating the UI to display multiple roles in the candidate card
    * Enhancing the `classify` command to match any of the candidate's roles:
      ```
      classify j/Software Engineer
      // Will show candidates who applied for Software Engineer, 
      // even if they applied for other roles too
      ```
    * Modifying the storage format to support arrays of job positions and teams per candidate

2. **Prevent Scheduling Interviews in the Past**: Currently, RecruitIntel allows scheduling interviews for past dates, which can lead to confusion and invalid data. We plan to enhance the interview scheduling feature by:
    * Adding date validation to ensure interview times are always in the future:
      ```
      // Current behavior
      interview 1 2023-01-01 10:00 30    // Allows scheduling in the past
      
      // Enhanced behavior
      interview 1 2023-01-01 10:00 30    
      // Error: Cannot schedule interviews in the past. The interview date 
      // must be after the current date and time
      ```
    * Implementing real-time date validation that:
        - Checks if the proposed interview date/time is at least 1 hour in the future
        - Provides helpful error messages with the current date/time for reference

3. **Enhanced Interview Sorting with Past Interview Handling**: Currently, the sort command doesn't distinguish between upcoming and past interviews, making it difficult to figure out if an interview has happened or not. We plan to enhance the sorting functionality by:
    * Adding flags to the sort command for more flexible sorting options:
      ```
      //Default: sorts all interviews
      sort 
      
      //Sorts interviews that haven't been completed first                    
      sort --nc             
      ```
    * Key behaviors:
        - sort --nc sorts upcoming interviews and places past interviews at the end, right before candidates with no interviews
        - Adds visual indicators for past interviews in the UI

4. **Improved Tag Management**: Currently, tags are completely replaced when editing, which can lead to accidental tag deletion. We plan to enhance tag management by:
    * Adding specific tag commands:
      ```
      tag 1 add t/Python        // Adds Python tag to candidate #1
      tag 1 remove t/Python     // Removes only Python tag
      tag 1 clear              // Removes all tags
      ```
    * Example usage scenarios:
      ```
      // Current behavior (using edit command)
      edit 1 t/Python          // Replaces ALL existing tags with just Python
      
      // Enhanced behavior (using new tag commands)
      tag 1 add t/Python       // Adds Python while preserving existing tags
      tag 1 remove t/Java      // Removes only Java tag, keeps others
      ```
    * Implementing tag validation:
        - Prevents duplicate tags automatically
        - Warns before removing all tags
    * Adding tag management features:
        - Group related tags (e.g., programming languages, soft skills)

5. **Enhanced Interview Command with Types**: Currently, the interview command only captures timing information without structuring the recruitment process. We plan to enhance the interview scheduling system by:
    * Adding interview type for better tracking of candidates in the recruitment process:
      ```
      //Schedule HR Interview
      interview 1 2024-03-20 14:00 60 --ty HR           
      
      //Schedule 1st Technical Interview
      interview 1 2024-03-22 10:00 90 --ty TECH --n 1   
      
      //Schedule Hiring Manager Interview
      interview 1 2024-03-25 15:00 45 --ty HM         
      ```
    * Supporting standard interview types:
      - HR: Initial screening interview
      - TECH: Technical assessment (can be numbered 1,2,...)
      - HM: Hiring manager interview
      - TEAM: Team fit discussion
      - SYS: System design interview
      - CODE: Coding assessment
      - BEHAV: Behavioral interview
      - FIN: Final round interview
      
    * Managing interview status:
      ```
      // Removes the scheduled interview for candidate #1
      interview 1 clear                
      ```
    * Adding type-based filtering:
      ```
      list interview --ty TECH   // List candidates with technical interviews
      ```

6. **Enhanced Notes Management**: Currently, notes can only be added as a single entry with limited character space. We plan to enhance the notes functionality by:
    * Adding commands for more flexible note management:
      ```
      // Adds a note entry for candidate #1
      note 1 add Great technical skills in the system design interview    
      
      // Appends some text to the 1st note
      note 1 append 1 Also showed strong team collaboration mindset        
      
      // Clears all text of the 1st note
      note 1 clear 1          
      
      // Removes the second note                                            
      note 1 remove 2                                                   
      ```
    * Supporting enhanced note features:
        - Increased character limit to 1500 characters for each note
        - Multiple note entries for each candidate
        - Timestamp for each note entry
    * Example usage:
      ```
      > note 1 add Technical Interview: Candidate showed strong Java knowledge.
      Added note #1 to John Doe
      
      > note 1 append Particularly impressed with his system design skills.
      Updated note #1 for John Doe
      
      > note 1 add Strong communication and intrapersonal skills. Involved 
      with alot of social work and clubs at university.
      Added note #2 to John Doe
      
      > note 1 list
      Notes for John Doe:
      1. Technical Interview: Candidate showed strong Java knowledge. 
      Particularly impressed with his system design skills.
      2. Strong communication and intrapersonal skills. Involved with alot 
      of social work and clubs at university.
      ```
      

