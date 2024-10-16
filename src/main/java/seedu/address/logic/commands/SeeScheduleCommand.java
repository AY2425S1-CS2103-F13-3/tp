package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.schedule.Meeting;

/**
 * Represents a command to view the schedule for a specific week starting from a given date.
 * The date is provided by the user and must follow the format "DD-MM-YYYY".
 */
public class SeeScheduleCommand extends Command {
    public static final String COMMAND_WORD = "see";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": See your schedule for the week. "
            + "Parameters: d/\n"
            + "Example: " + COMMAND_WORD + " d/10-10-2024";

    public static final String MESSAGE_INVALID_DATE = "Date must be in the format DD-MM-YYYY.";

    private final Predicate<Meeting> predicate;

    public SeeScheduleCommand(Predicate<Meeting> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        model.changeWeeklySchedule(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_SCHEDULE_LISTED_OVERVIEW, "temp", "temp"));
    }
}
