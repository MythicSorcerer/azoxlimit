package com.ximotu.azox.listeners;

import com.ximotu.azox.AzoxLimit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class CraftListener implements Listener {

    @EventHandler
    public void onPrepareCraft(@NotNull final PrepareItemCraftEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("sanitize.check-intervals.on-craft", true)) {
            return;
        }

        final ItemStack result = event.getInventory().getResult();
        if (result != null) {
            ItemSanitizer.sanitizeItem(result);
        }
    }

    @EventHandler
    public void onCraftItem(@NotNull final CraftItemEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("sanitize.check-intervals.on-craft", true)) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        final ItemStack result = event.getCurrentItem();
        if (result != null) {
            ItemSanitizer.sanitizeItem(result);
        }
    }
}
