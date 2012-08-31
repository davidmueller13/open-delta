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
package au.org.ala.delta.intkey.directives;

import java.text.ParseException;

import org.apache.commons.lang.StringUtils;

import au.org.ala.delta.directives.AbstractDirective;
import au.org.ala.delta.directives.args.DirectiveArguments;
import au.org.ala.delta.intkey.directives.invocation.IntkeyDirectiveInvocation;
import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.intkey.ui.UIUtils;

public abstract class IntkeyDirective extends AbstractDirective<IntkeyContext> {

    protected DirectiveArguments _args;
    protected boolean _errorIfNoDatasetLoaded;

    public IntkeyDirective(boolean errorIfNoDatasetLoaded, String... controlWords) {
        super(controlWords);
        _errorIfNoDatasetLoaded = errorIfNoDatasetLoaded;
    }

    @Override
    public final int getArgType() {
        // Not relevant for Intkey. This is only used for import/export of
        // directives
        // in the delta editor.
        return 0;
    }

    @Override
    public final DirectiveArguments getDirectiveArgs() {
        return _args;
    }

    @Override
    public final void parse(IntkeyContext context, String data) throws ParseException {
        _args = DirectiveArguments.textArgument(data);
    }

    @Override
    public final void process(IntkeyContext context, DirectiveArguments directiveArguments) throws Exception {
        parseAndProcess(context, directiveArguments.getFirstArgumentText());
    }

    @Override
    public final void parseAndProcess(IntkeyContext context, String data) throws Exception {

        if (context.getDataset() == null && _errorIfNoDatasetLoaded) {

            // If we get here we are processing a directive that requires a
            // dataset to be loaded.
            // If the data set is not loaded it is because either, or both, of
            // the FILE directives have not been encountered

            // we'll inject them using the defaults, depending on whether they
            // have been processed or not
            if (context.getTaxaFile() == null) {
                try {
                    IntkeyDirectiveInvocation invoc = new FileTaxaDirective().doProcess(context, "iitems");
                    context.executeDirective(invoc);
                } catch (IntkeyDirectiveParseException ex) {
                    // do nothing - null dataset handled below
                }
            }

            if (context.getCharactersFile() == null) {
                try {
                    IntkeyDirectiveInvocation invoc = new FileCharactersDirective().doProcess(context, "ichars");
                    context.executeDirective(invoc);
                } catch (IntkeyDirectiveParseException ex) {
                    // do nothing - null dataset handled below
                }
            }

            // If we get here, and the dataset is still null, there is an actual
            // issue with either the files being missing, or not loadable
            if (context.getDataset() == null) {
                String message = UIUtils.getResourceString("DirectiveCallNoDatasetLoaded.error", getControlWordsAsString());
                throw new RuntimeException(message);
            }
        }

        if (data != null) {
            data = data.trim();
        }

        IntkeyDirectiveInvocation invoc = doProcess(context, data);

        if (invoc != null) {
            context.executeDirective(invoc);
        }

    }

    protected abstract IntkeyDirectiveInvocation doProcess(IntkeyContext context, String data) throws Exception;

    public String getControlWordsAsString() {
        return StringUtils.join(getControlWords(), " ").toUpperCase();
    }

}
