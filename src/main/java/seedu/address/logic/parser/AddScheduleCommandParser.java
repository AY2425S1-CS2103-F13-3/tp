package seedu.address.logic.parser;

import static seedu.address.commons.util.PrefixPresenceUtil.arePrefixesPresent;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddScheduleCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddScheduleCommand object.
 */
public class AddScheduleCommandParser implements Parser<AddScheduleCommand> {

    @Override
    public AddScheduleCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_CONTACT, PREFIX_NAME, PREFIX_DATE, PREFIX_TIME);

        // Validate that all required prefixes are present
        if (!arePrefixesPresent(argMultimap, PREFIX_CONTACT, PREFIX_NAME, PREFIX_DATE, PREFIX_TIME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddScheduleCommand.MESSAGE_USAGE));
        }

        try {
            // Parse multiple contact indices from the input
            List<Index> contactIndexes = argMultimap.getAllValues(PREFIX_CONTACT).stream()
                    .flatMap(value -> Stream.of(value.split("\\s+"))) // Split multiple indices by spaces
                    .map(contactIndexStr -> {
                        try {
                            return ParserUtil.parseIndex(contactIndexStr);
                        } catch (ParseException e) {
                            throw new RuntimeException(e); // Wrap ParseException to unchecked exception
                        }
                    }).collect(Collectors.toList());

            // Parse the schedule name
            String name = argMultimap.getValue(PREFIX_NAME).get().trim();

            // Parse and validate the date
            LocalDate date;
            try {
                date = LocalDate.parse(argMultimap.getValue(PREFIX_DATE).get(),
                        DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (DateTimeParseException dtpe) {
                throw new ParseException(AddScheduleCommand.MESSAGE_INVALID_DATE);
            }

            // Parse and validate the time
            LocalTime time;
            try {
                time = LocalTime.parse(argMultimap.getValue(PREFIX_TIME).get(),
                        DateTimeFormatter.ofPattern("HHmm"));
            } catch (DateTimeParseException dtpe) {
                throw new ParseException(AddScheduleCommand.MESSAGE_INVALID_TIME);
            }

            // Return AddScheduleCommand with the parsed contact indexes, name, date, and time
            return new AddScheduleCommand(contactIndexes, name, date, time);
        } catch (RuntimeException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddScheduleCommand.MESSAGE_USAGE));
        }
    }
}
