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

package ca.tweetzy.flight.config.yaml;

import ca.tweetzy.flight.config.ConfigEntry;
import ca.tweetzy.flight.config.IConfiguration;
import ca.tweetzy.flight.config.WriteableConfigEntry;
import ca.tweetzy.flight.utils.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * The original author of this code is SpraxDev, the original is from SongodaCore,
 * the following code below, may not reflect the original version.
 */public class YamlConfigEntry implements WriteableConfigEntry {
    protected final @NotNull YamlConfiguration config;
    protected final @NotNull String key;
    protected @Nullable Object defaultValue;

    protected @Nullable Map<Integer, Pair<@Nullable String, @Nullable Function<@Nullable Object, @Nullable Object>>> upgradeSteps;

    public YamlConfigEntry(@NotNull YamlConfiguration config, @NotNull String key, @Nullable Object defaultValue) {
        this.config = config;
        this.key = key;
        this.defaultValue = defaultValue;
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
    public @Nullable Object getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public @Nullable Map<Integer, Pair<@Nullable String, @Nullable Function<@Nullable Object, @Nullable Object>>> getUpgradeSteps() {
        return this.upgradeSteps;
    }

    @Override
    public void setDefaultValue(@Nullable Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public ConfigEntry withUpgradeStep(int version, @Nullable String keyInGivenVersion, @Nullable Function<Object, Object> valueConverter) {
        if (keyInGivenVersion == null && valueConverter == null) {
            throw new IllegalArgumentException("You must provide either a key or a value converter");
        }

        if (this.upgradeSteps == null) {
            this.upgradeSteps = new HashMap<>(1);
        }

        this.upgradeSteps.put(version, new Pair<>(keyInGivenVersion, valueConverter));

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YamlConfigEntry that = (YamlConfigEntry) o;
        return this.config.equals(that.config) &&
                this.key.equals(that.key) &&
                Objects.equals(this.defaultValue, that.defaultValue) &&
                Objects.equals(this.upgradeSteps, that.upgradeSteps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.config, this.key, this.defaultValue, this.upgradeSteps);
    }
}
