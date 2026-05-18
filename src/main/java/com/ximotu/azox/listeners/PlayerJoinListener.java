package com.ximotu.azox.listeners;

import com.ximotu.azox.AzoxLimit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("sanitize.check-intervals.on-player-join", true)) {
            return;
        }

        final Player player = event.getPlayer();

        for (final ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                ItemSanitizer.sanitizeItem(item);
            }
        }

        for (final ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null) {
                ItemSanitizer.sanitizeItem(item);
            }
        }

        final ItemStack offHand = player.getInventory().getItemInOffHand();
        if (offHand != null) {
            ItemSanitizer.sanitizeItem(offHand);
        }

        AzoxLimit.getInstance().debug("Sanitized player " + player.getName() + "'s inventory on join.");
    }
}
