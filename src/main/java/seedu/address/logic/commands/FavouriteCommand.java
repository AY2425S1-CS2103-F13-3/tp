package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Represents a command to mark contacts as favourites.
 */
public class FavouriteCommand extends Command {
    public static final String COMMAND_WORD = "favourite";
    public static final String MESSAGE_USAGE = COMMAND_WORD + " Makes a Contact have Favourite Status "
            + "Parameters: c/CONTACT_INDEXES \n"
            + "Example: " + COMMAND_WORD + " c/1 2";
    public static final String MESSAGE_SUCCESS = "Favourite contact(s) updated: %s";
    public static final String MESSAGE_INVALID_INDEX = "One or more contact indexes are invalid.";
    private final List<Index> contactIndexes;
    /**
     * Creates a FavouriteCommand to mark the specified contacts as favourites.
     *
     * @param contactIndexes The list of indexes of contacts to be marked as favourite.
     */
    public FavouriteCommand(List<Index> contactIndexes) {
        requireNonNull(contactIndexes);
        this.contactIndexes = contactIndexes;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        for (Index i : contactIndexes) {
            if (i.getZeroBased() >= lastShownList.size() || i.getZeroBased() < 0) {
                throw new CommandException("One or more contact indexes are invalid.");
            }
        }
        List<Person> personsList = contactIndexes.stream()
            .map(index -> lastShownList.get(index.getZeroBased()))
            .collect(Collectors.toList());
        StringBuilder favouritePersonMessage = new StringBuilder();
        for (Person p : personsList) {
            Person favouritePerson = createFavouritePerson(p);
            model.setPerson(p, favouritePerson);
            favouritePersonMessage.append(favouritePerson.toNameString()).append("\n");
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, favouritePersonMessage));
    }
    private static Person createFavouritePerson(Person personToEdit) {
        assert personToEdit != null;
        return personToEdit.setFavouritePerson();
    }
}
