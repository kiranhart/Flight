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

package ca.tweetzy.flight.files.comments.format;

import ca.tweetzy.flight.files.configuration.comments.CommentType;
import ca.tweetzy.flight.files.configuration.comments.KeyTree;

/**
 * {@link YamlCommentFormat#PRETTY} formatter
 */
public class PrettyYamlCommentFormatter extends YamlCommentFormatter {

    public PrettyYamlCommentFormatter() {
        this(new YamlCommentFormatterConfiguration());
    }

    public PrettyYamlCommentFormatter(final YamlCommentFormatterConfiguration blockFormatter) {
        this(blockFormatter, new YamlSideCommentFormatterConfiguration());
    }

    public PrettyYamlCommentFormatter(final YamlCommentFormatterConfiguration blockFormatter, final YamlSideCommentFormatterConfiguration sideFormatter) {
        super(blockFormatter, sideFormatter);
        this.stripPrefix(true).trim(true);
    }

    @Override
    public String dump(final String comment, final CommentType type, final KeyTree.Node node) {
        if (type == CommentType.BLOCK && node != null && node.getIndentation() == 0 && !node.isFirstNode()) { // Block comment for root keys except the first key
            final YamlCommentFormatterConfiguration blockCommentFormatterConfiguration = this.formatterConfiguration(CommentType.BLOCK);
            final String defaultPrefixFirst = blockCommentFormatterConfiguration.prefixFirst();
            final String defaultPrefixMultiline = blockCommentFormatterConfiguration.prefixMultiline();

            // Prepend default first prefix with a blank line
            blockCommentFormatterConfiguration.prefix('\n' + defaultPrefixFirst, defaultPrefixMultiline);

            final String dump = super.dump(comment, type, node);

            // Reset first prefix to default
            blockCommentFormatterConfiguration.prefix(defaultPrefixFirst, defaultPrefixMultiline);

            return dump;
        }
        return super.dump(comment, type, node);
    }

}
