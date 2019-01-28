# FernAPI
This is meant to be an api for my forge mods (or other's) and plugins. This plugin is compiled in 1.8.9, but should work regaurdless of your mod/plugin's version unless it uses outdated libraries or other means of change.

[Installation](https://github.com/Fernthedev/FernAPI#installation)

[Features](https://github.com/Fernthedev/FernAPI#features)

[Usage](https://github.com/Fernthedev/FernAPI#usage)

## Installation
To install this, you may either clone this repository and run 
`clean publishToMavenLocal` to add it to your local maven repository. You can also add it as a jar through 
```xml
<dependency>
    <groupId>com.github.fernthedev</groupId>
    <artifactId>fernapi</artifactId>
    <version>LATEST</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/path</systemPath>
</dependency>
```

## Features:
[f] = Forge

[b] = Bungee

[s] = Spigot

[u] = All of the above, in other words universal

- UUID Fetch [u]
- ChatAPI [u]
  - Clickable text
  - Hover message
  - Color code support

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
// Can only convert ProxiedPlayer, org.bukkit.entity.Player and/or EntityPlayer
IFPlayer fPlayer = Universal.convertPlayerObjectToFPlayer(player);

TextMessage textMessage = new TextMessage("Message with color code");
textMessage.setClickData(new ClickData(ClickData.Action.SUGGEST_COMMAND,"/example"));
textMessage.setHoverData(new HoverData(HoverData.Action.SHOW_TEXT,"hover text with color code"));
fPlayer.sendChatMessage(textMessage);
```

PlaceHolderAPI (Bungee)
```java
AskPlaceHolder askPlaceHolder = new AskPlaceHolder(Player,PlaceHolder);

askPlaceHolder.setRunnable(new MessageRunnable() {
    @Override
    public void run() {
        super.run();
        sender.sendMessage("The player's placeholder value of " + args[1] + " is " + askPlaceHolder.getPlaceHolderResult());
    }
});
```
