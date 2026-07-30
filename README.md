# Sentinel Shield

Sentinel Shield is a lightweight client-side Fabric mod that changes the color of shields depending on their current combat state.

The mod is designed primarily for Minecraft PvP and makes shield timing easier to read without adding HUD elements or changing normal shield mechanics.

## Features

- Colors shields that are ready to use.
- Displays a separate delay color during the first 5 ticks of shield use.
- Displays cooldown colors after a shield is disabled.
- Supports smooth color transitions during shield cooldown.
- Supports first-person and third-person shield rendering.
- Displays shield states for other players.
- Predicts remote shield cooldowns after disabling a shield with an axe.
- Configurable colors and color strength.
- Optional integration with Mod Menu.
- Client-side only.

## Shield states

### Ready

The shield is available and can be used.

### Delay

The player has started using the shield, but the shield is not yet protecting them.

This state lasts for the first 5 ticks of shield use.

### Active

The shield has completed its activation delay and is protecting the player.

The Active state uses the configured Ready color.

### Cooldown

The shield has been disabled and cannot currently be used.

For other players, cooldown detection is predicted locally because Minecraft does not synchronize the complete item cooldown state of remote players to every client.

## Configuration

Install Mod Menu to access the Sentinel Shield configuration screen directly from the mod list.

Available settings include:

- Enable or disable the mod.
- Ready color and strength.
- Delay color and strength.
- Cooldown color and strength.
- Smooth cooldown color transition.
- Reset configuration to defaults.

Setting a color strength to `0%` disables that tint and displays the normal shield texture.

## Requirements

- Minecraft 1.21.11
- Fabric Loader 0.19.3 or newer
- Fabric API 0.141.6+1.21.11 or newer
- Java 21 or newer
- Nythral Library 1.0.0 or newer

## Optional dependencies

- Mod Menu

## Installation

1. Install Fabric Loader.
2. Install Fabric API.
3. Install Nythral Library.
4. Place Sentinel Shield in the Minecraft `mods` directory.
5. Optionally install Mod Menu to access the configuration screen.

## Client-side mod

Sentinel Shield is entirely client-side.

It does not need to be installed on the Minecraft server.

## Source code

The source code is available in this repository.

Bug reports and feature requests can be submitted through GitHub Issues.

## License

Sentinel Shield is available under the MIT License.