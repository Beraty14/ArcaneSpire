Set objShell = CreateObject("WScript.Shell")
Set objFSO = CreateObject("Scripting.FileSystemObject")

' Get current directory
currentDir = objFSO.GetParentFolderName(WScript.ScriptFullName)

' Show Welcome Message
response = MsgBox("Arcane Spire oyununu masaustune kurmak (kisayol olusturmak) istiyor musunuz?", vbYesNo + vbQuestion, "Arcane Spire Kurulum Sihirbazi")

If response = vbYes Then
    ' Create Shortcut
    desktopPath = objShell.SpecialFolders("Desktop")
    Set objLink = objShell.CreateShortcut(desktopPath & "\Arcane Spire.lnk")
    
    objLink.TargetPath = currentDir & "\ArcaneSpire_Oyna.vbs"
    objLink.WorkingDirectory = currentDir
    objLink.IconLocation = currentDir & "\src\images\app_icon.ico"
    objLink.Save
    
    MsgBox "Kurulum Basariyla Tamamlandi!" & vbCrLf & vbCrLf & "Masaustunuzdeki 'Arcane Spire' ikonuna cift tiklayarak maceraya atilabilirsiniz. Iyi eglenceler!", vbInformation, "Kurulum Basarili"
End If
