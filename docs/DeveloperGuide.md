---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
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

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data, user preference and schedule data in JSON format, and read them back into corresponding objects.
* inherits from `ScheduleStorage`, `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### View meeting contacts feature

Using `FindCommand`, we list contacts which are in the meeting based on UID.

<puml src="diagrams/MeetingContactsSequenceDiagram.puml"/>

### Add meetings to a schedule feature

Using `AddScheduleCommand`, we add a meeting to a schedule.

<puml src="diagrams/AddScheduleSequenceDiagram.puml"/>

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
    * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


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

* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps
* Busy Students and Professionals with a need to coordinate available meeting times with their contacts

**Value proposition**: manage contacts faster than a typical mouse/GUI driven app

Our application helps to coordinate meeting timings with relevant contacts, by keeping track of the schedules of the user’s contacts. This will be very useful for busy Students and Professionals that have many meetings with different people throughout the day.

By making the commands similar to Natural Language, our application aims to provide a user-centric experience. The ease of use would make the product an attractive alternative to current Calendar applications that require the user to navigate multiple screens and input their meetings.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                               | I want to …​                                                                 | So that…​                                                                            |
|----------|--------------------------------------------|-----------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| `* * *`  | new user                                   | see usage instructions                                                            | refer to instructions when I forget how to use the App                                    |
| `* * *`  | user                                       | add a new person                                                                  |                                                                                           |
| `* * *`  | user                                       | delete a person                                                                   | remove entries that I no longer need                                                      |
| `* * *`  | user                                       | find a person by name                                                             | locate details of persons without having to go through the entire list                    |
| `* *`    | user                                       | hide private contact details                                                      | minimize chance of someone else seeing them by accident                                   |
| `*`      | user with many persons in the address book | sort persons by name                                                              | locate a person easily                                                                    |
| `* * *`  | novice user                                | be able to add my schedule                                                        | I am able to keep track of my own schedule to plan meetings with others                   |
| `* * *`  | novice user                                | be able to delete my schedule                                                     | I am able to remove unwanted schedule                                                     |
| `* * *`  | novice user                                | be able to see my schedule                                                        | I am able to find a suitable timing for me to schedule meetings with others               |
| `* * *`  | beginner user                              | be able to see information of my contacts                                         | I am able to find the relevant information needed to contact and arrange for meetings     |
| `* *`    | new user                                   | have a tutorial on the SeeRee 2.0                                                 | I can use it efficiently in planning for my weekly tasks                                  |
| `* *`    | new user                                   | have a settings tab                                                               | I can configure SeeRee 2.0 to my needs and preferences                                    |
| `* *`    | beginner user                              | have error messages that inform me of my mistakes                                 | I can learn the correct implementation of the commands of the CLI                         |
| `* *`    | beginner user                              | edit present contacts that I have                                                 | I do not need to delete made contacts and can easily edit any changes                     |
| `* *`    | novice user                                | be able to edit my schedule                                                       | I am able to adjust my schedule details according to any changes that occur               |
| `* *`    | cli enthusiast                             | be able to navigate the entire app using only keyboard commands                   | I can use the app effectively without a mouse                                             |
| `*`      | new user                                   | have a nice streamlined CLI interface                                             | it is easy on my eyes when I am focusing on planning my tasks                             |
| `*`      | expert user                                | be able to see the schedules of my contacts                                       | I am able to schedule meetings at approriate timings for everyone                         |
| `*`      | expert user                                | delete large groups of contacts                                                   | I am able to reduce clutter of information and schedules when they become irrelevant      |
| `*`      | expert user                                | add large groups of contacts                                                      | I am able to efficiently add related members and not have to type redundant information   |
| `*`      | beginner user                              | have reminders for project and task deadline                                      | I am able to stay on track with my work within the week                                   |
| `*`      | expert user                                | be able to create personalised shortcuts for commonly used functions              | I am able to save time on frequently performed tasks                                      |
| `*`      | beginner user                              | add notes to each contact (e.g. project role, preferred contact method)           | I can remember important details when reaching out                                        |
| `*`      | new user                                   | categorize my contacts by groups (e.g. students, team members)                    | I can easily find the right people based on the project or class                          |
| `*`      | novice user                                | mark certain contacts as "favorites"                                              | I can easily access key people I frequently interact with, like project leads or students |
| `*`      | expert user                                | create contact reminders (e.g. "check in with students after the midterm")        | I remember to follow up at key points throughout the semester                             |
| `*`      | new user                                   | sort my contacts alphabetically or by other method                                | I can quickly find the contact I need                                                     |
| `*`      | expert user                                | archive contacts that I no longer need but might want to reference in the future  | my main contact list stays clutter-free without losing old connections                    |
| `*`      | new user                                   | have a reccomended path to travel to meeting rooms                                | I will be able to reach meeting venues easily                                             |
| `*`      | speedy user                                | be able to check whether a specific meeting time has conflicts easily             | plan meeting time more efficiently                                                        |
| `*`      | busy user                                  | have notifications of my upcoming meetings everyday                               | I will remember what meetings I want to attend                                            |
| `*`      | team member                                | export my schedule into a nice text using a single command                        | I will be able to communicate to my teammates my schedule easily                          |
| `*`      | team member                                | import my teammate schedule using a single command                                | I can use the App to plan meeting times easily                                            |
| `*`      | careless user                              | be able to bulk edit or delete the tasks for the day                              |                                                                                           |
| `*`      | long time user                             | merge duplicate contacts easily                                                   | I can consolidate information from multiple entries                                       |
| `*`      | speedy user                                | (I want) the app to load my schedule within 2 seconds                             | I can quickly access my information without waiting                                       |
| `*`      | non-native English speaker                 | (I want) the app to be available in multiple languages                            | I can use it in my preferred language                                                     |
| `*`      | expert user                                | be able to export my data in standard formats (e.g., iCal, CSV)                   | I can easily migrate my information if needed                                             |
| `*`      | power user                                 | be able to customize the app's interface and features to my preferences           | I can optimize my workflow                                                                |

### Use cases

For all use cases below, the **System** is the `SeeRee 2.0` and the **Actor** is the `user`, unless specified otherwise

<u>**Use case: UC0 - Delete a person**</u>

**MSS**

1.  User requests to list persons
2.  AddressBook shows a list of persons
3.  User requests to delete a specific person in the list
4.  AddressBook deletes the person

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

<u>**Use Case: UC1 - Adding events to a schedule**</u>

**Preconditions:**
- User is logged in with access to own schedule.

**Guarantees**
- User’s schedule will be updated with new events only if the command is successful.
- User’s old events on the schedule will not be edited.

**MSS**
1. User requests to add a new event to their schedule.
2. System adds a new event to the user's schedule.
3. User’s schedule is updated with new event.

   Use case ends.

**Extensions**
- 1a. System detects an error in the entered data command format.

    - 1a1. System requests the user to enter the proper command format.

      Use case resume from step 1.

- 1b. System detects a duplicate error in the entered data command.

    - 1b1. System requests the user to enter a non-conflicting time for the event.

      Use case resumes from step 1.

<u>**Use Case: UC2 - Delete events of a schedule**</u>

**Preconditions**
- User is logged in with access to own schedule
- The specific event to be deleted exists within the schedule

**Guarantees**
- User’s schedule will only be updated if the command is successful.
- Other events on the user’s schedule will not be deleted.
- There will not be gaps between the indexes of the new schedule after deletion.

**MSS**
1. User requests to delete a specific event on their schedule.
2. System deletes the event.
3. User’s schedule is updated without the removed event.

   Use case ends.

**Extensions**
- 1a. The given index is invalid

    - 1a1. System shows an error message

      Use case resume from step 1.

<u>**Use Case: UC3 - View schedule**</u>

**Preconditions**
- User is logged in with access to own schedule.

**MSS**
1. User requests to see a specific week in his schedule
2. System updates schedule window to display schedule for that week

   Use case ends.

**Extensions**
- 1a. The input date specified by the user does not follow the date format.

    - 1a1. System shows an error message, notifying the user the correct date format

      Use case ends.

<u>**Use Case: UC4 - View contact information**</u>

**Preconditions**
- User is logged in with access to own contacts

**Guarantees**
- Contacts with that name will be displayed.
- If no match, display a message notifying the user that no contacts with that name are found.

**MSS**
1. User finds for contact using name.
2. System looks for contacts with that name.
3. System displays all contacts with that name.

   Use case ends.

**Extensions**
- 2a. System is unable to find any user with that name.

    - 2a1. System notifies the user it is unable to find any user with that name.

      Use case ends.

<u>**Use Case: UC5 - Editing an existing schedule**</u>

**Preconditions**
- Users are logged in with access to their own schedule.
- The schedule to be edited already exists in the system.

**Guarantees**
- The schedule will be updated with the provided details only if the command is successful.
- The schedule's details will remain unchanged if the command fails.

**MSS**
1. User requests to edit an existing schedule using the edit-schedule command.
2. System checks the format of the entered data and verifies that the index is in range.
3. System updates the schedule with the provided name, date, time, and contact changes (if specified).
4. User's schedule is successfully updated.

   Use case ends.

**Extensions**
- 1a. The system detects an error in the entered command format (e.g., invalid date or time format, index out of range).

    - 1a1. The system displays an error message: "Invalid edit-schedule format. Please make sure that the index is in range. d/[DATE] is in the format of DD-MM-YYYY. t/[TIME] is in the format of hhmm (24 hours notation)."

      Use case resumes from step 1.

- 1b. The system detects that the contact index to be added or removed is out of range or invalid.

    - 1b1. The system displays an error message indicating the invalid contact index.

      Use case resumes from step 1.

- 1c. The system detects that the contact specified for removal is not in the current schedule.

    - 1c1. The system displays an error message: "Contact not found in the schedule."

      Use case resumes from step 1.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Is portable, the software should work without requiring an installer, only requires Java 17.
5.  The data should be stored locally and should be in a human editable text file.
6.  The product should be for a single user.
7.  The product is not required to use a Database Management System to store data.
8.  The product is not required to depend on your a remote server.
9.  The GUI should work well, the GUI should be usable.
10. The product should be packaged into a single JAR file.
11. The product is not required to use third-party frameworks/libraries/services.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Meeting**: A user-defined event that includes information such as a name, date, time, and associated contacts. Can be edited, added, or deleted.
* **Schedule**: A collection of events (meetings) associated with a user’s contacts. The schedule can be viewed, modified, and checked for conflicts.
* **Contact**: An individual or entity that is linked to a schedule. Contacts can be added or removed from schedules via commands.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

    1. Test case: `delete 1`<br>
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

    1. Test case: `delete 0`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

    1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_

### Adding a Schedule

1. Test case: add-schedule c/1 n/Team Meeting d/x t/1400 OR add-schedule c/1 n/Team Meeting d/11-10-2024 t/x

    1. Expected: Invalid command format!
       add-schedule: Adds a schedule to a contact. Parameters: c/CONTACT_INDEX n/NAME d/DATE t/TIME
       Example: add-schedule c/1 n/Dinner d/10-10-2024 t/1800

2. Test case: add-schedule c/1 n/Team Meeting d/11-10-2024 t/1400

    1. Expected: New schedule added: Team Meeting on 2024-10-11 at 14:00

3. Test case: add-schedule c/1 n/Team Meeting d/10-10-2024 t/1400 (duplicated command)

    1. Expected: This schedule conflicts with an existing schedule.

### View schedule

1. Test case: see d/

    1. Expected: Invalid command format!
       see: See your schedule for the week. Parameters: d/
       Example: see d/10-10-2024

2. Test case: see d/11-10-2024

    1. Expected: Sun 06-10-2024 to Sat 12-10-2024 schedule listed!

### Edit schedule

1. Test case:

    1. Expected:

### Delete schedule

1. Test case:

    1. Expected: 
