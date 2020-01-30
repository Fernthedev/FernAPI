# FernAPI
This plugin is compiled in 1.14.4, but should work regaurdless of your plugin's version unless it uses outdated libraries or other means of change.

This is a runtime API that works as a compability layer for multiple server platforms. It is basically the Java of Minecraft Java Servers. It is mostly just interfaces of code implemented in different server codebases that will achieve mostly similar or same results though some features, notably plugin messaging and/or plugin data, at the moment are not fully implemented in some server platforms

[![Release](https://jitpack.io/v/Fernthedev/FernAPI.svg)](https://jitpack.io/#Fernthedev/FernAPI)
[![CircleCI](https://circleci.com/gh/Fernthedev/FernAPI.svg?style=svg)](https://circleci.com/gh/Fernthedev/FernAPI)
[![Jitpack](https://jitci.com/gh/Fernthedev/FernAPI/svg)](https://jitci.com/gh/Fernthedev/FernAPI)
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

[vat] = Velocity (Alpha support, not tested)

[ve] = Velocity (Should work)

[u] = All of the above, in other words Universal

- [UUID Fetch](features/uuid_fetch.md) [u] [ve]
- [ChatAPI](features/chatapi.md) [u] [vat]
  - Clickable text
  - Hover message
  - Color code support
- [List sorter (Sorter Class, check methods)](https://github.com/Fernthedev/FernAPI/blob/stable/core/src/main/java/com/github/fernthedev/fernapi/universal/util/ListUtil.java) [u]
- [Bungee/Spigot/Sponge/Velocity plugin messaging](features/plugin_messaging.md) [u*] [vat]
    - *Sponge has not been fully tested, please feel free to report any bugs at issues at repo.
- [MySQL](features/mysql.md) [u] [vat]
- [Universal Commands](features/ucommand.md) [u] [vet]

## Usage
Usage moved to [here](usage.md)
