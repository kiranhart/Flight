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

import java.util.LinkedHashMap;
import java.util.stream.Stream;

/**
 * Date Created: April 09 2022
 * Time Created: 11:42 p.m.
 *
 * @author Kiran Hart
 */
public final class NestedCommand {

    final Command parent;
    final LinkedHashMap<String, Command> children = new LinkedHashMap<>();

    protected NestedCommand(Command parent) {
        this.parent = parent;
    }

    public NestedCommand addSubCommand(Command command) {
        command.getSubCommands().forEach(cmd -> children.put(cmd.toLowerCase(), command));
        return this;
    }

    public NestedCommand addSubCommands(Command... commands) {
        Stream.of(commands).forEach(command -> command.getSubCommands().forEach(cmd -> children.put(cmd.toLowerCase(), command)));
        return this;
    }
}
