# FernAPI
This is meant to be an api for my sponge and plugins. This plugin is compiled in 1.8.9, but should work regaurdless of your mod/plugin's version unless it uses outdated libraries or other means of change.

[![Release](https://jitpack.io/v/Fernthedev/FernAPI.svg)](https://jitpack.io/#Fernthedev/FernAPI)
[![CircleCI](https://circleci.com/gh/Fernthedev/FernAPI.svg?style=svg)](https://circleci.com/gh/Fernthedev/FernAPI)

[Installation](https://github.com/Fernthedev/FernAPI#installation)

[Features](https://github.com/Fernthedev/FernAPI#features)

[Usage](https://github.com/Fernthedev/FernAPI#usage)

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

[u] = All of the above, in other words universal

- [UUID Fetch](https://github.com/Fernthedev/FernAPI#uuid-fetch) [u]
- [ChatAPI](https://github.com/Fernthedev/FernAPI#chatapi) [u]
  - Clickable text
  - Hover message
  - Color code support
- [List sorter (Sorter Class, check methods)](src/main/java/com/github/fernthedev/fernapi/universal/Sorter.java) [u]
- [Bungee/Spigot/Sponge plugin messaging](https://github.com/Fernthedev/FernAPI#bungeespigot-plugin-messaging-must-implement-pluginmessagehandler) [u]
- [MySQL](https://github.com/Fernthedev/FernAPI#mysql) [u]
- [Universal Commands](https://github.com/Fernthedev/FernAPI#universal-commands) [u]

## Usage:
Main class should extend one of these classes respectively.
```
FernBungeeAPI
FernSpongeAPI
FernSpigotAPI
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
IFPlayer fPlayer = Universal.convertPlayerObjectToFPlayer(player);

TextMessage textMessage = new TextMessage("Message with color code");
textMessage.setClickData(new ClickData(ClickData.Action.SUGGEST_COMMAND,"/example"));
textMessage.setHoverData(new HoverData(HoverData.Action.SHOW_TEXT,"hover text with color code"));
fPlayer.sendChatMessage(textMessage);
```

### PlaceHolderAPI (Bungee)
```java
AskPlaceHolder askPlaceHolder = new AskPlaceHolder(Player,PlaceHolder);

askPlaceHolder.setRunnable(new MessageRunnable() {
    @Override
    public void run() {
        super.run();
        //Whatever you want.
        sender.sendMessage("The player's placeholder value of " + args[1] + " is " + askPlaceHolder.getPlaceHolderResult());
    }
});
```

### Bungee/Spigot Plugin Messaging (Must implement PluginMessageHandler)
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

        channels.add(new Channel("pluginName:channel",Channel.ChannelAction.BOTH));

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
        //Handling extra data is so:
        String data1 = in.readUTF(); //DATA1
        String data2 = in.readUTF(); //DATA2
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

data = new PluginMessageData(stream,"Forward",player.getServer().getInfo().getName(),Sub channel such as "Channel1",Channel you defined in your getChannels() method "pluginName:channel");

data.addData("Data1");

data.addData("Data2"); //MESSAGE 2

Universal.getMessageHandler().sendPluginData(fplayer,data);
```

### MySQL
Example usage can be found [here](https://github.com/Fernthedev/FernAPI/tree/master/com.github.fernthedev.fernapi-common/src/main/java/com/github/fernthedev/com.github.fernthedev.fernapi/universal/examples)

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
