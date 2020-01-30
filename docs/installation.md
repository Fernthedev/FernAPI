
# Installation

## Non-Local Installation
### Maven:
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
```xml
<dependency>
    <groupId>com.github.Fernthedev.FernAPI</groupId> <!-- Since1.9b6+: Use FernAPI at end of groupdId. Lower versions use groupId without FernAPI and instead provide it in artifactId -->
    <artifactId>all</artifactId> <!-- Since 1.9b6+: Use name of server platform, or 'all' to include all platforms. Eg. spigot, bungee, sponge, velocity. You may even import the core to implement your own API, though it isn't recommended -->

    <!-- Tag can be found in releases at github repository -->
    <version>Tag</version>
</dependency>
```

### Gradle:
```gradle
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```
```gradle
dependencies {
//Tag can be found in releases at github repository
// Since 1.9b6+: Use name of server platform, or 'all' to include all platforms. Eg. spigot, bungee, sponge, velocity. You may even import the core to implement your own API, though it isn't recommended
// Since1.9b6+: Use FernAPI at end of groupdId. Lower versions use groupId without FernAPI and instead provide it in artifactId
        implementation 'com.github.Fernthedev.FernAPI:all:Tag'
}
```

## Local Installation
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

## Shading
After you have added it as a dependency, it is required to shade the library. 
To do this,you can use other methods if you liked, I suggest these:

For gradle: https://github.com/johnrengelman/shadow

For maven: https://maven.apache.org/plugins/maven-shade-plugin/usage.html