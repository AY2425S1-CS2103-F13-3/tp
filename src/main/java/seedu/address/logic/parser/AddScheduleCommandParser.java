package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddScheduleCommand;
import seedu.address.logic.parser.exceptions.ParseException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

public class AddScheduleCommandParser implements Parser<AddScheduleCommand> {

    @Override
    public AddScheduleCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CONTACT, PREFIX_NAME, PREFIX_DATE, PREFIX_TIME);

        if (!arePrefixesPresent(argMultimap, PREFIX_CONTACT, PREFIX_NAME, PREFIX_DATE, PREFIX_TIME) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddScheduleCommand.MESSAGE_USAGE));
        }

        try {
            Index contactIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CONTACT).get());
            String name = argMultimap.getValue(PREFIX_NAME).get().trim();
            LocalDate date = LocalDate.parse(argMultimap.getValue(PREFIX_DATE).get(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            LocalTime time = LocalTime.parse(argMultimap.getValue(PREFIX_TIME).get(), DateTimeFormatter.ofPattern("HHmm"));

            // Convert contactIndex to its zero-based integer value if needed.
            return new AddScheduleCommand(contactIndex.getZeroBased(), name, date, time);
        } catch (DateTimeParseException dtpe) {
            throw new ParseException(AddScheduleCommand.MESSAGE_INVALID_DATE);
        } catch (NumberFormatException nfe) {
            throw new ParseException(AddScheduleCommand.MESSAGE_INVALID_TIME);
        } catch (Exception e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddScheduleCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Returns true if all specified prefixes are present in the given {@code ArgumentMultimap}.
     */
    private boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        for (Prefix prefix : prefixes) {
            if (!argumentMultimap.getValue(prefix).isPresent()) {
                return false;
            }
        }
        return true;
    }
}
