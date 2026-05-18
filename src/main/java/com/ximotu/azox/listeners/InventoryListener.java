package com.ximotu.azox.listeners;

import com.ximotu.azox.AzoxLimit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryOpen(@NotNull final InventoryOpenEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("sanitize.check-intervals.on-inventory-open", true)) {
            return;
        }
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        final Player player = (Player) event.getPlayer();
        final InventoryType type = event.getInventory().getType();

        if (type == InventoryType.CRAFTING || type == InventoryType.WORKBENCH) {
            for (final ItemStack item : event.getInventory().getContents()) {
                if (item != null) {
                    ItemSanitizer.sanitizeItem(item);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(@NotNull final InventoryClickEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("sanitize.check-intervals.on-inventory-open", true)) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        final ItemStack currentItem = event.getCurrentItem();
        if (currentItem != null) {
            ItemSanitizer.sanitizeItem(currentItem);
        }

        final ItemStack cursorItem = event.getCursor();
        if (cursorItem != null) {
            ItemSanitizer.sanitizeItem(cursorItem);
        }
    }

    @EventHandler
    public void onInventoryDrag(@NotNull final InventoryDragEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("sanitize.check-intervals.on-inventory-open", true)) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        for (final ItemStack item : event.getNewItems().values()) {
            ItemSanitizer.sanitizeItem(item);
        }
    }
}
