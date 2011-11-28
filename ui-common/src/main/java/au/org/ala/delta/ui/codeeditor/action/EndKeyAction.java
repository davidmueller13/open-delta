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
package au.org.ala.delta.ui.codeeditor.action;

import java.awt.event.ActionEvent;

import au.org.ala.delta.ui.codeeditor.CodeTextArea;

public class EndKeyAction extends KeyAction {

  /**
     *
     */
    private static final long serialVersionUID = 1L;

/**
   * Constructs a EndKeyAction instance with specific arguments.
   * @param textArea The text area.
   */
  public EndKeyAction(CodeTextArea textArea) {
    super(textArea);
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e) {

    if (isCtrlPressed(e)) {
      if (isShiftPressed(e)) {
        codeEditor.select(codeEditor.getMarkPosition(), codeEditor.getDocumentLength());
      } else {
        codeEditor.setCaretPosition(codeEditor.getDocumentLength());
      }
    } else {
      int caret = codeEditor.getCaretPosition();

      int lastOfLine = codeEditor.getLineEndOffset(codeEditor.getCaretLine()) - 1;

      int lastDocument = codeEditor.getDocumentLength();

      if (caret == lastDocument) {
        codeEditor.getToolkit().beep();
        return;
      } else {
        caret = lastOfLine;
      }

      if (isShiftPressed(e)) {
        codeEditor.select(codeEditor.getMarkPosition(), caret);
      } else {
        codeEditor.setCaretPosition(caret);
      }
    }
  }

}
