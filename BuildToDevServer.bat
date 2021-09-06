set modname=%1
set modfile=%2

if not x%modfile:-fabric-mc=%==x%modfile% GOTO FABRIC
if not x%modfile:-forge-mc=%==x%modfile% GOTO FORGE
:AFTER
git -C %~dp0minecraft-server add . 
git -C %~dp0minecraft-server\ commit -m "build commit"
git -C %~dp0minecraft-server\ push git@github.com:TheWorldAnchor/minecraft-server.git dev
EXIT

:FABRIC
del %~dp0minecraft-server\mods\fabric\%modname%*.jar
copy %~dp0fabric\%modname%\build\libs\%modfile% %~dp0minecraft-server\mods\fabric\%modfile%
GOTO AFTER

:FORGE
del %~dp0minecraft-server\mods\forge\%modname%*.jar
copy %~dp0forge\%modname%\build\libs\%modfile% %~dp0minecraft-server\mods\forge\%modfile%
GOTO AFTER