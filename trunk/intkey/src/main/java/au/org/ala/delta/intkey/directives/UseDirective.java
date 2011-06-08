package au.org.ala.delta.intkey.directives;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.FloatRange;
import org.apache.commons.lang.math.IntRange;

import au.org.ala.delta.directives.args.DirectiveArgType;
import au.org.ala.delta.directives.args.DirectiveArgs;
import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.intkey.model.IntkeyDataset;
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
import au.org.ala.delta.intkey.ui.UIUtils;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.CharacterDependency;
import au.org.ala.delta.model.IntegerCharacter;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.RealCharacter;
import au.org.ala.delta.model.TextCharacter;
import au.org.ala.delta.model.format.CharacterFormatter;

public class UseDirective extends IntkeyDirective {

    // TODO show message box when try to set a character value but it fails due
    // to it not
    // available - need to do anything else when this happens?

    // TODO UI should only update once at the end - should not update for each
    // controlling
    // character that is prompted for.

    private static Pattern COMMA_SEPARATED_VALUE_PATTERN = Pattern.compile("^.+,.*$");

    public UseDirective() {
        super("use");
    }

    @Override
    public DirectiveArgs getDirectiveArgs() {
        throw new NotImplementedException();
    }

    @Override
    public int getArgType() {
        return DirectiveArgType.DIRARG_INTKEY_ATTRIBUTES;
    }

    @Override
    public IntkeyDirectiveInvocation doProcess(IntkeyContext context, String data) throws Exception {
        return doProcess(context, data, false);
    }

    public IntkeyDirectiveInvocation doProcess(IntkeyContext context, String data, boolean change) throws Exception {
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
                String directiveName = change ? directiveName = StringUtils.join(new ChangeDirective().getControlWords(), " ").toUpperCase() : StringUtils.join(_controlWords, " ").toUpperCase();
                CharacterKeywordSelectionDialog dlg = new CharacterKeywordSelectionDialog(UIUtils.getMainFrame(), context, directiveName);
                dlg.setVisible(true);
                List<Character> selectedCharacters = dlg.getSelectedCharacters();
                if (selectedCharacters.size() > 0) {
                    for (Character ch : selectedCharacters) {
                        characterNumbers.add(ch.getCharacterId());
                        specifiedValues.add(null);
                    }
                }
            }

            UseDirectiveInvocation invoc = new UseDirectiveInvocation(change, suppressAlreadySetWarning);

