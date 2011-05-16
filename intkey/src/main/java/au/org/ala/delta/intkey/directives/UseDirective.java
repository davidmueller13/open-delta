package au.org.ala.delta.intkey.directives;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.commons.lang.math.FloatRange;
import org.apache.commons.lang.math.IntRange;

import au.org.ala.delta.intkey.model.CharacterComparator;
import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.intkey.model.specimen.CharacterValue;
import au.org.ala.delta.intkey.model.specimen.IntegerValue;
import au.org.ala.delta.intkey.model.specimen.MultiStateValue;
import au.org.ala.delta.intkey.model.specimen.RealValue;
import au.org.ala.delta.intkey.model.specimen.TextValue;
import au.org.ala.delta.intkey.ui.CharacterKeywordSelectionDialog;
import au.org.ala.delta.intkey.ui.CharacterSelectionDialog;
import au.org.ala.delta.intkey.ui.IntegerInputDialog;
import au.org.ala.delta.intkey.ui.MultiStateInputDialog;
import au.org.ala.delta.intkey.ui.RealInputDialog;
import au.org.ala.delta.intkey.ui.TextInputDialog;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.IntegerCharacter;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.RealCharacter;
import au.org.ala.delta.model.TextCharacter;

public class UseDirective extends IntkeyDirective {

    // TODO complete parsing for non-text character values
    // TODO do "toString()" for invocation class
    // TODO show message box when try to set a character value but it fails due
    // to it not
    // available - need to do anything else when this happens?

    private static Pattern COMMA_SEPARATED_VALUE_PATTERN = Pattern.compile("^.+,.*$");

    public UseDirective() {
        super("use");
    }

    @Override
    public IntkeyDirectiveInvocation doProcess(IntkeyContext context, String data) throws Exception {
        if (context.getDataset() != null) {
            boolean suppressAlreadySetWarning = false;

            List<Integer> characterNumbers = new ArrayList<Integer>();
            List<String> specifiedValues = new ArrayList<String>();

            if (data != null && data.trim().length() > 0) {
                List<String> subCommands = ParsingUtils.splitDataIntoSubCommands(data);

                for (String subCmd : subCommands) {
                    // TODO need to handle additional undocumented flags
                    if (subCmd.equalsIgnoreCase("/M")) {
                        suppressAlreadySetWarning = true;
                    } else {
                        parseSubcommands(subCmd, characterNumbers, specifiedValues, context);
                    }
                }

            } else {
                // No characters specified, prompt the user to select characters
                CharacterKeywordSelectionDialog dlg = new CharacterKeywordSelectionDialog(context.getMainFrame(), context);
                dlg.setVisible(true);
                List<Character> selectedCharacters = dlg.getSelectedCharacters();
                if (selectedCharacters.size() > 0) {
                    for (Character ch : selectedCharacters) {
                        characterNumbers.add(ch.getCharacterId());
                        specifiedValues.add(null);
                    }
                }
            }

            UseDirectiveInvocation invoc = new UseDirectiveInvocation(suppressAlreadySetWarning);

            for (int i = 0; i < characterNumbers.size(); i++) {
                int charNum = characterNumbers.get(i);
                au.org.ala.delta.model.Character ch = context.getDataset().getCharacter(charNum);

                // Parse the supplied value for each character, or prompt for
                // one if no value was supplied

                String charValue = specifiedValues.get(i);

                if (charValue != null) {
                    if (ch instanceof MultiStateCharacter) {
                        List<Integer> stateValues = ParsingUtils.parseMultiStateCharacterValue(charValue);
                        invoc.addCharacterValue((MultiStateCharacter) ch, new MultiStateValue((MultiStateCharacter) ch, stateValues));
                    } else if (ch instanceof IntegerCharacter) {
                        IntRange intRange = ParsingUtils.parseIntegerCharacterValue(charValue);
                        invoc.addCharacterValue((IntegerCharacter) ch, new IntegerValue((IntegerCharacter) ch, intRange));
                    } else if (ch instanceof RealCharacter) {
                        FloatRange floatRange = ParsingUtils.parseRealCharacterValue(charValue);
                        invoc.addCharacterValue((RealCharacter) ch, new RealValue((RealCharacter) ch, floatRange));
                    } else if (ch instanceof TextCharacter) {
                        List<String> stringList = ParsingUtils.parseTextCharacterValue(charValue);
                        invoc.addCharacterValue((TextCharacter) ch, new TextValue((TextCharacter) ch, stringList));
                    } else {
                        throw new IllegalArgumentException("Unrecognized character type");
                    }
                } else {
                    invoc.addCharacterValue(ch, null);
                }
            }
            return invoc;
        } else {
            throw new IntkeyDirectiveParseException("Need to have a dataset loaded before USE can be called.");
        }

        // TODO Auto-generated method stub

        // INITALIZE

        // PROCESS CHARACTERS WITH ATTRIBUTES FIRST
        // for each character specified
        // process controlling characters of the character (dataset.cc_process)
        // use character

        // PROCESS CHARACTERS WITHOUT ATTRIBUTES NEXT
    }

