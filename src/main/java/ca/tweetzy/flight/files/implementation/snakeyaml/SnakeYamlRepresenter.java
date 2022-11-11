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

import ca.tweetzy.flight.files.configuration.ConfigurationSection;
import ca.tweetzy.flight.files.configuration.serialization.ConfigurationSerializable;
import ca.tweetzy.flight.files.configuration.serialization.ConfigurationSerialization;
import ca.tweetzy.flight.files.implementation.api.QuoteValue;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Bukkit
 * @author Carleslc
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/file/YamlRepresenter.java">Bukkit Source</a>
 */
public class SnakeYamlRepresenter extends Representer {

    public SnakeYamlRepresenter() {
        this.multiRepresenters.put(ConfigurationSection.class, new RepresentConfigurationSection());
        this.multiRepresenters.put(ConfigurationSerializable.class, new RepresentConfigurationSerializable());
        this.multiRepresenters.put(QuoteValue.class, new RepresentQuoteValue());
    }

    private final class RepresentConfigurationSection extends RepresentMap {

        @Override
        public Node representData(final Object data) {
            return super.representData(((ConfigurationSection) data).getValues(false));
        }

    }

    private final class RepresentConfigurationSerializable extends RepresentMap {

        @Override
        public Node representData(final Object data) {
            final ConfigurationSerializable serializable = (ConfigurationSerializable) data;
            final Map<String, Object> values = new LinkedHashMap<>();
            values.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(serializable.getClass()));
            values.putAll(serializable.serialize());

            return super.representData(values);
        }

    }

    private final class RepresentQuoteValue implements Represent {

        @Override
        public Node representData(final Object data) {
            final QuoteValue<?> quoteValue = (QuoteValue<?>) data;

            final DumperOptions.ScalarStyle quoteScalarStyle = SnakeYamlQuoteValue.getQuoteScalarStyle(quoteValue.getQuoteStyle());
            final Object value = quoteValue.getValue();

            if (value == null) {
                return representScalar(Tag.NULL, "", quoteScalarStyle);
            }

            DumperOptions.ScalarStyle defaultScalarStyle = getDefaultScalarStyle();
            setDefaultScalarStyle(quoteScalarStyle); // change default scalar style

            final Node node = SnakeYamlRepresenter.this.representData(value);

            setDefaultScalarStyle(defaultScalarStyle); // restore default scalar style

            return node;
        }

    }
}
