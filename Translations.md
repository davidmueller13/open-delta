The text displayed in the open-delta software is stored separately in Java properties files.  A brief overview of the file format is:

  1. Lines starting with a `#` are comments and do not need to be translated.
  1. Blank lines are ignored by the program.
  1. Lines that do need to be translated have the form: <`key`>` = `<`value`>`  (e.g. DeltaEditor.warningTitle = WARNING - Not for production use).
  1. Lines that specify location of icon files, (e.g. "firstTaxon.Action.icon = icons/icon.png"), accelerator keys (e.g. "saveDirectiveFile.Action.accelerator = control S") or numeric values (e.g. blockingDialogTimer.delay = 500) do not need to be translated.
  1. The & characters in some of the text identify characters for menu shortcuts.
  1. The %s characters in some of the text are used for text substitution by the program.

To provide a translation, create a file that contains all of the same keys as the english version (see below) but uses the target language as the value of each key.

As an example, here is a sample from the English and Czech language files:
### English version ###

```
DeltaEditor.windowTitleWithoutFilename = DELTA - DEscription Language for TAxonomy (prototype)
DeltaEditor.windowTitleWithFilename = DELTA - DEscription Language for TAxonomy (prototype) - %s
DeltaEditor.warning = This software is not for production use.  Please backup your data files before opening them using this application.
DeltaEditor.warningTitle = WARNING - Not for production use
DeltaEditor.closeWithoutSavingMessage = The document has been changed.\nDo you want to save the changes?
DeltaEditor.newDataSetName = New Dataset
```

### Czech version ###
```
DeltaEditor.windowTitleWithoutFilename = DELTA - DEscription Language for TAxonomy (prototyp)
DeltaEditor.windowTitleWithFilename = DELTA - DEscription Language for TAxonomy (prototyp) - %s
DeltaEditor.warning = Tento program není určen pro pracovní použití. Před používáním programu si zálohujte svá data.
DeltaEditor.warningTitle = Upozornění - není určeno pro pracovní použití
DeltaEditor.closeWithoutSavingMessage = Tento dokument byl změněn.\nChcete uložit změny?
DeltaEditor.newDataSetName = Nová databáze

```

## English Text ##
### DELTA Editor ###

```
#
AboutBox.windowTitle = About DELTA
AboutBox.applicationTitle = DELTA Editor

Application.id = DeltaViewer
Application.title = DELTA - DEscription Language for TAxonomy
Application.version = 1.0-RC1
Application.vendor = open-delta
Application.vendorId = open-delta
Application.homepage = http://code.google.com/p/open-delta/
Application.description = Allows creation and editing of DELTA data sets
Application.lookAndFeel = system
Application.shortName = DELTA Editor

#Delta Editor Main Window

DeltaEditor.windowTitleWithoutFilename = DELTA - DEscription Language for TAxonomy
DeltaEditor.windowTitleWithFilename = DELTA - DEscription Language for TAxonomy  - %s
DeltaEditor.warning = Please backup your data files before opening them using this application.
DeltaEditor.warningTitle = WARNING - Backup Recommended
DeltaEditor.closeWithoutSavingMessage = The document has been changed.\nDo you want to save the changes?
DeltaEditor.unableToCloseMessage = Unable to close one or more windows - please fix any validation errors before continuing
DeltaEditor.newDataSetName = New Dataset
DeltaEditor.fileExists = The file name you have selected already exists.  Please use a unique file name.

# File menu
mnuFile.text = &File

newFile.Action.text = New Dataset
newFile.Action.shortDescription = Creates a new Dataset
newFile.Action.icon = icons/page_add.png

loadFile.Action.text = Open Dataset...
loadFile.Action.accelerator = control O
loadFile.Action.icon = icons/folder_page.png

closeFile.Action.text = Close Dataset
closeFile.Action.shortDescription = Closes the current Dataset

saveFile.Action.text = Save Dataset
saveFile.Action.shortDescription = Saves the contents of the current data set
saveFile.Action.accelerator = control S
saveFile.Action.icon = icons/disk.png

saveAsFile.Action.text = Save Dataset &As...
saveAsFile.Action.shortDescription = Saves the contents of the current data set into a new file


importDirectives.Action.text = &Import directives...
importDirectives.Action.shortDescription = Imports DELTA directives into the current Dataset

exportDirectives.Action.text = &Export directives...
exportDirectives.Action.shortDescription = Exports the current Dataset to a set of DELTA directives files

exitApplication.Action.text = E&xit

# Edit menu
mnuEdit.text = &Edit
copy.Action.text = &Copy
selectAll.Action.text=&Select All
selectAll.Action.accelerator = control A
copySelectedWithHeaders.Action.text = Copy Selected Including Headers
copySelectedWithHeaders.Action.accelerator = control H

# Search menu
mnuSearch.text = &Search
find.Action.text = Find/Replace...
find.Action.accelerator = control F
findNext.Action.text = Find Next
findNext.Action.accelerator = F3

# View menu
mnuView.text = &View
newTreeView.Action.text = Attribute editor (&Trees)
newTreeView.Action.shortDescription = Creates and displays a tree view attribute editor based on the currently selected data set

newGridView.Action.text = Attribute editor (&Grid)
newGridView.Action.shortDescription = Creates and displays a tree view attribute editor based on the currently selected data set

viewCharacterEditor.Action.text = &Character editor
viewCharacterEditor.Action.shortDescription = Allows the currently selected character to be edited
viewCharacterEditor.Action.enabled = false

viewTaxonEditor.Action.text = &Taxon editor
viewTaxonEditor.Action.shortDescription = Allows the currently selected taxon to be edited
viewTaxonEditor.Action.enabled = false

viewActionSets.Action.text = &Action sets
viewActionSets.Action.enabled = false

viewImageSettings.Action.text = &Image settings
viewImageSettings.Action.enabled = false

# Window menu
mnuWindow.text = &Window

tileFrames.Action.text = Tile Frames
tileFramesHorizontally.Action.text = Tile Frames Horizontally
cascadeFrames.Action.text = Cascade Frames
arrangeIcons.Action.text = Arrange Icons
closeAllFrames.Action.text = Close All
chooseFont.Action.text = Set Font

mnuLF.text = Look & feel

metalLookAndFeel.Action.text = Metal
systemLookAndFeel.Action.text = System
nimbusLookAndFeel.Action.text = Nimbus

# Help menu
mnuHelp.text = &Help

mnuItHelpContents.text = Contents...

mnuItHelpOnSelection.text = Select Component

openAbout.Action.text = About

#Matrix Viewer
MatrixViewer.windowTitle = Matrix Viewer - %s
ItemColumnModel.columnName =  

#Tree Viewer
TreeViewer.windowTitle = Tree Viewer - %s 

#Attribute Editor
advanceItem.Action.text =
advanceItem.Action.shortDescription = Enter will advance the selection to the next Item
advanceItem.Action.icon = icons/item_advance.png

advanceCharacter.Action.text =
advanceCharacter.Action.shortDescription = Enter will advance the selection to the next Character
advanceCharacter.Action.icon = icons/character_advance.png

advanceNone.Action.text =
advanceNone.Action.shortDescription = Enter will not advance the selection
advanceNone.Action.icon = icons/none_advance.png


# Validation errors
errors.EAP_BAD_STATE_NUMBER = An invalid state number was detected near character position %d
errors.EAP_UNMATCHED_CLOSEBRACK = An unmatched closing bracket was found near character position %d
errors.EAP_MISSING_CLOSEBRACK = An opening bracket did not have a matching closing bracket near character position %d
errors.EAP_BADATTR_SYMBOL = A bad or invalid symbol was found near character position %d
errors.EAP_IS_INAPPLICABLE = This attribute has been made inapplicable, but an attempt to encode data was found near character position %d
errors.EAP_CHARTYPE_UNDEFINED = The character type has not yet been defined, so no encoding should be made near character position %d
errors.EAP_EXCLUSIVE_ERROR = This exclusive multistate character has been encoded with more that one state near character position %d
errors.EAP_BAD_NUMERIC_ORDER = Numeric values not in ascending order near character position %d
errors.EAP_BAD_RTF = RTF markup appears to have been used outside a text character or comment near character position %d
errors.EAP_BAD_RTF_BRACKET = RTF markup appears to contain unmatched brackets near character position %d
errors.EAP_BAD_SLASH = A slash followed by white space is not permitted in this context near character position %d
errors.EMPTY_ITEM_DESCRIPTION = The item name field is blank.  Please enter a name for this item.
errors.COMMENT_MISMATCH = There is a mismatched comment symbol at position %d 	
errors.UNRECOGNISED_DIRECTIVE = Unrecognised directive at postition %s: %s
errors.DIRECTIVE_PARSE_ERROR = Error at position %s: %s
	
# Import / Export Dialog.
okPressed.Action.text = OK
cancelPressed.Action.text = Cancel
directorySelected.Action.text = Chan&ge...
updateFilter.Action.text = ...
ImportDialog.dialogTitle = Select DELTA files to import
ImportDialog.directoryChooserTitle = Browse for import directory
ImportDialog.specificationsPanelTitle = Specifications file
ImportDialog.charsPanelTitle = Characters file
ImportDialog.itemsPanelTitle = Items file
ImportDialog.otherPanelTitle = Other directives files
ImportDialog.possiblePanelTitle = Possible directives files
ImportDialog.directiveTypePanelTitle = Directive type
ImportDialog.filterDialogTitle = Change File Filter
ImportDialog.filterDialogMessage = Enter wildcards patterns matching those filenames you wish to exclude.\n\nMultiple patterns should be separated with a semicolon (;) There should be no spaces between patterns.\n
ImportDialog.directoryLabel = Import directory:

closePressed.Action.text = Close

ExportDialog.dialogTitle = Select DELTA files to export
ExportDialog.directoryChooserTitle = Browse for export directory
ExportDialog.specificationsPanelTitle = Specifications file
ExportDialog.charsPanelTitle = Characters file
ExportDialog.itemsPanelTitle = Items file
ExportDialog.otherPanelTitle = Other directives files
ExportDialog.possiblePanelTitle = Do not export
ExportDialog.directiveTypePanelTitle = Directive type
ExportDialog.directoryLabel = Export directory:
ExportDialog.lineSeparator.system.default = System default
ExportDialog.lineSeparator.unix = Unix
ExportDialog.lineSeparator.windows = Windows (legacy)

directiveTypeConfor.text = Confor
directiveTypeIntkey.text = Intkey
directiveTypeDist.text = Dist
directiveTypeKey.text = Key
changeImportDirectory.Action.text=Change...
changeExportDirectory.Action.text=Change...
exportLineSeparator.text= Line separator
importExportExcludeLabel.text = Exclude

