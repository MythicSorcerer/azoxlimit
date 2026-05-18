package com.ximotu.azox.listeners;

import com.ximotu.azox.AzoxLimit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class BrewingBlockListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBrew(@NotNull final BrewEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("limited-items.enabled", true)) {
            return;
        }

        final Block brewer = event.getBlock();
        if (brewer == null || brewer.getType() != Material.BREWING_STAND) {
            return;
        }

        final var state = brewer.getState();
        if (!(state instanceof org.bukkit.inventory.InventoryHolder holder)) {
            return;
        }

        final var inventory = holder.getInventory();
        if (!(inventory instanceof org.bukkit.inventory.BrewerInventory brewerInventory)) {
            return;
        }

        final ItemStack ingredient = brewerInventory.getIngredient();
        if (ingredient == null || ingredient.getType().isAir()) {
            return;
        }

        final List<String> blockedIngredients = AzoxLimit.getInstance().getConfig()
                .getStringList("limited-items.blocked-brewing-recipes");

        if (blockedIngredients.isEmpty()) {
            return;
        }

        final String ingredientName = ingredient.getType().name();

        if (blockedIngredients.stream().anyMatch(s -> s.equalsIgnoreCase(ingredientName))) {
            event.setCancelled(true);
            AzoxLimit.getInstance().debug("Blocked brewing recipe with ingredient: " + ingredientName);
        }
    }
}