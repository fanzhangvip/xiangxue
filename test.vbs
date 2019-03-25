Set wmi = GetObject("winmgmts:")
Set collection = wmi.ExecQuery("select * from Win32_Process")
For Each process in collection
WScript.Echo process.getObjectText_
Next