# Import Status Dialog
importStatusDialog.title = Importing DELTA data set...
exportStatusDialog.title = Exporting DELTA data set...
importExportFinished.Action.text = Done
cancelImportExport.Action.text = Cancel
continueImportExport.Action.text = Continue
pauseOnErrors.Action.text = Pause on error

importHeading.text = Importing DELTA data set
exportHeading.text = Exporting DELTA data set
lastShowDirectiveLabel.text = Text from last *SHOW directive:
importExportStatisticsLabel.text = Statistics
importExportTotalLabel.text = Total
importExportDirectivesLabel.text = Directives:
importExportErrorsLabel.text = Errors:
importExportCurrentLabel.text = Current:
importExportCurrentFileLabel.text = Current file:
importDirectoryLabel.text = Import directory:
exportDirectoryLabel.text = Export directory:
importExportHeadingLabel.text = Heading:

importReport.heading = DELTA - Import log
exportReport.heading = DELTA - Export log
importExportReport.dataSetLabel = Dataset: %s
importReport.directoryLabel = Import directory: %s
exportReport.directoryLabel = Export directory: %s
importReport.timeLabel = Import began %s
exportReport.timeLabel = Export began %s
importExportReport.directivesFileLabel = Directives file: \\b %s \\b0
importExportReport.fileTypeLabel = File type %s
importReport.success = Import succeeded.
exportReport.success = Export succeeded.
importReport.failure = Import \\b failed \\b0 .
exportReport.failure = Export \\b failed \\b0 .
importReport.finished = Import finished
exportReport.finished = Export finished
importReport.failureMessage = \\b %s files failed to import correctly \\b0
exportReport.failureMessage = \\b %s files failed to export correctly \\b0


# Item Edit Dialog.
ItemEditor.titleSuffix = Item Edit - %s
ItemEditor.editTaxonLabelText = Edit taxon name:
ItemEditor.selectTaxonLabelText = Select taxon:
taxonNumberLabel.text = Taxon Number:
selectItemByName.Action.text = Select
editTaxonNameLabel.text = Edit taxon name:
itemVarianceChanged.Action.text = Treat as &Variant
itemEditDone.Action.text = D&one
helpWithTaxonEditorButton.text = Help

# Item menu actions
editItem.Action.text = Edit selected item
addItem.Action.text = Append new item to end of list
addItem.Action.accelerator = alt insert
insertItem.Action.text = Insert new item as this position
insertItem.Action.accelerator = insert
deleteItem.Action.text = Delete selected item
deleteItem.Action.accelerator = delete
cancel.Action.text = Cancel

# Character Edit Dialog.
CharacterEditor.titleSuffix = Character edit - %s
CharacterEditor.editCharacterLabelText = Edit feature description:
CharacterEditor.selectCharacterLabelText = Select character:
characterNumberLabel.text = Character Number:
characterTypeLabel.text = Character Type:
lblEditCharacterName.text = Edit feature description:
selectCharacterByName.Action.text = Select
characterEditDone.Action.text = D&one
mandatoryChanged.Action.text = Mandatory
exclusiveChanged.Action.text = Exclusive
characterNotesLabel.text = Edit character notes 
characterUnitsLabel.text = Units:
states.tab.title = States
units.tab.title = Units
notes.tab.title = Notes
images.tab.title = Images
controls.tab.title = Controls
controlledBy.tab.title = Controlled by
moveToControlledList.Action.icon = icons/arrow_left.png
moveFromControlledList.Action.icon = icons/arrow_right.png
moveToInapplicableList.Action.icon = icons/arrow_left.png
moveFromInapplicableList.Action.icon = icons/arrow_right.png
definedByStatesLabel.text = Defined by states:
makesInapplicableLabel.text = Makes inapplicable:
characterListLabel.text = Character list:
controllingAttributeLabel.text = Controlling attribute:
redefineControllingAttribute.Action.text = Redefine
madeInapplicableByLabel.text = Made inapplicable by:
definedControllingAttributesLabel.text = Defined controlling attributes:
defineControllingAttributeButton.editText = Redefine
defineControllingAttributeButton.newText = Define

#Character menu actions
editCharacter.Action.text = Edit selected character
addCharacter.Action.text = Append new character to end of list
addCharacter.Action.accelerator = alt insert
insertCharacter.Action.text = Insert new character as this position
insertCharacter.Action.accelerator = insert
deleteCharacter.Action.text = Delete selected character
deleteCharacter.Action.accelerator = delete
expandAll.Action.text = Expand all
collapseAll.Action.text = Collapse all


# Image details tab (Item and Character editor)
displayImage.Action.text = Display
addImage.Action.text = Add...
deleteImage.Action.text = Delete
deleteSound.Action.icon = icons/cross.png
deleteSound.Action.shortDescription = Deletes the selected sound
playSound.Action.icon = icons/play.png
playSound.Action.shortDescription = Plays the selected sound
addSound.Action.text = Insert...
addSound.Action.shortDescription = Select a sound to add to the image
confirmNotOnImagePath.title = Not on image path
confirmNotOnImagePath.message = You have selected a file which is not on the current image path. \nYou may still use this file, but its name will be saved as a full path specification.\nDo you really want to use this file?
confirmDuplicateFilename.title = Duplicate file exists
confirmDuplicateFilename.message = A file with the same name exists already on the image path.  \nYou may still use this file, but its name will be saved as a full path specification.\nDo you really want to use this file? 
imageDetailsSubjectLabel.text = Subject text:
imageDetailsDevelopersNotesLabel.text = Developer's notes:
imageDetailsSoundTitle = Image Sound
imageDetailsImageFilesLabel.text = Image files:

# State editor tab
stateEditor.definedStatesLabel.text = Defined states:
stateEditor.stateDescriptionLabel.text = Edit state description:
addState.Action.text = Add
deleteState.Action.text = Delete
toggleStateImplicit.Action.text = Implicit

# Dialog messages
deleteImage.title = Delete Image?
deleteImage.message = Please confirm that you really wish to delete this image
deleteItem.title = Delete Taxon?
deleteItem.message = Please confirm that you really wish to delete this taxon:\n%s
errorLoadingImage.title = Image not found
errorLoadingImage.message = Unable to locate the image: %s
errorPlayingSound.title = Unable to play sound
errorPlayingSound.message = Unable to play the sound file : %s
uncodedMandatoryItems.title = Uncoded items
uncodedMandatoryItems.message = Not all items have been encoded for this "mandatory" character!
cannotMakeCharacterExclusive.title=Cannot make Character exclusive
cannotMakeCharacterExclusive.message=Items are currently encoded for this character as having multiple states.
deleteState.title = Delete state?
deleteState.message = Please confirm that you really wish to delete this state:\n%s
deleteCharacter.title = Delete character?
deleteCharacter.message = Please confirm that you really wish to delete this character:\n%s
deleteDependency.title = Delete Controlling Attribute?
deleteDependency.message = There are no states selected. Do you wish to DELETE this controlling attribute?\n%s
saveBeforeClosing.title = Save changes?
saveBeforeClosing.message = The data has been edited.  Save changes?
deleteOverlay.title = Delete overlay?
deleteOverlay.message = Please confirm that you really wish to delete all image overlays
controllingAttributeLabelPrompt.title = Attribute label
controllingAttributeLabelPrompt.message = Enter a label for this controlling attribute.\n\nBy default, a label is generated from this character's feature and state descriptions.\n
circularDependencyError.title = Error adding dependency
circularDependencyError.message = Unable to add this dependency, since it would (perhaps indirectly) make a character dependent on itself.

findCharacter.title = Find Character
findItem.title = Find Item

# Character types for display
characterType.Text = Text
characterType.IntegerNumeric = Integer numeric
characterType.OrderedMultiState = Ordered multistate
characterType.UnorderedMultiState = Unordered multistate
characterType.RealNumeric = Real numeric

#Image/Overlay Editor
subjectMenu.text = Subject
controlMenu.text = Control
windowMenu.text = Window
# Control Menu
nextImage.Action.text = Next image
previousImage.Action.text = Previous image
nextCharacterWithImage.Action.text = Next character with image
previousCharacterWithImage.Action.text = Previous character with image
nextItemWithImage.Action.text = Next item with image
previousItemWithImage.Action.text = Previous item with image
showCharacterDetails.Action.text = Full text
showImageNotes.Action.text = Notes
showCharacterNotes.Action.text = Notes
# Window Menu
toggleScaling.Action.text = Scaled
toggleHideText.Action.text = Hide text
toggleHideHotSpots.Action.text = Hide hotspots
replaySound.Action.text = Replay sound
replayVideo.Action.text = Replay video
reloadImage.Action.text = Reload image
fitToImage.Action.text = Fit to image
fullScreen.Action.text = Full Screen
togglePreviewMode.Action.text = Preview mode
aboutImage.Action.text = About image...
closeImage.Action.text = Close

#Overlay popup menu
editSelectedOverlay.Action.text=Edit overlay
deleteSelectedOverlay.Action.text=Delete overlay
deleteAllOverlays.Action.text=Delete all overlays
displayImageSettings.Action.text=Image Settings...
cancelPopup.Action.text=Cancel 
overlayPopup.stackOverlayMenu=Stack overlay
overlayPopup.insertOverlayMenu=Insert overlay
overlayPopup.alignButtonsMenu=Align buttons	

stackSelectedOverlayHigher.Action.text=Higher
stackSelectedOverlayLower.Action.text=Lower
stackSelectedOverlayOnTop.Action.text=On top
stackSelectedOverlayOnBottom.Action.text=On bottom
		
addTextOverlay.Action.text=Text
addItemNameOverlay.Action.text=Item (taxon) name
addAllUsualOverlays.Action.text=All usual overlays
addFeatureDescriptionOverlay.Action.text=Feature description
addStateOverlay.Action.text=State
addHotspot.Action.text=Hotspot
addOkOverlay.Action.text=Ok
addCancelOverlay.Action.text=Cancel
addNotesOverlay.Action.text=Notes
addImageNotesOverlay.Action.text=Image notes
		
useDefaultButtonAlignment.Action.text=Use default
alignButtonsVertically.Action.text=Vertically
alignButtonsHorizontally.Action.text=Horizontally
dontAlignButtons.Action.text=Unaligned

overlayEditDialog.title=Modify overlay
okOverlayChanges.Action.text = Ok
applyOverlayChanges.Action.text = Apply
cancelOverlayChanges.Action.text = Cancel
overlayEditStateNumberLabel.text=State number:
overlayEditAdditionalTextLabel.text=Additional text:
overlayEditCentreInBoxLabel.text=Centre in box:
overlayEditIncludeCommentsLabel.text=Include comments:
overlayEditOmitDescriptionLabel.text=Omit description:
overlayEditUseIntegralHeightLabel.text=Use integral height:
overlayEditDimensionsLabel.text=Dimensions
overlayEditImageUnitsLabel.text=Image units
overlayEditLinesLabel.text=Lines
overlayEditXLabel.text=X:
overlayEditYLabel.text=Y:
overlayEditWidthLabel.text=W:
overlayEditHeightLabel.text=H:

