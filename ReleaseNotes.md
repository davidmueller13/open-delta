# Release 1.02 #
This is the 1.02 release of the Open DELTA suite.

#### New Features ####

The Windows installer is now bundled with the Java 1.7.0\_u55 JRE.

#### Bug Fixes ####
  * [ISSUE 236](https://code.google.com/p/open-delta/issues/detail?id=236) - Hardcoding implicit state fails
  * [ISSUE 250](https://code.google.com/p/open-delta/issues/detail?id=250) - Intkey unable to open some files form crustacea.net
  * [ISSUE 251](https://code.google.com/p/open-delta/issues/detail?id=251) - Inconsistent taxon highlighting after character deletion.


# Release 1.01 #

This is the 1.01 release of the Open DELTA suite.

## Known Problems ##
  * [ISSUE 140](https://code.google.com/p/open-delta/issues/detail?id=140) - PClass not implemented.
  * [ISSUE 141](https://code.google.com/p/open-delta/issues/detail?id=141) - NSim not implemented.
  * [ISSUE 205](https://code.google.com/p/open-delta/issues/detail?id=205) - Mac OS installer file associations don't work correctly.

## DELTA Editor Release Notes ##

#### New Features ####
None.

#### Bug Fixes ####
  * [ISSUE 212](https://code.google.com/p/open-delta/issues/detail?id=212) - Export directives should warn if the dataset has not been saved
  * [ISSUE 213](https://code.google.com/p/open-delta/issues/detail?id=213) - Open saved DELTA (.dlt) file
  * [ISSUE 220](https://code.google.com/p/open-delta/issues/detail?id=220) - scrolling corrupted in grid view if header row is resized
  * [ISSUE 243](https://code.google.com/p/open-delta/issues/detail?id=243) - Deleting character states associated with a controlling character causes editing problems


#### Known Problems ####
  * [ISSUE 126](https://code.google.com/p/open-delta/issues/detail?id=126) - The import dialog exclusion filter doesn't seem to work correctly on Windows XP.
  * [ISSUE 127](https://code.google.com/p/open-delta/issues/detail?id=127) - Opening an action set in the editor marks the data set as needing to be saved.
  * [ISSUE 134](https://code.google.com/p/open-delta/issues/detail?id=134) - Pressing ESC in the attribute editor commits changes instead of cancelling them.
  * [ISSUE 214](https://code.google.com/p/open-delta/issues/detail?id=214) - Missing keyboard shortcuts from DELTA editors
  * [ISSUE 222](https://code.google.com/p/open-delta/issues/detail?id=222) - Coding an overlapping range in a directive can result in strange output
  * [ISSUE 223](https://code.google.com/p/open-delta/issues/detail?id=223) - Errors in font size conversion in the DELTA Editor
  * [ISSUE 228](https://code.google.com/p/open-delta/issues/detail?id=228) - Default "Centre in box" preference not respected by the image editor
  * [ISSUE 232](https://code.google.com/p/open-delta/issues/detail?id=232) - Increase the size of the output window in Action Sets and make abnormal termination more prominent.
  * [ISSUE 233](https://code.google.com/p/open-delta/issues/detail?id=233) - no scroll bar for states in grid view
  * [ISSUE 234](https://code.google.com/p/open-delta/issues/detail?id=234) - unable to reopen dlt file
  * [ISSUE 236](https://code.google.com/p/open-delta/issues/detail?id=236) - Hardcoding implicit state fails in DELTA editor
  * [ISSUE 237](https://code.google.com/p/open-delta/issues/detail?id=237) - Character descriptions not matching in nexus files (+ two other issues)
  * [ISSUE 238](https://code.google.com/p/open-delta/issues/detail?id=238) - Variant attributes not importing correctly
  * [ISSUE 239](https://code.google.com/p/open-delta/issues/detail?id=239) - Impossible to rectify some errors in delta editor within open delta
  * [ISSUE 244](https://code.google.com/p/open-delta/issues/detail?id=244) - Various DELTA Editor issues reported by Mike Dallwitz via DELTA-L

## Intkey Release Notes ##

#### New Features ####
None.

#### Bug Fixes ####
  * [ISSUE 215](https://code.google.com/p/open-delta/issues/detail?id=215) - Error "Index 13, Size 13" when running input file
  * [ISSUE 216](https://code.google.com/p/open-delta/issues/detail?id=216) - Text from intkey OUTPUT directives appears in output file in incorrect order
  * [ISSUE 224](https://code.google.com/p/open-delta/issues/detail?id=224) - Newlines and semicolons in Intkey dataset titles cause problems with preferences
  * [ISSUE 230](https://code.google.com/p/open-delta/issues/detail?id=230) - Some intkey datasets fail to load with a nullpointerexception.


#### Known Problems ####
  * [ISSUE 156](https://code.google.com/p/open-delta/issues/detail?id=156) - Attempting to view character images when there are none causes issues in Intkey
  * [ISSUE 163](https://code.google.com/p/open-delta/issues/detail?id=163) - Double click issues in Intkey
  * [ISSUE 167](https://code.google.com/p/open-delta/issues/detail?id=167) - Font size in RTF reports
  * [ISSUE 169](https://code.google.com/p/open-delta/issues/detail?id=169) - Hanging styles should be used in windows displaying hierarchical information
  * [ISSUE 170](https://code.google.com/p/open-delta/issues/detail?id=170) - Integer values are sometimes displayed with a decimal point
  * [ISSUE 174](https://code.google.com/p/open-delta/issues/detail?id=174) - When there is more than 1 level in a menu, the pop-up help is obscured by the next level
  * [ISSUE 178](https://code.google.com/p/open-delta/issues/detail?id=178) - Cannot select states, characters, taxa using the keyboard
  * [ISSUE 179](https://code.google.com/p/open-delta/issues/detail?id=179) - Cannot select from keywords and lists simultaneously
  * [ISSUE 183](https://code.google.com/p/open-delta/issues/detail?id=183) - The character-notes window should be positioned away from the underlying dialog when possible
  * [ISSUE 187](https://code.google.com/p/open-delta/issues/detail?id=187) - When any state-selection window is canceled, or a state-selection text window is closed, the focus should move to the next character
  * [ISSUE 192](https://code.google.com/p/open-delta/issues/detail?id=192) - Should be able to selection multiple items with single clicks in state selection windows.
  * [ISSUE 200](https://code.google.com/p/open-delta/issues/detail?id=200) - Popup windows have no options for maximizing or minimizing
  * [ISSUE 204](https://code.google.com/p/open-delta/issues/detail?id=204) - RTF renderer does not centre text in some circumstances
  * [ISSUE 217](https://code.google.com/p/open-delta/issues/detail?id=217) - Cannot append output to multiple files in Intkey

## CONFOR Release Notes ##

#### Bug Fixes ####
  * [ISSUE 218](https://code.google.com/p/open-delta/issues/detail?id=218) - Incorrect formatting in tonatr output
  * [ISSUE 225](https://code.google.com/p/open-delta/issues/detail?id=225) - tokey not excluding characters
  * [ISSUE 226](https://code.google.com/p/open-delta/issues/detail?id=226) - tokey writing incorrect values for item abundances
  * [ISSUE 242](https://code.google.com/p/open-delta/issues/detail?id=242) - ToInt in some cases writes character values to the wrong items

#### Known Issues ####
  * [ISSUE 245](https://code.google.com/p/open-delta/issues/detail?id=245) - Various CONFOR issues reported by Mike Dallwitz via DELTA-L
  * [ISSUE 136](https://code.google.com/p/open-delta/issues/detail?id=136) - page break directives not implemented (NEW LISTING PAGE, NEW PRINT PAGE, PAGE LENGTH)
  * [ISSUE 137](https://code.google.com/p/open-delta/issues/detail?id=137) - DATA COMPRESSION not implemented
  * [ISSUE 138](https://code.google.com/p/open-delta/issues/detail?id=138) - CHINESE FORMAT not implemented
  * [ISSUE 139](https://code.google.com/p/open-delta/issues/detail?id=139) - INSERT UNCODED ATTRIBUTES not implemented
  * USE LANGUAGE not implemented
  * ALTERNATE LANGUAGES not implemented
  * [ISSUE 219](https://code.google.com/p/open-delta/issues/detail?id=219) - EXCLUDE CHARACTERS directive corrupts
  * [ISSUE 221](https://code.google.com/p/open-delta/issues/detail?id=221) - Omitted prefix due to LINK CHARACTERS not the same as CONFOR in some circumstances
  * [ISSUE 235](https://code.google.com/p/open-delta/issues/detail?id=235) - confor tonex not producing correct Nexus file
  * [ISSUE 240](https://code.google.com/p/open-delta/issues/detail?id=240) - inability to edit directive files
  * [ISSUE 241](https://code.google.com/p/open-delta/issues/detail?id=241) - confor tonatsr generates individual .rtf files that will not display in Open-Delta Intkey


## KEY Release Notes ##

#### Bug Fixes ####
  * [ISSUE 227](https://code.google.com/p/open-delta/issues/detail?id=227) - KEY fails to open dataset
  * [ISSUE 231](https://code.google.com/p/open-delta/issues/detail?id=231) - Formatting errors with taxon names in tabulated KEY output
  * [ISSUE 229](https://code.google.com/p/open-delta/issues/detail?id=229) - Open DELTA produces different KEY than original

# Release 1.0 #

This is the 1.0 release of the Open DELTA suite.

## Known Problems ##
  * [ISSUE 140](https://code.google.com/p/open-delta/issues/detail?id=140) - PClass not implemented.
  * [ISSUE 141](https://code.google.com/p/open-delta/issues/detail?id=141) - NSim not implemented.
  * [ISSUE 205](https://code.google.com/p/open-delta/issues/detail?id=205) - Mac OS installer file associations don't work correctly.

## DELTA Editor Release Notes ##

#### New Features ####
  * [ISSUE 212](https://code.google.com/p/open-delta/issues/detail?id=212) - Added a warning if exporting a dataset that has not yet been saved.

#### Bug Fixes ####
None.

#### Known Problems ####
  * [ISSUE 126](https://code.google.com/p/open-delta/issues/detail?id=126) - The import dialog exclusion filter doesn't seem to work correctly on Windows XP.
  * [ISSUE 127](https://code.google.com/p/open-delta/issues/detail?id=127) - Opening an action set in the editor marks the data set as needing to be saved.
  * [ISSUE 134](https://code.google.com/p/open-delta/issues/detail?id=134) - Pressing ESC in the attribute editor commits changes instead of cancelling them.

## Intkey Release Notes ##

#### New Features ####
None.

#### Bug Fixes ####
  * [ISSUE 210](https://code.google.com/p/open-delta/issues/detail?id=210) - User prompted to add dataset URLs to dataset index when the URL has already been added
  * [ISSUE 59](https://code.google.com/p/open-delta/issues/detail?id=59) - Intkey hangs when loading remotely hosted images
  * [ISSUE 33](https://code.google.com/p/open-delta/issues/detail?id=33) - Intkey character layout can be untidy

#### Known Problems ####
  * [ISSUE 156](https://code.google.com/p/open-delta/issues/detail?id=156) - Attempting to view character images when there are none causes issues in Intkey
  * [ISSUE 163](https://code.google.com/p/open-delta/issues/detail?id=163) - Double click issues in Intkey
  * [ISSUE 167](https://code.google.com/p/open-delta/issues/detail?id=167) - Font size in RTF reports
  * [ISSUE 169](https://code.google.com/p/open-delta/issues/detail?id=169) - Hanging styles should be used in windows displaying hierarchical information
  * [ISSUE 170](https://code.google.com/p/open-delta/issues/detail?id=170) - Integer values are sometimes displayed with a decimal point
  * [ISSUE 174](https://code.google.com/p/open-delta/issues/detail?id=174) - When there is more than 1 level in a menu, the pop-up help is obscured by the next level
  * [ISSUE 178](https://code.google.com/p/open-delta/issues/detail?id=178) - Cannot select states, characters, taxa using the keyboard
  * [ISSUE 179](https://code.google.com/p/open-delta/issues/detail?id=179) - Cannot select from keywords and lists simultaneously
  * [ISSUE 183](https://code.google.com/p/open-delta/issues/detail?id=183) - The character-notes window should be positioned away from the underlying dialog when possible
  * [ISSUE 187](https://code.google.com/p/open-delta/issues/detail?id=187) - When any state-selection window is canceled, or a state-selection text window is closed, the focus should move to the next character
  * [ISSUE 192](https://code.google.com/p/open-delta/issues/detail?id=192) - Should be able to selection multiple items with single clicks in state selection windows.
  * [ISSUE 200](https://code.google.com/p/open-delta/issues/detail?id=200) - Popup windows have no options for maximizing or minimizing
  * [ISSUE 204](https://code.google.com/p/open-delta/issues/detail?id=204) - RTF renderer does not centre text in some circumstances

# Release Candidate 4 #

The Open DELTA Release Candidate 4 (RC4) release has been created primarily to address bugs identified via feedback on RC3 received from the community.

## Known Problems ##
  * [ISSUE 140](https://code.google.com/p/open-delta/issues/detail?id=140) - PClass not implemented.
  * [ISSUE 141](https://code.google.com/p/open-delta/issues/detail?id=141) - NSim not implemented.
  * [ISSUE 205](https://code.google.com/p/open-delta/issues/detail?id=205) - Mac OS installer file associations don't work correctly.

## DELTA Editor Release Notes ##

#### New Features ####
None.

#### Bug Fixes ####
  * [ISSUE 211](https://code.google.com/p/open-delta/issues/detail?id=211) - Treat as variant checkbox not updating character state unless other fields are changed at the same time.

#### Known Problems ####
  * [ISSUE 126](https://code.google.com/p/open-delta/issues/detail?id=126) - The import dialog exclusion filter doesn't seem to work correctly on Windows XP.
  * [ISSUE 127](https://code.google.com/p/open-delta/issues/detail?id=127) - Opening an action set in the editor marks the data set as needing to be saved.
  * [ISSUE 134](https://code.google.com/p/open-delta/issues/detail?id=134) - Pressing ESC in the attribute editor commits changes instead of cancelling them.

## Intkey Release Notes ##

#### New Features ####
None.

#### Bug Fixes ####
  * [ISSUE 198](https://code.google.com/p/open-delta/issues/detail?id=198) - The minimum size of taxon image windows is too large
  * [ISSUE 199](https://code.google.com/p/open-delta/issues/detail?id=199) - Intkey image scaling issues
  * [ISSUE 203](https://code.google.com/p/open-delta/issues/detail?id=203) - Images and RTF descriptions viewed from the Web are not cached
  * [ISSUE 197](https://code.google.com/p/open-delta/issues/detail?id=197) - Multiple windows displayed via the ‘Taxon Information’ dialog are not automatically tiled
  * [ISSUE 171](https://code.google.com/p/open-delta/issues/detail?id=171) - The displays generated by the ‘differences’ buttons should show non-overlapping differences in bold.
  * [ISSUE 191](https://code.google.com/p/open-delta/issues/detail?id=191) - Fix double click in image selection windows
  * [ISSUE 193](https://code.google.com/p/open-delta/issues/detail?id=193) - In a state-selection image window hotspots should be invisible
  * [ISSUE 177](https://code.google.com/p/open-delta/issues/detail?id=177) - Double click required for selection of character
  * [ISSUE 190](https://code.google.com/p/open-delta/issues/detail?id=190) - Should be able to double-click to select in text list selection windows
  * [ISSUE 184](https://code.google.com/p/open-delta/issues/detail?id=184) - The state-selection dialog in Normal mode should have a reduced set of buttons
  * [ISSUE 202](https://code.google.com/p/open-delta/issues/detail?id=202) - It should be possible to open a Web dataset by specifying a URL in the ‘Open’ dialog
  * [ISSUE 161](https://code.google.com/p/open-delta/issues/detail?id=161) - Datasets index is not implemented
  * [ISSUE 182](https://code.google.com/p/open-delta/issues/detail?id=182) - Incorrect fonts are used in image overlays
  * [ISSUE 180](https://code.google.com/p/open-delta/issues/detail?id=180) - Options that require data files should be grey in the menus if data files are not present

#### Known Problems ####
  * [ISSUE 59](https://code.google.com/p/open-delta/issues/detail?id=59) - Intkey hangs when loading remotely hosted images
  * [ISSUE 156](https://code.google.com/p/open-delta/issues/detail?id=156) - Attempting to view character images when there are none causes issues in Intkey
  * [ISSUE 163](https://code.google.com/p/open-delta/issues/detail?id=163) - Double click issues in Intkey
  * [ISSUE 167](https://code.google.com/p/open-delta/issues/detail?id=167) - Font size in RTF reports
  * [ISSUE 169](https://code.google.com/p/open-delta/issues/detail?id=169) - Hanging styles should be used in windows displaying hierarchical information
  * [ISSUE 170](https://code.google.com/p/open-delta/issues/detail?id=170) - Integer values are sometimes displayed with a decimal point
  * [ISSUE 174](https://code.google.com/p/open-delta/issues/detail?id=174) - When there is more than 1 level in a menu, the pop-up help is obscured by the next level
  * [ISSUE 178](https://code.google.com/p/open-delta/issues/detail?id=178) - Cannot select states, characters, taxa using the keyboard
  * [ISSUE 179](https://code.google.com/p/open-delta/issues/detail?id=179) - Cannot select from keywords and lists simultaneously
  * [ISSUE 183](https://code.google.com/p/open-delta/issues/detail?id=183) - The character-notes window should be positioned away from the underlying dialog when possible
  * [ISSUE 187](https://code.google.com/p/open-delta/issues/detail?id=187) - When any state-selection window is canceled, or a state-selection text window is closed, the focus should move to the next character
  * [ISSUE 192](https://code.google.com/p/open-delta/issues/detail?id=192) - Should be able to selection multiple items with single clicks in state selection windows.
  * [ISSUE 200](https://code.google.com/p/open-delta/issues/detail?id=200) - Popup windows have no options for maximizing or minimizing
  * [ISSUE 204](https://code.google.com/p/open-delta/issues/detail?id=204) - RTF renderer does not centre text in some circumstances

# Release Candidate 3 #
The Open DELTA Release Candidate 3 (RC3) release has been created primarily to address bugs identified via feedback on RC2 received from the community.

## Known Problems ##
  * [ISSUE 140](https://code.google.com/p/open-delta/issues/detail?id=140) - PClass not implemented.
  * [ISSUE 141](https://code.google.com/p/open-delta/issues/detail?id=141) - NSim not implemented.
  * [ISSUE 205](https://code.google.com/p/open-delta/issues/detail?id=205) - Mac OS installer file associations don't work correctly.

## DELTA Editor Release Notes ##

#### Bug Fixes ####
  * [ISSUE 148](https://code.google.com/p/open-delta/issues/detail?id=148) - CONFOR does not recognise punctuation at end of a directive during data import
  * [ISSUE 149](https://code.google.com/p/open-delta/issues/detail?id=149) - DELTA Editor does not recognise non-contiguous character numbers in CHARACTER NOTES directive
  * [ISSUE 152](https://code.google.com/p/open-delta/issues/detail?id=152) - Editor problems with import and export of ITEMS file apparently due to very large text character content

#### Known Problems ####
  * [ISSUE 126](https://code.google.com/p/open-delta/issues/detail?id=126) - The import dialog exclusion filter doesn't seem to work correctly on Windows XP.
  * [ISSUE 127](https://code.google.com/p/open-delta/issues/detail?id=127) - Opening an action set in the editor marks the data set as needing to be saved.
  * [ISSUE 134](https://code.google.com/p/open-delta/issues/detail?id=134) - Pressing ESC in the attribute editor commits changes instead of cancelling them.

## Intkey Release Notes ##

#### New Features ####
None.

#### Bug Fixes ####
  * [ISSUE 150](https://code.google.com/p/open-delta/issues/detail?id=150) - Intkey deadlock when prompting for input while processing the startup file
  * [ISSUE 151](https://code.google.com/p/open-delta/issues/detail?id=151) - Intkey prompting for input during startup when the original does not.
  * [ISSUE 158](https://code.google.com/p/open-delta/issues/detail?id=158) - Should be able to delete character values by selecting no value in prompt then clicking OK
  * [ISSUE 159](https://code.google.com/p/open-delta/issues/detail?id=159) - Best ordering should be recalculated when error tolerance is changed
  * [ISSUE 160](https://code.google.com/p/open-delta/issues/detail?id=160) - Issue with controlling characters and inapplicability
  * [ISSUE 162](https://code.google.com/p/open-delta/issues/detail?id=162) - Maximum number of recent datasets should be increased
  * [ISSUE 164](https://code.google.com/p/open-delta/issues/detail?id=164) - Font sizing
  * [ISSUE 165](https://code.google.com/p/open-delta/issues/detail?id=165) - Text in main pane touches left boundary
  * [ISSUE 166](https://code.google.com/p/open-delta/issues/detail?id=166) - The limits on the position of the vertical pane divider should be relaxed
  * [ISSUE 168](https://code.google.com/p/open-delta/issues/detail?id=168) - Font in output of SHOW command
  * [ISSUE 172](https://code.google.com/p/open-delta/issues/detail?id=172) - The ‘Best’ and ‘Natural’ order buttons should be grey when not applicable
  * [ISSUE 173](https://code.google.com/p/open-delta/issues/detail?id=173) - The ‘Information’ button should work when no taxa are selected
  * [ISSUE 175](https://code.google.com/p/open-delta/issues/detail?id=175) - ‘Display CharacterOrder’ should default to ‘Natural’ in Advanced mode
  * [ISSUE 176](https://code.google.com/p/open-delta/issues/detail?id=176) - ‘Display images’ should default to ‘Manual’ in Advanced mode.
  * [ISSUE 181](https://code.google.com/p/open-delta/issues/detail?id=181) - Some directives incorrectly fail due to no dataset being loaded
  * [ISSUE 185](https://code.google.com/p/open-delta/issues/detail?id=185) - Incorrect message when Identification complete
  * [ISSUE 186](https://code.google.com/p/open-delta/issues/detail?id=186) - When a state-selection image window is closed the text version of the window should be displayed.
  * [ISSUE 188](https://code.google.com/p/open-delta/issues/detail?id=188) - In ‘Natural’ order the focus should move to the next character after a character is used
  * [ISSUE 189](https://code.google.com/p/open-delta/issues/detail?id=189) - Focus on first character when character order recalculated or identification is restarted
  * [ISSUE 194](https://code.google.com/p/open-delta/issues/detail?id=194) - The ‘Illustrate Characters’ command doesn’t display the first illustrated character in the set
  * [ISSUE 195](https://code.google.com/p/open-delta/issues/detail?id=195) - When a character is re-used, currently set states should be in the dialog
  * [ISSUE 196](https://code.google.com/p/open-delta/issues/detail?id=196) - Issues with Display Images command
  * [ISSUE 201](https://code.google.com/p/open-delta/issues/detail?id=201) -  Setting the size of the main window via ‘Window > Tile’ is not implemented

#### Known Problems ####
  * [ISSUE 59](https://code.google.com/p/open-delta/issues/detail?id=59) - Intkey hangs when loading remotely hosted images
  * [ISSUE 156](https://code.google.com/p/open-delta/issues/detail?id=156) - Attempting to view character images when there are none causes issues in Intkey
  * [ISSUE 161](https://code.google.com/p/open-delta/issues/detail?id=161) - Datasets index is not implemented
  * [ISSUE 163](https://code.google.com/p/open-delta/issues/detail?id=163) - Double click issues in Intkey
  * [ISSUE 167](https://code.google.com/p/open-delta/issues/detail?id=167) - Font size in RTF reports
  * [ISSUE 169](https://code.google.com/p/open-delta/issues/detail?id=169) - Hanging styles should be used in windows displaying hierarchical information
  * [ISSUE 170](https://code.google.com/p/open-delta/issues/detail?id=170) - Integer values are sometimes displayed with a decimal point
  * [ISSUE 171](https://code.google.com/p/open-delta/issues/detail?id=171) - The displays generated by the ‘differences’ buttons should show non-overlapping differences in bold.
  * [ISSUE 174](https://code.google.com/p/open-delta/issues/detail?id=174) - When there is more than 1 level in a menu, the pop-up help is obscured by the next level
  * [ISSUE 177](https://code.google.com/p/open-delta/issues/detail?id=177) - Double click required for selection of character
  * [ISSUE 178](https://code.google.com/p/open-delta/issues/detail?id=178) - Cannot select states, characters, taxa using the keyboard
  * [ISSUE 179](https://code.google.com/p/open-delta/issues/detail?id=179) - Cannot select from keywords and lists simultaneously
  * [ISSUE 180](https://code.google.com/p/open-delta/issues/detail?id=180) - Options that require data files should be grey in the menus if data files are not present
  * [ISSUE 182](https://code.google.com/p/open-delta/issues/detail?id=182) - Incorrect fonts are used in image overlays
  * [ISSUE 183](https://code.google.com/p/open-delta/issues/detail?id=183) - The character-notes window should be positioned away from the underlying dialog when possible
  * [ISSUE 184](https://code.google.com/p/open-delta/issues/detail?id=184) - The state-selection dialog in Normal mode should have a reduced set of buttons
  * [ISSUE 187](https://code.google.com/p/open-delta/issues/detail?id=187) - When any state-selection window is canceled, or a state-selection text window is closed, the focus should move to the next character
  * [ISSUE 190](https://code.google.com/p/open-delta/issues/detail?id=190) - Should be able to double-click to select in text list selection windows
  * [ISSUE 191](https://code.google.com/p/open-delta/issues/detail?id=191) - Fix double click in image selection windows
  * [ISSUE 192](https://code.google.com/p/open-delta/issues/detail?id=192) - Should be able to selection multiple items with single clicks in state selection windows.
  * [ISSUE 193](https://code.google.com/p/open-delta/issues/detail?id=193) - In a state-selection image window hotspots should be invisible
  * [ISSUE 197](https://code.google.com/p/open-delta/issues/detail?id=197) - Multiple windows displayed via the ‘Taxon Information’ dialog are not automatically tiled
  * [ISSUE 198](https://code.google.com/p/open-delta/issues/detail?id=198) - The minimum size of taxon image windows is too large
  * [ISSUE 199](https://code.google.com/p/open-delta/issues/detail?id=199) - Intkey image scaling issues
  * [ISSUE 200](https://code.google.com/p/open-delta/issues/detail?id=200) - Popup windows have no options for maximizing or minimizing
  * [ISSUE 202](https://code.google.com/p/open-delta/issues/detail?id=202) - It should be possible to open a Web dataset by specifying a URL in the ‘Open’ dialog
  * [ISSUE 203](https://code.google.com/p/open-delta/issues/detail?id=203) - Images and RTF descriptions viewed from the Web are not cached
  * [ISSUE 204](https://code.google.com/p/open-delta/issues/detail?id=204) - RTF renderer does not centre text in some circumstances

# Release Candidate 2 #
The Open DELTA Release Candidate 2 (RC2) release has been created primarily to address a serious problem with Intkey.

## Known Problems ##
  * [ISSUE 140](https://code.google.com/p/open-delta/issues/detail?id=140) - PClass not implemented.
  * [ISSUE 141](https://code.google.com/p/open-delta/issues/detail?id=141) - NSim not implemented.

## DELTA Editor Release Notes ##

#### New Features ####
  * The DELTA Editor will now restore the most recently selected Look & Feel on startup

#### Bug Fixes ####
  * [ISSUE 145](https://code.google.com/p/open-delta/issues/detail?id=145) - Maximised internal frame on Mac OS still has a large drop shadow border.
  * [ISSUE 144](https://code.google.com/p/open-delta/issues/detail?id=144) - Spurious validation error when opening Character Editor on Linux
  * [ISSUE 41](https://code.google.com/p/open-delta/issues/detail?id=41) - Importing items into an existing data set is now supported.
  * [ISSUE 142](https://code.google.com/p/open-delta/issues/detail?id=142) - The todis directive file wasn't being included in a new dataset due to an import error.
  * [ISSUE 120](https://code.google.com/p/open-delta/issues/detail?id=120) - Actions - Results dialog has hardcoded english labels.

#### Known Problems ####
  * [ISSUE 126](https://code.google.com/p/open-delta/issues/detail?id=126) - The import dialog exclusion filter doesn't seem to work correctly on Windows XP.
  * [ISSUE 127](https://code.google.com/p/open-delta/issues/detail?id=127) - Opening an action set in the editor marks the data set as needing to be saved.
  * [ISSUE 134](https://code.google.com/p/open-delta/issues/detail?id=134) - Pressing ESC in the attribute editor commits changes instead of cancelling them.

## Intkey Release Notes ##

#### New Features ####
  * [ISSUE 143](https://code.google.com/p/open-delta/issues/detail?id=143) - Intkey imagepath a problem for site maintenance.

#### Bug Fixes ####
  * [ISSUE 146](https://code.google.com/p/open-delta/issues/detail?id=146) - Not doing well with intkey.ink (intkey datasets with no heading or sub heading would not load).
  * [ISSUE 147](https://code.google.com/p/open-delta/issues/detail?id=147) - Can't open recent files with spaces in the path or filename.

#### Known Problems ####
  * [ISSUE 33](https://code.google.com/p/open-delta/issues/detail?id=33) - Intkey character layout can be untidy.
  * [ISSUE 59](https://code.google.com/p/open-delta/issues/detail?id=59) - Intkey hangs when loading remotely hosted images.
  * [ISSUE 99](https://code.google.com/p/open-delta/issues/detail?id=99) - Information panels' "Mulitple Images" button inoperative.

# Release Candidate 1 #
The Open DELTA Release Candidate 1 (RC1) release consists predominantly of bug fixes.
Please make a copy of your existing DELTA data sets before accessing them with any of the Open DELTA programs.

## New Features ##
  * The implementation of the Key program has been completed.
  * The embedded JRE version has been updated to 1.6.0\_31

## General fixes ##
  * [ISSUE 125](https://code.google.com/p/open-delta/issues/detail?id=125) - Removed .svn folder from installer.

## Known Problems ##
  * [ISSUE 140](https://code.google.com/p/open-delta/issues/detail?id=140) - PClass not implemented.
  * [ISSUE 141](https://code.google.com/p/open-delta/issues/detail?id=141) - NSim not implemented.

## DELTA Editor Release Notes ##

#### New Features ####
  * [ISSUE 117](https://code.google.com/p/open-delta/issues/detail?id=117) - Long feature descriptions will be displayed in a tooltip if you hover the mouse over a character feature description.

#### Bug Fixes ####
  * [ISSUE 36](https://code.google.com/p/open-delta/issues/detail?id=36) - Can't add new character in grid view when there are zero characters
  * [ISSUE 37](https://code.google.com/p/open-delta/issues/detail?id=37) - Help is still missing
  * [ISSUE 49](https://code.google.com/p/open-delta/issues/detail?id=49) - The character edit dialog can be closed, and the selected character can change even if a validation error is present.
  * [ISSUE 50](https://code.google.com/p/open-delta/issues/detail?id=50) - Multistate characters with zero states are not validated
  * [ISSUE 55](https://code.google.com/p/open-delta/issues/detail?id=55) - Add new states always adds to the end instead of highlighted position.
  * [ISSUE 73](https://code.google.com/p/open-delta/issues/detail?id=73) - Exporting intkey directives with unix end of lines cause the original Inkey to crash.
  * [ISSUE 89](https://code.google.com/p/open-delta/issues/detail?id=89) - The DELTA Editor should launch Intkey in a new process
  * [ISSUE 96](https://code.google.com/p/open-delta/issues/detail?id=96) - Delta Editor import pre-selected home dir in wrong place.
  * [ISSUE 97](https://code.google.com/p/open-delta/issues/detail?id=97) - Action Sets Panel doesn't sort columns when column headings clicked
  * [ISSUE 108](https://code.google.com/p/open-delta/issues/detail?id=108) - deleting characters will affect layout for CONFOR
  * [ISSUE 109](https://code.google.com/p/open-delta/issues/detail?id=109) - The editor export is not handling items / items.txt well.
  * [ISSUE 114](https://code.google.com/p/open-delta/issues/detail?id=114) - Key not properly integrated with the DELTA Editor
  * [ISSUE 116](https://code.google.com/p/open-delta/issues/detail?id=116) - Offer to link the .dlt file extension to the Delta Editor on install
  * [ISSUE 118](https://code.google.com/p/open-delta/issues/detail?id=118) - Image Settings dialog sometimes opens too small
  * [ISSUE 115](https://code.google.com/p/open-delta/issues/detail?id=115) - Images won't upload, image path can't be specified
  * [ISSUE 123](https://code.google.com/p/open-delta/issues/detail?id=123) - Saving a DLT file on top of an existing DLT file is allowed and causes problems
  * [ISSUE 124](https://code.google.com/p/open-delta/issues/detail?id=124) - Internal frame titles are not updated during the saveAs operation
  * [ISSUE 129](https://code.google.com/p/open-delta/issues/detail?id=129) - taxon names don't redraw in matrix viewer while scrolling
  * [ISSUE 130](https://code.google.com/p/open-delta/issues/detail?id=130) - "treat items as variant" causes items file and item outputs to be truncated for the variant and all subsequent items
  * [ISSUE 131](https://code.google.com/p/open-delta/issues/detail?id=131) - tonatr confor directives leaves .rtf output file locked.
  * [ISSUE 121](https://code.google.com/p/open-delta/issues/detail?id=121) - process cannot access file because another process has locked portion of the file
  * [ISSUE 132](https://code.google.com/p/open-delta/issues/detail?id=132) - Triggering a validation error using the "next/previous" character spinner results in too many error dialogs
  * [ISSUE 133](https://code.google.com/p/open-delta/issues/detail?id=133) - Attribute editors don't commit until "Enter" is pressed.
  * [ISSUE 122](https://code.google.com/p/open-delta/issues/detail?id=122) - Issues with directives files validation


#### Known Problems ####
  * [ISSUE 41](https://code.google.com/p/open-delta/issues/detail?id=41) - Delta Editor doesn't support importing Items into an existing data set.
  * [ISSUE 120](https://code.google.com/p/open-delta/issues/detail?id=120) - Actions - Results dialog has hardcoded english labels.
  * [ISSUE 126](https://code.google.com/p/open-delta/issues/detail?id=126) - The import dialog exclusion filter doesn't seem to work correctly on Windows XP.
  * [ISSUE 127](https://code.google.com/p/open-delta/issues/detail?id=127) - Opening an action set in the editor marks the data set as needing to be saved.
  * [ISSUE 134](https://code.google.com/p/open-delta/issues/detail?id=134) - Pressing ESC in the attribute editor commits changes instead of cancelling them.
  * [ISSUE 142](https://code.google.com/p/open-delta/issues/detail?id=142) - 	Some directive file templates don't import correctly when a new dataset is created

## CONFOR Release Notes ##

#### New Features ####
  * DECIMAL PLACES has been implemented
  * PRINT ALL CHARACTERS has been implemented
  * ACCEPT DUPLICATE VALUES has been implemented
  * STOP AFTER ITEM has been implemented
  * OMIT LOWER FOR CHARACTERS has been implemented

#### Bug Fixes ####
  * [ISSUE 122](https://code.google.com/p/open-delta/issues/detail?id=122) - Validation and error handling during directives parsing has been sign
  * [ISSUE 83](https://code.google.com/p/open-delta/issues/detail?id=83) - Various issues with PRINT CHARACTER LIST (from Mike Dallwitz via DELTA-L)
  * Numeric attributes are now output in the same way as the original CONFOR during natural language translation (which is five significant figures (with no rounding of non-decimal digits) by default).
  * [ISSUE 106](https://code.google.com/p/open-delta/issues/detail?id=106) - Duplicate APPLICABLE CHARACTERS/INAPPLICABLE CHARACTERS / DEPENDENT CHARACTERS error reduced to a warning.

#### Known Problems ####
  * [ISSUE 136](https://code.google.com/p/open-delta/issues/detail?id=136) - page break directives not implemented (NEW LISTING PAGE, NEW PRINT PAGE, PAGE LENGTH)
  * [ISSUE 137](https://code.google.com/p/open-delta/issues/detail?id=137) - DATA COMPRESSION not implemented
  * [ISSUE 138](https://code.google.com/p/open-delta/issues/detail?id=138) - CHINESE FORMAT not implemented
  * [ISSUE 139](https://code.google.com/p/open-delta/issues/detail?id=139) - INSERT UNCODED ATTRIBUTES not implemented
  * USE LANGUAGE not implemented
  * ALTERNATE LANGUAGES not implemented

## DELFOR Release Notes ##
No changes

## DIST Release Notes ##
No changes

## Intkey Release Notes: ##

#### Bug Fixes ####

  * [ISSUE 100](https://code.google.com/p/open-delta/issues/detail?id=100) - In Intkey multiple images cannot be Cascaded, Tiled or Closed
  * [ISSUE 98](https://code.google.com/p/open-delta/issues/detail?id=98) - Information panel's "Illustrations" and "Other" display doesn't tile or cascade
  * [ISSUE 90](https://code.google.com/p/open-delta/issues/detail?id=90)  - Minor Intkey problem
  * [ISSUE 77](https://code.google.com/p/open-delta/issues/detail?id=77) - Intkey web search
  * [ISSUE 104](https://code.google.com/p/open-delta/issues/detail?id=104) - Inktey is not defaulting to iitems and ichars if FILE directives missing
  * [ISSUE 113](https://code.google.com/p/open-delta/issues/detail?id=113) - Intkey: conflict between different characters using same control
  * [ISSUE 112](https://code.google.com/p/open-delta/issues/detail?id=112) - Inkey doesn't resize large taxon images to tile in current window
  * [ISSUE 76](https://code.google.com/p/open-delta/issues/detail?id=76) - Intkey online help
  * [ISSUE 59](https://code.google.com/p/open-delta/issues/detail?id=59) - Intkey UI can be unresponsive when generating reports or loading files.

#### Known Problems ####
  * [ISSUE 33](https://code.google.com/p/open-delta/issues/detail?id=33) - Intkey character layout can be untidy.
  * [ISSUE 59](https://code.google.com/p/open-delta/issues/detail?id=59) - Intkey hangs when loading remotely hosted images.
  * [ISSUE 99](https://code.google.com/p/open-delta/issues/detail?id=99) - Information panels' "Mulitple Images" button inoperative.

## Key Release Notes ##

  * [ISSUE 101](https://code.google.com/p/open-delta/issues/detail?id=101) - The implementation of KEY has been completed.

# Milestone 10 #
The Milestone 10 (M10) release has progressed work on KEY, Intkey and CONFOR.  The DELFOR program has been implemented.
Please make a copy of your existing DELTA data sets before accessing them with any of the Open DELTA programs.

## DELTA Editor Release Notes ##

#### New Features ####
  * Improved integration with CONFOR / Intkey / DIST / KEY.

#### Bug Fixes ####
  * [ISSUE 64](https://code.google.com/p/open-delta/issues/detail?id=64) - Something is wrong with inapplicability checking after an import.
  * [ISSUE 85](https://code.google.com/p/open-delta/issues/detail?id=85) - chars, items, specs only exported second time.
  * [ISSUE 86](https://code.google.com/p/open-delta/issues/detail?id=86) - Intkey doesn't launch from the Editor if DELTA was installed with the .exe
  * [ISSUE 87](https://code.google.com/p/open-delta/issues/detail?id=87) - Intkey DEFINE CHARACTERS / DEFINE TAXA are not imported / exported correctly.

#### Known problems ####
  * [ISSUE 36](https://code.google.com/p/open-delta/issues/detail?id=36) - Can't add new character in grid view when there are zero characters
  * [ISSUE 37](https://code.google.com/p/open-delta/issues/detail?id=37) - Help is still missing
  * [ISSUE 41](https://code.google.com/p/open-delta/issues/detail?id=41) - Editor does not support importing chars or items into an existing data set.
  * [ISSUE 49](https://code.google.com/p/open-delta/issues/detail?id=49) - The character edit dialog can be closed, and the selected character can change even if a validation error is present.
  * [ISSUE 50](https://code.google.com/p/open-delta/issues/detail?id=50) - Multistate characters with zero states are not validated
  * [ISSUE 55](https://code.google.com/p/open-delta/issues/detail?id=55) - Add new states always adds to the end instead of highlighted position.
  * [ISSUE 73](https://code.google.com/p/open-delta/issues/detail?id=73) - Exporting intkey directives with unix end of lines cause the original Inkey to crash.
  * [ISSUE 89](https://code.google.com/p/open-delta/issues/detail?id=89) - The DELTA Editor should launch Intkey in a new process

## CONFOR Release Notes ##
#### New Features ####
  * TRANSLATE INTO PAYNE FORMAT is now supported
  * TRANSLATE INTO HENNING86 FORMAT is now supported
  * PRINT SUMMARY is now supported.
  * Error handling has been improved.

#### Bug Fixes ####
  * [ISSUE 61](https://code.google.com/p/open-delta/issues/detail?id=61) - problem: Action set: Translate into Intkey format (intkey.ink) not output correctly

#### Known Problems ####
  * Validation and error handling during directives parsing is not complete.
  * [ISSUE 83](https://code.google.com/p/open-delta/issues/detail?id=83) - Various issues with PRINT CHARACTER LIST (from Mike Dallwitz via DELTA-L)
  * USE LANGUAGE not implemented
  * ALTERNATE LANGUAGES not implemented
  * page break directives not implemented (NEW LISTING PAGE, NEW PRINT PAGE, PAGE LENGTH)
  * PRINT ALL CHARACTERS not implemented
  * ACCEPT DUPLICATE VALUES not implemented
  * DATA COMPRESSION not implemented
  * STOP AFTER ITEM not implemented
  * OMIT LOWER FOR CHARACTERS not implemented
  * CHINESE FORMAT not implemented
  * INSERT UNCODED ATTRIBUTES not implemented

## DELFOR Release Notes ##
DELFOR has been implemented.  A change from the original DELFOR is that the items file is required as an INPUT FILE.  This change has been made in the reorder file included with the sample.

## Intkey Release Notes ##

#### Bug Fixes ####
  * [ISSUE 79](https://code.google.com/p/open-delta/issues/detail?id=79) - Intkey RTF report dialog find menu.

## KEY Release Notes ##
KEY has been partially implemented.  The key can be created and printed but only a subset of the directives are implemented.


# Milestone 9 #

## General Release Notes: ##
The Milestone 9 (M9) release has progressed work on CONFOR, DIST, KEY and Intkey.

The DELTA software is considered an alpha release.  Please make a copy of your existing DELTA data sets before accessing them with any of the Open DELTA programs.

## DELTA Editor Release Notes: ##

#### New Features ####
  * KEY, DIST and IntKey are integrated into the DELTA Editor action sets.

#### Bug Fixes ####
  * [ISSUE 82](https://code.google.com/p/open-delta/issues/detail?id=82) - Date / Times in the Action Sets dialog are nonsense.
  * [ISSUE 74](https://code.google.com/p/open-delta/issues/detail?id=74) - Missing expand all on right click menu in tree view.
  * [ISSUE 67](https://code.google.com/p/open-delta/issues/detail?id=67) - RTF despace routine fails with unicode chars
  * [ISSUE 42](https://code.google.com/p/open-delta/issues/detail?id=42) - Action Sets running original programs

#### Known Problems ####
  * [ISSUE 36](https://code.google.com/p/open-delta/issues/detail?id=36) - Can't add new character in grid view when there are zero characters
  * [ISSUE 37](https://code.google.com/p/open-delta/issues/detail?id=37) - Help is still missing
  * [ISSUE 41](https://code.google.com/p/open-delta/issues/detail?id=41) - Editor does not support importing chars or items into an existing data set.
  * [ISSUE 49](https://code.google.com/p/open-delta/issues/detail?id=49) - The character edit dialog can be closed, and the selected character can change even if a validation error is present.
  * [ISSUE 50](https://code.google.com/p/open-delta/issues/detail?id=50) - Multistate characters with zero states are not validated
  * [ISSUE 55](https://code.google.com/p/open-delta/issues/detail?id=55) - Add new states always adds to the end instead of highlighted position.
  * [ISSUE 64](https://code.google.com/p/open-delta/issues/detail?id=64) - Inapplicable characters sometimes don't show up after an import.
  * [ISSUE 73](https://code.google.com/p/open-delta/issues/detail?id=73) - Exporting intkey directives with unix end of lines cause the original Inkey to crash.

## CONFOR Release Notes ##
#### New Features ####
  * TRANSLATE INTO NEXUS FORMAT is now supported
  * TRANSLATE INTO PAUP FORMAT is now supported
  * TRANSLATE INTO HENNING86 FORMAT is now supported
  * PRINT UNCODED CHARACTERS is now supported.
  * Error handling has been improved.

#### Bug Fixes ####
  * [ISSUE 63](https://code.google.com/p/open-delta/issues/detail?id=63) - Inapplicable bit not set in Intkey attributes
  * [ISSUE 65](https://code.google.com/p/open-delta/issues/detail?id=65) - Intkey directives and binaries not being created via OpenDelta Editor and Confor.sh
  * [ISSUE 68](https://code.google.com/p/open-delta/issues/detail?id=68) - INTKEY translation fails when encountering a numeric attribute coded with only a comment.
  * [ISSUE 69](https://code.google.com/p/open-delta/issues/detail?id=69) - Spaces in the value of the CHARACTER FOR OUTPUT FILES result in errors in the INTKEY translation
  * [ISSUE 75](https://code.google.com/p/open-delta/issues/detail?id=75) - CONFOR fails on real dataset
  * [ISSUE 81](https://code.google.com/p/open-delta/issues/detail?id=81) - CONFOR RTF reports have spaces in the rtf header which MS Word doesn't like

#### Known Problems ####
  * Validation and error handling during directives parsing is not complete.
  * [ISSUE 61](https://code.google.com/p/open-delta/issues/detail?id=61) - problem: Action set: Translate into Intkey format (intkey.ink) not output correctly
  * [ISSUE 83](https://code.google.com/p/open-delta/issues/detail?id=83) - Various issues with PRINT CHARACTER LIST (from Mike Dallwitz via DELTA-L)
  * Output from EMPHASIZE CHARACTERS only partially correct.
  * TRANSLATE INTO PAYNE FORMAT not implemented
  * PRINT SUMMARY not implemented
  * USE LANGUAGE not implemented
  * ALTERNATE LANGUAGES not implemented
  * page break directives not implemented (NEW LISTING PAGE, NEW PRINT PAGE, PAGE LENGTH)
  * PRINT ALL CHARACTERS not implemented
  * ACCEPT DUPLICATE VALUES not implemented
  * DATA COMPRESSION not implemented
  * STOP AFTER ITEM not implemented
  * OMIT LOWER FOR CHARACTERS not implemented
  * CHINESE FORMAT not implemented
  * INSERT UNCODED ATTRIBUTES not implemented


## DIST Release Notes ##
#### New Features ####
  * DIST has been implemented.

## Intkey Release Notes: ##

#### New Features ####
  * All advanced mode directives have now been implemented, with the exception of the DIAGNOSE directive.

#### Known Problems ####
  * [ISSUE 33](https://code.google.com/p/open-delta/issues/detail?id=33) - Intkey character layout can be untidy
  * [ISSUE 59](https://code.google.com/p/open-delta/issues/detail?id=59) - Intkey hangs when loading remotely hosted images
  * [ISSUE 60](https://code.google.com/p/open-delta/issues/detail?id=60) - Intkey UI can be unresponsi?ve when generating reports or loading files
  * [ISSUE 76](https://code.google.com/p/open-delta/issues/detail?id=76) - Intkey online help yet to be implemented
  * [ISSUE 77](https://code.google.com/p/open-delta/issues/detail?id=77) - Intkey web search yet to be implemented
  * [ISSUE 78](https://code.google.com/p/open-delta/issues/detail?id=78) - Intkey window tiling, cascading and closing yet to be implemeted
  * [ISSUE 79](https://code.google.com/p/open-delta/issues/detail?id=79) - Intkey RTF report dialog find menu yet to be implemented
  * [ISSUE 80](https://code.google.com/p/open-delta/issues/detail?id=80) - Intkey RTF report printing yet to be implemented

## KEY Release Notes: ##

#### New Features ####
  * A basic implementation of key has been completed. The ABASE and REUSE values are currently ignored. Item adundances are currently ignored.

### Known problems ###
  * The character ordering chosen by the BEST algorithm is in some circumstances slightly different to that chosen by the legacy version of key.
> This problem is being investigated.

# Milestone 8 #

## General Release Notes: ##
The Milestone 8 (M8) release has progressed work on CONFOR and Intkey.  Some fixes were made to the DELTA Editor.

The DELTA software is considered an alpha release.  Please make a copy of your existing DELTA data sets before accessing them with any of the Open DELTA programs.

## DELTA Editor Release Notes: ##

#### New Features ####
  * Help tooltips have been added to the directives file editor windows.
  * Action Sets now use the ported (Java) CONFOR when running CONFOR directives files.
  * [ISSUE 44](https://code.google.com/p/open-delta/issues/detail?id=44) - Search menu missing from Editor

#### Bug Fixes ####
  * [ISSUE 53](https://code.google.com/p/open-delta/issues/detail?id=53) - In grid view the tab key moves focus to the attribute editor.
  * [ISSUE 54](https://code.google.com/p/open-delta/issues/detail?id=54) - Number the open windows in the DELTA editor.
  * [ISSUE 56](https://code.google.com/p/open-delta/issues/detail?id=56) - Some status updates during export are missing.
  * [ISSUE 57](https://code.google.com/p/open-delta/issues/detail?id=57) - Run directives file currently doesn't use the export directory.
  * [ISSUE 58](https://code.google.com/p/open-delta/issues/detail?id=58) - Exporting a new data set doesn't work correctly.

#### Known Problems ####
  * [ISSUE 36](https://code.google.com/p/open-delta/issues/detail?id=36) - Can't add new character in grid view when there are zero characters
  * [ISSUE 37](https://code.google.com/p/open-delta/issues/detail?id=37) - Help is still missing
  * [ISSUE 41](https://code.google.com/p/open-delta/issues/detail?id=41) - Editor does not support importing chars or items into an existing data set.
  * [ISSUE 42](https://code.google.com/p/open-delta/issues/detail?id=42) - Action sets hooked up to old CONFOR/KEY/DIST
  * [ISSUE 49](https://code.google.com/p/open-delta/issues/detail?id=49) - The character edit dialog can be closed, and the selected character can change even if a validation error is present.
  * [ISSUE 50](https://code.google.com/p/open-delta/issues/detail?id=50) - Multistate characters with zero states are not validated

## CONFOR Release Notes ##
#### New Features ####
  * PRINT CHARACTER LIST is now supported
  * PRINT ITEM NAMES is now supported
  * PRINT ITEM DESCRIPTIONS is now supported
  * TRANSLATE INTO KEY FORMAT is now supported
  * TRANSLATE INTO DIST FORMAT is now supported
  * OUTPUT FORMAT HTML is now supported

#### Bug Fixes ####
  * Several fixes to the TRANSLATE INTO INTKEY FORMAT routine.

#### Known Problems ####
  * Validation and error handling during directives parsing is not complete.

## Intkey Release Notes: ##

#### New Features ####

  * "SEPARATE" character ordering
  * Describe report
  * Summary report
  * Other advanced mode commands

The majority of the advanced mode commands have been completed. See "Known Problems" below for a list of those that are yet to be implemented.

#### Known Problems ####

  * Help and context sensitive help are yet to be completed
  * [ISSUE 33](https://code.google.com/p/open-delta/issues/detail?id=33) - Intkey character layout can be untidy
  * [ISSUE 59](https://code.google.com/p/open-delta/issues/detail?id=59) - Intkey hangs when loading remotely hosted images
  * [ISSUE 60](https://code.google.com/p/open-delta/issues/detail?id=60) - Intkey UI can be unresponsi?ve when generating reports or loading files

The following advanced mode commands are yet to be implemented:

  * DEFINE ENDIDENTIFY
  * DEFINE SUBJECTS
  * DIAGNOSE
  * DISPLAY ENDIDENTIFY
  * DISPLAY CONTINUOUS
  * DISPLAY IMAGES
  * DISPLAY KEYWORDS
  * DISPLAY INPUT
  * DISPLAY SCALED
  * DISPLAY WINDOWING
  * OUTPUT CHARACTERS
  * OUTPUT TAXA
  * OUTPUT DESCRIBE
  * OUTPUT SUMMARY
  * OUTPUT DIAGNOSE
  * OUTPUT DIFFERENCES
  * OUTPUT SIMILARITIES
  * OUTPUT COMMENT
  * PREFERENCES
  * SET DEMONSTRATION
  * STATUS

# Milestone 7 #

## General Release Notes: ##
The Milestone 7 (M7) release has progressed work on the DELTA Editor and Intkey.

The DELTA software is considered an alpha release.  Please make a copy of your existing DELTA data sets before accessing them with any of the Open DELTA programs.

## DELTA Editor Release Notes: ##

#### New Features ####
  * Implemented the image settings dialog
  * Implemented directive file editing
  * Further work on import/export functionality


#### Known Problems ####
  * [ISSUE 36](https://code.google.com/p/open-delta/issues/detail?id=36) - Can't add new character in grid view when there are zero characters
  * [ISSUE 37](https://code.google.com/p/open-delta/issues/detail?id=37) - Help is still missing
  * [ISSUE 41](https://code.google.com/p/open-delta/issues/detail?id=41) - Editor does not support importing chars or items into an existing data set.
  * [ISSUE 42](https://code.google.com/p/open-delta/issues/detail?id=42) - Action sets hooked up to old CONFOR/KEY/DIST
  * [ISSUE 49](https://code.google.com/p/open-delta/issues/detail?id=49) - The character edit dialog can be closed, and the selected character can change even if a validation error is present.
  * [ISSUE 50](https://code.google.com/p/open-delta/issues/detail?id=50) - Multistate characters with zero states are not validated

## Intkey Release Notes: ##

#### New Features ####
  * Implemented taxon information dialog
  * Added support for remotely hosted datasets
  * Added tooltips for toolbar buttons and menu items
  * Reworked directive handling to support ?,?K,?L and ?S wildcards
  * Implemented DELETE, CHARACTERS and ILLUSTRATE TAXA directives
  * Basic implementation of the DESCRIBE directive
  * Fixed various image display problems

#### Known Problems ####
  * [ISSUE 33](https://code.google.com/p/open-delta/issues/detail?id=33) - Layout of character state image overlays can be untidy

# Milestone 6 #

## General Release Notes: ##
The Milestone 6 (M6) release has progressed work on the DELTA Editor and Intkey.

The DELTA software is considered an alpha release.  Please make a copy of your existing DELTA data sets before accessing them with any of the Open DELTA programs.

## DELTA Editor Release Notes: ##

#### New Features ####
  * Image Overlays can be added and edited.

#### Known Problems ####
  * [ISSUE 15](https://code.google.com/p/open-delta/issues/detail?id=15) - Real numeric characters cannot be edited in locales with a ',' as a decimal separator.
  * [ISSUE 16](https://code.google.com/p/open-delta/issues/detail?id=16) - no key combination to exit Complex-Attribute Editor pane
  * [ISSUE 17](https://code.google.com/p/open-delta/issues/detail?id=17) - no key combination to exit Complex-Attribute Editor pane
  * Status updates during import and export are not complete.
  * The cimages and timages directives don't import correctly.
  * Import / Export could use further testing.
  * Hotspots cannot be edited using the dialog.

## Intkey Release Notes: ##

#### New Features ####
  * INCLUDE CHARACTERS directive
  * INCLUDE TAXA directive
  * EXCLUDE CHARACTERS directive
  * EXCLUDE TAXA directive
  * SET TOLERANCE directive

  * Characters and taxa can now be searched for in the main window
  * Support for character, taxon, keyword and startup images
  * Buttons can be defined and added to the toolbar using directives files


#### Known Problems ####
  * Datasets that are hosted on a remote server cannot be used.
  * Image notes and character notes are currently not displayed.
  * Switching to full screen mode when viewing a character, taxon or keyword image does not currently work correctly.
  * Some toolbar buttons defined using directives files may not function correctly due to the directives that they run not yet being implemented. In addition, there is currently no support for the directive wildcards ?, ?K, ?L, ?L1 and ?S. Any buttons that use these wildcards will not function correctly.
  * The text color of image overlays does not invert when the overlay is selected. Hence once an overlay is selected, its text will no longer be visible.


# Milestone 5 #

## General Release Notes: ##
The Milestone 5 (M5) release has progressed work on the DELTA Editor and Intkey.

There is now a Spanish language translation available.  (thanks to Roberto Castro-Cortes).

The DELTA software is considered an alpha release.  Please make a copy of your existing DELTA data sets before accessing them with any of the Open DELTA programs.

## DELTA Editor Release Notes: ##

#### New Features ####
  * The character dependency editors have been implemented.
  * Character and Taxon images can be displayed including any included overlays.
  * The directive file export function has been implemented.

#### Known Problems ####
  * [ISSUE 15](https://code.google.com/p/open-delta/issues/detail?id=15) - Real numeric characters cannot be edited in locales with a ',' as a decimal separator.
  * [ISSUE 16](https://code.google.com/p/open-delta/issues/detail?id=16) - no key combination to exit Complex-Attribute Editor pane
  * [ISSUE 17](https://code.google.com/p/open-delta/issues/detail?id=17) - no key combination to exit Complex-Attribute Editor pane
  * Status updates during import and export are not complete.
  * The cimages and timages directives don't import correctly.
  * Import / Export could use further testing.

## CONFOR Release Notes: ##

#### New Features ####
  * TRANSLATE INTO DELTA FORMAT has begun implementation.

#### Known Problems ####
  * The DECIMAL PLACES directive is not supported.
  * If the output file is changed between the items and chars files the last output file will win.

## Intkey Release Notes: ##

Intkey can now be used to conduct basic investigations. We would appreciate any feedback that the community has at this point.

#### New Features ####
  * BEST ordering of dataset characters has been implemented
  * DIFFERENCES report has been implemented

#### Known Problems ####
  * An out of memory exception will be thrown if the content of a differences report is too large
  * Differences report yields incorrect results when Intkey's match type option is set to "Subset". This is an advanced feature and is currently only evident when using the command text bar. This issue needs investigation.

# Milestone 4 #

## General Release Notes: ##

The Milestone 4 (M4) release has progressed work on the DELTA Editor and Intkey.

There is now a French language translation available. (thanks to Yves Braet).

The DELTA software is considered an alpha release.  Please make a copy of your existing DELTA data sets before accessing them with any of the Open DELTA programs.

### Bug Fixes ###
  * [ISSUE 11](https://code.google.com/p/open-delta/issues/detail?id=11) - Can't compile from source.

## DELTA Editor Release Notes: ##

#### New Features ####
  * The character editor is largely complete.
  * [ISSUE 13](https://code.google.com/p/open-delta/issues/detail?id=13) - Select All and Copy Selected Including Headers missing
  * [ISSUE 14](https://code.google.com/p/open-delta/issues/detail?id=14) - Character and state editing

#### Bug Fixes ####
  * [ISSUE 12](https://code.google.com/p/open-delta/issues/detail?id=12) - Changing look and feel does not refresh existing views

#### Known Problems ####
  * Some warnings and validations are missing from the Character Editor.
  * New characters are created as unordered multistate by default.
  * States cannot be reordered using drag and drop in the Tree View.
  * The controlling attributes tab on the character editor is not complete.
  * The image related functionality on the Item editor is not complete.

## Intkey Release Notes ##
### New Features ###
  * CHANGE directive implemented
  * USE directive completed

Intkey can now be used to conduct a basic identification of a specimen. Character values can be entered/changed by double clicking on items in the _Available Characters_ and _Used Characters_ panes and following the prompts. Alternatively, _USE_ and _CHANGE_ commands can be entered into the command bar at the bottom of the application window.


---


# Milestone 3 #

## General Release Notes: ##

The Milestone 3 (M3) release has progressed work on the DELTA Editor, Intkey and CONFOR.

There is now a Czech language translation available. (thanks to Michal Manas).

The DELTA software is considered an alpha release.  Please make a copy of your existing DELTA data sets before accessing them with any of the Open DELTA programs.

## DELTA Editor Release Notes: ##

#### New Features ####
  * Items can be added / inserted / deleted using a right click menu on the tree and grid views.
  * Items can be reordered using drag and drop on the tree and grid views.
  * The attribute editor now supports undo and the behaviour of the enter key can be modified to change the selected item or character.
  * "Simple" attributes can now be edited inline in the tree view.
  * "Simple" multistate attributes can now be edited inline in the tree view or using the check boxes in the attribute editor.
  * Some tidy up of the look and feel (grid row heights, selection highlighting, single click editing).
  * Validation has been added to the item editor.
  * Some support for displaying and editing images to items has been added.  This remains incomplete.
  * The save menu item is only enabled when a dataset has been modified.

#### Known Problems ####
  * The image related functionality on the Item editor is only partially complete.


## Intkey Release Notes ##
### New Features ###

  * DEFINE CHARACTERS directive implemented.
  * RESTART directive partially implemented - optional switches not handled.
  * USE directive partially implemented - users can add values for characters through the command bar or by double-clicking on entries in the "Available Characters" pane. The "Available Characters" and "Used Characters" panes are updated when this is done.
  * Various user interface tweaks including the additional of the toolbar buttons to the "Available Characters" and "Available Taxa" panes.

## CONFOR Release Notes ##

#### New Features ####
  * The OMIT TYPESETTING MARKS directive is now supported.
  * The TYPESETTING MARKS directive is now supported.

#### Bug Fixes ####
  * [ISSUE 10](https://code.google.com/p/open-delta/issues/detail?id=10) - default values for directives are now supported, but have not yet been specified for all directives.

#### Known Problems ####
  * Error detection and validation of DELTA files is not fully implemented.  Any errors encountered are likely to abort the program.


---



# Milestone 2 #

## General Release Notes: ##

The Milestone 2 (M2) release has progressed work on the DELTA Editor and produced initial prototypes of
the CONFOR and Intkey programs.
As per Milestone 1, M2 is an alpha release and should not be used with production data.  You should make
a copy of your existing DELTA data sets before accessing them with any of the
Open DELTA programs.

To reflect the additional programs included in M2, the packaging of the downloads have been modified:
  * The installers now install the programs: Intkey, CONFOR and the DELTA Editor.
  * The jar file distribution has been replaced with a .zip/.tar.gz distribution.
  * The JNLP launcher has been removed.



## DELTA Editor Release Notes: ##

### New Features: ###

  * Implicit values are displayed correctly ([ISSUE 4](https://code.google.com/p/open-delta/issues/detail?id=4))
  * Characters made inapplicable are displayed correctly
  * New datasets can be created
  * DELTA text files can be imported into a new data set (only).
  * The user will be asked to if they want to save before closing a modified data set.
  * The Item/Taxon editor has been created, but item images are not yet supported.
  * Better support for Mac OS 10 menu bar.
  * Enter/shift Enter key in attribute editor advances/reverses selection. ([ISSUE 9](https://code.google.com/p/open-delta/issues/detail?id=9))
  * Special characters can be entered using key combinations as per the existing editor
  * The recent file list appears on the File menu.
  * Copy/Paste menus implemented. ([ISSUE 5](https://code.google.com/p/open-delta/issues/detail?id=5))
  * A new "Copy All" feature introduced to allow easy copy and paste into Excel. ([ISSUE 5](https://code.google.com/p/open-delta/issues/detail?id=5))

### Bug Fixes: ###

  * [ISSUE 3](https://code.google.com/p/open-delta/issues/detail?id=3) - Some non-letter symbols not displayed correctly in states
  * [ISSUE 6](https://code.google.com/p/open-delta/issues/detail?id=6) - RTF formatting not displayed correctly
  * [ISSUE 8](https://code.google.com/p/open-delta/issues/detail?id=8) - text not scrolling

## CONFOR Release Notes: ##

### New Features: ###

  * Supports the following directives:
    * NUMBER OF CHARACTERS
    * MAXIMUM NUMBER OF STATES
    * MAXIMUM NUMBER OF ITEMS
    * CHARACTER TYPES
    * NUMBERS OF STATES
    * CHARACTER LIST
    * ITEM DESCRIPTIONS
    * PRINT FILE
    * LISTING FILE
    * IMPLICIT VALUES

  * Partial support for:
    * TRANSLATE INTO (only NATURAL LANGAUGE currently supported, and only plain text output is produced)
    * PRINT WIDTH
    * OMIT REDUNDANT VARIANT ATTRIBUTES
    * INSERT REDUNDANT VARIANT ATTRIBUTES
    * INSERT IMPLICIT VLAUES


### Known Problems: ###

  * Error detection and reporting is not fully implemented. (i.e. errors will normally result in program termination).
  * Some directives (not listed above) are partially implemented and may produce incorrect results.
  * Typesetting marks are omitted regardless of whether the OMIT TYPESETTING MARKS directive is used.



## IntKey Release Notes: ##

### New Features: ###

  * Intkey data files can be opened and the characters and taxa displayed.
  * The advanced mode command interpreter functionality has been implemented.




---




# Milestone 1 #

The goals of milestone 1 of the Open DELTA project are:

  * To formally establish the project, including tool chain and build environment
  * To demonstrate and experiment with packaging and publishing options
  * Investigate and address some of the risk issues identified during the scoping exercise:
    * Java port of the slot file (`*`.dlt) backing store
    * RTF processing and editing
    * Context sensitive help system and "Tooltip" style assistance
    * Internationalization and Localization
    * Potential performance issues, including startup time, User Interface responsiveness and general processing performance.


For installation instructions see the [Installation Instructions](InstallationInstructions.md).

**Please note:** Milestone 1 is an alpha release, and should not be used with production data. You should make a copy of your existing Delta data sets before opening them with this version.


## DELTA Editor ##

### New Features ###

  * Existing .dlt files can be opened and viewed using the DELTA Editor. Both grid view and tree view attribute editors have been implemented.
  * Save and Save As are implemented
  * Simplified Chinese and Spanish languages. The current locale and language are taken from operating system settings, but can be overridden for demonstration purposes by supplying command line arguments when launching the jar file:
> To view Chinese captions, for example, launch the editor from the command line with the following command (assuming the jar file is in the current working directory):
```
   java -Duser.region=CN -Duser.language=zh -jar delta-editor-1.0-M1-with-dependencies.jar
```

> To view Spanish captions, use:
```
   java -Duser.region=ES -Duser.language=es -jar delta-editor-1.0-M1-with-dependencies.jar
```

**Note** Chinese and Spanish localisations have been chosen arbitrarily for demonstrative purposes. Please contact the project team should you be willing and able to contribute additional language translations.

  * Existing Taxon attributes can be edited, although state check boxes are currently read only
  * Help system (including context sensitive help) demonstration
  * Windows installer with or without a bundled Java Runtime Environment
  * Cross platform support.  Currently the application has been tested on Windows XP, Windows 7, Mac OS 10.6.6 (x86) and Ubuntu Linux.

### Bug Fixes ###
  * [Issue 1](https://code.google.com/p/open-delta/issues/detail?id=1): 	Attached dlt file causes issues

### Known Problems ###
  * The Save menu item is not enabled only when a file has been modified.
  * Features are still missing from read only mode, including Controlling/Controlled Attributes, Implicit values, Mandatory values.
  * Items and Characters cannot be edited, added, re-ordered or deleted.