package com.ximotu.azox.listeners;

import com.ximotu.azox.AzoxLimit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ItemInteractListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(@NotNull final PlayerInteractEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("sanitize.check-intervals.on-item-interact", true)) {
            return;
        }

        final ItemStack item = event.getItem();
        if (item != null) {
            ItemSanitizer.sanitizeItem(item);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemHeldChange(@NotNull final PlayerItemHeldEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("sanitize.check-intervals.on-item-interact", true)) {
            return;
        }

        final Player player = event.getPlayer();
        final ItemStack item = player.getInventory().getItem(event.getNewSlot());
        if (item != null) {
            ItemSanitizer.sanitizeItem(item);
        }
    }
}
