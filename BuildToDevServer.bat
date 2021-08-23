set modname=%1
set modfile=%2

if not x%modfile:-fabric-mc=%==x%modfile% GOTO FABRIC
if not x%modfile:-forge-mc=%==x%modfile% GOTO FORGE
:AFTER
git -C C:\Users\erik\Desktop\Github\minecraft-server add . 
git -C C:\Users\erik\Desktop\Github\minecraft-server\ commit -m "build commit"
git -C C:\Users\erik\Desktop\Github\minecraft-server\ push git@github.com:TheWorldAnchor/minecraft-server.git dev
EXIT

:FABRIC
del C:\Users\erik\Desktop\Github\minecraft-server\mods\fabric\%modname%*.jar
copy %~dp0fabric\%modname%\build\libs\%modfile% C:\Users\erik\Desktop\Github\minecraft-server\mods\fabric\%modfile%
GOTO AFTER

:FORGE
del C:\Users\erik\Desktop\Github\minecraft-server\mods\forge\%modname%*.jar
copy %~dp0forge\%modname%\build\libs\%modfile% C:\Users\erik\Desktop\Github\minecraft-server\mods\forge\%modfile%
GOTO AFTER