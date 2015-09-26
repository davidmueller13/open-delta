# Open DELTA Project Roadmap #


---


**Updated 27/04/2012**
_Changes from the original plan have been highlighted in the ~~strikethrough~~ font._

The release plan from the release candidate 1 release through to the 1.0 final build is as follows:
  * Important bug fixes identified in the release candidate release will be addressed.
  * If there are a lot of changes made, a release candidate 2 release may be considered before releasing a 1.0 final.

After 1.0 has been released, development resources will be limited and unfortunately only critical bug fixes will able to be addressed.



---


The release plan is to publish a series of monthly milestone (alpha) releases, each designated with an 'M' number, until the software is generally feature complete, at which time it will be considered in 'beta'. Once all open issues with beta releases have been addressed, the releases will be designated as Release Candidate (RC) until such time as the software is considered complete and stable, and designated as the Version 1.0 General Availability (GA) build.

**Note:** The following release schedule is provisional, and may deviate based on what is actually achieved each milestone.

![http://open-delta.googlecode.com/svn/wiki/RoadMap.attach/Delta_RoadMap.png](http://open-delta.googlecode.com/svn/wiki/RoadMap.attach/Delta_RoadMap.png)


<table border='1'>
<tr><td><h3>Milestone</h3></td><td><h3>Release Date</h3></td><td><h3>Description</h3></td></tr>
<tr><td valign='top'> <b>M1</b> </td><td valign='top'>	16/03/2011 </td>
<td>
Technology proof of concept, project framework setup:<br>
<ul><li>Development environment setup.  Google code repository, continuous integration build, distribution model.<br>
</li><li>DELTA Editor, read only data.<br>
</li><li>Help demonstration, including context sensitive help, tooltips.<br>
</li><li>About dialog with acknowledgements<br>
</li><li>Internationalisation.  Use of UTF-8 character encoding and resource bundles for all displayed text.<br>
</li><li>Save, Save As functions.<br>
</td>
</tr></li></ul>

<tr>
<td valign='top'><b>M2</b></td><td valign='top'>20/04/2011</td>
<td>
DELTA Editor:<br>
<ul><li>Edit existing item attributes.<br>
</li><li>Finish off read only view components.<br>
IntKey:<br>
</li><li>Project establishment.<br>
</li><li>Read file format.<br>
</li><li>Main window UI.<br>
</td>
</tr></li></ul>

<tr><td valign='top'><b>M3</b></td><td valign='top'>18/05/2011</td><td>DELTA Editor:<br>
<ul><li>Item editor<br>
</li><li>Add / Move / Edit / Delete Items<br>
IntKey:<br>
</li><li>Read IntKey file format<br>
</li><li>Command interpreter infrastructure<br>
CONFOR:<br>
</li><li>Parsing infrastructure, ability to read DELTA chars / specs / items files.<br>
</td>
</tr>
<tr>
<td valign='top'><b>M4</b></td><td valign='top'>15/06/2011</td>
<td>
DELTA Editor:<br>
</li><li>Character editor<br>
</li><li>Add / Move / Edit / Delete Characters<br>
IntKey:<br>
</li><li>Use command (this provides much of the functionality of intkey in normal mode).<br>
CONFOR:<br>
</li><li>Parsing infrastructure and general purpose directives<br>
</li><li>Read / Write DELTA files<br>
</td>
</tr>
<tr>
<td valign='top'><b>M5</b></td><td valign='top'>20/07/2011</td>
<td>
DELTA Editor:<br>
</li><li>Implement the Controlling / Controlled by attributes editors<br>
</li><li>Add / Remove controlling attributes<br>
</li><li>Import / Export DELTA files<br>
</li><li>Image Viewer / Hotspot selector<br>
IntKey:<br>
</li><li>BEST algorithm<br>
</li><li>RTF report infrastructure<br>
</li><li>Differences command<br>
</td>
</tr>
<tr>
<td valign='top'><b>M6</b></td><td valign='top'>17/08/2011</td>
<td>
DELTA Editor:<br>
</li><li>Action Sets (to the availability of CONFOR functions)<br>
</li><li>Image Editor<br>
IntKey:<br>
</li><li>Useable in Normal Mode<br>
</li><li>Commands accessible in normal mode completed.<br>
CONFOR:<br>
</li><li>Translate into Intkey format<br>
</td>
</tr></li></ul>

<tr>
<td valign='top'><b>M7</b></td><td valign='top'>14/09/2011</td>
<td>
DELTA Editor:<br>
<ul><li>Integrate the image editor.<br>
</li><li>Image settings dialog<br>
</li><li>Complete for BETA (search, help, fonts etc)<br>
</li><li>Testing<br>
IntKey:<br>
</li><li>Implementation of advanced mode commands.<br>
CONFOR:<br>
</li><li>Add state qualification handling (enhancement)<br>
</li><li>Handle multi-language output<br>
</li><li>Output to natural language format<br>
</td>
</tr></li></ul>

<tr>
<td valign='top'><b>M8</b></td><td valign='top'>19/10/2011</td>
<td>
IntKey:<br>
<ul><li>Advanced Mode functions complete<br>
</li><li>IntKey BETA ready<br>
CONFOR:<br>
</li><li>Output to KEY format<br>
</li><li>Integration test suite<br>
</td>
</tr></li></ul>

<tr>
<td valign='top'><b>M9</b></td><td valign='top'>16/11/2011</td>
<td>
KEY<br>
<ul><li><del>KEY implemented</del>
CONFOR<br>
</li><li>Output to other formats<br>
</td>
</tr></li></ul>

<tr>
<td valign='top'><b>1.0-BETA</b></td><td valign='top'>14/12/2011</td>
<td>
<ul><li><del>NSim</del>
</li><li><del>PClass</del>
</li><li>DIST<br>
</li><li>Tidy up any outstanding items<br>
</li><li>Main software feature complete<br>
</td>
</tr></li></ul>

<tr>
<td><b>1.0-RC1</b></td><td><del>01/04/2012</del> 27/07/2012</td>
<td>
<ul><li>Key implemented<br>
</li><li>Bug fixes from previous release</td>
</tr>
<tr>
<td><b>1.0-RC2</b></td><td>01/08/2012</td>
<td>
</li><li><a href='https://code.google.com/p/open-delta/issues/detail?id=120'>ISSUE 120</a> - Ensure the software is in a position to be able to be translated into other languages.<br>
</li><li><a href='https://code.google.com/p/open-delta/issues/detail?id=41'>ISSUE 41</a> - DELTA Editor does not support importing items into an existing data set.<br>
</li><li><a href='https://code.google.com/p/open-delta/issues/detail?id=142'>ISSUE 142</a> - 	Some directive file templates don't import correctly when a new dataset is created<br>
</li><li><a href='https://code.google.com/p/open-delta/issues/detail?id=144'>ISSUE 144</a> - Opening the character editor on Ubuntu 11 triggers a spurious validation error.<br>
</td>
</tr></li></ul>

<tr>
<td><b>1.0-GA</b></td><td><del>30/06/2012</del> 31/08/2012</td>
<td>
<ul><li>Fixes for important Issues found in the Release Candidate<br>
</td>
</tr></li></ul>

<tr>
<td><b>Post 1.0</b></td><td></td>
<td>
<ul><li>Critical bug fixes from 1.0 if they arise.<br>
</li><li><a href='https://code.google.com/p/open-delta/issues/detail?id=135'>ISSUE 135</a> - Implement a "Present by misinterpretation" feature<br>
</td>
</tr></li></ul>

</table>