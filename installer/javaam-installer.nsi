; JavaAm Windows Installer
; Built with NSIS (https://nsis.sourceforge.io/)
; To compile: makensis javaam-installer.nsi

!include "MUI2.nsh"
!include "x64.nsh"
!include "WinVer.nsh"

; Configuration
!define PRODUCT_NAME "JavaAm"
!define PRODUCT_VERSION "1.0.0"
!define PRODUCT_PUBLISHER "Amethyst Devs"
!define PRODUCT_WEB_SITE "https://github.com/amethystdevs/javaam"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"

; Installer attributes
Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile "javaam-${PRODUCT_VERSION}-installer.exe"
InstallDir "$PROGRAMFILES\JavaAm"
ShowInstDetails show
ShowUnInstDetails show

; MUI Settings
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

; Uninstaller pages
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

; Language
!insertmacro MUI_LANGUAGE "English"

; Installer sections
Section "Install"
    ; Check Java installation
    ClearErrors
    ReadRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
    ${If} ${Errors}
        MessageBox MB_YESNO|MB_ICONQUESTION "Java is required but not found.$\n$\nDo you want to download and install Java?" IDYES done IDNO notJava
        notJava:
            ExecShell "open" "https://www.oracle.com/java/technologies/downloads/"
            Abort "Java is required to use JavaAm"
        done:
    ${EndIf}
    
    SetOutPath "$INSTDIR"
    SetOverwrite try
    
    ; Copy main files from build artifacts
    File "..\build\javaam-compiler.jar"
    File "..\build\javaam.bat"
    
    ; Copy documentation
    SetOutPath "$INSTDIR\docs"
    File /r "..\docs\*.*"
    
    SetOutPath "$INSTDIR\examples"
    File /r "..\examples\*.*"
    
    SetOutPath "$INSTDIR"
    File "..\README.md"
    File "..\CHEATSHEET.md"
    File "..\LICENSE"
    
    ; Add installation folder to system PATH
    ReadRegExpandStr $0 HKLM "SYSTEM\CurrentControlSet\Control\Session Manager\Environment" "Path"
    ${If} $0 == ""
        StrCpy $0 "$INSTDIR"
    ${Else}
        StrCpy $0 "$0;$INSTDIR"
    ${EndIf}
    WriteRegExpandStr HKLM "SYSTEM\CurrentControlSet\Control\Session Manager\Environment" "Path" "$0"
    System::Call 'USER32::SendMessageTimeout(i 0xffff, i ${WM_SETTINGCHANGE}, i 0, t "Environment", i 0x0002, i 500, *i .r0)'

    ; Create Start Menu shortcuts
    CreateDirectory "$SMPROGRAMS\${PRODUCT_NAME}"
    CreateShortCut "$SMPROGRAMS\${PRODUCT_NAME}\JavaAm.lnk" "$INSTDIR\javaam.bat"
    CreateShortCut "$SMPROGRAMS\${PRODUCT_NAME}\Uninstall.lnk" "$INSTDIR\uninst.exe"
    CreateShortCut "$SMPROGRAMS\${PRODUCT_NAME}\Documentation.lnk" "$INSTDIR\README.md"
    
    ; Create registry entries for uninstall
    WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "DisplayName" "${PRODUCT_NAME} ${PRODUCT_VERSION}"
    WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\uninst.exe"
    WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
    WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
    WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
    
    ; Create uninstaller
    WriteUninstaller "$INSTDIR\uninst.exe"
    
    DetailPrint ""
    DetailPrint "Installation Complete!"
    DetailPrint "JavaAm is installed at: $INSTDIR"
    DetailPrint "You can now use: javaam compile file.jvm"
SectionEnd

; Uninstaller section
Section "Uninstall"
    ; Remove from PATH
    EnVar::DeleteValue "PATH" "$INSTDIR"
    
    ; Remove files and directories
    Delete "$INSTDIR\javaam-compiler.jar"
    Delete "$INSTDIR\javaam.bat"
    Delete "$INSTDIR\README.md"
    Delete "$INSTDIR\CHEATSHEET.md"
    Delete "$INSTDIR\LICENSE"
    Delete "$INSTDIR\uninst.exe"
    
    RMDir /r "$INSTDIR\docs"
    RMDir /r "$INSTDIR\examples"
    RMDir /r "$INSTDIR"
    
    ; Remove Start Menu shortcuts
    RMDir /r "$SMPROGRAMS\${PRODUCT_NAME}"
    
    ; Remove registry entries
    DeleteRegKey HKLM "${PRODUCT_UNINST_KEY}"
    
    DetailPrint "Uninstallation Complete!"
SectionEnd

; Uninstaller function
Function un.onUninstSuccess
    HideWindow
    MessageBox MB_ICONINFORMATION|MB_OK "JavaAm has been uninstalled successfully."
FunctionEnd