hotSpotEditDialog.title=Modify hostspot (State: %s)
popupSelected.Action.text = Pop up
useCustomColourSelected.Action.text=Use custom colour
displayColourChooser.Action.text = Choose colour
ellipseSelected.Action.text = Ellipse
rectangleSelected.Action.text = Rectangle

hotSpotColourChooser.title=Choose colour

#Image settings dialog
imageSettingsDialog.title = Image Settings - %s
okImageSettingsChanges.Action.text = Ok
applyImageSettingsChanges.Action.text = Apply
cancelImageSettingsChanges.Action.text = Cancel
addToImagePath.Action.text = Browse
imagePathFileChooser.title=Browse for image path
imageSettingsCentreInBox.text = Centre in box
imageSettingsIncludeComments.text = Include comments
imageSettingsOmitDescription.text = Omit description
imageSettingsUseIntegralHeight.text = Use integral height
imageSettingsHotspotsPopUp.text = Hotspots pop up
imageSettingsCustomPopupColour.text = Custom popup colour
imageSettingsButtonAlignment.text = Button alignment
imageSettingsFontName.text=	Font
imageSettingsFontSize.text = Size
imageSettingsFontBold.text = Bold
imageSettingsFontItalic.text = Italic
imageSettingsDefaultFont.text = Default:
imageSettingsFeatureFont.text = Feature:
imageSettingsButtonFont.text = Button:
imageSettingsSample.text = Sample:
imageSettingsSaveSampleAs.text = Save sample as comment
imageSettingsOverlayDefaults.title = Defaults for new overlays
imageSettingsImagePath.title = Image path	
imageSettingsOverlayFonts.title = Overlay font settings

#Button alignment combo box
imageSettingsDialog.buttonAlignment.noAlign = Don't align
imageSettingsDialog.buttonAlignment.alignVertically = Align vertically
imageSettingsDialog.buttonAlignment.alignHorizontally = Align horizontally

#Action sets dialog
actionSetsDialogView.title = Action Sets - %s
actionSetsActionSetsLabel.text = Action set: 
runDirectiveFile.Action.text = Run
editDirectiveFile.Action.text = Edit
deleteDirectiveFile.Action.text = Delete
doneWithActionSets.Action.text = Done
addDirectiveFile.Action.text = Add
actionSetsActionColumn = Action
actionSetsFileNameColumn = File name
actionSetsImportExportColumn = Imported / exported
actionSetsNoDescription = [Not specified]
actionSetsDefaultConforAction = Directives for program CONFOR
actionSetsDefaultIntkeyAction = Directives for program INTKEY
actionSetsDefaultDistAction = Directives for program DIST
actionSetsDefaultKeyAction = Directives for program KEY
actionSetsSpecsFileAction = (Dataset specifications)
actionSetsCharsFileAction = (Character list)
actionSetsItemsFileAction = (Item descriptions)
directiveFileNamePrompt.title = New Directive File
directiveFileNamePrompt.message = Please enter a name for the new directives file

applyChanges.Action.icon = icons/save.png
applyChanges.Action.shortDescription = Apply changes


deleteDirectiveFile.title = Delete directives file
deleteDirectiveFile.message = Are you SURE you want to delete this set of directives? (%s) 
errorRunningDirectiveFile.title = Unable to run program!
errorRunningDirectiveFile.message = Unable to run program for file: %s

directiveFileEditor.title=
saveDirectiveFile.Action.text = Save Directives File
saveDirectiveFile.Action.shortDescription = Saves the contents in the directives file editor.
saveDirectiveFile.Action.accelerator = control S
saveDirectiveFile.Action.icon = icons/disk.png

confirmExport.title = Query Export
confirmExport.message = Do you wish to export your DELTA directives associated with this program?\n\nYou will need to export these files if you have not done so previously, or if you have made changes to the data since the files were last exported.

# Results of running a directives file
runDirectivesProgressDialog.title = Actions - Results
runDirectivesProgressLabel.text = Progress
runDirectivesOutputFilesLabel.text = Output Files
runDirectivesOutputLabel.text = Output
runDirectivesFileCloseButton.text = Close
runDirectivesProgressDialog.fileNameColumn.text = File name
runDirectivesProgressDialog.viewButton.text = View...

# Warning when importing an Items file into a dataset and a duplicate item is encountered.
duplicateItemWarningDialog.title = Duplicate item encountered
overwriteItem.Action.text = Overwrite
retainItem.Action.text = Retain
actionDescriptionLabel.text = Please select the way in which you want this item to be treated
retainButtonLabel.text = Do NOT replace the description of this item
overwriteButtonLabel.text = Replace the description of this item
applyToAllCheckBox.text = Apply this choice to ALL duplicate items encountered in this file
duplicateItemWarningDescription.text = An item which is being imported has the same name as an item already in your database.  You may choose either to use the newly imported description to replace the current description, or to ignore the imported description and leave the current description unchanged.  You may also indicate whether to apply this choice to all subsequent duplicate items in this file.
#====== DeltaFileLoader.properties ======
title = Loads a DELTA file
description = A task that loads a DELTA file and creates a tree view
message = Loading file...
loading = Loading %s

BlockingDialog.title = Loading File...

# Wait half a second before displaying the dialog.
BlockingDialogTimer.delay = 500
BlockingDialog.cancelButton.text = Cancel

#====== CopySelectedTask.properties ============
title = Copy selected data
description = Copies selected grid data to the system clipboard
copying = Copying data to clipboard

BlockingDialog.title = Please wait

# Wait a second before displaying the dialog.
BlockingDialogTimer.delay = 500
BlockingDialog.cancelButton.text = Cancel

#====== CopySelectedWithHeadersTask.properties =======
title = Select All
description = Select all cells in the grid view

BlockingDialog.title = Please wait

# Wait a second before displaying the dialog.
BlockingDialogTimer.delay = 500
BlockingDialog.cancelButton.text = Cancel

#====== SelectAllTask.properties =======
title = Select All
description = Select all cells in the grid view

BlockingDialog.title = Please wait

# Wait a second before displaying the dialog.
BlockingDialogTimer.delay = 500
BlockingDialog.cancelButton.text = Cancel

#==============Validation messages==========
error.1 = Value constraints must be specified for all included RN characters.
error.2 = Directive out of order.
error.3 = Equivalent directive already used.
warning.3 = Equivalent directive already used.
error.4 = Errors in previous directives.
error.5 = Character %d has more than 32 states. It has been excluded.
error.6 = Sequence number or # expected.
error.7 = Slash missing after previous character or state description.
error.8 = Character number terminated by full stop expected.
error.9 = State number terminated by period+space expected.
error.10 = Missing data.
error.11 = Illegal symbol - integer expected.
error.12 = Illegal symbol.
error.1200 = Illegal symbol.  Expected %s. Found %s.
error.13 = Character number greater than specified maximum (%d).
error.14 = Character number out of order.
error.15 = State number out of order.
error.16 = Number of states not as specified (%d).
error.17 = Number of states in preceding character not as specified (%d).
error.18 = Preceding numeric character has too many "units".
error.19 = Preceding text character should not have "units" or "states".
error.20 = Error opening file SCRATCH%d.
error.21 = Invalid integer.
error.22 = Illegal value.
error.23 = Invalid real number.
error.24 = Only RN characters can be scaled.
error.25 = All characters excluded.
error.26 = All items excluded.
error.27 = Character %d is not integer.
error.28 = Word too long - truncated.
error.29 = Not all vocabulary words specified.
error.30 = Character %d must be numeric.
error.31 = Illegal delimiter.
error.32 = Previous item is empty.
error.33 = Only multistate characters allowed.
error.34 = State specified previously.
error.35 = Not enough space in data buffer. The length is %d.
error.36 = The directive(s) %s must precede the %s directive, or any other directive which may require this information.
error.37 = Invalid taxon number.
error.38 = # expected.
error.39 = Value already specified.
error.40 = Not all states specified.
error.41 = Invalid specification.
error.42 = Only RN characters allowed.
error.43 = Illegal combination of values.
error.44 = RN characters have been scaled.
error.45 = Processing of items stopped after item %d.
error.46 = Number of items exceeds specified maximum.
error.47 = Items excluded.
error.48 = Characters excluded from items.
error.49 = Typesetting marks omitted from items.
error.50 = Character descriptions required.
error.51 = Characters and states have not been specified.
error.52 = No previous file.
error.53 = 
error.54 = Invalid file name.
error.55 = File does not exist.
error.56 = Incompatible with previous use of this file.
error.57 = File is inaccessible or is a directory.
error.58 = File cannot be opened: %s
error.59 = Heading too long.
error.60 = Excess data, or missing delimiter.
error.61 = Value less than permitted minimum. (%s)
error.62 = Value greater than permitted maximum. (%s)
error.63 = Character number cannot be determined because of a previous error.
error.64 = Character %d has already been specified.
warning.64 = Character %d has already been specified.
error.65 = Value out of order.
error.66 = Integer number required.
error.67 = Value outside permitted range.
error.68 = Too many values.
error.69 = Not all characters specified.
error.70 =        Missing Characters -
error.71 = Real number required.
error.72 = Full stop missing.
error.73 = Missing character number.
error.74 = Illegal symbol. Character number expected.
error.75 = Invalid character number.
error.76 = Insufficient storage for headings.
error.77 = More than %d  words skipped without finding a legal delimiter.
error.78 = ****** More than 100 errors.
error.79 = *** Number of warnings = %d  
error.80 = Normal Termination.
error.81 = ****** Number of errors = %d  
error.82 = ****** Abnormal Termination.
error.83 = Too many files in use.
error.84 = Output field too short.
error.85 = Not enough storage. %d  locations required, %d  available.
error.86 = Record sequence error.
error.87 = Invalid or missing record sequence number.
error.88 = Input line too long.
error.89 = Output files - 
error.91 = Closing bracket is missing.
error.92 = Dependency error. Item %s - characters \:\
error.93 = Controlling character not applicable. Item %s  - characters \:\
error.94 = Controlling character not coded. Item %s - characters \:\
error.95 = Mandatory character %d  not coded. Item ?.
error.96 = Missing or invalid delimiter.
error.97 = Maximum of five values allowed.
error.98 = Slash missing after previous item name.
error.99 = Invalid control phrase.
error.100 = Unmatched closing bracket.
error.101 = Dependent character removed. Item %d  character \.
error.102 = Insufficient space to invert matrix. Increase the size of the data buffer.
warning.103 = %s: Taxon names duplicated or not matched- %s
error.104 = For efficiency reasons, character %d  has been treated as a RN.
error.105 = It would be more efficient to treat character %d  as a RN.
error.106 = Character %d  has been treated as real. Some information may have been lost.
error.107 = Error reading scratch file 4.
error.108 = Error opening output file - ?.
error.109 = Illegal or missing delimiter.
error.1100 ='Unmatched { or } in the preceding text.
error.111 = Not all values of character %s in item %s are within the specified key states.
error.112 = Characters excluded from character list.
error.113 = Typesetting marks omitted from character list.
error.114 = Character %d  is an ordered multistate.
error.115 = Final comment repositioned in item %s character %d.
error.116 = Character %d   has more than 15 states.
error.117 = No %s file specified.
error.118 = Empty output file %s closed and deleted.
error.119 = Output file %s closed.
error.120 = ****** Program bug %d . File %s line %d.
error.121 = ****** File %s does not exist.
error.122 = ****** File %s is not accessible.
error.123 = ****** Directives file not specified.
error.124 = ****** Invalid Symbol - %d (decimal). File %s line %d column %d.
error.125 = Too many taxon names in the previous directive.
error.126 = Unmatched < or > in the preceding text.
error.127 = 
error.128 = Delimiter must be a single character.
error.129 = More than %d  words read without finding a closing bracket for comment.
error.130 = Character %d  has more than 10 states. It has been excluded.
error.131 = Image character must be a text character.
error.132 = Character ranges must not include different types.
error.134 = Only numeric characters allowed.
error.135 = Number of characters not as specified (%d).
error.136 = Taxon name can only be used once-
error.137 = Maximum number of state codes permitted is 64.
error.138 = State number greater than specified maximum (%d).
error.139 = DEPENDENT CHARACTERS have not been specified. For best results, the development of the character list and the character dependencies should proceed together.
error.140 = TAXON IMAGES: 
error.141 = ADD CHARACTERS: 
error.142 = EMPHASIZE CHARACTERS: 
error.143 = Taxon name duplicated in items file-
error.144 = TAXON LINKS: 
error.145 = ADD CHARACTERS cannot be used with TRANSLATE INTO INTKEY FORMAT or TRANSLATE INTO NEXUS FORMAT
error.146 = EMPHASIZE CHARACTERS cannot be used with TRANSLATE INTO INTKEY FORMAT
error.147 = List item %d  has already been specified.
error.148 = Nested input files are not supported.
error.149 = Taxon name character must be a text character.
error.150 = The file may already be in use or the directory may not exist.
error.151 = Synonomy character must be a text character.
error.152 = Invalid language. Item %s - %s
error.153 = Invalid language - %s
error.154 = 
error.155 = ITEM HEADINGS: 
error.156 = No index file previously specified.
error.157 = Data cannot be record against the CHARACTER FOR TAXON IMAGES. Use the TAXON IMAGES directive.
warning.158 = The first item cannot be a variant item.
error.159 = 
error.160 = ITEM OUTPUT FILES: 
error.161 = 
error.162 = INDEX HEADINGS: 
error.163 = The directory does not exist and cannot be created.
error.164 = The directory "?" does not exist and cannot be created.
error.165 = Output File character must be a text character.
error.166 = Cannot specify key states for a text character
error.167 = Invalid character type.
error.168 = An unexpected error has been encountered.
error.169 = Missing dimensions for overlay.
error.170 = Duplicate dimension specified.     
error.171 = Invalid overlay type. 
error.172 = Flag only valid for hotspot overlays
error.173 = Exclusive characters may have only one value.
error.174 = Unmatched RTF brackets.
error.175 = Invalid RTF.
error.176 = Unsupported translation type: %s
warning.177 = Character %d is not real numeric
warning.178 = Unable to change Item to a variant Item
warning.179 = Unable to change a variant Item into a normal Item
warning.180 = Item "%s" was not imported. An Item with this name is already present in the database.


