### PlaceHolderAPI (Bungee/Velocity)
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