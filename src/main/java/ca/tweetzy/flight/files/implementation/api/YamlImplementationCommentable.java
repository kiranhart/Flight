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

import ca.tweetzy.flight.files.comments.YamlCommentMapper;
import ca.tweetzy.flight.files.configuration.comments.CommentType;
import ca.tweetzy.flight.files.configuration.comments.Commentable;
import ca.tweetzy.flight.files.file.YamlConfigurationOptions;

/**
 * A YAML implementation capable of processing comments.
 */
public abstract class YamlImplementationCommentable implements YamlImplementation, Commentable {

    /**
     * A comment mapper to add comments to sections or values
     **/
    protected YamlCommentMapper yamlCommentMapper;

    /**
     * Configuration options for loading and dumping Yaml.
     */
    protected YamlConfigurationOptions options;

    @Override
    public void setComment(final String path, final String comment, final CommentType type) {
        if (this.yamlCommentMapper != null) {
            this.yamlCommentMapper.setComment(path, comment, type);
        }
    }

    @Override
    public String getComment(final String path, final CommentType type) {
        if (this.yamlCommentMapper == null) {
            return null;
        }
        return this.yamlCommentMapper.getComment(path, type);
    }

    /**
     * Get the comment mapper to get or set comments.
     *
     * @return the comment mapper or null if parsing comments is not enabled
     */
    public YamlCommentMapper getCommentMapper() {
        return this.yamlCommentMapper;
    }

    @Override
    public void configure(final YamlConfigurationOptions options) {
        this.options = options;
    }
}
