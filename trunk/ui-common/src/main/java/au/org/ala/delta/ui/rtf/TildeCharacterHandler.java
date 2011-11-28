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
package au.org.ala.delta.ui.rtf;

import java.awt.event.KeyEvent;

public class TildeCharacterHandler extends SpecialCharHandler {
	
	@SuppressWarnings("unchecked")
	public TildeCharacterHandler() {
		super(KeyEvent.VK_BACK_QUOTE, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK, SpecialCharacterMode.Tilde, 0x7e,
		    pair('a', 0xe3),
		    pair('A', 0xc3),
		    pair('o', 0xf5),
		    pair('O', 0xd5),
		    pair('n', 0xf1),
		    pair('N', 0xd1)		
		);
	}

}
