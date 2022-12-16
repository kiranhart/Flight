/*
 * Flight
 * Copyright 2022 Kiran Hart
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

package ca.tweetzy.flight;

import ca.tweetzy.flight.comp.enums.ServerVersion;
import lombok.Getter;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public final class FlightPlayer {

    @Getter
    private final Player player;

    private FlightPlayer(@NotNull final Player player) {
        this.player = player;
    }

    public static FlightPlayer of(@NotNull Player player) {
        return new FlightPlayer(player);
    }

    /**
     * If the server version is below 1.9, return the item in the player's hand, otherwise return the item in the player's main hand
     *
     * @return The item in the player's hand.
     */
    public ItemStack getHand() {
        return ServerVersion.isServerVersionBelow(ServerVersion.V1_9) ? this.player.getInventory().getItemInHand() : this.player.getInventory().getItemInMainHand();
    }

    public void giveItem(@NotNull final ItemStack item, final Consumer<ItemStack> toBeDropped) {
        if (this.player.getInventory().firstEmpty() == -1) {
            if (toBeDropped == null) return;

            toBeDropped.accept(item);
            return;
        }

        this.player.getInventory().addItem(item);
    }

    public void giveItem(@NotNull final ItemStack item) {
        giveItem(item, drop -> this.player.getLocation().getWorld().dropItemNaturally(this.player.getLocation(), item));
    }
}
