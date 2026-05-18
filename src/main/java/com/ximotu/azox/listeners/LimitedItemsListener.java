package com.ximotu.azox.listeners;

import com.ximotu.azox.AzoxLimit;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class LimitedItemsListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCraft(final PrepareItemCraftEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("limited-items.enabled", true)) {
            return;
        }

        final ItemStack result = event.getInventory().getResult();
        if (result == null || result.getType().isAir()) {
            return;
        }

        final List<String> removeItems = AzoxLimit.getInstance().getConfig()
                .getStringList("limited-items.remove-items");
        final Material resultType = result.getType();

        if (removeItems.stream().anyMatch(s -> s.equalsIgnoreCase(resultType.name()))) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCraftItem(final CraftItemEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("limited-items.enabled", true)) {
            return;
        }

        final ItemStack result = event.getCurrentItem();
        if (result == null || result.getType().isAir()) {
            return;
        }

        final List<String> removeItems = AzoxLimit.getInstance().getConfig()
                .getStringList("limited-items.remove-items");

        final Material resultType = result.getType();
        if (removeItems.stream().anyMatch(s -> s.equalsIgnoreCase(resultType.name()))) {
            event.setCurrentItem(new ItemStack(Material.AIR));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemPickup(final EntityPickupItemEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("limited-items.enabled", true)) {
            return;
        }

        final ItemStack item = event.getItem().getItemStack();
        final Material itemType = item.getType();

        final List<String> blockPickup = AzoxLimit.getInstance().getConfig()
                .getStringList("limited-items.block-pickup");

        if (blockPickup.stream().anyMatch(s -> s.equalsIgnoreCase(itemType.name()))) {
            event.getItem().remove();
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemSpawn(final org.bukkit.event.entity.ItemSpawnEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("limited-items.enabled", true)) {
            return;
        }

        final ItemStack item = event.getEntity().getItemStack();
        final Material itemType = item.getType();

        final List<String> blockMobDrops = AzoxLimit.getInstance().getConfig()
                .getStringList("limited-items.block-mob-drops");

        if (blockMobDrops.stream().anyMatch(s -> s.equalsIgnoreCase(itemType.name()))) {
            event.getEntity().remove();
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDeath(final org.bukkit.event.entity.EntityDeathEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("limited-items.enabled", true)) {
            return;
        }

        final var drops = event.getDrops();
        final List<String> blockMobDrops = AzoxLimit.getInstance().getConfig()
                .getStringList("limited-items.block-mob-drops");

        drops.removeIf(drop -> blockMobDrops.stream()
                .anyMatch(s -> s.equalsIgnoreCase(drop.getType().name())));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockDispense(final BlockDispenseEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("limited-items.enabled", true)) {
            return;
        }

        final ItemStack dispensed = event.getItem();
        final Material itemType = dispensed.getType();

        final List<String> deleteTippedArrows = AzoxLimit.getInstance().getConfig()
                .getStringList("limited-items.delete-tipped-arrows");

        if (itemType == Material.TIPPED_ARROW) {
            final var meta = dispensed.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                final String displayName = LegacyComponentSerializer.legacySection()
                        .serialize(meta.displayName()).toLowerCase();

                for (final String blocked : deleteTippedArrows) {
                    if (displayName.contains(blocked.toLowerCase())) {
                        event.setCancelled(true);
                        event.setItem(new ItemStack(Material.AIR));
                        break;
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTippedArrowPickup(final EntityPickupItemEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("limited-items.enabled", true)) {
            return;
        }

        final ItemStack item = event.getItem().getItemStack();
        final Material itemType = item.getType();

        if (itemType != Material.TIPPED_ARROW) {
            return;
        }

        final List<String> blockedTippedArrows = AzoxLimit.getInstance().getConfig()
                .getStringList("limited-items.delete-tipped-arrows");

        if (blockedTippedArrows.isEmpty()) {
            return;
        }

        final var meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            final String displayName = LegacyComponentSerializer.legacySection()
                    .serialize(meta.displayName()).toLowerCase();

            for (final String blocked : blockedTippedArrows) {
                if (displayName.contains(blocked.toLowerCase())) {
                    event.getItem().remove();
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }
}