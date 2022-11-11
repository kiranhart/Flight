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


import ca.tweetzy.flight.files.utils.Validate;

public class YamlSideCommentFormatterConfiguration extends YamlCommentFormatterConfiguration {

    public static final String DEFAULT_SIDE_COMMENT_PREFIX = " " + DEFAULT_COMMENT_PREFIX;

    public YamlSideCommentFormatterConfiguration() {
        this(DEFAULT_SIDE_COMMENT_PREFIX);
    }

    public YamlSideCommentFormatterConfiguration(String sidePrefix) {
        super(sidePrefix);
    }

    public YamlSideCommentFormatterConfiguration(String sidePrefix, String prefixMultiline) {
        super(sidePrefix, prefixMultiline);
    }

    @Override
    public YamlSideCommentFormatterConfiguration prefix(String sidePrefix) {
        super.prefix(sidePrefix, DEFAULT_COMMENT_PREFIX);
        return this;
    }

    @Override
    public YamlSideCommentFormatterConfiguration prefix(String prefixFirst, String prefixMultiline) {
        super.prefix(prefixFirst, prefixMultiline);
        return this;
    }

    @Override
    public YamlSideCommentFormatterConfiguration suffix(String suffixLast) {
        super.suffix(suffixLast);
        return this;
    }

    @Override
    public YamlSideCommentFormatterConfiguration suffix(String suffixLast, String suffixMultiline) {
        super.suffix(suffixLast, suffixMultiline);
        return this;
    }

    @Override
    public YamlSideCommentFormatterConfiguration trim(boolean trim) {
        super.trim(trim);
        return this;
    }

    @Override
    public YamlSideCommentFormatterConfiguration stripPrefix(boolean stripPrefix) {
        super.stripPrefix(stripPrefix);
        return this;
    }

    @Override
    protected void checkCommentPrefix(final String sidePrefix) {
        Validate.isTrue(sidePrefix != null
                        && !sidePrefix.isEmpty()
                        && Character.isWhitespace(sidePrefix.charAt(0)),
                "Side comment prefix must start with space");
        super.checkCommentPrefix(sidePrefix);
    }

    @Override
    protected void checkCommentPrefixMultiline(final String commentPrefix) {
        super.checkCommentPrefix(commentPrefix);
    }
}
