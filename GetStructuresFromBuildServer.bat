del /f %~dp0fabric\world-anchor-structures\src\main\resources\data\world-anchor-structures\structures\*
scp -rpP 958 play.worldanchor.com:/opt/minecraft/build/world/generated/world-anchor-structures/structures/* %~dp0fabric\world-anchor-structures\src\main\resources\data\world-anchor-structures\structures
