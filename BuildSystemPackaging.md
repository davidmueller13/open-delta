# Introduction #

The Open DELTA software uses the [maven](http://maven.apache.org) build system.  The code is organised into maven modules, the purpose of each is described below.

### delta-common ###
The delta-common module contains:
  * The DeltaDataSet model used by many of the other programs to represent a data set.
  * The code to parse files in the DELTA directive file format and execute CONFOR directives.
  * The BEST algorithm used by the Intkey and Key programs.

### ui-common ###
The ui-common module contains:
  * Framework code shared by the DELTA Editor and Inkey.
  * Shared user interface components.
  * The Image and image overlay viewer.

### delta-editor ###
The delta-editor module contains:
  * The code to read and write the binary format (.dlt) used by the DELTA Editor.
  * The DELTA Editor user interface and associated functionality.

### intkey ###
The intkey module contains:
  * The code to read the Intkey binary format (.ink).
  * The Intkey user interface.

### confor ###
The confor module contains the CONFOR program.

### key ###
The key module contains the KEY program.

### dist ###
The dist module contains the DIST program.

### delfor ###
The delfor module contains the DEFFOR program.