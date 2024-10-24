package seedu.address.commons.util;

import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.Prefix;

/**
 * Utility class for checking the presence of required prefixes in an {@code ArgumentMultimap}.
 */
public class PrefixPresenceUtil {
    /**
     * Checks if all the specified {@code Prefix} objects are present in the given {@code ArgumentMultimap}.
     *
     * @param argumentMultimap The {@code ArgumentMultimap} containing user inputs mapped by prefixes.
     * @param prefixes The prefixes to check for presence in the {@code ArgumentMultimap}.
     * @return {@code true} if all specified prefixes are present, {@code false} otherwise.
     */
    public static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        for (Prefix prefix : prefixes) {
            if (!argumentMultimap.getValue(prefix).isPresent()) {
                return false;
            }
        }
        return true;
    }
}
