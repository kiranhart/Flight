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

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.utils.Pair;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The original author of this code is SpraxDev, the original is from SongodaCore,
 * the following code below, may not reflect the original version.
 */
public interface ConfigEntry {
    @NotNull String getKey();

    @NotNull IConfiguration getConfig();

    default boolean has() {
        return getConfig().has(getKey());
    }

    void set(@Nullable Object value);

    default @Nullable Object get() {
        return getConfig().get(getKey());
    }

    default @Nullable Object getOr(@Nullable Object fallbackValue) {
        return getConfig().getOr(getKey(), fallbackValue);
    }

    @Nullable Object getDefaultValue();

    void setDefaultValue(@Nullable Object defaultValue);

    /**
     * @see #withComment(Supplier)
     */
    @Contract("_ -> this")
    default ConfigEntry withComment(String comment) {
        return this.withComment(() -> comment);
    }

    /**
     * @see NodeCommentable#setNodeComment(String, Supplier)
     */
    @Contract("_ -> this")
    ConfigEntry withComment(Supplier<String> comment);

    /**
     * @return <code>&lt;configVersion, Pair&lt;keyInGivenVersion, valueConverter&gt;&gt;</code>
     */
    @Nullable Map<Integer, Pair<@Nullable String, @Nullable Function<@Nullable Object, @Nullable Object>>> getUpgradeSteps();

    /**
     * @see #withUpgradeStep(int, String, Function)
     */
    @Contract("_, _ -> this")
    default ConfigEntry withUpgradeStep(int version, @NotNull String keyInGivenVersion) {
        return withUpgradeStep(version, keyInGivenVersion, null);
    }

    /**
     * @param version           The version to upgrade from (e.g. 1 for the upgrade from 1 to 2)
     * @param keyInGivenVersion The old key in the given version or null if it didn't change
     * @param valueConverter    A function that converts the old version's value to a new one, or null if it didn't change
     */
    @Contract("_, null, null -> fail; _, _, _ -> this")
    ConfigEntry withUpgradeStep(int version, @Nullable String keyInGivenVersion, @Nullable Function<Object, Object> valueConverter);

    default @Nullable String getString() {
        return getStringOr(null);
    }

    @Contract("!null -> !null")
    default @Nullable String getStringOr(String fallbackValue) {
        Object value = get();

        return value == null ? fallbackValue : value.toString();
    }

    /**
     * @see #getIntOr(int)
     */
    default int getInt() {
        return getIntOr(0);
    }

    /**
     * Returns the values parsed as an integer.<br>
     * If it is a floating point number, it will be rounded down.
     *
     * @see Double#valueOf(String)
     */
    default int getIntOr(int fallbackValue) {
        String value = getString();

        if (value == null) {
            return fallbackValue;
        }

        return Double.valueOf(value).intValue();
    }

    /**
     * @see #getDoubleOr(double)
     */
    default double getDouble() {
        return getDoubleOr(0);
    }

    /**
     * Returns the values parsed as a double.
     *
     * @see Double#parseDouble(String)
     */
    default double getDoubleOr(double fallbackValue) {
        String value = getString();

        if (value == null) {
            return fallbackValue;
        }

        return Double.parseDouble(value);
    }

    /**
     * @see #getBooleanOr(boolean)
     */
    default boolean getBoolean() {
        return getBooleanOr(false);
    }

    /**
     * Returns the values parsed as a boolean.
     *
     * @see Boolean#parseBoolean(String)
     */
    default boolean getBooleanOr(boolean fallbackValue) {
        String value = getString();

        if (value == null) {
            return fallbackValue;
        }

        return Boolean.parseBoolean(value);
    }

    default @Nullable List<String> getStringList() {
        return getStringListOr(null);
    }

    @Contract("!null -> !null")
    default @Nullable List<String> getStringListOr(List<String> fallbackValue) {
        Object value = get();

        if (value instanceof List) {
            //noinspection unchecked
            return (List<String>) value;
        }

        return fallbackValue;
    }

    default @Nullable List<Integer> getIntList() {
        return getIntListOr(null);
    }

    @Contract("!null -> !null")
    default @Nullable List<Integer> getIntListOr(List<Integer> fallbackValue) {
        Object value = get();

        if (value instanceof List) {
            //noinspection unchecked
            return (List<Integer>) value;
        }

        return fallbackValue;
    }

    default CompMaterial getMaterial() {
        return getMaterialOr(null);
    }

    @Contract("!null -> !null")
    default @Nullable CompMaterial getMaterialOr(@Nullable CompMaterial defaultValue) {
        String value = getString();

        if (value == null) {
            return defaultValue;
        }

        return CompMaterial.matchCompMaterial(value).orElse(null);
    }

    default EntityType getEntityType() {
        return getEntityTypeOr(null);
    }

    @Contract("!null -> !null")
    default @Nullable EntityType getEntityTypeOr(@Nullable EntityType defaultValue) {
        String value = getString();

        if (value == null) {
            return defaultValue;
        }

        return EntityType.valueOf(value);
    }
}
