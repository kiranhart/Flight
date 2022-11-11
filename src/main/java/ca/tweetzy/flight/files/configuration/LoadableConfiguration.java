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

package ca.tweetzy.flight.files.configuration;

import ca.tweetzy.flight.files.exceptions.InvalidConfigurationException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface LoadableConfiguration {

    /**
     * Loads this configuration from the specified string.
     * <p>
     * All the values contained within this configuration will be removed,
     * leaving only settings and defaults, and the new values will be loaded
     * from the given string.
     * <p>
     * If the string is invalid in any way, an exception will be thrown.
     *
     * @param contents Contents of a Configuration to load.
     *
     * @throws IOException                   if cannot read contents.
     * @throws InvalidConfigurationException if the specified string is invalid.
     * @throws IllegalArgumentException      if contents is null.
     */
    void loadFromString(final String contents) throws IOException, InvalidConfigurationException;

    /**
     * Saves this configuration to a string, and returns it.
     *
     * @return a String containing this configuration.
     *
     * @throws IOException when the contents cannot be written for any reason.
     */
    String saveToString() throws IOException;

    /**
     * Loads this configuration from the specified reader.
     * <p>
     * All the values contained within this configuration will be removed,
     * leaving only settings and defaults, and the new values will be loaded
     * from the given string.
     * <p>
     * If the contents are invalid in any way, an exception will be thrown.
     *
     * @param reader Reader of a Configuration to load.
     *
     * @throws IOException                   if reader throws an IOException.
     * @throws InvalidConfigurationException if the specified configuration is invalid.
     * @throws IllegalArgumentException      if reader is null.
     */
    void load(final Reader reader) throws IOException, InvalidConfigurationException;

    /**
     * Saves this configuration to a writer.
     *
     * @param writer where to save this configuration
     *
     * @throws IOException when the contents cannot be written for any reason.
     */
    void save(final Writer writer) throws IOException;

}
