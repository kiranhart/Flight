/*
 * Flight
 * Copyright 2023 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.flight.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class ItemUtil {

    /**
     * If the item has a display name, return it, otherwise return the item's type
     *
     * @param itemStack The item stack to get the name of.
     *
     * @return The name of the item.
     */
    public String getItemName(@NonNull final ItemStack itemStack) {
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            return itemStack.getItemMeta().getDisplayName();
        } else {
            return ChatUtil.capitalizeFully(itemStack.getType());
        }
    }

    /**
     * If the item has lore, return it, otherwise return an empty list.
     *
     * @param stack The ItemStack to get the lore from.
     *
     * @return The item lore
     */
    public List<String> getItemLore(@NonNull final ItemStack stack) {
        final List<String> lore = new ArrayList<>();
        if (stack.hasItemMeta()) {
            if (stack.getItemMeta().hasLore() && stack.getItemMeta().getLore() != null) {
                lore.addAll(stack.getItemMeta().getLore());
            }
        }
        return lore;
    }

    /**
     * It returns a list of all the enchantments on the item
     *
     * @param stack The item stack to get the enchantments from.
     *
     * @return A list of enchants as strings
     */
    public List<String> getItemEnchantments(@NonNull final ItemStack stack) {
        final List<String> enchantments = new ArrayList<>();
        if (!stack.getEnchantments().isEmpty()) {
            stack.getEnchantments().forEach((k, i) -> {
                enchantments.add(k.getName());
            });
        }
        return enchantments;
    }
}
