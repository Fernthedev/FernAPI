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