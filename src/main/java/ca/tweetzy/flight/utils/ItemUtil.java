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

    public String getStackName(@NonNull final ItemStack itemStack) {
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            return itemStack.getItemMeta().getDisplayName();
        } else {
            return ChatUtil.capitalizeFully(itemStack.getType());
        }
    }

    public List<String> getItemLore(@NonNull final ItemStack stack) {
        final List<String> lore = new ArrayList<>();
        if (stack.hasItemMeta()) {
            if (stack.getItemMeta().hasLore() && stack.getItemMeta().getLore() != null) {
                lore.addAll(stack.getItemMeta().getLore());
            }
        }
        return lore;
    }

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
