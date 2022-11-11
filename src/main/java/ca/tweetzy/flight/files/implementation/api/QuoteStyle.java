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

package ca.tweetzy.flight.files.implementation.api;

public enum QuoteStyle {
    /**
     * Wrap values with single quotes.
     * <p/>
     * <pre>single: 'value'</pre>
     */
    SINGLE,
    /**
     * Wrap values with double quotes.
     * <p/>
     * <pre>double: "value"</pre>
     */
    DOUBLE,
    /**
     * Default style, without quotes when possible.
     * <p/>
     * <pre>plain: value</pre>
     * <p/>
     * If value have characters that must be escaped then {@link #SINGLE} quote style is used.
     */
    PLAIN,
    /**
     * <pre>
     * literal: |-
     *   Each line
     *   is literal
     *   and are joined with new lines
     * </pre>
     */
    LITERAL,
    /**
     * <pre>
     * folded: {@code >-}
     *   Each line
     *   is literal
     *   and are joined with spaces
     * </pre>
     */
    FOLDED
}
