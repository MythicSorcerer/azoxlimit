package com.ximotu.azox;

import com.ximotu.azox.commands.ReloadCommand;
import com.ximotu.azox.listeners.AnvilListener;
import com.ximotu.azox.listeners.BrewingBlockListener;
import com.ximotu.azox.listeners.CraftListener;
import com.ximotu.azox.listeners.DispenserListener;
import com.ximotu.azox.listeners.InventoryListener;
import com.ximotu.azox.listeners.ItemInteractListener;
import com.ximotu.azox.listeners.LimitedItemsListener;
import com.ximotu.azox.listeners.PlayerJoinListener;
import com.ximotu.azox.listeners.RecipeBlockListener;
import com.ximotu.azox.listeners.VillagerTradeListener;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public final class AzoxLimit extends JavaPlugin {

    @Getter
    private static AzoxLimit instance;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        if (!this.validateConfig()) {
            this.getLogger().log(Level.SEVERE, "Configuration validation failed. Plugin disabled.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.registerCommands();
        this.registerListeners();

        this.getLogger().log(Level.INFO, "AzoxLimit has been enabled.");
        if (this.getConfig().getBoolean("settings.debug", false)) {
            this.getLogger().log(Level.INFO, "Debug mode is enabled.");
        }
    }

    @Override
    public void onDisable() {
        this.getLogger().log(Level.INFO, "AzoxLimit has been disabled.");
        instance = null;
    }

    private boolean validateConfig() {
        if (!this.getConfig().contains("enchantments.max-levels")) {
            this.getLogger().log(Level.WARNING, "Missing enchantments.max-levels in config, using defaults.");
        }
        return true;
    }

    private void registerCommands() {
        final var cmd = new Command("azoxlimit") {
            {
                setPermission("azoxlimit.admin");
                setAliases(List.of("azoxlimitreload", "alreload"));
            }

            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                new ReloadCommand().onCommand(sender, this, commandLabel, args);
                return true;
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
                return Collections.emptyList();
            }
        };
        this.getServer().getCommandMap().register("azoxlimit", cmd);
    }

    private void registerListeners() {
        final var manager = this.getServer().getPluginManager();
        manager.registerEvents(new PlayerJoinListener(), this);
        manager.registerEvents(new InventoryListener(), this);
        manager.registerEvents(new ItemInteractListener(), this);
        manager.registerEvents(new CraftListener(), this);
        manager.registerEvents(new AnvilListener(), this);
        manager.registerEvents(new DispenserListener(), this);
        manager.registerEvents(new LimitedItemsListener(), this);
        manager.registerEvents(new RecipeBlockListener(), this);
        manager.registerEvents(new VillagerTradeListener(), this);
        manager.registerEvents(new BrewingBlockListener(), this);

        if (this.getConfig().getBoolean("limited-items.enabled", true)) {
            new RecipeBlockListener().initializeRecipes();
        }
    }

    public void reloadPluginConfig() {
        this.reloadConfig();
        final String prefix = this.getConfig().getString("settings.prefix", "&8[&6AzoxLimit&8] ");
        this.getServer().getConsoleSender().sendMessage(
                Component.text(prefix, NamedTextColor.GRAY)
                        .append(Component.text("Configuration reloaded.", NamedTextColor.GREEN))
        );
    }

    public boolean isDebug() {
        return this.getConfig().getBoolean("settings.debug", false);
    }

    public void debug(final String message) {
        if (this.isDebug()) {
            this.getLogger().log(Level.INFO, "[DEBUG] " + message);
        }
    }
}