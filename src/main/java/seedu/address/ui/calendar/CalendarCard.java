package seedu.address.ui.calendar;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.schedule.Meeting;
import seedu.address.ui.UiPart;

/**
 * An UI component that displays information of a {@code Meeting}.
 */
public class CalendarCard extends UiPart<Region> {

    private static final String FXML = "CalendarListCard.fxml";
    private final Meeting meeting;

    @FXML
    private HBox cardPane;
    @FXML
    private Label event;
    @FXML
    private Label id;
    @FXML
    private Label date;
    @FXML
    private Label time;
    @FXML
    private VBox associatedContacts;

    /**
     * Creates a {@code CalendarCard} with the given {@code meeting} and index to display.
     */
    public CalendarCard(Meeting meeting, int displayedIndex, ReadOnlyAddressBook addressBook) {
        super(FXML);
        this.meeting = meeting;
        id.setText(displayedIndex + ".");
        event.setText(meeting.getMeetingName());
        date.setText(meeting.getMeetingDate().toString());
        time.setText(meeting.getMeetingTime().toString());
        meeting.getContactUids()
            .forEach(contact -> {
                FilteredList<Person> filteredList = addressBook.getPersonList()
                        .filtered(p -> p.getUid().equals(contact));
                if (!filteredList.isEmpty()) {
                    associatedContacts.getChildren().add(
                            new Label(filteredList.get(0).getName().toString())
                    );
                }
            });
    }
}
