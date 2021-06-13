set modname=%1
set modfile=%2

del C:\Users\erik\Desktop\Github\fabric-server\mods\%modname%*.jar
copy %~dp0\%modname%\build\libs\%modfile% C:\Users\erik\Desktop\Github\fabric-server\mods\%modfile%
git -C C:\Users\erik\Desktop\Github\fabric-server add . 
git -C C:\Users\erik\Desktop\Github\fabric-server\ commit -m "build commit"
git -C C:\Users\erik\Desktop\Github\fabric-server\ push git@github.com:TheWorldAnchor/fabric-server.git dev