            for (int i = 0; i < characterNumbers.size(); i++) {
                int charNum = characterNumbers.get(i);
                au.org.ala.delta.model.Character ch = context.getDataset().getCharacter(charNum);

                // Parse the supplied value for each character, or prompt for
                // one if no value was supplied

                String charValue = specifiedValues.get(i);

                if (charValue != null) {
                    if (ch instanceof MultiStateCharacter) {
                        List<Integer> stateValues = ParsingUtils.parseMultiStateCharacterValue(charValue);
                        // TODO need error if non existent state values are
                        // listed
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
            JOptionPane.showMessageDialog(UIUtils.getMainFrame(), UIUtils.getResourceString("UseDirective.NoDataSetMsg"));
            return null;
        }

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
        private boolean _change;
        private boolean _suppressAlreadySetWarning;

        public UseDirectiveInvocation(boolean change, boolean suppressAlreadySetWarning) {
            _change = change;
            _suppressAlreadySetWarning = suppressAlreadySetWarning;

            // Use LinkedHashMap so that keys can be iterated over in the order
            // that they
            // were inserted.
            _characterValues = new LinkedHashMap<Character, CharacterValue>();
        }

        @Override
        public boolean execute(IntkeyContext context) {

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

            // Process characters with values specified first
            for (Character ch : charsWithValues) {
                processControllingCharacters(ch, context);
                if (checkCharacterUsable(ch, context)) {
                    CharacterValue characterVal = _characterValues.get(ch);
                    context.setValueForCharacter(ch, characterVal);
                }
            }

            if (charsNoValues.size() == 1) {
                Character ch = charsNoValues.get(0);
                processControllingCharacters(ch, context);
                if (checkCharacterUsable(ch, context)) {
                    CharacterValue characterVal = promptForCharacterValue(UIUtils.getMainFrame(), ch);
                    if (characterVal != null) {
                        // store this value so that the prompt does not need
                        // to
                        // be done for subsequent invocations
                        _characterValues.put(ch, characterVal);
                        context.setValueForCharacter(ch, characterVal);
                    } else {
                        // User hit cancel or did not enter a value when
                        // prompted.
                        // Abort execution when this happens
                        return false;
                    }
                } else {
                    // remove this value so that the user will not be prompted
                    // about it when the command is
                    // run additional times.
                    _characterValues.remove(ch);
                }
            } else {
                Collections.sort(charsNoValues);
                while (!charsNoValues.isEmpty()) {
                    String directiveName = _change ? directiveName = StringUtils.join(new ChangeDirective().getControlWords(), " ").toUpperCase() : StringUtils.join(_controlWords, " ").toUpperCase();

                    CharacterSelectionDialog selectDlg = new CharacterSelectionDialog(UIUtils.getMainFrame(), charsNoValues, directiveName);
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

                        processControllingCharacters(ch, context);
                        if (checkCharacterUsable(ch, context)) {
                            characterVal = promptForCharacterValue(UIUtils.getMainFrame(), ch);
                        } else {
                            // remove this value so that the user will not be
                            // prompted about it when the command is
                            // run additional times.
                            _characterValues.remove(ch);
                        }

                        if (characterVal != null) {
                            // store this value so that the prompt does not need
                            // to
                            // be done for subsequent invocations
                            _characterValues.put(ch, characterVal);
                            context.setValueForCharacter(ch, characterVal);
                            charsNoValues.remove(ch);
                        }
                    }
                }
            }

            context.specimenUpdateComplete();
            return true;
        }

        void addCharacterValue(au.org.ala.delta.model.Character ch, CharacterValue val) {
            _characterValues.put(ch, val);
        }

        private boolean checkCharacterUsable(Character ch, IntkeyContext context) {
            CharacterFormatter formatter = new CharacterFormatter(false, false, true, true);

            // is character fixed?

            // is character already used?
            if (!_suppressAlreadySetWarning && !_change) {
                if (context.getSpecimen().hasValueFor(ch)) {
                    String msg = String.format(UIUtils.getResourceString("UseDirective.CharacterAlreadyUsed"), formatter.formatCharacterDescription(ch));
                    int choice = JOptionPane.showConfirmDialog(UIUtils.getMainFrame(), msg, "Information", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }

            // is character unavailable?
            if (context.getSpecimen().isCharacterInapplicable(ch)) {
                String msg = String.format(UIUtils.getResourceString("UseDirective.CharacterUnavailable"), formatter.formatCharacterDescription(ch));
                JOptionPane.showMessageDialog(UIUtils.getMainFrame(), msg, "Information", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // is character excluded?

            return true;
        }

        private void processControllingCharacters(Character ch, IntkeyContext context) {
            List<CharacterDependency> allControllingChars = getFullControllingCharacterDependenciesList(ch, context.getDataset());

            // Used a linked hashmap as need to maintain the ordering returned
            // by getFullControllingCharacterDependenciesList()
            Map<MultiStateCharacter, Set<Integer>> controllingCharInapplicableStates = new LinkedHashMap<MultiStateCharacter, Set<Integer>>();

            // Look through all the CharacterDependency objects for the
            // character and for each controlling character, collect the list of
            // all states for the controlling character that will make the
            // dependent character inapplicable
            for (CharacterDependency cd : allControllingChars) {
                MultiStateCharacter cc = (MultiStateCharacter) context.getDataset().getCharacter(cd.getControllingCharacterId());

                if (context.getSpecimen().hasValueFor(cc)) {
                    continue;
                }

                // states for the controlling character that will make dependent
                // characters inapplicable
                Set<Integer> inapplicableStates = cd.getStates();

                if (controllingCharInapplicableStates.containsKey(cc)) {
                    controllingCharInapplicableStates.get(cc).addAll(inapplicableStates);
                } else {
                    controllingCharInapplicableStates.put(cc, new HashSet<Integer>(inapplicableStates));
                }
            }

            // For each controlling character, set its value or prompt the user
            // for its value as appropriate
            for (MultiStateCharacter cc : controllingCharInapplicableStates.keySet()) {
                Set<Integer> inapplicableStates = controllingCharInapplicableStates.get(cc);

                // states for the controlling character that will make dependent
                // characters applicable. At least one of these states needs to
                // be
                // set on the controlling character.
                Set<Integer> applicableStates = new HashSet<Integer>();

                for (int i = 1; i < cc.getStates().length + 1; i++) {
                    if (!inapplicableStates.contains(i)) {
                        applicableStates.add(i);
                    }
                }

                if (applicableStates.size() == cc.getStates().length) {
                    throw new RuntimeException(String.format("There are no states for character %s that will make character %s applicable", cc.getCharacterId(), ch.getCharacterId()));
                }

                // If not processing an input file, prompt the user to set the
                // value of the controlling character if it has been supplied as
                // an argument to the
                // NON AUTOMATIC CONTROLLING CHARACTERS confor directive, if the
                // dependent character has been supplied as an argument
                // to the USE CONTROLLING CHARACTERS FIRST confor directive, or
                // if there are multiple states that the controlling character
                // can be set to for which the dependent character will be
                // inapplicable.
                if (!context.isProcessingInputFile() && (cc.getNonAutoCc() || ch.getUseCc() || !cc.getNonAutoCc() && !cc.getUseCc() && applicableStates.size() > 1)) {
                    List<Integer> userSetStates = promptForMultiStateValue(UIUtils.getMainFrame(), (MultiStateCharacter) cc);
                    MultiStateValue val = new MultiStateValue((MultiStateCharacter) cc, new ArrayList<Integer>(userSetStates));
                    context.setValueForCharacter(cc, val);
                } else {
                    // let intkey automatically use the character
                    MultiStateValue val = new MultiStateValue((MultiStateCharacter) cc, new ArrayList<Integer>(applicableStates));
                    context.setValueForCharacter(cc, val);
                }

                // output USEd controlling characters directly to the log window
                // set the "used type" - by user or auto for the controlling
                // character

            }
        }

        // For the given character, recursively build a list of
        // CharacterDependency objects describing
        // all characters that control it, directly and indirectly.
        private List<CharacterDependency> getFullControllingCharacterDependenciesList(Character ch, IntkeyDataset ds) {
            List<CharacterDependency> retList = new ArrayList<CharacterDependency>();

            List<CharacterDependency> directControllingChars = ch.getControllingCharacters();
            if (directControllingChars != null) {
                for (CharacterDependency cd : directControllingChars) {
                    Character controllingChar = ds.getCharacter(cd.getControllingCharacterId());
                    retList.add(cd);

                    // Add all the indirect or "ancestor" character
                    // dependencies.
                    List<CharacterDependency> ancestorCharacterDependencies = getFullControllingCharacterDependenciesList(controllingChar, ds);
                    for (CharacterDependency ancestorCd : ancestorCharacterDependencies) {
                        // If an "ancestor" dependency is already in the list, remove it and reinsert it at 
                        // the front of the list. Need to ensure that values for the furthermost ancestors
                        // are set first, otherwise the Specimen will throw IllegalStateExceptions...
                        if (retList.contains(ancestorCd)) {
                            retList.remove(ancestorCd);
                        }
                        
                        retList.add(0, ancestorCd);
                    }
                }
            }
            
            return retList;
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
            UIUtils.showDialog(dlg);
            return dlg.getInputData();
        }

        private IntRange promptForIntegerValue(Frame frame, IntegerCharacter ch) {
            IntegerInputDialog dlg = new IntegerInputDialog(frame, ch);
            UIUtils.showDialog(dlg);
            return dlg.getInputData();
        }

        private FloatRange promptForRealValue(Frame frame, RealCharacter ch) {
            RealInputDialog dlg = new RealInputDialog(frame, ch);
            UIUtils.showDialog(dlg);
            return dlg.getInputData();
        }

        private List<String> promptForTextValue(Frame frame, TextCharacter ch) {
            TextInputDialog dlg = new TextInputDialog(frame, ch);
            UIUtils.showDialog(dlg);
            return dlg.getInputData();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (_change) {
                builder.append(StringUtils.join(new ChangeDirective().getControlWords(), "").toUpperCase());
            } else {
                builder.append(StringUtils.join(_controlWords, " ").toUpperCase());
            }
            builder.append(" ");
            for (Character ch : _characterValues.keySet()) {
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
