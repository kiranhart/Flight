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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * The original author of this code is SpraxDev, the original is from SongodaCore,
 * the following code below, may not reflect the original version.
 */
public interface IConfiguration {
    /**
     * This method returns whether a given key is set memory, ignoring its possibly null value.
     *
     * {@link #set(String, Object)}
     * {@link #unset(String)}
     */
    boolean has(String key);

    /**
     * This method returns the value for a given key.
     * A value of null can mean that the key does not exist or that the value is null.
     *
     * @see #has(String)
     */
    @Nullable
    Object get(String key);

    /**
     * This method is mostly identical to {@link #get(String)}
     * but returns the given default value if the key doesn't exist or the value is null.
     */
    @Nullable
    Object getOr(String key, @Nullable Object defaultValue);

    /**
     * This method sets a given key to a given value in memory.
     *
     * @return The previous value associated with key, or null if there was no mapping for key
     *
     * @see #save(Writer)
     */
    Object set(@NotNull String key, @Nullable Object value);

    /**
     * This method removes the given key from memory together with its value.
     *
     * @return The previous value associated with key, or null if there was no mapping for key
     */
    Object unset(String key);

    /**
     * This method clears all the configuration values from memory that have been loaded or set.
     *
     * @see #load(Reader)
     */
    void reset();

    /**
     * This method parses and loads the configuration and stores them as key-value pairs in memory.
     * Keys that are not loaded with this call but still exist in memory, are removed.
     * Additional data may be read depending on the implementation (e.g. comments).
     *
     * @see #reset()
     */
    void load(Reader reader) throws IOException;

    /**
     * This method serializes the key-value pairs in memory and writes them to the given writer.
     * Additional data may be written depending on the implementation (e.g. comments).
     */
    void save(Writer writer) throws IOException;
}