```

### Common to Intkey and the DELTA Editor ###

```
#About Box 
closeAboutBox.Action.text = &OK

showLicense.Action.text = View &License Details
showLicense.Action.accelerator = control L
showLicense.Action.shortDescription = View details about the software license

showSystemInfo.Action.text = Show &System Information

AboutBox.windowTitle = [OVERRIDE IN CLIENT]
AboutBox.applicationTitle = [OVERRIDE IN CLIENT]
AboutBox.versionString = Version %s
AboutBox.attributionLine1 = Part of the Open DELTA suite
AboutBox.attributionLine2 = <center>Based on work by M.J. Dallwitz, T.A. Paine and E.J. Zurcher</center>
AboutBox.copyrightString = <center>Copyright Atlas of Living Australia 2011</center>
AboutBox.alaAttribution = <center>This development is being funded by the <a href="http://www.ala.org.au">Atlas of Living Australia</a>.</center>

#License Info Box
LicenseInfoBox.windowTitle = License Information
LicenseInfoBox.licenseAttribution = This software is made available under Version 1.1 of the Mozilla Public License
LicenseInfoBox.sourceCodeLocation = Source code can be downloaded from: http://code.google.com/p/open-delta
closeLicenseInfoBox.Action.text = OK
closeLicenseInfoBox.Action.mnemonic = O

#System Info Box
SystemInfoBox.windowTitle = System Information
copySystemInfoToClipboard.Action.text = Copy to clipboard
copySystemInfoToClipboard.Action.mnemonic = C
closeSystemInfoBox.Action.text = OK
closeSystemInfoBox.Action.mnemonic = O

#Search Dialog (Common)
searchDialog.lblFind = Find
searchDialog.lblMatchCase = Match Case
searchDialog.lblWrapSearch = Wrap Search
searchDialog.groupDirection = Direction
searchDialog.directionForwards = Forwards
searchDialog.directionBackwards = Backwards
searchDialog.btnFindNext = Find Next
searchDialog.btnCancel = Cancel
searchDialog.optionsPanelTitle = Options

#Search Replace Dialog
searchReplaceDialog.title = Search/Replace
searchReplaceDialog.findButton = Find
searchReplaceDialog.replaceFindButton = Replace/Find
searchReplaceDialog.replaceButton = Replace
searchReplaceDialog.replaceAllButton = Replace All
searchReplaceDialog.findLabel = Find:
searchReplaceDialog.replaceLabel = Replace with:
searchReplaceDialog.directionPanelTitle = Direction
searchReplaceDialog.forwardRadioButton = Forward
searchReplaceDialog.backwardRadioButton = Backward
searchReplaceDialog.optionsPanelTitle = Options
searchReplaceDialog.caseSensitiveCheck = Match Case
searchReplaceDialog.wrapSearchCheck = Wrap Search
searchReplaceDialog.closeButton = Close

#RTF Editor toolbar items
text_bold.Action.icon = text_bold.png
text_bold.Action.shortDescription = Bold
text_italic.Action.icon = text_italic.png
text_italic.Action.shortDescription = Italic
text_underline.Action.icon = text_underline.png
text_underline.Action.shortDescription = Underline
text_superscript.Action.icon = text_superscript.png
text_superscript.Action.shortDescription = Superscript
text_subscript.Action.icon = text_subscript.png
text_subscript.Action.shortDescription = Subscript
undo.Action.icon = arrow_undo.png
undo.Action.shortDescription = Reverses all changes made to this attribute

# Text used in image overlays
imageOverlay.okButton.text = Ok
imageOverlay.cancelButton.text = Cancel
imageOverlay.notesButton.text = Notes
imageOverlay.imageNotesButton.text = Notes

# About Image Dialog
okPressed.Action.text = OK
aboutImageDialog.title = About this image
captionLabel.text = Caption:
pathNameLabel.text = Pathname:
imageTypeLabel.text = Image type:
widthLabel.text = Width:
heightLabel.text = Height:
aboutImageDialog.colourLabel.text = Displayed using %d colours

#Code Editor toolbar items
show_whitespace.Action.icon = icons/show_whitespace.png
show_whitespace.Action.shortDescription = Show or hide whitespace characters
show_linenumbers.Action.icon = icons/show_linenumbers.png
show_linenumbers.Action.shortDescription = Show or hide line numbers
editor_find.Action.icon = icons/editor_find.png
editor_find.Action.shortDescription = Find and/or replace text
editor_gotoline.Action.icon = icons/editor_gotoline.png
editor_gotoline.Action.shortDescription = Go to a line number

errors.empty.character.description = The character description cannot be blank
errors.empty.character.description.title = Empty description
errors.items.with.multiple.states.encoded = Items exist with multiple states encoded for this character
errors.items.with.multiple.states.encoded.title = Multiple states coded
errors.only.multistate.chars.exclusive = Only multi-state characters can be marked has having exclusive values
errors.only.multistate.chars.exclusive.title = Not a multistate character
errors.multistate.character.no.states = This multi-state character must have at least one state defined
errors.multistate.character.no.states.title = No states defined
errors.coded.data.exists = Coded data exists for this character. Changing its character type may have undesirable consequences.
errors.coded.data.exists.title = Coded data exists
errors.unknown.character.type = The character type cannot be unknown
errors.unknown.character.type.title = Character type not set

#Goto line dialog
gotoLineDialog.title = Goto line
gotoLineDialog.lineNumberLabel.text = Line number
gotoLineDialog.okButton.text = Ok
gotoLineDialog.cancelButton.text = Cancel
```

### Intkey ###

```
Application.id = Intkey
Application.title = Intkey
Application.version = 1.0-BETA-SNAPSHOT
Application.vendor = open-delta
Application.vendorId = open-delta
Application.homepage = http://code.google.com/p/open-delta/
Application.description = An interactive program for identifying a specimen by comparing it with stored descriptions
Application.lookAndFeel = system
Application.shortName = Intkey

Intkey.windowTitleWithDatasetTitle = Intkey: {0}
Intkey.availableCharactersCaption = Available Characters ({0})
Intkey.bestCharactersCaption = Best Characters ({0})
Intkey.separateCharactersCaption = Separate {0} ({1})
Intkey.usedCharactersCaption = Used Characters ({0})
Intkey.remainingTaxaCaption = Remaining Taxa ({0})
Intkey.eliminatedTaxaCaption = Eliminated Taxa ({0})
Intkey.calculatingBestCaption = Calculating best characters
Intkey.errorDlgTitle = Error
Intkey.informationDlgTitle = Information
Intkey.displayingReportCaption = Displaying report...
Intkey.logDialogTitle = Log

Intkey.identificationCompleteCaption = Identification complete.
Intkey.availableCharactersCannotSeparateCaption = The available characters cannot separate the remaining taxa.
Intkey.noMatchingTaxaRemainCaption = No matching taxa remain.
Intkey.charactersExcludedCannotSeparateCaption = Characters have been excluded from the identification process, and the available characters cannot separate the remaining taxa.
Intkey.mismatchesAllowCannotSeparateCaption = Mismatches between the specimen and the taxa have been allowed, and the available characters cannot separate the remaining taxa.
Intkey.selectCharacterKeywordsCaption = Select character keywords
Intkey.selectTaxonKeywordsCaption = Select taxon keywords
Intkey.badlyFormedRTFContentMessage = Badly formed RTF content.
Intkey.separateInformationMessage = In the 'Remaining Taxa' window, select the taxon to the separated.\nSelect one and only one taxon. 
Intkey.noHelpAvailableCaption = No help available.
Intkey.saveReportToFilePrompt = This report is approximately {0}MB in size and may take a long time to display. Would you like to save it to disk? 
Intkey.errorWritingToFileError = Error writing to file
Intkey.errorReadingRTFFileError = Error reading RTF content from file
Intkey.rtfFileTooLargeError = RTF content is too large to fit in memory. Try increasing the memory available to the Java Virtual Machine.

