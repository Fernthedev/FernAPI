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