    private void parseSubcommands(String subCmd, List<Integer> characterNumbers, List<String> specifiedValues, IntkeyContext context) throws Exception {

        List<Integer> parsedCharacterNumbers;

        // switch
        if (COMMA_SEPARATED_VALUE_PATTERN.matcher(subCmd).matches()) {
            // comma separated value: c,v

            String lhs = null;
            String rhs = null;

            String[] innerPieces = subCmd.split(",");
            if (innerPieces.length > 2) {
                throw new Exception("Bad Syntax");
            }

            lhs = innerPieces[0];

            if (innerPieces.length == 2) {
                rhs = innerPieces[1];
            }

            parsedCharacterNumbers = parseLHS(lhs, context);
            for (int c : parsedCharacterNumbers) {
                specifiedValues.add(rhs);
            }
        } else {
            parsedCharacterNumbers = parseLHS(subCmd, context);
            for (int c : parsedCharacterNumbers) {
                specifiedValues.add(null);
            }
        }
        characterNumbers.addAll(parsedCharacterNumbers);
    }

    private List<Integer> parseLHS(String lhs, IntkeyContext context) {
        List<Integer> retList = new ArrayList<Integer>();

        IntRange range = ParsingUtils.parseIntRange(lhs);
        if (range != null) {
            for (int i : range.toArray()) {
                retList.add(i);
            }
        } else {
            // TODO handle exception if not valid keyword passed.
            List<Character> charList = context.getCharactersForKeyword(lhs);
            for (Character c : charList) {
                retList.add(c.getCharacterId());
            }
        }

        return retList;
    }

    class UseDirectiveInvocation implements IntkeyDirectiveInvocation {

        private Map<au.org.ala.delta.model.Character, CharacterValue> _characterValues;
        private boolean _suppressAlreadySetWarning;

        public UseDirectiveInvocation(boolean suppressAlreadySetWarning) {
            _suppressAlreadySetWarning = suppressAlreadySetWarning;
            
            //Use LinkedHashMap so that keys can be iterated over in the order that they
            //were inserted.
            _characterValues = new LinkedHashMap<Character, CharacterValue>();
        }

        @Override
        public boolean execute(IntkeyContext context) {
            // These are the values that will be stored in the specimen
            // Each time execute() is called some values may be omitted from
            // this list...
            // TODO finish comment
            List<Character> charactersToUse = new ArrayList<Character>();

            // Split up characters that have had their values specified and
            // those that
            // haven't. They need to be processed differently.
            List<Character> charsWithValues = new ArrayList<Character>();
            List<Character> charsNoValues = new ArrayList<Character>();

            for (Character ch : _characterValues.keySet()) {
                if (_characterValues.get(ch) == null) {
                    charsNoValues.add(ch);
                } else {
                    charsWithValues.add(ch);
                }
            }

            // Validate each of the character values that has been specified.
            for (Character ch : charsWithValues) {
                if (checkCharacterUsable(ch, context)) {
                    charactersToUse.add(ch);
                } 
            }

            if (charsNoValues.size() == 1) {
                Character ch = charsNoValues.get(0);
                if (checkCharacterUsable(ch, context)) {
                    CharacterValue characterVal = promptForCharacterValue(context.getMainFrame(), ch);
                    if (characterVal != null) {
                        // store this value so that the prompt does not need
                        // to
                        // be done for subsequent invocations
                        _characterValues.put(ch, characterVal);
                        charactersToUse.add(ch);
                    } else {
                        // User hit cancel or did not enter a value when
                        // prompted.
                        // Abort execution when this happens
                        return false;
                    }
                } else {
                    // remove this value so that the user will not be prompted about it when the command is
                    // run additional times.
                    _characterValues.remove(ch);
                }
            } else {
                Collections.sort(charsNoValues, new CharacterComparator());
                while (!charsNoValues.isEmpty()) {

                    CharacterSelectionDialog selectDlg = new CharacterSelectionDialog(context.getMainFrame(), charsNoValues);
                    selectDlg.setVisible(true);

                    List<Character> selectedCharacters = selectDlg.getSelectedCharacters();

                    if (selectedCharacters.isEmpty()) {
                        // User hit cancel or did not select any characters.
                        // Abort
                        // execution when this happens.
                        // Directive should not be stored in
                        // Execution history
                        return false;
                    }

                    for (Character ch : selectedCharacters) {

                        CharacterValue characterVal = null;

                        if (checkCharacterUsable(ch, context)) {
                            characterVal = promptForCharacterValue(context.getMainFrame(), ch);
                        } else {
                            // remove this value so that the user will not be prompted about it when the command is
                            // run additional times.
                            _characterValues.remove(ch);
                        }

                        if (characterVal != null) {
                            // store this value so that the prompt does not need
                            // to
                            // be done for subsequent invocations
                            _characterValues.put(ch, characterVal);
                            charactersToUse.add(ch);
                            charsNoValues.remove(ch);
                        }
                    }
                }
            }

            for (Character ch : charactersToUse) {
                CharacterValue characterVal = _characterValues.get(ch);

                context.setValueForCharacter(ch, characterVal);
            }

            return true;
        }

