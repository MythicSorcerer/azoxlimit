package com.ximotu.azox.listeners;

import com.ximotu.azox.AzoxLimit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class AnvilListener implements Listener {

    @EventHandler
    public void onPrepareAnvil(@NotNull final PrepareAnvilEvent event) {
        if (!AzoxLimit.getInstance().getConfig().getBoolean("sanitize.check-intervals.on-anvil", true)) {
            return;
        }

        final ItemStack result = event.getResult();
        if (result != null) {
            ItemSanitizer.sanitizeItem(result);
            event.setResult(result);
        }
    }
}
