package au.org.ala.delta.intkey.directives;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.FloatRange;
import org.apache.commons.lang.math.IntRange;

public class ParsingUtils {
    private static Pattern INT_RANGE_PATTERN = Pattern.compile("^(-?\\d+)-(-?\\d+)$");
    private static Pattern FLOAT_RANGE_PATTERN = Pattern.compile("^(-?\\d+(\\.\\d+)?)-(-?\\d+(\\.\\d+)?)$");

    public static Set<Integer> parseMultistateOrIntegerCharacterValue(String charValue) {
        Set<Integer> selectedStates = new HashSet<Integer>();
        
        //split on "/" character to get a list of ranges
        String[] tokens = charValue.split("/");
        
        for (String token: tokens) {
            IntRange r = parseIntRange(token);
            
            if (r == null) {
                throw new IllegalArgumentException("Invalid integer value");
            }
            
            for (int i: r.toArray()) {
                selectedStates.add(i);
            }
        }
        
        return selectedStates;
    }

    public static FloatRange parseRealCharacterValue(String charValue) {
        //The "/" character is interpreted as the range separator when 
        //parsing a real value.
        charValue = charValue.replace("/", "-");
        
        FloatRange r = parseFloatRange(charValue);

        if (r == null) {
            throw new IllegalArgumentException("Invalid real value");
        }

        return r;

    }

    public static List<String> parseTextCharacterValue(String charValue) {
        // Remove surrounding quotes if they are present
        charValue = removeEnclosingQuotes(charValue);

        List<String> retList = new ArrayList<String>();
        for (String s : charValue.split("/")) {
            retList.add(s);
        }

        return retList;
    }

    // TODO this method is the same as a method on the AbstractDirective
    // class. Need to refactor that one out to avoid duplication here.
    public static IntRange parseIntRange(String text) {
        try {
            Matcher m = INT_RANGE_PATTERN.matcher(text);
            if (m.matches()) {
                int lhs = Integer.parseInt(m.group(1));
                int rhs = Integer.parseInt(m.group(2));
                return new IntRange(lhs, rhs);
            } else {
                return new IntRange(Integer.parseInt(text));
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static FloatRange parseFloatRange(String text) {
        try {
            Matcher m = FLOAT_RANGE_PATTERN.matcher(text);
            if (m.matches()) {
                float lhs = Float.parseFloat(m.group(1));
                float rhs = Float.parseFloat(m.group(3));
                return new FloatRange(lhs, rhs);
            } else {
                return new FloatRange(Float.parseFloat(text));
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static List<String> tokenizeDirectiveCall(String data) {
        List<String> subCommands = new ArrayList<String>();

        boolean inQuotedString = false;
        int endLastSubcommand = 0;
        for (int i = 0; i < data.length(); i++) {
            boolean isEndSubcommand = false;

            char c = data.charAt(i);

            if (c == '"') {
                // Ignore quote if it is in the middle of a string - 
                // don't throw error for unmatched quotes.
                // this is the behaviour in the legacy intkey - may change this
                // later.

                if (i == 0) {
                    inQuotedString = true;
                } else if (i != data.length() - 1) {
                    char preceedingChar = data.charAt(i - 1);
                    char followingChar = data.charAt(i + 1);
                    if (inQuotedString && (followingChar == ' ' || followingChar == ',' || followingChar == '\n')) {
                        inQuotedString = false;
                    } else if (!inQuotedString && (preceedingChar == ' ' || preceedingChar == ',' || preceedingChar == '\n')) {
                        inQuotedString = true;
                    }
                }
            } else if ((c == ' ' || c == '\n') && !inQuotedString) {
                // if we're not inside a quoted string, then a space or newline designates
                // the end of a subcommand
                isEndSubcommand = true;
            }

            if (i == (data.length() - 1)) {
                // end of data string always designates the end of a subcommand
                isEndSubcommand = true;
            }

            if (isEndSubcommand) {
                String subCommand = null;
                if (endLastSubcommand == 0) {
                    subCommand = data.substring(endLastSubcommand, i + 1);
                } else {
                    subCommand = data.substring(endLastSubcommand + 1, i + 1);
                }

                // use trim to remove any remaining whitespace. Tokens that
                // consist solely of whitespace should be completely omitted.
                String trimmedSubcommand = subCommand.trim();
                if (trimmedSubcommand.length() > 0) {
                    subCommands.add(subCommand.trim());
                }
                endLastSubcommand = i;
            }
        }

        return subCommands;
    }

    public static String removeEnclosingQuotes(String str) {
        if (str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"') {
            return (str.substring(1, str.length() - 1));
        }
        return str;
    }

}
