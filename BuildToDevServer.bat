set modname=%1
set modfile=%2
set ip=70.35.203.229

del C:\Users\erik\Desktop\Github\Fabric_Server_Mod_Jars\%modname%*.jar
copy %~dp0\%modname%\build\libs\%modfile% C:\Users\erik\Desktop\Github\Fabric_Server_Mod_Jars\%modfile%
git -C C:\Users\erik\Desktop\Github\Fabric_Server_Mod_Jars add . 
git -C C:\Users\erik\Desktop\Github\Fabric_Server_Mod_Jars\ commit -m "build commit"
git -C C:\Users\erik\Desktop\Github\Fabric_Server_Mod_Jars\ push git@github.com:TheWorldAnchor/Fabric_Server_Mod_Jars.git


start /d "C:\Program Files (x86)\Minecraft Launcher\" MinecraftLauncher.exe