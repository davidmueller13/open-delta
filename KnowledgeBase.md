

# Issues with multi monitor software such as MaxiVista (Windows only) #

Remote display server software, such as MaxiVista, can cause problems with Delta due to the way that Java determines if hardware acceleration can be used to render windows. If IntKey, for example, is started on a 'local' display (i.e. one that is attached directly to the computer running IntKey), it is likely that Java can use hardware accelerated graphics (assuming the video card has support for DirectDraw). If the IntKey window is then moved to a 'remote' display (a monitor plugged into a different computer, but logically attached via display server software), hardware acceleration is unavailable, and a series of exceptions are cascaded, essentially crashing the application until the window is moved back to a local display. Likewise, problems can also arise if the application first starts on a 'remote' display, and is then moved to a local display. The solution to this problem is to start the Delta applications with the following command line switch applied, which will prevent Java from using Direct Draw (hardware acceleration).

```
-Dsun.java2d.noddraw=true
```

See Specifying JVM options to windows executables for more information.

# Specifying JVM options to executables (Windows Only) #

In order to pass JVM options (such as max heap size etc.) to the Delta programs that are launched via Windows you need to create a file in the same location as the executable, with the same name, but with the extension `.l4j.ini` instead of `.exe`.

**Note**: If you have installed Delta for "All users" on Windows Vista or later (by default under `C:\program files\Open Delta` or `c:\program files (x86)\Open Delta`), you will need elevated permissions to create the file. The easiest way to do this is to start your text editor (for example, Notepad.exe) "as administrator". For example, find notepad.exe and right click and select "Run as administrator". Then save the file under the same folder where `IntKey.exe` is located.

For example, to set the maximum memory heap size available to IntKey you would need to create a file called `IntKey.l4j.ini` and add the following line:

```
-Xmx1024m
```

# "Unable to run program for file" error when running Action Sets #

For large DELTA data sets, the default JVM memory size can be insufficient to run some CONFOR actions using the Action Sets dialog.

To fix this problem, increase the memory available to the DELTA Editor by adding -Xmx1024m to the `DeltaEditor.l4j.ini` as described in "Specifying JVM options", above.