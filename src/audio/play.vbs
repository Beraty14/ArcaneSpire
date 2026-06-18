Set WMP = CreateObject("WMPlayer.OCX")
WMP.URL = WScript.Arguments(0)
WMP.settings.setMode "loop", True
WMP.settings.volume = 14
WMP.controls.play
While True
  WScript.Sleep 1000
Wend
