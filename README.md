# mc-manhunt
A simple Manhunt plugin for servers.

## Description

The goal of this plugin is to experience a reverse manhunt! One player is randomly selected to become a hunter, while the others try to speedrun the game. The hunters goal is to stop them by any means necessary. When he dies, he gets to try again, while the others can just spectate after dying.

## Starting the game

To start the game, run the command 
```
/starthunt
```

## Building the mod locally

Use `./gradlew build` to build the mod file. The file can be located in `build/libs/HunterHardcore-1.0.jar`

## Server installation

In the root folder of the server, simply create a folder named `plugin` and add the build `.jar` file.

Make sure that the server is running Spigot
