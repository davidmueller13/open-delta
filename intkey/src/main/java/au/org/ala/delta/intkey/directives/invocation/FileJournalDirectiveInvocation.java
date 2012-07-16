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
package au.org.ala.delta.intkey.directives.invocation;

import java.io.File;
import java.io.IOException;

import au.org.ala.delta.Logger;
import au.org.ala.delta.intkey.model.IntkeyContext;

public class FileJournalDirectiveInvocation extends BasicIntkeyDirectiveInvocation {
    private File _file;

    public void setFile(File file) {
        this._file = file;
    }

    @Override
    public boolean execute(IntkeyContext context) {
        try {
            context.setJournalFile(_file);
            return true;
        } catch (IOException ex) {
            Logger.error(ex);
            context.getUI().displayErrorMessage(String.format("Error opening journal file %s", _file.getAbsolutePath()));
            return false;
        }
    }
}
