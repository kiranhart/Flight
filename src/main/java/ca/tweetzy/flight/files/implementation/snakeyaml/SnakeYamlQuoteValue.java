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

package ca.tweetzy.flight.files.implementation.snakeyaml;

import ca.tweetzy.flight.files.implementation.api.QuoteStyle;
import org.yaml.snakeyaml.DumperOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SnakeYamlQuoteValue {

    private static final Map<QuoteStyle, DumperOptions.ScalarStyle> QUOTE_SCALAR_STYLES = mapQuoteScalarStyles();

    public static DumperOptions.ScalarStyle getQuoteScalarStyle(final QuoteStyle quoteStyle) {
        return QUOTE_SCALAR_STYLES.get(quoteStyle);
    }

    private static Map<QuoteStyle, DumperOptions.ScalarStyle> mapQuoteScalarStyles() {
        final Map<QuoteStyle, DumperOptions.ScalarStyle> map = new HashMap<>();
        map.put(null, DumperOptions.ScalarStyle.PLAIN);
        map.put(QuoteStyle.PLAIN, DumperOptions.ScalarStyle.PLAIN);
        map.put(QuoteStyle.SINGLE, DumperOptions.ScalarStyle.SINGLE_QUOTED);
        map.put(QuoteStyle.DOUBLE, DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        map.put(QuoteStyle.LITERAL, DumperOptions.ScalarStyle.LITERAL);
        map.put(QuoteStyle.FOLDED, DumperOptions.ScalarStyle.FOLDED);
        return Collections.unmodifiableMap(map);
    }
}
