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

package ca.tweetzy.flight.gui.helper;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Date Created: April 02 2022
 * Time Created: 12:48 p.m.
 *
 * @author Kiran Hart
 */
@UtilityClass
public final class InventoryBorder {

    /**
     * @param rows The number of rows in the gui.
     * @return A list of integers representing the gui border.
     */
    public static List<Integer> getBorders(final int rows) {
        final List<Integer> borders = new ArrayList<>();

        for (int index = 0; index < rows * 9; index++) {
            int row = index / 9;
            int column = (index % 9) + 1;

            if (row == 0 || row == rows - 1 || column == 1 || column == 9)
                borders.add(index);
        }

        return borders;
    }


    /**
     * @param rows The number of rows in the gui.
     * @return A list of integers that represent the slots excluding border
     */
    public static List<Integer> getInsideBorders(final int rows) {
        final List<Integer> inner = new ArrayList<>();

        for (int index = 0; index < rows * 9; index++) {
            int row = index / 9;
            int column = (index % 9) + 1;

            if (row == 0 || row == rows - 1 || column == 1 || column == 9)
                continue;

            inner.add(index);
        }

        return inner;
    }
}
