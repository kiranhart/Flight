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

package ca.tweetzy.flight.config;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * The original author of this code is SpraxDev, the original is from SongodaCore,
 * the following code below, may not reflect the original version.
 */public interface WriteableConfigEntry extends ConfigEntry {
    @Override
    default void set(@Nullable Object value) {
        getConfig().set(getKey(), value);
    }

    @Override
    default ConfigEntry withComment(Supplier<String> comment) {
        ((NodeCommentable) getConfig()).setNodeComment(getKey(), comment);

        return this;
    }
}
