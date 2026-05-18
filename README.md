# AzoxLimit

A Minecraft Paper server plugin that limits enchantments and removes non-vanilla items/attributes from the game.

## Features

### Enchantment Limiting
- Caps enchantment levels to configured maximum values
- Removes non-vanilla enchantments from items
- Configurable per-enchantment max levels

### Attribute Control
- Removes non-vanilla attribute modifiers from items
- Configurable whitelist of allowed attributes

### Item Sanitization
- Sanitizes items from multiple sources:
  - Player inventory (on join/open)
  - Crafting (players + dispensers)
  - Anvil combinations
  - Villager trades
  - Mob drops
  - Brewing recipes

### Limited Items
- Blocks specific item recipes (netherite, mace)
- Removes banned items from player inventories
- Prevents specific brewing recipes
- Removes tipped arrows from dispensers

## Configuration

All configuration is in `config.yml`:

```yaml
settings:
  prefix: "&8[&6AzoxLimit&8] "
  debug: false

enchantments:
  enabled: true
  max-levels:
    SHARPNESS: 5
    EFFICIENCY: 5
    # ... other enchantments

attributes:
  enabled: true
  remove-non-vanilla: true
  allowed-attributes:
    GENERIC_ATTACK_DAMAGE: true
    GENERIC_MOVEMENT_SPEED: true
    # ... other attributes

sanitize:
  enabled: true
  check-intervals:
    on-player-join: true
    on-inventory-open: true
    on-item-interact: true
    on-craft: true
    on-anvil: true

limited-items:
  enabled: true
  blocked-recipes:
    - mace
    - netherite_ingot
  remove-items:
    - mace
    - netherite_ingot
```

## Commands

| Command | Description | Permission |
|---------|-------------|-------------|
| `/azoxlimit` | Reload configuration | `azoxlimit.admin` |
| `/azoxlimitreload` | Reload configuration | `azoxlimit.admin` |
| `/alreload` | Reload configuration | `azoxlimit.admin` |

## Permissions

- `azoxlimit.admin` - Reload configuration

## Installation

1. Download the latest release from [GitHub Releases](https://github.com/ximotu/azoxlimit/releases)
2. Place the JAR file in your server's `plugins` folder
3. Restart or reload the server

## Building

```bash
mvn clean package
```

Output: `target/azox-limit-1.0.0.jar`

## Requirements

- Minecraft Paper 1.21.11+
- Java 21+

## License

MIT License