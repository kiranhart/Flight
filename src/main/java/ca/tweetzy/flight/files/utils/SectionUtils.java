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

package ca.tweetzy.flight.files.utils;

import ca.tweetzy.flight.files.configuration.ConfigurationSection;

import java.util.Map;

public final class SectionUtils {

    public static void convertMapsToSections(final Map<?, ?> values, final ConfigurationSection section) {
        if (values == null) {
            return;
        }
        for (final Map.Entry<?, ?> entry : values.entrySet()) {
            Object keyObject = entry.getKey();
            String key;
            if (keyObject == null) {
                key = "";
            } else {
                key = keyObject.toString();
            }
            final Object value = entry.getValue();

            if (value instanceof Map) {
                convertMapsToSections((Map<?, ?>) value, section.createSection(key));
            } else {
                section.set(key, value);
            }
        }
    }

}
