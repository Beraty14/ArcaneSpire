Set WMP = CreateObject("WMPlayer.OCX")
WMP.URL = WScript.Arguments(0)
WMP.settings.volume = 30
WMP.controls.play
While WMP.playState <> 1
  WScript.Sleep 50
Wend
