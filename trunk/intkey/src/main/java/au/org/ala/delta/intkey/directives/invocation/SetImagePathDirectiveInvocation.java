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

import java.util.ArrayList;
import java.util.List;

import au.org.ala.delta.intkey.model.IntkeyContext;

public class SetImagePathDirectiveInvocation extends IntkeyDirectiveInvocation {

    private List<String> _imagePaths;

    public void setImagePaths(String imagePathsAsString) {
        _imagePaths = new ArrayList<String>();
        for (String path : imagePathsAsString.split(";")) {
            _imagePaths.add(path);
        }
    }

    @Override
    public boolean execute(IntkeyContext context) {
        context.setImagePaths(_imagePaths);
        return true;
    }

}
