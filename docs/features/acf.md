## Aikar's Command Framework

Aikar's command framework can be found [here](https://github.com/aikar/commands) 
Follow the instructions on installation

## Usage with FernAPI
FernAPI has some specific classes to allow more cross-platform support than vanilla ACF.

To register the command, you must use this:
```java
Universal.getCommandHandler().registerCommand(new Command());
```

The context resolver can be found [here](https://github.com/Fernthedev/FernAPI/blob/e9349c25c085abbad0022c3ca51cf7badead98ba/core/src/main/java/com/github/fernthedev/fernapi/universal/util/UniversalContextResolvers.java)

Here are some classes and flags:
- `IFPlayer<?>`
    - other
        - When specified, it makes the context resolver use the name of the player rather than the sender by default
    - defaultself
        - Used in conjunction with `other` to specify that when an argument is not defined, it is by default the sender
    - uuid
        - Used in conjunction with `other` to specify that the argument is UUID and not player name.
    - offline
        -  Used in conjunction with `other` to specify that the player provided can be offline.
- `IFPlayer<?>[]`
    - allowmissing
        - If provided, it allows the player to provide players that are not found online. They are not added to the list
    - allowempty
        - Allows the list to be empty
    - splitter
        - The splitter used to separate players in the list
- `OfflineFPlayer`
    - uuid
        - Used in conjunction with `other` to specify that the argument is UUID and not player name.
- `IFConsole<?>`
- `FernCommandIssuer`

To 