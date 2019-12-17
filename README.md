# FernAPI
This plugin is compiled in 1.14.4, but should work regaurdless of your plugin's version unless it uses outdated libraries or other means of change.

This API allows for use of code that should work accross all server platforms. It aims to allow developers to create better plugins without having incosistency across platforms and ease their workflow and project workspaces.
This API also contains some useful features such as a MySQL Java Wrapper and some useful code for commands such as argument auto-complete helper methods and a UUID fetcher with other features not mentioned.

[![Release](https://jitpack.io/v/Fernthedev/FernAPI.svg)](https://jitpack.io/#Fernthedev/FernAPI)
[![CircleCI](https://circleci.com/gh/Fernthedev/FernAPI.svg?style=svg)](https://circleci.com/gh/Fernthedev/FernAPI)
[![Jitpack](https://jitci.com/gh/Fernthedev/FernAPI/svg)](https://jitci.com/gh/Fernthedev/FernAPI)
[![Build Status](https://dev.azure.com/Fernthedev/FernAPI/_apis/build/status/Fernthedev.FernAPI?branchName=master)](https://dev.azure.com/Fernthedev/FernAPI/_build/latest?definitionId=5&branchName=master)
![](https://github.com/Fernthedev/FernAPI/workflows/Java%20CI/badge.svg)

[Installation](#installation)

[Features](#features)

[Usage](#usage)

## Installation

### Non-Local Installation
#### Maven:
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

```xml
<dependency>
    <groupId>com.github.Fernthedev</groupId>
    <artifactId>FernAPI</artifactId>
    <!-- Tag can be found in releases at github repository -->
    <version>Tag</version>
</dependency>
```

#### Gradle:
```gradle
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```

```gradle
dependencies {
//Tag can be found in releases at github repository
        implementation 'com.github.Fernthedev:FernAPI:Tag'
}
```

### Local Installation
To install this, you may either clone this repository and run 
`clean publishToMavenLocal` to add it to your local maven repository. You can also add it as a jar through 
```xml
<dependency>
    <groupId>fernthedev</groupId>
    <artifactId>com.github.fernthedev.fernapi</artifactId>
    <version>LATEST</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/path</systemPath>
</dependency>
```

### Shading
After you have added it as a dependency, it is required to shade the library. To do this,you can use other methods if you liked, I suggest these:
For gradle: https://github.com/johnrengelman/shadow

For maven: https://maven.apache.org/plugins/maven-shade-plugin/usage.html

## Features:
[SP] = Sponge

[b] = Bungee

[s] = Spigot

[vat] = Velocity (Alpha support, not tested)

[ve] = Velocity (Should work)

[u] = All of the above, in other words com.github.fernthedev.fernapi.com.github.fernthedev.fernapi.universal

- [UUID Fetch](#uuid-fetch) [u] [ve]
- [ChatAPI](#chatapi) [u] [vat]
  - Clickable text
  - Hover message
  - Color code support
- [List sorter (Sorter Class, check methods)](https://github.com/Fernthedev/FernAPI/blob/master/src/main/java/com/github/fernthedev/fernapi/com.github.fernthedev.fernapi.com.github.fernthedev.fernapi.universal/Sorter.java) [u]
- [Bungee/Spigot/Sponge/Velocity plugin messaging](#bungeespigotspongevelocity-plugin-messaging-must-implement-pluginmessagehandler) [u*] [vat]
    - *Sponge has not been fully tested, please feel free to report any bugs at issues at repo.
- [MySQL](#mysql) [u] [vat]
- [Universal Commands](#com.github.fernthedev.fernapi.com.github.fernthedev.fernapi.universal-commands) [u] [vet]

## Usage:
Main class should extend one of these classes respectively.
```
FernBungeeAPI
FernSpongeAPI
FernSpigotAPI
FernVelocityAPI
```
[Bungee/Spigot Required] This is required in order for the api to work in spigot and/or bunge
```java

public void onEnable() {
super.onEnable();
}
```
For sponge, you should have 
```java
    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    super.onServerStart(event);
    }
```
For Velocity
```java
    public FernVelocityAPI(ProxyServer server, Logger logger) {
        super(server, logger);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        super.onProxyInitialization(event);
        // Do some operation demanding access to the Velocity API here.
        // For instance, we could register an event:
    }
```

To get a fplayer instance, run 
```java
// Can only convert ProxiedPlayer, org.bukkit.entity.Player and/or EntityPlayer
IFPlayer fPlayer = Universal.convertPlayerObjectToFPlayer(player);
```

### UUID Fetch:
```java
String uuid = UUIDFetcher.getUUID(Name);
String name = UUIDFetcher.getName(UUID);
List<UUIDFetcher.PlayerHistory> names = UUIDFetcher.getNameHistory(uuidPlayer);
```

### ChatAPI
```java
// Can only convert ProxiedPlayer, org.bukkit.entity.Player and/or EntityPlayer
IFPlayer fPlayer = Universal.getMethods().convertPlayerObjectToFPlayer(player);
// Or
IFPlayer fPlayer = Universal.getMethods().getPlayerFromName(String name);
IFPlayer fPlayer = Universal.getMethods().getPlayerFromUUID(UUID uuid);

TextMessage textMessage = new TextMessage("Message with color code");
textMessage.setClickData(new ClickData(ClickData.Action.SUGGEST_COMMAND,"/example"));
textMessage.setHoverData(new HoverData(HoverData.Action.SHOW_TEXT,"hover text with color code"));
fPlayer.sendChatMessage(textMessage);
```

### PlaceHolderAPI (Bungee)
```java
AskPlaceHolder velocityAskPlaceHolder = new AskPlaceHolder(Player,PlaceHolder);

velocityAskPlaceHolder.setRunnable(new MessageRunnable() {
    @Override
    public void run() {
        super.run();
        //Whatever you want.
        sender.sendMessage("The player's placeholder value of " + args[1] + " is " + velocityAskPlaceHolder.getPlaceHolderResult());
    }
});
```

### Bungee/Spigot/Sponge/Velocity Plugin Messaging (Must implement PluginMessageHandler)
```java
public class Test extends PluginMessageHandler {

    /**
     * This is the channel name that will be registered incoming and outcoming
     * This is where you specify the channels you want to listen to
     * Just make a new List<Channel> instance and add an instance of the channel accordingly.
     *
     * @return The channels that will be incoming and outgoing
     * @see AskPlaceHolder as an example
     */
    @Override
    public List<Channel> getChannels() {
        List<Channel> channels = new ArrayList<>();

        channels.add(new Channel(namespace,channelName,Channel.ChannelAction.BOTH));

        return channels;
    }

    /**
     * The event called when message is received from the channels registered
     *
     * @param data The data received for use of the event.
     * @param channel The channel it was received from, for use of multiple channels in one listener
     */
    @Override
    public void onMessageReceived(PluginMessageData data, Channel channel) {
        //Whatever you want
        // data Contains all the data you would need.
        // Extra data is so:
        for(String extraData : data.getExtraData()) {
            
        }
        //You read the data in the order you add it, so if you add DATA1 first, you read it first, then second DATA2 etc.
        //This works for both adding to output stream or using data.addData();
    }
}
```
To register the listener, run 
```java
Universal.getMessageHandler().registerMessageHandler(new Test());
```
To receive messages, the sender also has to use the API. This is an example how:
```java
ByteArrayOutputStream stream = new ByteArrayOutputStream();
DataOutputStream out = new DataOutputStream(stream);

data = new PluginMessageData(stream,player.getServer().getInfo().getName() or "All" or ServerInfo.getName(),SubChannel within pluginChannel,new Channel(namespace, pluginChannel, ChannelAction));

data.addData("Data1");

data.addData("Data2"); //MESSAGE 2

Universal.getMessageHandler().sendPluginData(fplayer,data);
```

### MySQL
Example usage can be found [here](https://github.com/Fernthedev/FernAPI/blob/master/src/main/java/com/github/fernthedev/fernapi/com.github.fernthedev.fernapi.com.github.fernthedev.fernapi.universal/examples)

Use the DatabaseManager class for managing tables, and TableInfo for grabbing the data.
RowData class contains a list of ColumnData which contains the data.
[My FernCommands plugin](https://github.com/Fernthedev/FernAPI) can also be used as an example of MySQL.

### Universal Commands
Universal Commands are commands that can be registered with the same class and code in spigot, sponge and bungee without necessarily adding server specific code, but that can be done anyways.
```java
public class UC extends UniversalCommand() {
    @Override
    public void execute(CommandSender sender, String[] args) {

    }
};
```
and register with 
```java
Universal.getCommandHandler().registerFernCommand(universalCommand);
```
