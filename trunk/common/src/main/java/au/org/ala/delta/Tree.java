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
package au.org.ala.delta;

import java.util.ArrayList;
import java.util.List;

import au.org.ala.delta.directives.AbstractDirective;
import au.org.ala.delta.directives.DirectiveSearchResult;
import au.org.ala.delta.directives.DirectiveSearchResult.ResultType;

public class Tree {

    /**
     * If true (the default), matching is done against the first three
     * characters of a control word. Supplied text of less than three characters
     * will not match, and any characters past the first three are ignored for
     * matching purposes.
     */
    private boolean _matchFirstThreeCharacters;

    private TreeNodeList _toplevel;

    public Tree() {
        _toplevel = new TreeNodeList(null, "ROOT");
        _matchFirstThreeCharacters = true;
    }

    @SuppressWarnings("rawtypes")
    public void addDirective(AbstractDirective directive) {
        TreeNodeList p = _toplevel;

        String[] words = directive.getControlWords();
        String key = words[0];
        for (int i = 0; i < words.length - 1; ++i) {

            key = makeKey(words[i]);

            if (p.getChildren().containsKey(key)) {
                TreeNode n = p.getChildren().get(key);
                if (n instanceof DirectiveTreeNode && i == words.length - 1) {
                    throw new RuntimeException("Directive tree already contains directive: " + directive.toString());
                } else {
                    p = (TreeNodeList) n;
                }
            } else {
                TreeNodeList newChild = new TreeNodeList(p, key);
                p.addChild(newChild);
                p = newChild;
            }
        }
        p.addChild(new DirectiveTreeNode(p, makeKey(words[words.length - 1]), directive));
    }

    public DirectiveSearchResult findDirective(List<String> controlWords) {
        TreeNodeList p = _toplevel;
        for (int i = 0; i < controlWords.size(); i++) {

            String key = makeKey(controlWords.get(i));
            if (p.getChildren().containsKey(key)) {
                TreeNode n = p.getChildren().get(key);
                if (i == controlWords.size() - 1 && n instanceof DirectiveTreeNode) {
                    return new DirectiveSearchResult(ResultType.Found, ((DirectiveTreeNode) n).getDirective());
                } else {
                    p = (TreeNodeList) n;
                }
            } else {
                if (!_matchFirstThreeCharacters) {
                    List<String> matchedKeys = new ArrayList<String>();
                    for (String nodeKey : p.getChildren().keySet()) {
                        if (nodeKey.startsWith(key)) {
                            matchedKeys.add(nodeKey);
                        }
                    }

                    if (matchedKeys.size() == 1) {
                        TreeNode n = p.getChildren().get(matchedKeys.get(0));
                        return new DirectiveSearchResult(ResultType.Found, ((DirectiveTreeNode) n).getDirective());
                    } else if (matchedKeys.size() > 1) {
                        return new DirectiveSearchResult(ResultType.MoreSpecificityRequired, null);
                    }
                }

                return new DirectiveSearchResult(ResultType.NotFound, null);
            }
        }
        return new DirectiveSearchResult(ResultType.MoreSpecificityRequired, null);
    }

    private String makeKey(String str) {
        if (str == null) {
            return null;
        }

        str = str.trim().toLowerCase();

        if (_matchFirstThreeCharacters) {
            if (str.length() > 3) {
                str = str.substring(0, 3);
            }
        }
        return str;
    }

    public void dump() {
        _toplevel.dump(0);
    }

    public void visit(TreeVisitor visitor) {
        _toplevel.visit(visitor);
    }

    public static interface TreeVisitor {
        void visit(TreeNode node);
    }

    public void setMatchFirstThreeCharacters(boolean matchFirstThreeCharacters) {
        this._matchFirstThreeCharacters = matchFirstThreeCharacters;
    }

}