# File menu
mnuFile.text = &File
mnuFile.toolTipText = File input, output and program exit
mnuFileRecents.text = Recent &Datasets
mnuFileRecents.toolTipText = Open a recently used dataset 
mnuItNewDataSet.text = &New Data Set...
mnuItNewDataSet.toolTipText = Change to a different data set 
mnuItPreferences.text = &Preferences...
mnuItPreferences.toolTipText = Select and execute an input file specifying preferred parameter settings
mnuItContents.text = &Contents...
mnuItContents.toolTipText = Select a file containing a table of contents for the current data set, and select an entry from that table
mnuFileCmds.text = &File
mnuFileCmds.toolTipText = Specify names of input and output files
mnuItFileInput.text = &Input...
mnuItFileOutput.text = &Output...
mnuItFileDisplay.text = &Display...
mnuItFileLog.text = &Log...
mnuItFileJournal.text = &Journal...
mnuItFileClose.text = Clo&se...
mnuItFileCharacters.text = Characters...
mnuItFileTaxa.text = &Taxa...
mnuOutputCmds.text = &Output
mnuOutputCmds.toolTipText = Generate files for input to other programs
mnuItOutputCharacters.text = &Characters...
mnuItOutputTaxa.text = &Taxa...
mnuItOutputDescribe.text = D&escribe...
mnuItOutputSummary.text = &Summary...
mnuItOutputDiagnose.text = &Diagnose...
mnuItOutputDifferences.text = Diffe&rences...
mnuItOutputSimilarities.text = Simi&larities...
mnuItOutputComment.text = Co&mment...
mnuItComment.text = Co&mment...
mnuItComment.toolTipText = Annotate the output window and files 
mnuItShow.text = &Show...
mnuItShow.toolTipText = Display text on the screen 
mnuItNormalMode.Action.text = No&rmal Mode
mnuItNormalMode.Action.shortDescription = Switch to normal mode
mnuItAdvancedMode.Action.text = &Advanced Mode
mnuItAdvancedMode.Action.shortDescription= Switch to advanced mode
mnuItExitApplication.Action.text = Quit (E&xit)
mnuItExitApplication.Action.shortDescription = Exit from the program

# Queries menu
mnuQueries.text = &Queries
mnuQueries.toolTipText = Interrogate data
mnuItRestart.text = &Restart
mnuItRestart.toolTipText = Start a new identification
mnuItDescribe.text = &Describe...
mnuItDescribe.toolTipText = Display descriptions of taxa
mnuItDiagnose.text = D&iagnose...
mnuItDiagnose.toolTipText = Generate and display diagnostic descriptions of taxa
mnuItDifferences.text = Di&fferences...
mnuItDifferences.toolTipText = Display the differences between taxa
mnuItSimilarities.text = &Similarities...
mnuItSimilarities.toolTipText = Display the similarities between taxa
mnuItSummary.text = S&ummary...
mnuItSummary.toolTipText = Display a summary of the data

# Browsing menu
mnuBrowsing.text = &Browsing
mnuBrowsing.toolTipText = View character list, taxon names or illustrations
mnuItCharacters.text = &Characters...
mnuItCharacters.toolTipText = Display characters
mnuItTaxa.text = &Taxa...
mnuItTaxa.toolTipText = Display names of taxa
mnuFind.text = &Find...
mnuFind.toolTipText = Find a text string in character descriptions or taxon names
mnuItFindCharacters.text = &Characters...
mnuItFindTaxa.text = &Taxa...
mnuIllustrate.text = &Illustrate...
mnuIllustrate.toolTipText = Display Images
mnuItIllustrateCharacters.text = &Characters...
mnuItIllustrateTaxa.text = &Taxa...
mnuItInformation.text = I&nformation...

# Settings menu
mnuSettings.text = &Settings
mnuSettings.toolTipText = Set program and data parameters
# Set submenu
mnuSet.text = &Set
mnuSet.toolTipText = &Set parameters
mnuAutotolerance.text = Aut&otolerance
mnuItAutotoleranceOn.text = O&n
mnuItAutotoleranceOff.text = O&ff
mnuDemonstration.text = Demo&nstration
mnuItDemonstrationOn.text = O&n
mnuItDemonstrationOff.text = O&ff
mnuItDiagLevel.text = &DiagLevel...
mnuDiagType.text = Dia&gType
mnuItDiagTypeSpecimens.text = &Specimens
mnuItDiagTypeTaxa.text = &Taxa
mnuItExact.text = &Exact
mnuFix.text = &Fix
mnuItFixOn.text = O&n
mnuItFixOff.text = O&ff
mnuItImagePath.text = &Imagepath...
mnuItInfoPath.text = Info&path...
mnuItMatch.text = &Match...
mnuItRbase.text = &Rbase...
mnuItReliabilities.text = Rel&iabilities
mnuItStopBest.text = &Stopbest...
mnuItTolerance.text = &Tolerance...
mnuItVaryWt.text = &Varywt...
# Display submenu
mnuDisplay.text = &Display
mnuDisplay.toolTipText = Set screen display
mnuCharacterOrder.text = C&haracterOrder
mnuItCharacterOrderBest.text = &Best
mnuItCharacterOrderNatural.text = &Natural
mnuItCharacterOrderSeparate.text = &Separate...
mnuComments.text = &Comments
mnuItCommentsOn.text = O&n
mnuItCommentsOff.text = O&ff
mnuContinuous.text = Co&ntinuous
mnuItContinuousOn.text = O&n
mnuItContinuousOff.text = O&ff
mnuEndIdentify.text = &EndIdentify
mnuItEndIdentifyOn.text = O&n
mnuItEndIdentifyOff.text = O&ff
mnuItImages.text = I&mages...
mnuInapplicables.text = &Inapplicables
mnuItInapplicablesOn.text = O&n
mnuItInapplicablesOff.text = O&ff
mnuInput.text = In&put
mnuItInputOn.text = O&n
mnuItInputOff.text = O&ff
mnuKeywords.text = &Keywords
mnuItKeywordsOn.text = O&n
mnuItKeywordsOff.text = O&ff
mnuNumbering.text = Num&bering
mnuItNumberingOn.text = O&n
mnuItNumberingOff.text = O&ff
mnuLog.text = &Log
mnuItLogOn.text = O&n
mnuItLogOff.text = O&ff
mnuScaled.text = &Scaled
mnuItScaledOn.text = O&n
mnuItScaledOff.text = O&ff
mnuUnknowns.text = &Unknowns
mnuItUnknownsOn.text = O&n
mnuItUnknownsOff.text = O&ff

# Define submenu
mnuDefine.text = De&fine
mnuDefine.toolTipText = Define: a keyword to represent a set of characters or taxa; a toolbar button; actions to be taken at the completion of an identification; information commands; or subjects for selection of taxon images.
mnuItDefineCharacters.text = &Characters...
mnuItDefineTaxa.text = &Taxa...
mnuItDefineNames.text = &Names...
mnuItDefineButton.text = &Button...
mnuItDefineEndIdentify.text = &EndIdentify...
mnuItDefineInformation.text = &Information...
mnuItDefineSubjects.text = &Subjects...
# Include submenu
mnuInclude.text = &Include
mnuInclude.toolTipText = Restrict all subsequent operations to subsets of characters or taxa
mnuItIncludeCharacters.text = &Characters
mnuItIncludeTaxa.text = &Taxa
# Exclude submenu
mnuExclude.text = &Exclude
mnuExclude.toolTipText = Exclude subsets of characters or taxa from all subsequent operations
mnuItExcludeCharacters.text = &Characters
mnuItExcludeTaxa.text = &Taxa
# Status submenu
mnuStatus.text = S&tatus
mnuStatus.toolTipText = Display parameter values
mnuItStatusDisplay.text = &Display 
mnuStatusInclude.text = &Include
mnuItStatusIncludeCharacters.text = &Characters
mnuItStatusIncludeTaxa.text = &Taxa
mnuStatusExclude.text = &Exclude
mnuItStatusExcludeCharacters.text = &Characters
mnuItStatusExcludeTaxa.text = &Taxa
mnuItStatusFiles.text = &Files
mnuItStatusSet.text = &Set
mnuItStatusAll.text = &All 

# ReExecute menu
mnuReExecute.text = &ReExecute
mnuReExecute.toolTipText = Retrieve, edit and execute previous commands
mnuItReExecute.Action.text = &ReExecute...

# New Data Set Dialog
browseForFile.Action.text = Browse...

# Window menu
mnuWindow.text = &Window
mnuWindow.toolTipText = Window management
mnuItCascadeWindows.Action.text = &Cascade
mnuItTileWindows.Action.text = &Tile
mnuItCloseAllWindows.Action.text = Close &All
mnuLF.text = &Look and Feel
metalLookAndFeel.Action.text = &Metal
systemLookAndFeel.Action.text = &System
nimbusLookAndFeel.Action.text = &Nimbus

# Help menu
mnuHelp.text = &Help
mnuHelp.toolTipText = Intkey help
mnuItHelpTopics.text = &Topics...
mnuItHelpCommands.text = &Commands...
mnuItHelpAbout.Action.text = &About Intkey

# About Box 
AboutBox.windowTitle = About Intkey
AboutBox.applicationTitle = Intkey

# Global option buttons
btnContextHelp.Action.icon = icons/helpa.png
btnContextHelp.Action.shortDescription = For more help, press this button, then any other active button

# Character toolbar buttons
btnRestart.Action.icon = icons/restarts.png
btnRestart.Action.shortDescription = Restart identification or query

btnBestOrder.Action.icon = icons/obests.png
btnBestOrder.Action.shortDescription = 'Best' order

btnSeparate.Action.icon = icons/oseps.png
btnSeparate.Action.shortDescription = 'Separate a given taxon' order

btnNaturalOrder.Action.icon = icons/onats.png
btnNaturalOrder.Action.shortDescription = Natural order

btnDiffSpecimenTaxa.Action.icon = icons/diff_ss.png
btnDiffSpecimenTaxa.Action.shortDescription = Differences between the specimen and the remaining taxa

btnSetTolerance.Action.icon = icons/set_tols.png
btnSetTolerance.Action.shortDescription = Set error tolerance
 
btnSetMatch.Action.icon = icons/set_mats.png
btnSetMatch.Action.shortDescription = Set match

