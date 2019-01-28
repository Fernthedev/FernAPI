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
- List sorter (Sorter Class, check methods) [u]
- Bungee/Spigot plugin messaging [b/s]

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
        //Whatever you want.
        sender.sendMessage("The player's placeholder value of " + args[1] + " is " + askPlaceHolder.getPlaceHolderResult());
    }
});
```

Bungee/Spigot Plugin Messaging (Must implement PluginMessageHandler)
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

data = new PluginMessageData(stream,"Forward",player.getServer().getInfo().getName(),Subchannel such as "pluginName:channel");

data.addData("Data1");

data.addData("Data2"); //MESSAGE 2

Universal.getMessageHandler().sendPluginData(fplayer,data);
```
