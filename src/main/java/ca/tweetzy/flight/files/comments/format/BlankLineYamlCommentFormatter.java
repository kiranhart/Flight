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
import ca.tweetzy.flight.files.utils.StringUtils;

/**
 * {@link YamlCommentFormat#BLANK_LINE} formatter
 */
public class BlankLineYamlCommentFormatter extends YamlCommentFormatter {

    public BlankLineYamlCommentFormatter() {
        this(new YamlCommentFormatterConfiguration());
    }

    public BlankLineYamlCommentFormatter(final YamlCommentFormatterConfiguration blockFormatter) {
        this(blockFormatter, new YamlSideCommentFormatterConfiguration());
    }

    public BlankLineYamlCommentFormatter(final YamlCommentFormatterConfiguration blockFormatter, final YamlSideCommentFormatterConfiguration sideFormatter) {
        super(blockFormatter, sideFormatter);
        this.stripPrefix(true).trim(false);
        blockFormatter.prefix('\n' + blockFormatter.prefixFirst(), blockFormatter.prefixMultiline());
    }

    @Override
    public String dump(final String comment, final CommentType type, final KeyTree.Node node) {
        if (type == CommentType.SIDE) {
            final String defaultPrefixFirst = sideFormatter.prefixFirst();
            final String blankLineSideFirstPrefix = '\n' + StringUtils.stripIndentation(defaultPrefixFirst);
            sideFormatter.prefix(blankLineSideFirstPrefix, sideFormatter.prefixMultiline());
            final String dump = super.dump(comment, type, node);
            sideFormatter.prefix(defaultPrefixFirst, sideFormatter.prefixMultiline());
            return dump;
        }
        return super.dump(comment, type, node);
    }

}
