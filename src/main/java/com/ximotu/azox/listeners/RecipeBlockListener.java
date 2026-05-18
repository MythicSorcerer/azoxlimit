package com.ximotu.azox.listeners;

import com.ximotu.azox.AzoxLimit;
import org.bukkit.Material;
import org.bukkit.Keyed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class RecipeBlockListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("limited-items.enabled", true)) {
            return;
        }

        removeBlockedRecipes();
    }

    public void initializeRecipes() {
        removeBlockedRecipes();
    }

    private void removeBlockedRecipes() {
        final List<String> blockedRecipes = AzoxLimit.getInstance().getConfig()
                .getStringList("limited-items.blocked-recipes");

        if (blockedRecipes.isEmpty()) {
            return;
        }

        final var logger = AzoxLimit.getInstance().getLogger();
        final var server = AzoxLimit.getInstance().getServer();
        int removed = 0;

        for (final String blocked : blockedRecipes) {
            final Material resultMaterial;
            try {
                resultMaterial = Material.getMaterial(blocked.toUpperCase());
            } catch (Exception e) {
                continue;
            }

            if (resultMaterial == null) {
                continue;
            }

            final var list = server.getRecipesFor(new org.bukkit.inventory.ItemStack(resultMaterial));
            for (final Recipe recipe : list) {
                if (recipe instanceof Keyed keyed) {
                    final var key = keyed.getKey();
                    server.removeRecipe(key);
                    removed++;
                    logger.info("Removed recipe: " + key);
                }
            }
        }

        logger.info("Total recipes removed: " + removed);
    }
}