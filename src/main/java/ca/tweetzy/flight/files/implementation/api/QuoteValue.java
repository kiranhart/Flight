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


import ca.tweetzy.flight.files.utils.StringUtils;

import java.util.Objects;

public class QuoteValue<T> {

    protected final T value;
    protected final QuoteStyle quoteStyle;

    public QuoteValue(T value, QuoteStyle quoteStyle) {
        this.value = value;
        this.quoteStyle = quoteStyle;
    }

    public T getValue() {
        return this.value;
    }

    public QuoteStyle getQuoteStyle() {
        return this.quoteStyle;
    }

    @Override
    public String toString() {
        return this.quoteStyle.toString() + "=" + (this.value == null ? "!!null" : StringUtils.quoteNewLines(this.value.toString()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuoteValue<?> that = (QuoteValue<?>) o;
        return Objects.equals(value, that.value) && quoteStyle == that.quoteStyle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, quoteStyle);
    }

}
