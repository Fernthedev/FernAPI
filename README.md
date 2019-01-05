# FernAPI
This is meant to be an api for my forge mods (or other's) and plugins. This plugin is compiled in 1.8.9, but should work regaurdless of your mod/plugin's version unless it uses outdated libraries or other means of change.

[f] = Forge

[b] = Bungee

[s] = Spigot

[u] = All of the above, in other words universal

# Features:
- UUID Fetch [u]

## Usage:
Main class should extend one of these classes respectively.
```
FernBungeeAPI
FernForgeAPI
FernSpigotAPI
```
[Bungee/Spigot Required] This is required in order for the api to work in spigot and/or bunge
```java

public void onEnable() {
super.onEnable();
}
```

UUID Fetch:
```java
String uuid = UUIDFetcher.getUUID(Name);
String name = UUIDFetcher.getName(UUID);
List<UUIDFetcher.PlayerHistory> names = UUIDFetcher.getNameHistory(uuidPlayer);
```

ChatAPI
```java
//This will vary depending on the server.
//SPIGOT
SpigotFPlayer spigotFPlayer = new SpigotFPlayer(player);
//FORGE
ForgeFPlayer forgeFPlayer = new ForgeFPlayer(player);
//BUNGEE
BungeeFPlayer bungeeFPlayer = new ForgeFPlayer(player);


ChatMessage chatMessage = new ChatMessage("Message with color code");
chatMessage.setClickData(new ChatMessage.ClickData(ClickAction.SUGGEST_COMMAND,"/example"));
chatMessage.setHoverData(new ChatMessage.HoverData(HoverAction.SHOW_TEXT,"hover text with color code"));
fPlayer.sendChatMessage(chatMessage);
