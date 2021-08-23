set modname=%1
set modfile=%2

del C:\Users\erik\Desktop\Github\minecraft-server\mods\%modname%*.jar
copy %~dp0\%modname%\build\libs\%modfile% C:\Users\erik\Desktop\Github\minecraft-server\mods\%modfile%
git -C C:\Users\erik\Desktop\Github\minecraft-server add . 
git -C C:\Users\erik\Desktop\Github\minecraft-server\ commit -m "build commit"
git -C C:\Users\erik\Desktop\Github\minecraft-server\ push git@github.com:TheWorldAnchor/minecraft-server.git dev