btnSubsetCharacters.Action.icon = icons/inc_cs.png
btnSubsetCharacters.Action.shortDescription = Use a subset of the characters

btnFindCharacter.Action.icon = icons/finds.png
btnFindCharacter.Action.shortDescription = Find text in characters

# Taxa toolbar buttons
btnTaxonInfo.Action.icon = icons/infos.png
btnTaxonInfo.Action.shortDescription = Information about taxa

btnDiffTaxa.Action.icon = icons/diff_ts.png
btnDiffTaxa.Action.shortDescription = Differences between taxa

btnSubsetTaxa.Action.icon = icons/inc_ts.png
btnSubsetTaxa.Action.shortDescription = Use a subset of the taxa

btnFindTaxon.Action.icon = icons/finds.png
btnFindTaxon.Action.shortDescription = Find text in taxon names

# MessagePanel
MessagePanel_Help.Action.text = Help

# AllowMismatchMessagePanel
AllowMismatchMessagePanel.allowOneMismatchCaption = Allow 1 Mismatch
AllowMismatchMessagePanel.allowMultipleMismatchesCaption = Allow {0} Mismatches
AllowMismatchMessagePanel_Help.Action.text = Help

# Character Value Input Dialog
CharacterValueInputDialog.fullCharacterTextCaption = Full text of character
CharacterValueInputDialog.notesCaption = Notes
characterValueInputDialog_OK.Action.text = OK
characterValueInputDialog_OK.Action.mnemonic = O
characterValueInputDialog_Images.Action.text = Images
characterValueInputDialog_Images.Action.mnemonic = I
characterValueInputDialog_FullText.Action.text = Full Text
characterValueInputDialog_FullText.Action.mnemonic = F
characterValueInputDialog_Search.Action.text = Search
characterValueInputDialog_Search.Action.mnemonic = S
characterValueInputDialog_Cancel.Action.text = Cancel
characterValueInputDialog_Cancel.Action.mnemonic = C
characterValueInputDialog_Notes.Action.text = Notes
characterValueInputDialog_Notes.Action.mnemonic = N
characterValueInputDialog_Help.Action.text = Help
characterValueInputDialog_Help.Action.mnemonic = H

# Keyword selection dialog
keywordSelectionDialog_OK.Action.text = OK
keywordSelectionDialog_OK.Action.mnemonic = O
keywordSelectionDialog_DeselectAll.Action.text = Deselect All
keywordSelectionDialog_DeselectAll.Action.mnemonic = D
keywordSelectionDialog_List.Action.text = List
keywordSelectionDialog_List.Action.mnemonic = L
keywordSelectionDialog_Images.Action.text = Images
keywordSelectionDialog_Images.Action.mnemonic = I
keywordSelectionDialog_Search.Action.text = Search
keywordSelectionDialog_Search.Action.mnemonic = S
keywordSelectionDialog_Cancel.Action.text = Cancel
keywordSelectionDialog_Cancel.Action.mnemonic = C
keywordSelectionDialog_Help.Action.text = Help
keywordSelectionDialog_Help.Action.mnemonic = H
keywordSelectionDialog_SelectFromCaption = Select from:

# Character keyword selection dialog
CharacterKeywordSelectionDialog.title = {0}: Select character keywords
CharacterKeywordSelectionDialog.selectFromAllCharactersCaption = All characters
CharacterKeywordSelectionDialog.selectFromIncludedCharactersCaption = Included characters
CharacterKeywordSelectionDialog.allCharactersInSelectedSetExcludedCaption = All characters in the specified set have been excluded.

# Taxon keyword selection dialog
TaxonKeywordSelectionDialog.title = {0}: Select taxon keywords
TaxonKeywordSelectionDialog.selectFromAllTaxaCaption = All taxa
TaxonKeywordSelectionDialog.selectFromIncludedTaxaCaption = Included taxa
TaxonKeywordSelectionDialog.allTaxaInSelectedSetExcludedCaption = All taxa in the specified set have been excluded.

# Character selection dialog
CharacterSelectionDialog.title = {0}: Select characters
CharacterSelectionDialog.titleFromKeyword = {0}: Select characters (from ''{1}'')
CharacterSelectionDialog.fullTextOfCharacterCaption = Full text of character
CharacterSelectionDialog.notesCaption = Notes
characterSelectionDialog_OK.Action.text = OK
characterSelectionDialog_OK.Action.mnemonic = O
characterSelectionDialog_SelectAll.Action.text = Select All
characterSelectionDialog_SelectAll.Action.mnemonic = A
characterSelectionDialog_Keywords.Action.text = Keywords
characterSelectionDialog_Keywords.Action.mnemonic = K
characterSelectionDialog_Images.Action.text = Images
characterSelectionDialog_Images.Action.mnemonic = I
characterSelectionDialog_Search.Action.text = Search
characterSelectionDialog_Search.Action.mnemonic = S
characterSelectionDialog_Cancel.Action.text = Cancel
characterSelectionDialog_Cancel.Action.mnemonic = C
characterSelectionDialog_DeselectAll.Action.text = Deselect All
characterSelectionDialog_DeselectAll.Action.mnemonic = D
characterSelectionDialog_FullText.Action.text = Full Text
characterSelectionDialog_FullText.Action.mnemonic = F
characterSelectionDialog_Notes.Action.text = Notes
characterSelectionDialog_Notes.Action.mnemonic = N
characterSelectionDialog_Help.Action.text = Help
characterSelectionDialog_Help.Action.mnemonic = H

# Taxon selection dialog
TaxonSelectionDialog.title = {0}: Select taxa
TaxonSelectionDialog.titleFromKeyword = {0}: Select taxa (from ''{1}'')
TaxonSelectionDialog.fullTextOfTaxonNameCaption = Full text of taxon name
taxonSelectionDialog_OK.Action.text = OK
taxonSelectionDialog_OK.Action.mnemonic = O
taxonSelectionDialog_SelectAll.Action.text = Select All
taxonSelectionDialog_SelectAll.Action.mnemonic = A
taxonSelectionDialog_Keywords.Action.text = Keywords
taxonSelectionDialog_Keywords.Action.mnemonic = K
taxonSelectionDialog_Images.Action.text = Images
taxonSelectionDialog_Images.Action.mnemonic = I
taxonSelectionDialog_Search.Action.text = Search
taxonSelectionDialog_Search.Action.mnemonic = S
taxonSelectionDialog_Cancel.Action.text = Cancel
taxonSelectionDialog_Cancel.Action.mnemonic = C
taxonSelectionDialog_DeselectAll.Action.text = Deselect All
taxonSelectionDialog_DeselectAll.Action.mnemonic = D
taxonSelectionDialog_FullText.Action.text = Full Text
taxonSelectionDialog_FullText.Action.mnemonic = F
taxonSelectionDialog_Help.Action.text = Help
taxonSelectionDialog_Help.Action.mnemonic = H

# Text Input Dialog
TextInputDialog.title = Enter text

# Integer Input Dialog
IntegerInputDialog.title = Enter value or values
IntegerInputDialog.validationErrorMessage = Integer value(s) required
IntegerInputDialog.validationErrorTitle = Invalid input

# Real Input Dialog
RealInputDialog.title = Enter value or range of values
RealInputDialog.validationErrorMessage = Real value(s) required
RealInputDialog.validationErrorTitle = Invalid input

# Mulitstate Input Dialog
MultiStateInputDialog.title = Select state or states
MultiStateInputDialog.selectionConfirmationMessage = You have selected all of the character states. Is that what you want?
MultiStateInputDialog.selectionConfirmationTitle = Confirm selection

# Use Directive Messages
UseDirective.NoDataSetMsg = Need to have a dataset loaded before USE can be called.
UseDirective.CharacterAlreadyUsed = Character ''{0}'' has already been used. Do you want to change the value(s) you entered?
UseDirective.CharacterUnavailable = Character ''{0}'' is unavailable.
UseDirective.CharacterExcluded = Character ''{0}'' is excluded.
UseDirective.CharacterFixed = Character ''{0}'' is fixed.
UseDirective.CharacterMaybeInapplicable = Character ''{0}'' may be inapplicable. Do you still want to use it?
UseDirective.InvalidCharacterNumber = Invalid character number ''{0}''. Valid numbers are in the range 1-{1}.
UseDirective.InvalidIntegerValue = Invalid integer value ''{0}'' for character {1} - ''{2}''.
UseDirective.InvalidRealValue = Invalid real value ''{0}'' for character {1} - ''{2}''.
UseDirective.InvalidStateValue = Invalid state value ''{0}'' for character {1} - ''{2}''. This character has {3} states.
UseDirective.TaxonToSeparateEliminatedMsg = The value for character ''{0}'' caused {1} to be eliminated. Do you want to change that value?

# Display CharacterOrder Separate Message
DisplayCharacterOrderSeparate.TaxonNoLongerInContentionMsg = {0} is no longer in contention

# Differences Directive Captions
DifferencesDirective.ReportTitle = Differences 
DifferencesDirective.Specimen = Specimen
DifferencesDirective.NoDifferences = No differences
DifferencesDirective.OneDifference = 1 difference
DifferencesDirective.ManyDifferences = {0} differences
DifferencesDirective.ReportTitle = Differences
DifferencesDirective.Progress.Calculating = Calculating differences...
DifferencesDirective.Progress.Generating = Generating differences report ({0}%)

# Differences Directive Captions
OutputDifferencesDirective.Progress.Calculating = Calculating differences...
OutputDifferencesDirective.Progress.Generating = Generating output differences report ({0}%)

# Similarities Directive Captions
SimilaritiesDirective.NoDifferences = No similarities
SimilaritiesDirective.OneDifference = 1 similarity
SimilaritiesDirective.ManyDifferences = {0} similarities
SimilaritiesDirective.ReportTitle = Similarities
SimilaritiesDirective.Progress.Calculating = Calculating similarities...
SimilaritiesDirective.Progress.Generating = Generating similarities report ({0}%)

# Output Similarities Directive Captions
OutputSimilaritiesDirective.Progress.Calculating = Calculating similarities...
OutputSimilaritiesDirective.Progress.Generating = Generating output similarities report ({0}%)

# Summary Directive Captions
SummaryDirective.ReportTitle = Summary
SummaryDirective.ReportHeading = Summary for Items: {0}
SummaryDirective.UnknownInapplicableRecorded = U/I/Recorded
SummaryDirective.DistributionOfValues = Distribution of values
SummaryDirective.Mean = Mean
SummaryDirective.StandardDeviation = Standard Deviation
SummaryDirective.Minimum = Minimum
SummaryDirective.Maximum = Maximum
SummaryDirective.MultiStateCharacterDetail = {0} states
SummaryDirective.IntegerCharacterDetail = integer
SummaryDirective.RealCharacterDetail = real
SummaryDirective.TextCharacterDetail = text
SummaryDirective.ItemNumber = Item {0}
SummaryDirective.Progress.Generating = Generating summary report ({0}%)

