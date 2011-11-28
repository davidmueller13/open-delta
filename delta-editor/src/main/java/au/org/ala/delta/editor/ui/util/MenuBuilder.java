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
package au.org.ala.delta.editor.ui.util;

import java.util.List;

import javax.swing.ActionMap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Helper class that will assemble a menu from a list of Strings.
 */
public class MenuBuilder {

	/**
	 * Builds and attaches menu items to the supplied menu.  The menu items will be built based on
	 * the strings in the supplied actionNames.  
	 * If the name is a "-" a menu separator will be added.
	 * Otherwise for each string in the actionNames list, a new menu item will be created and 
	 * configured with an action from the action map identified by the name.
	 * If the name starts with a "*", a checkbox menu item will be created
	 * instead.
	 * @param menu the menu to build.
	 * @param actionNames the names of actions, in the order they should appear on the menu.
	 * @param actionMap contains the actions to be attached to the menu.  There must be an action 
	 * for each name in the actionNames parameter.
	 * @return an array containing the menu items that have been added.
	 * If the actionNames parameter contained separators, these elements will
	 * be null in the returned array.
	 */
	public static JMenuItem[] buildMenu(JMenu menu, String[] actionNames, ActionMap actionMap) {
		JMenuItem[] menus = new JMenuItem[actionNames.length];
		int index = 0;
		for (String action : actionNames) {
			menus[index++] = addMenuItem(menu, action, actionMap);
		}
		return menus;
	}
	
	public static JMenuItem[] buildMenu(JMenu menu, List<String> actionNames, ActionMap actionMap) {
		return buildMenu(menu, actionNames.toArray(new String[actionNames.size()]), actionMap);
	}
	
	public static JMenuItem[] buildMenu(JPopupMenu menu, String[] actionNames, ActionMap actionMap) {
		JMenuItem[] items = new JMenuItem[actionNames.length];
		int i = 0;
		for (String action : actionNames) {
			items[i++] = addMenuItem(menu, action, actionMap);
		}
		return items;
	}
	
	public static JMenuItem[] buildMenu(JPopupMenu menu, List<String> actionNames, ActionMap actionMap) {
		return buildMenu(menu, actionNames.toArray(new String[actionNames.size()]), actionMap);
	}
	/**
	 * Creates and adds a menu item to the supplied menu with an action identified by the supplied actionName.
	 * 
	 * @param menu
	 *            the menu to add the new item to.
	 * @param actionName
	 *            the name of the action, or "-" to add a separator.
	 */
	private static JMenuItem addMenuItem(JMenu menu, String actionName, ActionMap actionMap) {
		JMenuItem menuItem = null;
		if ("-".equals(actionName)) {
			menu.addSeparator();
		} else {
			
			if (actionName.startsWith("*")) {
				menuItem = new JCheckBoxMenuItem();
				actionName = actionName.substring(1);
			}
			else {
				menuItem = new JMenuItem();
			}
			
			menuItem.setAction(actionMap.get(actionName));
			menu.add(menuItem);
		}
		return menuItem;
	}
	
	private static JMenuItem addMenuItem(JPopupMenu menu, String actionName, ActionMap actionMap) {
		JMenuItem menuItem = null;
		
		if ("-".equals(actionName)) {
			menu.addSeparator();
		} else {
			if (actionName.startsWith("*")) {
				menuItem = new JCheckBoxMenuItem();
				actionName = actionName.substring(1);
			}
			else {
				menuItem = new JMenuItem();
			}
			
			menuItem.setAction(actionMap.get(actionName));
			menu.add(menuItem);
		}
		return menuItem;
	}

}