        void addCharacterValue(au.org.ala.delta.model.Character ch, CharacterValue val) {
            _characterValues.put(ch, val);
        }

        private boolean checkCharacterUsable(Character ch, IntkeyContext context) {
            // is character fixed?

            // is character already used?
            if (!_suppressAlreadySetWarning) {
                if (context.getSpecimen().hasValueFor(ch)) {
                    String msg = String.format("Character %s has already used. Do you want to change the value(s) you entered?", ch.getCharacterId());
                    int choice = JOptionPane.showConfirmDialog(context.getMainFrame(), msg, "Information", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }

            // is character unavailable?
            // is character excluded?

            return true;
        }

        private CharacterValue promptForCharacterValue(Frame frame, Character ch) {
            CharacterValue characterVal = null;

            if (ch instanceof MultiStateCharacter) {
                List<Integer> stateValues = promptForMultiStateValue(frame, (MultiStateCharacter) ch);
                if (stateValues.size() > 0) {
                    characterVal = new MultiStateValue((MultiStateCharacter) ch, stateValues);
                }
            } else if (ch instanceof IntegerCharacter) {
                IntRange intRange = promptForIntegerValue(frame, (IntegerCharacter) ch);
                if (intRange != null) {
                    characterVal = new IntegerValue((IntegerCharacter) ch, intRange);
                }
            } else if (ch instanceof RealCharacter) {
                FloatRange floatRange = promptForRealValue(frame, (RealCharacter) ch);
                if (floatRange != null) {
                    characterVal = new RealValue((RealCharacter) ch, floatRange);
                }
            } else if (ch instanceof TextCharacter) {
                List<String> stringList = promptForTextValue(frame, (TextCharacter) ch);
                if (stringList.size() > 0) {
                    characterVal = new TextValue((TextCharacter) ch, stringList);
                }
            } else {
                throw new IllegalArgumentException("Unrecognized character type");
            }

            return characterVal;
        }

        private List<Integer> promptForMultiStateValue(Frame frame, MultiStateCharacter ch) {
            MultiStateInputDialog dlg = new MultiStateInputDialog(frame, ch);
            dlg.setVisible(true);
            return dlg.getInputData();
        }

        private IntRange promptForIntegerValue(Frame frame, IntegerCharacter ch) {
            IntegerInputDialog dlg = new IntegerInputDialog(frame, ch);
            dlg.setVisible(true);
            return dlg.getInputData();
        }

        private FloatRange promptForRealValue(Frame frame, RealCharacter ch) {
            RealInputDialog dlg = new RealInputDialog(frame, ch);
            dlg.setVisible(true);
            return dlg.getInputData();
        }

        private List<String> promptForTextValue(Frame frame, TextCharacter ch) {
            TextInputDialog dlg = new TextInputDialog(frame, ch);
            dlg.setVisible(true);
            return dlg.getInputData();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("USE ");
            for (Character ch: _characterValues.keySet()) {
                CharacterValue val = _characterValues.get(ch);
                builder.append(" ");
                builder.append(ch.getCharacterId());
                builder.append(",");
                builder.append(val.toShortString());
            }
            return builder.toString();
        }
    }
}
