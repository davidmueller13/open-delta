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
package au.org.ala.delta.key.directives;

import java.io.File;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.directives.AbstractTextDirective;
import au.org.ala.delta.directives.args.DirectiveArguments;
import au.org.ala.delta.directives.validation.DirectiveError;
import au.org.ala.delta.key.KeyContext;
import au.org.ala.delta.util.Utils;

public abstract class AbstractKeyOutputFileDirective extends AbstractTextDirective {

    protected AbstractKeyOutputFileDirective(String... controlWords) {
        super(controlWords);
    }

    @Override
    public void process(DeltaContext context, DirectiveArguments directiveArguments) throws Exception {
        KeyContext keyContext = (KeyContext) context;

        String fileName = directiveArguments.getFirstArgumentText().trim();
        File datasetDirectory = ((KeyContext) context).getDataDirectory();
        File file = Utils.createFileFromPath(fileName, datasetDirectory);

        try {
            file.createNewFile();
            parseFile(file, keyContext);
        } catch (Exception ex) {
            throw DirectiveError.asException(DirectiveError.Error.FILE_INACCESSABLE, fileName.length() + 1);
        }

    }

    abstract void parseFile(File file, KeyContext context) throws Exception;

}
