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

package ca.tweetzy.flight.command;

import ca.tweetzy.flight.utils.Common;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Date Created: April 09 2022
 * Time Created: 11:37 p.m.
 *
 * @author Kiran Hart
 */
public abstract class Command {

    private final AllowedExecutor allowedExecutor;
    private final List<String> subCommands = new ArrayList<>();

    protected Command(AllowedExecutor allowedExecutor, String... subCommands) {
        this.allowedExecutor = allowedExecutor;
        this.subCommands.addAll(Arrays.asList(subCommands));
    }

    public final List<String> getSubCommands() {
        return Collections.unmodifiableList(this.subCommands);
    }

    public final void addSubCommand(String command) {
        this.subCommands.add(command);
    }

    protected boolean isNoConsole() {
        return this.allowedExecutor == AllowedExecutor.PLAYER;
    }

    public AllowedExecutor getAllowedExecutor() {
        return allowedExecutor;
    }

    protected abstract ReturnType execute(CommandSender sender, String... args);

    protected abstract List<String> tab(CommandSender sender, String... args);

    public abstract String getPermissionNode();

    public abstract String getSyntax();

    public abstract String getDescription();

    protected void tell(@NonNull final CommandSender sender, @NonNull final String msg) {
        Common.tell(sender, true, msg);
    }

    protected void tellNoPrefix(@NonNull final CommandSender sender, @NonNull final String msg) {
        Common.tell(sender, false, msg);
    }
}
