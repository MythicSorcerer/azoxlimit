package com.ximotu.azox.listeners;

import com.google.common.collect.Multimap;
import com.ximotu.azox.AzoxLimit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ItemSanitizer {

    private static final Set<NamespacedKey> VANILLA_ENCHANTMENT_KEYS = new HashSet<>();

    static {
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("protection"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("fire_protection"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("feather_falling"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("blast_protection"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("projectile_protection"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("respiration"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("aqua_affinity"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("thorns"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("depth_strider"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("frost_walker"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("binding_curse"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("sharpness"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("smite"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("bane_of_arthropods"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("knockback"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("fire_aspect"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("looting"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("sweeping"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("efficiency"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("silk_touch"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("unbreaking"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("fortune"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("power"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("punch"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("flame"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("infinity"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("luck_of_the_sea"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("lure"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("loyalty"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("impaling"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("riptide"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("channeling"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("multishot"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("quick_charge"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("piercing"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("density"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("breach"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("wind_charge"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("soul_speed"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("swift_sneak"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("mending"));
        VANILLA_ENCHANTMENT_KEYS.add(NamespacedKey.minecraft("vanishing_curse"));
    }

    public static void sanitizeItem(final ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return;
        }

        final AzoxLimit plugin = AzoxLimit.getInstance();
        if (plugin == null) {
            return;
        }

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        boolean modified = false;

        if (plugin.getConfig().getBoolean("enchantments.enabled", true)) {
            modified |= sanitizeEnchantments(item, meta);
        }

        if (plugin.getConfig().getBoolean("attributes.enabled", true)) {
            modified |= sanitizeAttributes(meta);
        }

        modified |= sanitizePotionLevels(item, meta);

        if (modified) {
            item.setItemMeta(meta);
        }
    }

private static boolean sanitizePotionLevels(final ItemStack item, final ItemMeta meta) {
        final var potionMeta = meta instanceof org.bukkit.inventory.meta.PotionMeta pm ? pm : null;
        if (potionMeta == null) {
            return false;
        }

        final var customEffects = potionMeta.getCustomEffects();
        if (customEffects.isEmpty()) {
            return false;
        }

        boolean modified = false;
        final var iterator = customEffects.iterator();
        while (iterator.hasNext()) {
            final PotionEffect effect = iterator.next();
            if (effect.getAmplifier() > 10) {
                AzoxLimit.getInstance().debug("Removed illegal potion effect " +
                    effect.getType().getName() + " with amplifier " + effect.getAmplifier() +
                    " from " + item.getType());
                iterator.remove();
                modified = true;
            }
        }

        return modified;
    }

    private static boolean sanitizeEnchantments(final ItemStack item, final ItemMeta meta) {
        final AzoxLimit plugin = AzoxLimit.getInstance();
        final Map<Enchantment, Integer> enchantments = meta.getEnchants();
        if (enchantments.isEmpty()) {
            return false;
        }

        boolean modified = false;

        for (final Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            final Enchantment enchantment = entry.getKey();
            final int currentLevel = entry.getValue();
            final NamespacedKey enchantKey = enchantment.getKey();

            if (!VANILLA_ENCHANTMENT_KEYS.contains(enchantKey)) {
                meta.removeEnchant(enchantment);
                plugin.debug("Removed non-vanilla enchantment " + enchantKey.asString() + " from " + item.getType());
                modified = true;
                continue;
            }

            final String configKey = enchantKey.getKey().toUpperCase();
            final int configuredMax = plugin.getConfig().getInt("enchantments.max-levels." + configKey, enchantment.getMaxLevel());

            if (currentLevel > configuredMax) {
                meta.removeEnchant(enchantment);
                meta.addEnchant(enchantment, configuredMax, false);
                plugin.debug("Capped enchantment " + configKey + " from " + currentLevel + " to " + configuredMax);
                modified = true;
            }
        }

        return modified;
    }

    private static boolean sanitizeAttributes(final ItemMeta meta) {
        final AzoxLimit plugin = AzoxLimit.getInstance();
        final Multimap<Attribute, AttributeModifier> attributeModifiers = meta.getAttributeModifiers();

        if (attributeModifiers == null || attributeModifiers.isEmpty()) {
            return false;
        }

        final Multimap<Attribute, AttributeModifier> newModifiers = com.google.common.collect.LinkedHashMultimap.create();

        boolean modified = false;
        for (final Map.Entry<Attribute, AttributeModifier> entry : attributeModifiers.entries()) {
            final Attribute attribute = entry.getKey();
            final String attributeName = attribute.getKey().getKey();

            final boolean isAllowed = plugin.getConfig().getBoolean("attributes.allowed-attributes." + attributeName, false);

            if (plugin.getConfig().getBoolean("attributes.remove-non-vanilla", true)) {
                if (!isAllowed) {
                    plugin.debug("Removed non-allowed attribute " + attributeName);
                    modified = true;
                    continue;
                }
            }
            newModifiers.put(entry.getKey(), entry.getValue());
        }

        if (modified) {
            meta.setAttributeModifiers(newModifiers);
        }

        return modified;
    }
}