# Diagnose Directive Captions
DiagnoseDirective.ReportTitle = Diagnose
DiagnoseDirective.Progress.Generating = Generating diagnose report ({0}%)
DiagnoseDirective.DiagnosticLevelAttained = Diagnostic level {0} attained.
DiagnoseDirective.DiagnosticLevelNotAttained = Diagnostic level {0} not attained.
DiagnoseDirective.DiagnoseIncomplete = Diagnosis for {0} is incomplete.

# Ouput Diagnose Directive Captions
OutputDiagnoseDirective.Progress.Generating = Generating output diagnose report ({0}%)

# Describe Directive Captions
DescribeDirective.ReportTitle = Describe
DescribeDirective.Progress.Generating = Generating describe report ({0}%)

# Output Describe Directive Captions
OutputDescribeDirective.Progress.Generating = Generating output describe report ({0}%)

# SET RBASE Directive Messages
SetRBaseDirective.InvalidValue = Invalid rbase value. A valid value is a real number in the range 1-5.

# Generic Errors
CharactersFileNotFound.error = Characters file ''{0}'' could not be found
TaxaFileNotFound.error = Taxa file ''{0}'' could not be found
RedefineSystemKeyword.error = ''{0}'' is a system keyword and cannot be redefined
CharacterKeywordNotFoundOrAmbiguous.error = Character keyword ''{0}'' not found or ambiguous
TaxonKeywordNotFoundOrAmbiguous.error = Taxon keyword ''{0}'' not found or ambiguous
NoCharactersInSet.error = There are no characters in the specified set
NoTaxaInSet.error = There are no taxa in the specified set
InvalidSetMatchOption.error = Invalid option for SET MATCH: ''{0}''
InvalidTaxonNumber.error = Invalid taxon number {0}, expecting number in the range 1-{0}
InvalidTaxonName.error = ''{0}'' is not a valid taxon name
InvalidCharacterNumber.error = Invalid character number {0}, expecting number in the range 1-{0} 
DirectiveCallNoDatasetLoaded.error = The command ''{0}'' requires data files. Use the NewDataSet option in the Files menu.
DirectiveParseError.error = An error occurred while parsing the command ''{0}''. See the log for details. 
UnrecognizedTaxonKeyword.error = Unrecognized taxon keyword ''{0}''
UnrecognizedCharacterKeyword.error = Unrecognized character keyword ''{0}''
ErrorCreatingFile.error = Error creating file
ErrorCreatingFileWithName.error = Error creating file ''{0}''
IntegerValueRequired.error = Integer value required
RealValueRequired.error = Real value required
NoOutputFileOpen.error = No OUTPUT file is open
ImageDisplayDisabled.error = Image display is disabled.
CouldNotDisplayImage.error = An error occurred displaying an image: ''{0}''.
CouldNotDisplayFile.error = Error displaying file: {0}.
CouldNotDisplayFileDesktopError.error = Could not open file: {0}. This is likely due to deficiencies of your Java installation. \n Try coping the URL for the file from the field below and using it to open the file in an external program:   

# Log messages
OneTaxonRemains.log = 1 taxon remains.
MultipleTaxaRemain.log = {0} taxa remain.
ReliabilitiesSet.log = Reliabilities set.
CharactersFixed.log = Characters fixed.
FixOff.log = Fix off.
ExactCharacters.log = EXACT characters set.
NoExactCharacters.log No EXACT characters.
OneCharacterIncluded.log = 1 character included.
MultipleCharactersIncluded.log = {0} characters included.
OneCharacterExcluded.log = 1 character excluded.
MultipleCharactersExcluded.log = {0} characters excluded.
OneTaxonIncluded.log = 1 taxon included.
MultipleTaxaIncluded.log = {0} taxa included.
OneTaxonExcluded.log = 1 taxon excluded.
MultipleTaxaExcluded.log = {0} taxa excluded.
KeywordDeleted.log = Keyword deleted.
ErrorToleranceSet.log = Error tolerance set to {0}.
DiagtypeSet.log = DiagType set to ''{0}''.
ImagepathSet.log = Image path set to ''{0}''.
InfopathSet.log = Information path set to ''{0}''.
SetMatch.log = ''Match'' set to {0}.
NewIdentification.log = New identification started. {0} taxa included.
NewIdentificationTaxaExcluded.log = New identification started. {0} taxa included. (Taxa have been excluded.)
NewQuery.log = New query started. {0} taxa included.
NewQueryTaxaExcluded.log = New query started. {0} taxa included. (Taxa have been excluded.)
SpecimenDataCleared.log = Specimen data cleared. {0} taxa included.
SpecimenDataClearedTaxaExcluded.log = Specimen data cleared. {0} taxa included. (Taxa have been excluded.)

# ReExecute Dialog
ReExecuteDialog.windowTitle = Select command to re-execute
ReExecuteDialog.editPrompt = Edit command before re-execution
reExecuteDialog_Execute.Action.text = Execute
reExecuteDialog_Execute.Action.mnemonic = E
reExecuteDialog_Cancel.Action.text = Cancel
reExecuteDialog_Cancel.Action.mnemonic = C
reExecuteDialog_Edit.Action.text = Edit
reExecuteDialog_Edit.Action.mnemonic = D


#RtfReportDisplayDialog
rtfReportDisplayDialog_mnuFile.text = &File
rtfReportDisplayDialog_mnuItSaveAs.Action.text = Save As
rtfReportDisplayDialog_mnuItSaveAs.Action.mnemonic = S
rtfReportDisplayDialog_mnuItPrint.Action.text = Print
rtfReportDisplayDialog_mnuItPrint.Action.mnemonic = P
rtfReportDisplayDialog_mnuEdit.text = &Edit
rtfReportDisplayDialog_mnuItFind.Action.text = Find...
rtfReportDisplayDialog_mnuItFind.Action.mnemonic = F
rtfReportDisplayDialog_mnuItFind.Action.accelerator = control F
rtfReportDisplayDialog_mnuItCopy.Action.text = Copy
rtfReportDisplayDialog_mnuItCopy.Action.mnemonic = C
rtfReportDisplayDialog_mnuItCopy.Action.accelerator = control C
rtfReportDisplayDialog_mnuItSelectAll.Action.text = Select All
rtfReportDisplayDialog_mnuItSelectAll.Action.mnemonic = S
rtfReportDisplayDialog_mnuItSelectAll.Action.accelerator = control A
rtfReportDisplayDialog_mnuWindow.text = &Window
rtfReportDisplayDialog_mnuItCascade.Action.text = Cascade
rtfReportDisplayDialog_mnuItCascade.Action.mnemonic = C
rtfReportDisplayDialog_mnuItTile.Action.text = Tile
rtfReportDisplayDialog_mnuItTile.Action.mnemonic = T
rtfReportDisplayDialog_mnuItCloseAll.Action.text = Close All
rtfReportDisplayDialog_mnuItCloseAll.Action.mnemonic = A

RtfReportDisplayDialog.fileChooserTitle = Save As
RtfReportDisplayDialog.fileFilterDescription = Rich Text Format (RTF) file (*.rtf)
RtfReportDisplayDialog.fileWriteError = Error writing to file ''{0}'' 

# ImageDialog
ImageDialog.mnuSubjectCaption = Subject
ImageDialog.mnuSubjectMnemonic = S
ImageDialog.mnuControlCaption = Control
ImageDialog.mnuControlMnemonic = T
ImageDialog.mnuWindowCaption = Window
ImageDialog.mnuWindowMnemonic  = W
nextImage.Action.text = Next Image
nextImage.Action.mnemonic = N
nextImage.Action.accelerator = control DOWN
previousImage.Action.text = Previous Image
previousImage.Action.mnemonic = P
previousImage.Action.accelerator = control UP
toggleScaling.Action.text = Scaled
toggleScaling.Action.mnemonic = S
toggleHideText.Action.text = Hide text
toggleHideText.Action.mnemonic = H
replaySound.Action.text = Replay sound
replaySound.Action.mnemonic = R
replayVideo.Action.text = Replay video
replayVideo.Action.mnemonic = V
fitToImage.Action.text = Fit to image
fitToImage.Action.mnemonic = I
fullScreen.Action.text = Full screen
fullScreen.Action.mnemonic = F
cascade.Action.text = Cascade
cascade.Action.mnemonic = C
tile.Action.text = Tile
tile.Action.mnemonic = T
closeAll.Action.text = Close All
closeAll.Action.mnemonic = A
aboutImage.Action.text = About Image...
aboutImage.Action.mnemonic = M

# FindInCharactersDialog
findCharacters.Action.text = Find
findCharacters.Action.mnemonic = F
nextCharacter.Action.text = Next
nextCharacter.Action.mnemonic = N
previousCharacter.Action.text = Previous
previousCharacter.Action.mnemonic = P
findCharactersDone.Action.text = Done
findCharactersDone.Action.mnemonic = D
FindInCharactersDialog.enterSearchStringCaption = Enter search string:
FindInCharactersDialog.searchStatesCaption = Search states
FindInCharactersDialog.searchUsedCharactersCaption = Search used characters
FindInCharactersDialog.windowTitle = Find in characters
FindInCharactersDialog.windowTitleNumberFound = Find in characters: {0} found
FindInCharactersDialog.noCharactersFoundMessage = No characters found.

# FindInTaxaDialog
findTaxa.Action.text = Find
findTaxa.Action.mnemonic = F
nextFoundTaxon.Action.text = Next
nextFoundTaxon.Action.mnemonic = N
previousFoundTaxon.Action.text = Previous
previousFoundTaxon.Action.mnemonic = P
findTaxaDone.Action.text = Done
findTaxaDone.Action.mnemonic = D
FindInTaxaDialog.enterSearchStringCaption = Enter search string:
FindInTaxaDialog.selectOneCaption = Select one
FindInTaxaDialog.selectAllCaption = Select all
FindInTaxaDialog.searchSynonymsCaption = Search synonyms
FindInTaxaDialog.searchEliminatedTaxaCaption = Search eliminated taxa
FindInTaxaDialog.windowTitle = Find in taxa
FindInTaxaDialog.windowTitleNumberFound = Find in taxa: {0} found
FindInTaxaDialog.noTaxaFoundMessage = No taxa found.

# TaxonInformationDialog
firstTaxon.Action.icon = icons/SkipStart.png
previousTaxon.Action.icon = icons/previous.png
nextTaxon.Action.icon = icons/next.png
lastTaxon.Action.icon = icons/SkipEnd.png
displaySelectedTaxonInformation.Action.text = Display
displaySelectedTaxonInformation.Action.mnemonic = S
displayMultipleImages.Action.text = Multiple Images...
displayMultipleImages.Action.mnemonic = M
webSearch.Action.text = Web Search...
webSearch.Action.mnemonic = W
deselectAllTaxonInformation.Action.text = Deselect All
deselectAllTaxonInformation.Action.mnemonic = D
done.Action.text = Done
done.Action.mnemonic = O
TaxonInformationDialog.noImagesCaption = No images

