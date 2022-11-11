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

package ca.tweetzy.flight.files;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.files.file.YamlFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Date Created: April 14 2022
 * Time Created: 7:49 p.m.
 *
 * @author Kiran Hart
 */
public final class ConfigSetting {

    private final YamlFile config;
    private final String key;
    private final Object defaultValue;
    private final String[] comments;

    public ConfigSetting(@NotNull YamlFile config, @NotNull String key, @NotNull Object defaultValue, String... comments) {
        this.config = config;
        this.key = key;
        this.defaultValue = defaultValue;
        this.comments = comments;
        config.addSetting(this);
    }

    public List<Integer> getIntegerList() {
        return config.getIntegerList(key);
    }

    public List<String> getStringList() {
        return config.getStringList(key);
    }

    public boolean getBoolean() {
        return config.getBoolean(key);
    }

    public boolean getBoolean(boolean def) {
        return config.getBoolean(key, def);
    }

    public int getInt() {
        return config.getInt(key);
    }

    public int getInt(int def) {
        return config.getInt(key, def);
    }

    public long getLong() {
        return config.getLong(key);
    }

    public long getLong(long def) {
        return config.getLong(key, def);
    }

    public double getDouble() {
        return config.getDouble(key);
    }

    public double getDouble(double def) {
        return config.getDouble(key, def);
    }

    public String getString() {
        return config.getString(key);
    }

    public String getString(String def) {
        return config.getString(key, def);
    }

    public Object getObject() {
        return config.get(key);
    }

    public Object getObject(Object def) {
        return config.get(key, def);
    }

    @NotNull
    public CompMaterial getMaterial() {
        return CompMaterial.matchCompMaterial(config.getString(key)).orElse(CompMaterial.STONE);
    }

    @NotNull
    public CompMaterial getMaterial(@NotNull CompMaterial def) {
        String val = config.getString(key);
        CompMaterial mat = val != null ? CompMaterial.matchCompMaterial(config.getString(key)).orElse(CompMaterial.STONE) : null;
        return mat != null ? mat : def;
    }

    public YamlFile getConfig() {
        return config;
    }

    public String getKey() {
        return key;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public String[] getComments() {
        return comments;
    }
}
