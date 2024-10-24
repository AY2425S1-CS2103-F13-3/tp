package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.PrefixPresenceUtil.arePrefixesPresent;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FavouriteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FavouriteCommand object.
 */
public class FavouriteCommandParser implements Parser<FavouriteCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FavouriteCommand
     * and returns a FavouriteCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public FavouriteCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CONTACT);
        // Validate that all required prefixes are present
        if (!arePrefixesPresent(argMultimap, PREFIX_CONTACT)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FavouriteCommand.MESSAGE_USAGE));
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
            return new FavouriteCommand(contactIndexes);
        } catch (RuntimeException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FavouriteCommand.MESSAGE_USAGE));
        }
    }
}
