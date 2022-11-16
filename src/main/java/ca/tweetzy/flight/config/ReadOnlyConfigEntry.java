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

import ca.tweetzy.flight.utils.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The original author of this code is SpraxDev, the original is from SongodaCore,
 * the following code below, may not reflect the original version.
 */
public class ReadOnlyConfigEntry implements ConfigEntry {
    protected final @NotNull IConfiguration config;
    protected final @NotNull String key;

    public ReadOnlyConfigEntry(@NotNull IConfiguration config, @NotNull String key) {
        this.config = config;
        this.key = key;
    }

    @Override
    public @NotNull String getKey() {
        return this.key;
    }

    @Override
    public @NotNull IConfiguration getConfig() {
        return this.config;
    }

    @Override
    @Contract(" -> null")
    public @Nullable Object getDefaultValue() {
        return null;
    }

    @Override
    @Contract("_ -> fail")
    public void setDefaultValue(@Nullable Object defaultValue) {
        throw new UnsupportedOperationException("Cannot set defaultValue on a read-only config entry");
    }

    @Override
    @Contract("_ -> fail")
    public ConfigEntry withComment(Supplier<String> comment) {
        throw new UnsupportedOperationException("Cannot set comment on a read-only config entry");
    }

    @Override
    @Contract(" -> null")
    public Map<Integer, Pair<String, Function<Object, Object>>> getUpgradeSteps() {
        return null;
    }

    @Override
    @Contract("_, _, _ -> fail")
    public ConfigEntry withUpgradeStep(int version, @Nullable String keyInGivenVersion, @Nullable Function<Object, Object> valueConverter) {
        throw new UnsupportedOperationException("Cannot set upgrade step on a read-only config entry");
    }

    @Override
    @Contract("_ -> fail")
    public void set(@Nullable Object value) {
        throw new UnsupportedOperationException("Cannot set value on a read-only config entry");
    }
}
