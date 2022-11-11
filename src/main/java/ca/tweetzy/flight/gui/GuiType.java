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

package ca.tweetzy.flight.gui;

import org.bukkit.event.inventory.InventoryType;

public enum GuiType {

    STANDARD(InventoryType.CHEST, 6, 9),
    DISPENSER(InventoryType.DISPENSER, 9, 3),
    HOPPER(InventoryType.HOPPER, 5, 1);

    protected final InventoryType type;
    protected final int rows;
    protected final int columns;

    GuiType(InventoryType type, int rows, int columns) {
        this.type = type;
        this.rows = rows;
        this.columns = columns;
    }
}
