# Usage

## Required Starting point
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

[UUID Fetch:](features/uuid_fetch.md)
---

[ChatAPI](features/chatapi.md)
---

[Bungee/Spigot/Sponge/Velocity Plugin Messaging (Must implement PluginMessageHandler)](features/plugin_messaging.md)
---

[MySQL](features/mysql.md)
---

[Universal Commands](features/ucommand.md)
---

