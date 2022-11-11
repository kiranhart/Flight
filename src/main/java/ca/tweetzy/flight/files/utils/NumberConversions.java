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

/**
 * Utils for casting number types to other number types
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/util/NumberConversions.java">Bukkit Source</a>
 */
public final class NumberConversions {

    private NumberConversions() {
    }

    public static int floor(final double num) {
        final int floor = (int) num;
        return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }

    public static int ceil(final double num) {
        final int floor = (int) num;
        return floor == num ? floor : floor + (int) (~Double.doubleToRawLongBits(num) >>> 63);
    }

    public static int round(final double num) {
        return NumberConversions.floor(num + 0.5d);
    }

    public static double square(final double num) {
        return num * num;
    }

    public static int toInt(final Object object) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }

        try {
            return Integer.parseInt(object.toString());
        } catch (final NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public static float toFloat(final Object object) {
        if (object instanceof Number) {
            return ((Number) object).floatValue();
        }

        try {
            return Float.parseFloat(object.toString());
        } catch (final NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public static double toDouble(final Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }

        try {
            return Double.parseDouble(object.toString());
        } catch (final NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public static long toLong(final Object object) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        }

        try {
            return Long.parseLong(object.toString());
        } catch (final NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public static short toShort(final Object object) {
        if (object instanceof Number) {
            return ((Number) object).shortValue();
        }

        try {
            return Short.parseShort(object.toString());
        } catch (final NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public static byte toByte(final Object object) {
        if (object instanceof Number) {
            return ((Number) object).byteValue();
        }

        try {
            return Byte.parseByte(object.toString());
        } catch (final NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

}
