# Software Prerequisites #
To checkout and build Open DELTA, the following software must be installed on your computer.
  1. Java 6+ _Please note that there are dependencies that currently prevent Open DELTA from being built using Open JDK.  Please use the Oracle JDK to build Open DELTA_
  1. A subversion client
  1. maven

# Standard build #

```
svn checkout http://open-delta.googlecode.com/svn/trunk/ open-delta-read-only
cd open-delta-read-only
mvn clean install
```

The output of this build will be the .zip and .tar.gz archives, located in open-delta-read-only/target

# Building the Windows Installers #
These steps must be executed on a computer running Windows.
1. Download and install the Nullsoft Scriptable Install System from http://nsis.sourceforge.net/Main_Page
2. Checkout and build the project as follows:
```
svn checkout http://open-delta.googlecode.com/svn/trunk/ open-delta-read-only
cd open-delta-read-only
mvn -Pwin-installer clean install
```

The output of this build will include the .zip and .tar.gz archives, as well as the Windows executables.

# Building the Mac OS Installer #
These steps must be executed on a computer running Mac OS.
1. Checkout and build the project as follows:
```
svn checkout http://open-delta.googlecode.com/svn/trunk/ open-delta-read-only
cd open-delta-read-only
mvn clean install
```
2. Create the .dmg as follows:
```
cd installer/mac_os
./updateDmg.sh
```

The output of this step will be a file called Open DELTA.dmg in the target directory.