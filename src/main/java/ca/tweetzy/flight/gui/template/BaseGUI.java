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

package ca.tweetzy.flight.gui.template;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Date Created: April 09 2022
 * Time Created: 9:22 p.m.
 *
 * @author Kiran Hart
 */
public abstract class BaseGUI extends Gui {

    private final Gui parent;

    public BaseGUI(final Gui parent, @NonNull final String title, final int rows) {
        this.parent = parent;
        setTitle(Common.colorize(title));
        setRows(rows);
        setDefaultSound(null);
        setDefaultItem(QuickItem.of(CompMaterial.BLACK_STAINED_GLASS_PANE).name(" ").make());
    }

    public BaseGUI(final Gui parent, @NonNull final String title) {
        this(parent, title, 6);
    }

    public BaseGUI(@NonNull final String title) {
        this(null, title, 6);
    }

    /**
     * Draw the gui.
     */
    protected abstract void draw();


    /**
     * It adds a back button to the bottom left of the GUI
     *
     * @param override The GUI to show when the back button is clicked.
     */
    protected void applyBackExit(Gui override) {
        setButton(getBackExitButtonSlot(), getBackButton(), click -> click.manager.showGUI(click.player, override));
    }

    /**
     * If the GUI has a parent, then the back button will be set to the back button, otherwise it will be set to the exit button
     */
    protected void applyBackExit() {
        if (this.parent == null) {
            setButton(getBackExitButtonSlot(), getExitButton(), click -> click.gui.close());
        } else {
            setButton(getBackExitButtonSlot(), getBackButton(), click -> click.manager.showGUI(click.player, this.parent));
        }

    }

    protected List<Integer> fillSlots() {
        return IntStream.rangeClosed(0, 44).boxed().collect(Collectors.toList());
    }

    protected abstract ItemStack getBackButton();

    protected abstract ItemStack getExitButton();

    protected abstract ItemStack getPreviousButton();

    protected abstract ItemStack getNextButton();

    protected abstract int getPreviousButtonSlot();

    protected abstract int getNextButtonSlot();

    protected int getBackExitButtonSlot() {
        return this.rows * 9 - 9;
    }
}
