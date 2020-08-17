# FernAPI
This plugin is compiled in 1.14.4 - 1.16, but should work regardless of your plugin's version unless it uses outdated libraries or other means of change.

This is a runtime API that works as a compatibility layer for multiple server platforms. It is basically the Java of Minecraft Java Servers. It is mostly just interfaces of code implemented in different server codebases that will achieve mostly similar or same results though some features, notably plugin messaging and/or plugin data, at the moment are not fully implemented in some server platforms

The latest release is in [Github releases](https://github.com/Fernthedev/FernAPI/releases)
[![CircleCI](https://circleci.com/gh/Fernthedev/FernAPI.svg?style=svg)](https://circleci.com/gh/Fernthedev/FernAPI)
[![JitCI](https://jitci.com/gh/Fernthedev/FernAPI/svg)](https://jitci.com/gh/Fernthedev/FernAPI)
[![Build Status](https://dev.azure.com/Fernthedev/FernAPI/_apis/build/status/Fernthedev.FernAPI?branchName=stable)](https://dev.azure.com/Fernthedev/FernAPI/_build/latest?definitionId=5&branchName=stable)
![Java CI](https://github.com/Fernthedev/FernAPI/workflows/Java%20CI/badge.svg)

[Installation](#installation)

[Features](#features)

[Usage](#usage)

## Installation

Installation moved to [here](installation.md)

## Features:
[SP] = Sponge

[b] = Bungee

[s] = Spigot

[v] = Velocity

[u] = All of the above, in other words Universal

- [UUID Fetch](features/uuid_fetch.md) [u]
- [Legacy ChatAPI based on Bungee components](features/chatapi.md) [u]
  - Clickable text
  - Hover message
  - Color code support
- [Adventure Text API](features/adventure.md)
- [List sorter (Sorter Class, check methods)](https://github.com/Fernthedev/FernAPI/blob/stable/core/src/main/java/com/github/fernthedev/fernapi/universal/util/ListUtil.java) [u]
- [Bungee/Spigot/Sponge/Velocity plugin messaging](features/plugin_messaging.md) [u]
    - *Sponge has not been fully tested, please feel free to report any bugs at issues at repo.
- [MySQL](features/mysql.md) [u]
- [Aikar ACF support](features/acf.md) [u]

## Usage
Usage moved to [here](usage.md)
