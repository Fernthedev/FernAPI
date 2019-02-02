If you get an exception named such as IncorrectSetupException, it means you have not setup the api correctly.
To fix this, you have to have your main plugin/mod class extend one of 3 classes (already extend their API class like JavaPlugin for spigot etc.)
```
FernBungeeAPI
FernSpigotAPI
FernForgeAPI
```
After you have extended one of these classes, for bungee and spigot you have to add 
```java
public void onEnable() {
    super.onEnable();
}
```
That should be all, unless the API updates.