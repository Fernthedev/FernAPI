If you get an exception named such as IncorrectSetupException, it means you have not setup the api correctly.
To fix this, you have to have your main plugin/mod class extend one of 3 classes (they already extend their API class like JavaPlugin for spigot etc.)
Except Velocity, which still requires the annotation.
Note: You still need the files such as bungee.yml, plugin.yml etc.
```
FernBungeeAPI
FernSpigotAPI
FernSpongeAPI
FernVelocityAPI
```
After you have extended one of these classes, for bungee and spigot you have to add 
```java
public void onEnable() {
    super.onEnable();
}

public void onDisable() {
    super.onDisable();
}
```
For sponge, you should have 
```java
    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        super.onServerStart(event);
    }

    @Listener
    public void onServerStop(GameStoppedEvent event) {
        super.onServerStop(event):        
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

    @Subscribe
    public void onProxyStop(ProxyShutdownEvent event) {
        super.onProxyStop(event);        
    }
```
That should be all, unless the API updates.