#TaxonImageDialog
viewNextTaxon.Action.text = Next Taxon
viewNextTaxon.Action.mnemonic = X
viewNextTaxon.Action.accelerator = control PAGE_DOWN
viewPreviousTaxon.Action.text = Previous Taxon
viewPreviousTaxon.Action.mnemonic = V
viewPreviousTaxon.Action.accelerator = control PAGE_UP
displayMultipleImages.Action.text = Multiple Images...
displayMultipleImages.Action.mnemonic = M

# WebSearch
WebSearchDialog_search.Action.text = Search
WebSearchDialog_search.Action.mnemonic = S
WebSearchDialog_cancel.Action.text = Cancel
WebSearchDialog_cancel.Action.mnemonic = C
WebSearchDialog.websearchTitle = Web search
WebSearchDialog.searchForLabel = Search for:
WebSearchDialog.usingLabel = Using:

#CharacterInputDialog
CharacterImageDialog.integerTitle = Enter value or values
CharacterImageDialog.realTitle = Enter value or range of values
CharacterImageDialog.multistateTitle = Select state or states
CharacterImageDialog.imageForViewingOnlyMessage = This image is for viewing only. To enter character values, you should first select a character from the top-left pane in the main window.
CharacterImageDialog.notEditableTitle = Character {0}: {1}
viewNextCharacter.Action.text = Next Character
viewNextCharacter.Action.mnemonic = X
viewNextCharacter.Action.accelerator = control PAGE_DOWN
viewPreviousCharacter.Action.text = Previous Character
viewPreviousCharacter.Action.mnemonic = V
viewPreviousCharacter.Action.accelerator = control PAGE_UP

#OnOffPromptDialog
onOffPromptDialog_OkPressed.Action.text = OK
onOffPromptDialog_OkPressed.Action.mnemonic = O
onOffPromptDialog_CancelPressed.Action.text = Cancel
onOffPromptDialog_CancelPressed.Action.mnemonic = C
rdBtnOn.text = ON
rdBtnOff.text = OFF

#ContentsDialog
ContentsDialog.title = Select topic
contentsDialogOkButtonPressed.Action.text = OK
contentsDialogOkButtonPressed.Action.mnemonic = O
contentsDialogCancelButtonPressed.Action.text = Cancel
contentsDialogCancelButtonPressed.Action.mnemonic = C

#OutputFileSelectionDialog
OutputFileSelectionDialog.title = Select output file to close
outputFileSelectionDialog_OkPressed.Action.text = OK
outputFileSelectionDialog_OkPressed.Action.mnemonic = O
outputFileSelectionDialog_CancelPressed.Action.text = Cancel
outputFileSelectionDialog_CancelPressed.Action.mnemonic = C

# SET MATCH prompt dialog
SetMatchPromptDialog.title = Set Match
SetMatchPromptDialog.inapplicablesCaption = Inapplicables
SetMatchPromptDialog.unknownsCaption = Unknowns
SetMatchPromptDialog.subsetCaption = Subset
SetMatchPromptDialog.overlapCaption = Overlap
SetMatchPromptDialog.exactCaption = Exact
SetMatchPromptDialog.setValuesForIdentificationCaption = Set values for identification
SetMatchPromptDialog.setValuesForInformationRetrievalCaption = Set values for information retrieval
setMatchPromptDialog_okAction.Action.text = OK
setMatchPromptDialog_okAction.Action.mnemonic = O
setMatchPromptDialog_cancelAction.Action.text = Cancel
setMatchPromptDialog_cancelAction.Action.mnemonic = C
setMatchPromptDialog_helpAction.Action.text = Help
setMatchPromptDialog_helpAction.Action.mnemonic = H

#DEFINE BUTTON prompt dialog
DefineButtonDialog.title = Define Button
DefineButtonDialog.enterFileNameCaption = Select image file to display on the button
DefineButtonDialog.enterCommandsCaption = Enter the commands to be associated with the button
DefineButtonDialog.enterBriefHelpCaption = Enter brief help for the button
DefineButtonDialog.enterDetailedHelpCaption = Enter more detailed help for the button (optional)
DefineButtonDialog.enableOnlyIfUsedCaption = Enable only if there are used characters
DefineButtonDialog.enableInAllModesCaption = Enable in ALL modes
DefineButtonDialog.enableInNormalModeCaption = Enable in Normal Mode only
DefineButtonDialog.enableInAdvancedModeCaption = Enable in Advanced mode only
DefineButtonDialog.insertSpaceCaption = Insert a space between buttons
DefineButtonDialog.removeAllCaption = Remove all buttons
DefineButtonDialog.inputValidationError = Missing value for image file, commands or brief help.
DefineButtonDialog.fileFilterDescription = Image Files (*.bmp, *.png) 
DefineButtonDialog_Browse.Action.text = Browse...
DefineButtonDialog_Browse.Action.mnemonic = B
DefineButtonDialog_OK.Action.text = OK
DefineButtonDialog_OK.Action.mnemonic = O
DefineButtonDialog_Cancel.Action.text = Cancel 
DefineButtonDialog_Cancel.Action.mnemonic = C 
DefineButtonDialog_Help.Action.text = Help
DefineButtonDialog_Help.Action.mnemonic = H

#DISPLAY IMAGES prompt dialog
DisplayImagesDialog.title = Display Images
DisplayImagesDialog.imageDisplayModeCaption = Image display mode:
DisplayImagesDialog.autoCaption = Auto
DisplayImagesDialog.manualCaption = Manual
DisplayImagesDialog.offCaption = Off
DisplayImagesDialog.generateReportCaption = Generate report:
DisplayImagesDialog.missingImageListCaption = Missing image list
DisplayImagesDialog.characterImageListCaption = Character image list
DisplayImagesDialog.taxonImageListCaption = Taxon image list
DisplayImagesDialog_OK.Action.text = OK
DisplayImagesDialog_OK.Action.mnemonic = O
DisplayImagesDialog_Cancel.Action.text = Cancel
DisplayImagesDialog_Cancel.Action.mnemonic = C
DisplayImagesDialog_Help.Action.text = Help
DisplayImagesDialog_Help.Action.mnemonic = H

#"Select Multiple Images" dialog
MultipleImagesDialog.title = Select Multiple Images
MultipleImagesDialog.allImagesCurrentTaxonCaption = All images of the current taxon
MultipleImagesDialog.firstImageSelectedTaxaCaption = First images of all selected taxa
MultipleImagesDialog.allImagesSelectedTaxaCaption = All images of all selected taxa
MultipleImagesDialog.closeAllOpenWindowsCaption = Close all open windows first
MultipleImagesDialog.selectBySubjectCaption = Select by subject
MultipleImagesDialog.noImagesForSpecifiedTaxaMsg = No images for the specified taxa.
MultipleImagesDialog_OK.Action.text = OK
MultipleImagesDialog_OK.Action.mnemonic = O
MultipleImagesDialog_Cancel.Action.text = Cancel
MultipleImagesDialog_Cancel.Action.mnemonic = C

# Simple Search Dialog
SimpleSearchDialog.title = Search
SimpleSearchDialog.enterStringCaption = Enter search string:
SimpleSearchDialog.informationDialogTitle = Information
SimpleSearchDialog.noMoreOccurrencesCaption = No more occurrences of ''{0}''
SimpleSearchDialog_Search.Action.text = Search
SimpleSearchDialog_Search.Action.mnemonic = S
SimpleSearchDialog_Cancel.Action.text = Cancel
SimpleSearchDialog_Cancel.Action.mnemonic = C

# DISPLAY IMAGES MISSING_IMAGE_LIST report messages
MissingImageList.CharacterImageFileNotFound = Image file ''{0}'' for character {1} does not exist.
MissingImageList.TaxonImageFileNotFound = Image file ''{0}'' for {1} does not exist.
MissingImageList.NoImages = There are no character or taxon images.
MissingImageList.AllPresent = All images present.

#DISPLAY IMAGES CHARACTER_IMAGE_LIST report  messages
CharacterImageList.NoImages = There are no character images.

#DISPLAY IMAGES TAXON_IMAGE_LIST report  messages
TaxonImageList.NoImages = There are no taxon images.

#Busy GlassPane
BusyGlassPane.icon = icons/ajax-loader.gif
BusyGlassPane.cancelButtonCaption = Cancel

ToolbarHelpDialog.title = Toolbar Help
ToolbarHelpDialog_OK.Action.text = OK
ToolbarHelpDialog_OK.Action.mnemonic = O

# Captions for STATUS directives
Status.title = Status 
Status.onValue = on
Status.offValue = off
Status.IncludeCharacters.title = *Status Include Characters
Status.IncludeCharacters.content = Include characters: {0}. {1}
Status.IncludeTaxa.title = *Status Include Taxa
Status.IncludeTaxa.content = Include taxa: {0}. {1}
Status.ExcludeCharacters.title = *Status Exclude Characters
Status.ExcludeCharacters.content = Exclude characters: {0}. {1}
Status.ExcludeTaxa.title = *Status Include Taxa
Status.ExcludeTaxa.content = Include taxa: {0}. {1}
Status.Files.title = *Status Files
Status.Files.heading = FILES
Status.Files.noFiles = There are no output files.
Status.Files.journalFile = journal: {0}
Status.Files.logFile = log: {0}
Status.Files.outputFile = output: {0}
Status.Set.title = *Status Set
Status.Set.heading = Set:
Status.Set.line1 = autotolerance {0}, stopbest={1}, rbase={2}, tolerance={3}, varywt={4}, demonstration {5}, imagepath={6}, infopath={7}
Status.Set.matchSettings = Match {0}
Status.Set.diagSettings = Diaglevel = {0}, Diagtype = {1}
Status.Set.reliabilities = Reliabilities: {0}
Status.Set.exactCharacters = EXACT characters: {0}
Status.Set.fixedCharacters = FIXED characters: {0}
Status.Set.emptyCharacterSet = none
Status.Set.inapplicablesMatchValue = inapplicables
Status.Set.unknownsMatchValue = unknowns
Status.Set.overlapMatchValue = overlap
Status.Set.exactMatchValue = exact
Status.Set.specimensDiagType = specimens
Status.Set.taxaDiagType = taxa
Status.Display.title = *Status Display
Status.Display.content = Display unknowns {0}, inapplicables {1}, comments {2}, continuous {3}, endidentify {4}, keywords {5}, log {6}, input {7}, numbering {8}, images {9}, scaled {10}
Status.Display.imageDisplayManual = manual
Status.Display.imageDisplayAuto = auto
Status.All.title = *Status All

```