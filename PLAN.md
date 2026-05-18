# AzoxLimit Plugin Plan (1.21.11)

A Minecraft Paper plugin that limits enchantments and removes non-vanilla attributes from items.

## Features

- **Enchantment Limiting**: Caps enchantment levels to configured max values (defaults to vanilla max)
- **Non-Vanilla Enchantment Removal**: Removes any enchantments not in the vanilla list
- **Attribute Control**: Removes or allows specific attribute modifiers
- **Configurable**: All limits and options are configurable via config.yml

## Configuration

### Enchantment Max Levels
Set custom max levels for each enchantment:
```yaml
enchantments:
  max-levels:
    SHARPNESS: 5      # Vanilla max
    EFFICIENCY: 10   # Custom limit
```

### Attribute Control
Configure which attributes are allowed:
```yaml
attributes:
  allowed-attributes:
    GENERIC_ATTACK_DAMAGE: true
    GENERIC_MOVEMENT_SPEED: false
```

## Commands

- `/azoxlimit` or `/azoxlimitreload` - Reload configuration

## Permissions

- `azoxlimit.admin` - Permission to reload config

## Build

```bash
mvn clean package
```

Output: `target/azox-limit-1.0.0.jar`
