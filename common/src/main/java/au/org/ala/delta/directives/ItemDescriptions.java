/*******************************************************************************
 * Copyright (C) 2011 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ******************************************************************************/
package au.org.ala.delta.directives;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.Logger;
import au.org.ala.delta.directives.args.DirectiveArgType;
import au.org.ala.delta.directives.args.DirectiveArguments;
import au.org.ala.delta.directives.validation.DirectiveError;
import au.org.ala.delta.directives.validation.DirectiveException;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MutableDeltaDataSet;

import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

/**
 * Parses and processes the ITEM DESCRIPTIONS directive.
 */
public class ItemDescriptions extends AbstractTextDirective {

    public static final String[] CONTROL_WORDS = {"item", "descriptions"};

    private boolean _normalizeBeforeParsing = false;
    private boolean _allowDuplicates;

    public ItemDescriptions() {
        super(CONTROL_WORDS);
        registerPrerequiste(NumberOfCharacters.class);
        registerPrerequiste(MaximumNumberOfItems.class);
        registerPrerequiste(MaximumNumberOfStates.class);
        _normalizeBeforeParsing = false;
    }

    public ItemDescriptions(boolean normalizeBeforeParsing) {
        super(CONTROL_WORDS);
        registerPrerequiste(NumberOfCharacters.class);
        registerPrerequiste(MaximumNumberOfItems.class);
        registerPrerequiste(MaximumNumberOfStates.class);
        _normalizeBeforeParsing = normalizeBeforeParsing;

    }


    @Override
    public int getArgType() {
        return DirectiveArgType.DIRARG_INTERNAL;
    }

    @Override
    public void process(DeltaContext context, DirectiveArguments data) throws Exception {
        _allowDuplicates = context.getAcceptDuplicateValues();
        StringReader reader = new StringReader(data.getFirstArgumentText());
        ItemsParser parser = new ItemsParser(context, reader, _normalizeBeforeParsing);
        parser.parse();
    }

    @Override
    public int getOrder() {
        return 5;
    }

    protected Item createItem(DeltaContext context, int itemNumber, String description) {
        Item item = context.getDataSet().addItem(itemNumber);
        item.setDescription(description);
        return item;
    }

    protected Item createVariantItem(DeltaContext context, int masterItemNumber, int itemNumber, String description) {
        Item item = context.getDataSet().addVariantItem(masterItemNumber, itemNumber);
        item.setDescription(description);
        return item;
    }

    protected void checkItemCount(DeltaContext context, int itemIndex, int position) throws DirectiveException {
        if (itemIndex > context.getMaximumNumberOfItems()) {
            throw new DirectiveError(DirectiveError.Error.TOO_MANY_ITEMS, position).asException();
        }
    }

    protected void addAttribute(DeltaContext context, Item item, int charIdx, int startOffset, String value) throws DirectiveException {
        MutableDeltaDataSet dataSet = context.getDataSet();

        Attribute attribute = dataSet.getAttribute(item.getItemNumber(), charIdx);
        try {

            attribute.setValueFromString(value);
        } catch (DirectiveException e) {
            e.getError().setPosition(e.getErrorOffset() + startOffset + 1);
            context.addError(e.getError());
        }
    }


    class ItemsParser extends AbstractStreamParser {

        /**
         * The last Item parsed that is not a variant Item
         */
        private int _lastMaster;

        private boolean _normalizeBeforeParsing = false;


        public ItemsParser(DeltaContext context, Reader reader, boolean normalizeBeforeParsing) {
            super(context, reader);
            _normalizeBeforeParsing = normalizeBeforeParsing;

        }

        @Override
        public void parse() throws ParseException {

            int itemIndex = 1;
            while (skipTo('#') && _currentInt >= 0) {

                checkItemCount(getContext(), itemIndex, _position);

                parseItem(itemIndex);
                itemIndex++;
            }
        }


        private void parseItem(int itemIndex) throws ParseException {

            Set<Integer> encounteredChars = new HashSet<Integer>();
            MutableDeltaDataSet dataSet = getContext().getDataSet();
            assert _currentChar == '#';
            readNext();


            boolean variant = isVariant(itemIndex);
            String itemName = readToNextEndSlashSpace();
            Logger.debug("Parsing Item %s", itemName);

            Item item = createItem(itemIndex, variant, itemName);
            item.setDescription(cleanWhiteSpace(itemName.trim()));
            skipWhitespace();
            while (_currentChar != '#' && _currentInt >= 0) {
                parseAttribute(encounteredChars, dataSet, item);
            }
        }

        private void parseAttribute(Set<Integer> encounteredChars, MutableDeltaDataSet dataSet, Item item) throws ParseException {
            int charIdx = readInteger();

            int startOffset = _position;


            String strValue = null;
            String comment = null;

            if (_currentChar == '<') {
                comment = readComment();
            }
            if (_currentChar == ',') {
                readNext();
                strValue = readStateValue();
            } else if (isWhiteSpace(_currentChar)) {
                if (comment == null) {
                    strValue = "U";
                } else {
                    strValue = "";
                }
            }

            Character ch = getContext().getCharacter(charIdx);
            if (encounteredChars.contains(ch.getCharacterId())) {
                if (!_allowDuplicates) {
                    throw DirectiveError.asException(DirectiveError.Error.CHARACTER_ALREADY_SPECIFIED, startOffset, ch.getCharacterId());
                } else {

                    getContext().addError(new DirectiveError(DirectiveError.Warning.CHARACTER_ALREADY_SPECIFIED, startOffset, ch.getCharacterId()));
                }
            } else {
                encounteredChars.add(ch.getCharacterId());

            }
            StringBuilder value = new StringBuilder();
            if (comment != null) {
                value.append(comment);
            }
            if (strValue != null) {
                value.append(strValue);
            }

            String theValue = value.toString();
            if (_normalizeBeforeParsing) {
                theValue = cleanWhiteSpace(theValue.trim());
            }
            addAttribute(getContext(), item, charIdx, startOffset, theValue);

            skipWhitespace();
        }



        private boolean isVariant(int itemIndex) throws ParseException {
            boolean variant = (_currentChar == '+');
            if (variant) {
                if (itemIndex == 1) {
                    _context.addError(new DirectiveError(DirectiveError.Warning.FIRST_ITEM_CANNOT_BE_VARIANT, _position, (Object[]) null));
                    variant = false;
                }
                readNext();
            }
            return variant;
        }

        private Item createItem(int itemIndex, boolean variant, String description) throws ParseException {
            Item item;
            if (variant) {
                item = ItemDescriptions.this.createVariantItem(getContext(), _lastMaster, itemIndex, description);
            }
            else {
                item = ItemDescriptions.this.createItem(getContext(), itemIndex, description);
                _lastMaster = itemIndex;
            }

            return item;
        }

        protected DeltaContext getContext() {
            return (DeltaContext) _context;
        }

        private String readStateValue() throws ParseException {
            String value = readToNextSpaceComments();
            return value;
        }
    }
}
