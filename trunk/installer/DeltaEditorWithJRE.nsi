!include .\DeltaEditorMain.nsi

OutFile "..\target\${OUTPUT-FILE-NAME}"

Function CustomAddFiles
    ; Include the JRE
    File /r "$%JAVA_HOME%\jre"
FunctionEnd

Function un.CustomRemoveFiles
    ; Remove the JRE
    RmDir /r /REBOOTOK "$INSTDIR\jre"
FunctionEnd