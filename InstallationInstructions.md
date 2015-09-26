
---

## Please backup your DELTA files before using them with this application ##


---


## Installing the Open DELTA software ##

Releases of the DELTA software are available in the [Downloads](http://code.google.com/p/open-delta/downloads/list) section of this web site.

Currently we distribute the Open DELTA software as either a .zip/.tar.gz archive (all platforms) or as an installer (Microsoft Windows or Mac OS X).


## Windows ##

### If you have a Java 6 or 7 runtime environment installed ###

  1. Download and run http://downloads.ala.org.au/p/Open%20DELTA/open-delta-1.02-Installer-NOJRE.exe

### If you do not have a Java 6 or 7 runtime environment installed or do not know if you have a Java 6 or 7 runtime installed ###

  1. Download and run http://downloads.ala.org.au/p/Open%20DELTA/open-delta-1.02-Installer.exe

### Choosing which users to install the application for ###
The installation wizard provides you with the choice to either install Open DELTA for anyone using the computer,
or only for the current user:

![http://open-delta.googlecode.com/svn/wiki/InstallationInstructions.attach/ChooseInstallMode.png](http://open-delta.googlecode.com/svn/wiki/InstallationInstructions.attach/ChooseInstallMode.png)

You should choose to install only for the current user if:
  * You do not have administrator rights on the computer
  * The other users of the computer will not want to use Open DELTA

### Running Open DELTA ###
The tools in the Open DELTA suite can be run from the Windows Start Menu under _All Programs/Open DELTA Suite_.

### Uninstalling Open DELTA ###
An uninstallation wizard is provided on windows. The uninstallation wizard can be run by selecting _Uninstall Open DELTA Suite_ from the Windows
Start Menu under _All Programs/Open DELTA Suite_. It can also be run from the Control Panel under _Programs and Features_ or _Add/Remove Programs_
on earlier versions of Windows.


## Mac OS (version 10.6.8 and above) ##
  1. Download  http://downloads.ala.org.au/p/Open%20DELTA/Open%20DELTA.dmg to your computer.
  1. Double click on the file in the Finder application.
  1. In the window that opens, drag the "Open DELTA" folder into the "Applications" folder.
  1. If you are using OS X 10.8 you may encounter an error ""Delta Editor" can't be opened because it is from an unidentified developer".  If you encounter this error, you can hold down the "control" key before clicking on the Application to be presented with a different dialog that allows you to proceed.
  * Please note there is a bug in the installer that prevents the file associations from working.**See [ISSUE 205](https://code.google.com/p/open-delta/issues/detail?id=205) for more information.**



## Other Platforms ##
You must have a Java 6 or 7 runtime environment installed on your computer.

  1. Download either http://downloads.ala.org.au/p/Open%20DELTA/open-delta-1.02-bin.zip or http://downloads.ala.org.au/p/Open%20DELTA/open-delta-1.02-bin.tar.gz to your computer.
  1. Open a terminal and navigate to the directory you downloaded the archive to.
  1. Use your favourite archive extracting tool or use the command below to extract the contents of the archive:
```
tar xvfz open-delta-1.02-bin.tar.gz
```

Once the files have been extracted, the shell scripts will not be executable by default.
  1. Open a terminal and change to the directly where you extracted the archive.
  1. Execute the following commands to make the scripts executable.
```
cd open-delta-1.02/bin
chmod u+x *.sh
```


To run the DELTA Editor
```
./DeltaEditor.sh
```
To run Intkey:
```
./Intkey.sh
```
To run CONFOR:
```
./CONFOR.sh <directive file name>
```