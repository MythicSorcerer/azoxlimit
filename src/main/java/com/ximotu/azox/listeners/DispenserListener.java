package com.ximotu.azox.listeners;

import com.ximotu.azox.AzoxLimit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;

public final class DispenserListener implements Listener {

    @EventHandler
    public void onInventoryMoveItem(@NotNull final InventoryMoveItemEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("sanitize.check-intervals.on-item-interact", true)) {
            return;
        }

        final ItemStack item = event.getItem();
        if (item != null) {
            ItemSanitizer.sanitizeItem(item);
        }
    }

    @EventHandler
    public void onInventoryPickupItem(@NotNull final InventoryPickupItemEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("sanitize.check-intervals.on-item-interact", true)) {
            return;
        }

        final ItemStack item = event.getItem().getItemStack();
        if (item != null) {
            ItemSanitizer.sanitizeItem(item);
        }
    }
}
