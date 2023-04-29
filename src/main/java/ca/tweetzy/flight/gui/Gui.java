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

import ca.tweetzy.flight.FlightPlugin;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.comp.enums.CompSound;
import ca.tweetzy.flight.comp.enums.ServerVersion;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.events.GuiCloseEvent;
import ca.tweetzy.flight.gui.events.GuiDropItemEvent;
import ca.tweetzy.flight.gui.events.GuiOpenEvent;
import ca.tweetzy.flight.gui.events.GuiPageEvent;
import ca.tweetzy.flight.gui.methods.Clickable;
import ca.tweetzy.flight.gui.methods.Closable;
import ca.tweetzy.flight.gui.methods.Droppable;
import ca.tweetzy.flight.gui.methods.Openable;
import ca.tweetzy.flight.gui.methods.Pagable;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.InventoryUpdate;
import ca.tweetzy.flight.utils.QuickItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Gui {

   	/*
	==============================================================
						  CORE GUI SETTINGS
	==============================================================
	 */

    protected GuiManager guiManager;
    protected Gui parent = null;
    protected Inventory inventory;
    protected String title;

    @Getter
    protected int rows = 6;
    protected int page = 1;
    protected int pages = 1;

    /*
	==============================================================
						  Input/Drop Settings
	==============================================================
	 */
    protected boolean acceptsItems = false;
    protected boolean allowDropItems = true;
    protected boolean allowClose = true;
    protected boolean open = false;

    @Getter
    @Setter
    protected boolean allowShiftClick = false;

    /*
	==============================================================
						  Conditional Settings
	==============================================================
	 */
    protected final Map<Integer, Boolean> unlockedCells = new HashMap<>();
    protected final Map<Integer, ItemStack> cellItems = new HashMap<>();
    protected final Map<Integer, Map<ClickType, Clickable>> conditionalButtons = new HashMap<>();

    /*
	==============================================================
				     Timed/Single Click Settings
	==============================================================
	 */
    protected final Map<Integer, Long> timedClicks = new HashMap<>();
    protected final Map<Integer, Long> lastClicked = new HashMap<>();
    protected final Map<Integer, Boolean> singleClicks = new HashMap<>();

    /*
	==============================================================
				        Pagination Settings
	==============================================================
	 */
    protected int nextPageIndex = -1;
    protected int prevPageIndex = -1;
    protected ItemStack nextPageItem;
    protected ItemStack prevPageItem;
    protected ItemStack nextPage; // can't remember why there's two, gotta fix this when I get time
    protected ItemStack prevPage; // can't remember why there's two, gotta fix this when I get time

    /*
	==============================================================
				        Misc Settings
	==============================================================
	 */
    protected ItemStack blankItem = QuickItem.of(CompMaterial.BLACK_STAINED_GLASS_PANE).name(" ").make();
    private final static ItemStack AIR = CompMaterial.AIR.parseItem();
    protected CompSound defaultSound = CompSound.UI_BUTTON_CLICK;

    /*
	==============================================================
				          Actions
	==============================================================
	 */

    protected Clickable defaultClicker = null;
    protected Clickable privateDefaultClicker = null;
    protected Openable opener = null;
    protected Closable closer = null;
    protected Droppable dropper = null;
    protected Pagable pager = null;

    /*
	==============================================================
				      START OF LOGIC
	==============================================================
	 */

    public Gui() {
        this.rows = 3;
    }

    public Gui(@Nullable Gui parent) {
        this.parent = parent;
    }

    public Gui(int rows) {
        this.rows = Math.max(1, Math.min(6, rows));
    }

    public Gui(int rows, @Nullable Gui parent) {
        this.parent = parent;
        this.rows = Math.max(1, Math.min(6, rows));
    }

    @NotNull
    public List<Player> getPlayers() {
        if (this.inventory == null) {
            return Collections.emptyList();
        }

        return this.inventory.getViewers().stream()
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .collect(Collectors.toList());
    }

    public boolean isOpen() {
        // double check
        if (this.inventory != null && this.inventory.getViewers().isEmpty()) {
            this.open = false;
        }

        return this.open;
    }

    public Gui setAcceptsItems(boolean acceptsItems) {
        this.acceptsItems = acceptsItems;
        return this;
    }

    public void setButtonSingleClick(final int slot) {
        this.singleClicks.put(slot, false);
    }

    public void setButtonTimeout(final int slot, final long milliseconds) {
        this.timedClicks.put(slot, milliseconds);
    }

    /**
     * Set if items in the player's cursor will be cleared when the GUI is
     * closed
     */
    public Gui setAllowDrops(boolean allow) {
        this.allowDropItems = allow;
        return this;
    }

    public Gui setAllowClose(boolean allow) {
        this.allowClose = allow;
        return this;
    }

    /**
     * Close the GUI without calling onClose() and without opening any parent
     * GUIs
     */
    public void exit() {
        this.allowClose = true;
        this.open = false;

        this.inventory.getViewers().stream()
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .collect(Collectors.toList())
                .forEach(Player::closeInventory);
    }

    /**
     * Close the GUI as if the player closed it normally
     */
    public void close() {
        this.allowClose = true;

        this.inventory.getViewers().stream()
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .collect(Collectors.toList())
                .forEach(Player::closeInventory);
    }

    @NotNull
    public Gui setUnlocked(int cell) {
        this.unlockedCells.put(cell, true);
        return this;
    }

    @NotNull
    public Gui setUnlocked(int row, int col) {
        final int cell = col + row * 9;
        this.unlockedCells.put(cell, true);
        return this;
    }

    @NotNull
    public Gui setUnlockedRange(int cellFirst, int cellLast) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            this.unlockedCells.put(cell, true);
        }
        return this;
    }

    @NotNull
    public Gui setUnlockedRange(int cellFirst, int cellLast, boolean open) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            this.unlockedCells.put(cell, open);
        }
        return this;
    }

    @NotNull
    public Gui setUnlockedRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast) {
        final int last = cellColLast + cellRowLast * 9;

        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            this.unlockedCells.put(cell, true);
        }

        return this;
    }

    @NotNull
    public Gui setUnlockedRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, boolean open) {
        final int last = cellColLast + cellRowLast * 9;

        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            this.unlockedCells.put(cell, open);
        }

        return this;
    }

    @NotNull
    public Gui setUnlocked(int cell, boolean open) {
        this.unlockedCells.put(cell, open);
        return this;
    }

    @NotNull
    public Gui setUnlocked(int row, int col, boolean open) {
        final int cell = col + row * 9;
        this.unlockedCells.put(cell, open);
        return this;
    }

    @NotNull
    public Gui setTitle(String title) {
        if (title == null) {
            title = "";
        }

        if (!title.equals(this.title)) {
            this.title = title;

            if (this.inventory != null) {
                // update active inventory
                final List<Player> toUpdate = getPlayers();

                final String finalTitle = title;
                toUpdate.forEach(viewer -> InventoryUpdate.updateInventory(FlightPlugin.getInstance(), viewer, Common.colorize(finalTitle)));
            }
        }

        return this;
    }

    @NotNull
    public Gui setRows(int rows) {
        this.rows = Math.max(1, Math.min(6, rows));
        return this;
    }

    @NotNull
    public Gui setDefaultAction(@Nullable Clickable action) {
        this.defaultClicker = action;
        return this;
    }

    @NotNull
    protected Gui setPrivateDefaultAction(@Nullable Clickable action) {
        this.privateDefaultClicker = action;
        return this;
    }

    @NotNull
    public Gui setDefaultItem(@Nullable ItemStack item) {
        this.blankItem = item;
        return this;
    }

    @Nullable
    public ItemStack getDefaultItem() {
        return this.blankItem;
    }

    @Nullable
    public ItemStack getItem(int cell) {
        if (this.inventory != null && this.unlockedCells.getOrDefault(cell, false)) {
            return this.inventory.getItem(cell);
        }

        return this.cellItems.get(cell);
    }

    @Nullable
    public ItemStack getItem(int row, int col) {
        final int cell = col + row * 9;

        if (this.inventory != null && this.unlockedCells.getOrDefault(cell, false)) {
            return this.inventory.getItem(cell);
        }

        return this.cellItems.get(cell);
    }

    @NotNull
    public Gui setItem(int cell, @Nullable ItemStack item) {
        this.cellItems.put(cell, item);

        if (this.inventory != null && cell >= 0 && cell < this.inventory.getSize()) {
            this.inventory.setItem(cell, item);
        }

        return this;
    }

    @NotNull
    public Gui setItem(int row, int col, @Nullable ItemStack item) {
        final int cell = col + row * 9;
        return setItem(cell, item);
    }

    @NotNull
    public Gui setAction(int cell, @Nullable Clickable action) {
        setConditional(cell, null, action);
        return this;
    }

    @NotNull
    public Gui setAction(int row, int col, @Nullable Clickable action) {
        setConditional(col + row * 9, null, action);
        return this;
    }

    @NotNull
    public Gui setAction(int cell, @Nullable ClickType type, @Nullable Clickable action) {
        setConditional(cell, type, action);
        return this;
    }

    @NotNull
    public Gui setAction(int row, int col, @Nullable ClickType type, @Nullable Clickable action) {
        setConditional(col + row * 9, type, action);
        return this;
    }

    @NotNull
    public Gui setActionForRange(int cellFirst, int cellLast, @Nullable Clickable action) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            setConditional(cell, null, action);
        }

        return this;
    }

    @NotNull
    public Gui setActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, @Nullable Clickable action) {
        final int last = cellColLast + cellRowLast * 9;

        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            setConditional(cell, null, action);
        }

        return this;
    }

    @NotNull
    public Gui setActionForRange(int cellFirst, int cellLast, @Nullable ClickType type, @Nullable Clickable action) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            setConditional(cell, type, action);
        }

        return this;
    }

    @NotNull
    public Gui setActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, @Nullable ClickType type, @Nullable Clickable action) {
        final int last = cellColLast + cellRowLast * 9;

        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            setConditional(cell, type, action);
        }

        return this;
    }

    @NotNull
    public Gui clearActions(int cell) {
        this.conditionalButtons.remove(cell);
        return this;
    }

    @NotNull
    public Gui clearActions(int row, int col) {
        return clearActions(col + row * 9);
    }

    @NotNull
    public Gui setButton(int cell, ItemStack item, @Nullable Clickable action) {
        setItem(cell, item);
        setConditional(cell, null, action);

        return this;
    }

    @NotNull
    public Gui setButton(int row, int col, @Nullable ItemStack item, @Nullable Clickable action) {
        final int cell = col + row * 9;

        setItem(cell, item);
        setConditional(cell, null, action);

        return this;
    }

    @NotNull
    public Gui setButton(int cell, @Nullable ItemStack item, @Nullable ClickType type, @Nullable Clickable action) {
        setItem(cell, item);
        setConditional(cell, type, action);

        return this;
    }

    @NotNull
    public Gui setButton(int row, int col, @Nullable ItemStack item, @Nullable ClickType type, @Nullable Clickable action) {
        final int cell = col + row * 9;

        setItem(cell, item);
        setConditional(cell, type, action);

        return this;
    }

    protected void setConditional(int cell, @Nullable ClickType type, @Nullable Clickable action) {
        final Map<ClickType, Clickable> conditionals = this.conditionalButtons.computeIfAbsent(cell, k -> new HashMap<>());
        conditionals.put(type, action);
    }

    @NotNull
    public Gui setOnOpen(@Nullable Openable action) {
        this.opener = action;
        return this;
    }

    @NotNull
    public Gui setOnClose(@Nullable Closable action) {
        this.closer = action;
        return this;
    }

    @NotNull
    public Gui setOnDrop(@Nullable Droppable action) {
        this.dropper = action;
        return this;
    }

    @NotNull
    public Gui setOnPage(@Nullable Pagable action) {
        this.pager = action;
        return this;
    }

    public Gui setNextPage(ItemStack item) {
        this.nextPage = item;
        return this;
    }

    public Gui setPrevPage(ItemStack item) {
        this.prevPage = item;
        return this;
    }

    public void reset() {
        if (this.inventory != null) {
            this.inventory.clear();
        }

        setActionForRange(0, 53, null);
        this.cellItems.clear();
        update();
    }

    @NotNull
    public Gui setNextPage(int cell, @NotNull ItemStack item) {
        this.nextPageItem = this.cellItems.get(cell);
        this.nextPageIndex = cell;
        this.nextPage = item;

        if (this.page < this.pages) {
            setButton(nextPageIndex, nextPage, ClickType.LEFT, (event) -> this.nextPage());
        }

        return this;
    }

    @NotNull
    public Gui setNextPage(int row, int col, @NotNull ItemStack item) {
        return setNextPage(col + row * 9, item);
    }

    @NotNull
    public Gui setPrevPage(int cell, @NotNull ItemStack item) {
        this.prevPageItem = this.cellItems.get(cell);
        this.prevPageIndex = cell;
        this.prevPage = item;

        if (this.page > 1) {
            setButton(prevPageIndex, prevPage, ClickType.LEFT, (event) -> this.prevPage());
        }

        return this;
    }

    @NotNull
    public Gui setPrevPage(int row, int col, @NotNull ItemStack item) {
        return setPrevPage(col + row * 9, item);
    }

    public void setPages(int pages) {
        this.pages = Math.max(1, pages);

        if (this.page > pages) {
            setPage(pages);
        }
    }

    public void setPage(int page) {
        int lastPage = this.page;
        this.page = Math.max(1, Math.min(pages, page));

        if (this.pager != null && this.page != lastPage) {
            this.pager.onPageChange(new GuiPageEvent(this, guiManager, lastPage, this.page));

            // page markers
            updatePageNavigation();
        }
    }

    public void changePage(int direction) {
        int lastPage = this.page;
        this.page = Math.max(1, Math.min(this.pages, this.page + direction));

        if (this.pager != null && this.page != lastPage) {
            this.pager.onPageChange(new GuiPageEvent(this, this.guiManager, lastPage, this.page));

            // page markers
            updatePageNavigation();
        }
    }

    public void nextPage() {
        if (this.page < this.pages) {
            int lastPage = this.page;
            ++this.page;

            // page switch events
            if (this.pager != null) {
                this.pager.onPageChange(new GuiPageEvent(this, this.guiManager, lastPage, this.page));
                updatePageNavigation();
            }
        }
    }

    public void prevPage() {
        if (this.page > 1) {
            int lastPage = this.page;
            --this.page;

            if (this.pager != null) {
                this.pager.onPageChange(new GuiPageEvent(this, this.guiManager, lastPage, this.page));
                updatePageNavigation();
            }
        }
    }

    protected void updatePageNavigation() {
        if (this.prevPage != null) {
            if (this.page > 1) {
                this.setButton(this.prevPageIndex, this.prevPage, ClickType.LEFT, (event) -> this.prevPage());
            } else {
                this.setItem(this.prevPageIndex, this.prevPageItem);
                this.clearActions(this.prevPageIndex);
            }
        }

        if (this.nextPage != null) {
            if (this.pages > 1 && this.page != this.pages) {
                this.setButton(this.nextPageIndex, this.nextPage, ClickType.LEFT, (event) -> this.nextPage());
            } else {
                this.setItem(this.nextPageIndex, this.nextPageItem);
                this.clearActions(this.nextPageIndex);
            }
        }

        if (this.page == 1) {
            this.setItem(this.prevPageIndex, getDefaultItem());
            this.clearActions(this.prevPageIndex);
        }

        if (this.page == this.pages) {
            this.setItem(this.nextPageIndex, getDefaultItem());
            this.clearActions(this.nextPageIndex);
        }
    }

    @NotNull
    protected Inventory getOrCreateInventory(@NotNull GuiManager manager) {
        return this.inventory != null ? this.inventory : generateInventory(manager);
    }

    @NotNull
    protected Inventory generateInventory(@NotNull GuiManager manager) {
        this.guiManager = manager;
        final int cells = this.rows * 9;

        createInventory();

        for (int i = 0; i < cells; ++i) {
            final ItemStack item = this.cellItems.get(i);
            this.inventory.setItem(i, item != null ? item : (this.unlockedCells.getOrDefault(i, false) ? AIR : this.blankItem));
        }

        return this.inventory;
    }

    protected void createInventory() {
        this.inventory = new GuiHolder(this.guiManager, this).newInventory(rows * 9, Common.colorize(this.title == null ? "" : trimTitle(this.title)));
    }

    public void update() {
        if (this.inventory == null) {
            return;
        }

        final int cells = this.rows * 9;
        for (int i = 0; i < cells; ++i) {
            final ItemStack item = this.cellItems.get(i);
            this.inventory.setItem(i, item != null ? item : (this.unlockedCells.getOrDefault(i, false) ? AIR : this.blankItem));
        }
    }

    protected static String trimTitle(String title) {
        if (title == null) {
            return "";
        }

        if (ServerVersion.isServerVersionBelow(ServerVersion.V1_9) && title.length() > 32) {
            return title.charAt(30) == '\u00A7' ? title.substring(0, 30) : title.substring(0, 31);
        }

        return title;
    }

    protected boolean onClickOutside(@NotNull GuiManager manager, @NotNull Player player, @NotNull InventoryClickEvent event) {
        return this.dropper == null || this.dropper.onDrop(new GuiDropItemEvent(manager, this, player, event));
    }

    protected boolean onClick(@NotNull GuiManager manager, @NotNull Player player, @NotNull Inventory inventory, @NotNull InventoryClickEvent event) {
        final int cell = event.getSlot();
        Map<ClickType, Clickable> conditionals = this.conditionalButtons.get(cell);

        Clickable button;
        if (conditionals != null && ((button = conditionals.get(event.getClick())) != null || (button = conditionals.get(null)) != null)) {
            if (this.singleClicks.containsKey(cell)) {
                if (this.singleClicks.get(cell)) {
                    return false;
                }

                this.singleClicks.put(cell, true);
            }

            if (this.timedClicks.containsKey(cell)) {
                if (!this.lastClicked.containsKey(cell)) {
                    this.lastClicked.put(cell, System.currentTimeMillis());
                } else {
                    if (System.currentTimeMillis() < this.lastClicked.get(cell) + this.timedClicks.get(cell))
                        return false;
                    else
                        this.lastClicked.put(cell, System.currentTimeMillis());
                }
            }

            if (!this.allowShiftClick && event.isShiftClick())// TODO: Info - used to stop players from spam shift clicking items out of gui
                return false;

            button.onClick(new GuiClickEvent(manager, this, player, event, cell, true));

        } else {
            // no event for this button
            if (this.defaultClicker != null) {
                // this is a default action, not a triggered action
                this.defaultClicker.onClick(new GuiClickEvent(manager, this, player, event, cell, true));
            }

            if (privateDefaultClicker != null) {
                // this is a private default action, not a triggered action
                privateDefaultClicker.onClick(new GuiClickEvent(manager, this, player, event, cell, true));
            }

            return false;
        }

        return true;
    }

    protected boolean onClickPlayerInventory(@NotNull GuiManager manager, @NotNull Player player, @NotNull Inventory openInv, @NotNull InventoryClickEvent event) {
        return false;
    }

    public void onOpen(@NotNull GuiManager manager, @NotNull Player player) {
        this.open = true;
        this.guiManager = manager;

        if (this.opener != null) {
            this.opener.onOpen(new GuiOpenEvent(manager, this, player));
        }
    }

    public void onClose(@NotNull GuiManager manager, @NotNull Player player) {
        if (!this.allowClose) {
            manager.showGUI(player, this);
            return;
        }

        boolean showParent = this.open && this.parent != null;

        if (closer != null) {
            this.closer.onClose(new GuiCloseEvent(manager, this, player));
        }

        if (showParent) {
            manager.showGUI(player, parent);
        }
    }

    public CompSound getDefaultSound() {
        return this.defaultSound;
    }

    public void setDefaultSound(CompSound sound) {
        this.defaultSound = sound;
    }
}
