package com.ximotu.azox.listeners;

import com.ximotu.azox.AzoxLimit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class VillagerTradeListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(@NotNull final PlayerInteractEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("limited-items.enabled", true)) {
            return;
        }

        final Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.SMITHING_TABLE) {
            return;
        }

        for (final Entity entity : block.getWorld().getNearbyEntities(block.getLocation(), 2.5, 2.5, 2.5)) {
            if (entity instanceof Villager villager) {
                filterVillagerTrades(villager);
            } else if (entity instanceof WanderingTrader trader) {
                filterWanderingTrader(trader);
            }
        }
    }

    private void filterVillagerTrades(final Villager villager) {
        final List<MerchantRecipe> recipes = new ArrayList<>(villager.getRecipes());
        if (recipes.isEmpty()) {
            return;
        }

        final List<String> blockedItems = AzoxLimit.getInstance().getConfig()
                .getStringList("limited-items.block-villager-trades");

        if (blockedItems.isEmpty()) {
            return;
        }

        recipes.removeIf(recipe -> {
            final var result = recipe.getResult();
            return blockedItems.stream().anyMatch(s -> s.equalsIgnoreCase(result.getType().name()));
        });

        villager.setRecipes(recipes);
    }

    private void filterWanderingTrader(final WanderingTrader trader) {
        final List<MerchantRecipe> recipes = new ArrayList<>(trader.getRecipes());
        if (recipes.isEmpty()) {
            return;
        }

        final List<String> blockedItems = AzoxLimit.getInstance().getConfig()
                .getStringList("limited-items.block-villager-trades");

        if (blockedItems.isEmpty()) {
            return;
        }

        recipes.removeIf(recipe -> {
            final var result = recipe.getResult();
            return blockedItems.stream().anyMatch(s -> s.equalsIgnoreCase(result.getType().name()));
        });

        trader.setRecipes(recipes);
    }
}