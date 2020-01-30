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
