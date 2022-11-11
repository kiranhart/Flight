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


import ca.tweetzy.flight.files.utils.StringUtils;
import ca.tweetzy.flight.files.utils.Validate;

import java.util.Objects;

/**
 * Various settings for controlling the input and output of a {@link Configuration}
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/ConfigurationOptions.java">Source</a>
 */
public class ConfigurationOptions {

    private final Configuration configuration;

    private char pathSeparator = '.';

    private boolean copyDefaults = true;

    private int indent = 2;

    protected ConfigurationOptions(final Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Returns the {@link Configuration} that this object is responsible for.
     *
     * @return Parent configuration
     */
    public Configuration configuration() {
        return this.configuration;
    }

    /**
     * Gets the char that will be used to separate {@link
     * ConfigurationSection}s
     * <p>
     * This value does not affect how the {@link Configuration} is stored,
     * only in how you access the data. The default value is '.'.
     *
     * @return Path separator
     */
    public char pathSeparator() {
        return this.pathSeparator;
    }

    /**
     * Sets the char that will be used to separate {@link
     * ConfigurationSection}s
     * <p>
     * This value does not affect how the {@link Configuration} is stored,
     * only in how you access the data. The default value is '.'
     *
     * @param value Path separator
     *
     * @return This object, for chaining
     */
    public ConfigurationOptions pathSeparator(final char value) {
        Validate.isTrue(value != '\\', value + " is used for escaping and cannot be a path separator");
        Validate.isTrue(value != '[' && value != ']', value + " is used for indexing and cannot be a path separator");
        this.pathSeparator = value;
        StringUtils.setSeparator(value);
        return this;
    }

    /**
     * Checks if the {@link Configuration} should copy values from its default
     * {@link Configuration} directly.
     * <p>
     * If this is true, all values in the default Configuration will be
     * directly copied, making it impossible to distinguish between values
     * that were set and values that are provided by default. As a result,
     * {@link ConfigurationSection#contains(String)} will always
     * return the same value as {@link
     * ConfigurationSection#isSet(String)}. The default value is
     * true.
     *
     * @return Whether or not defaults are directly copied
     */
    public boolean copyDefaults() {
        return this.copyDefaults;
    }

    /**
     * Sets if the {@link Configuration} should copy values from its default
     * {@link Configuration} directly.
     * <p>
     * If this is true, all values in the default Configuration will be
     * directly copied, making it impossible to distinguish between values
     * that were set and values that are provided by default. As a result,
     * {@link ConfigurationSection#contains(String)} will always
     * return the same value as {@link
     * ConfigurationSection#isSet(String)}. The default value is
     * true.
     *
     * @param value Whether or not defaults are directly copied
     *
     * @return This object, for chaining
     */
    public ConfigurationOptions copyDefaults(final boolean value) {
        this.copyDefaults = value;
        return this;
    }

    /**
     * Gets how much spaces should be used to indent each line.
     *
     * @return How much to indent by
     */
    public int indent() {
        return this.indent;
    }

    /**
     * Sets how much spaces should be used to indent each line.
     *
     * @param value New indent
     *
     * @return This object, for chaining
     */
    public ConfigurationOptions indent(int value) {
        this.indent = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigurationOptions)) return false;
        ConfigurationOptions that = (ConfigurationOptions) o;
        return indent == that.indent &&
                pathSeparator == that.pathSeparator &&
                copyDefaults == that.copyDefaults &&
                Objects.equals(configuration, that.configuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(indent, pathSeparator, copyDefaults, configuration);
    }
}
