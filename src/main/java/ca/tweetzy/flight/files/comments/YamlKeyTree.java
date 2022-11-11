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

package ca.tweetzy.flight.files.comments;


import ca.tweetzy.flight.files.configuration.comments.KeyTree;
import ca.tweetzy.flight.files.file.YamlConfigurationOptions;

public class YamlKeyTree extends KeyTree {

    public YamlKeyTree(final YamlConfigurationOptions options) {
        super(options);
    }

    @Override
    public YamlConfigurationOptions options() {
        return (YamlConfigurationOptions) this.options;
    }

    @Override
    protected KeyTree.Node createNode(final KeyTree.Node parent, final int indent, final String key) {
        return new YamlCommentNode(parent, indent, key);
    }

    public class YamlCommentNode extends KeyTree.Node {

        YamlCommentNode(final Node parent, final int indent, final String name) {
            super(parent, indent, name);
        }

        @Override
        protected KeyTree.Node add(final String key, final boolean priority) {
            int indent = 0;
            if (this != YamlKeyTree.this.root) {
                indent = this.indent;
                if (this.isList) {
                    indent += YamlKeyTree.this.options().indentList();
                } else {
                    indent += YamlKeyTree.this.options.indent();
                }
            }
            return this.add(indent, key, priority);
        }

        @Override
        public void isList(int listSize) {
            super.isList(listSize);

            if (this.parent != null && this.parent.isList) {
                this.indent = this.parent.indent + YamlKeyTree.this.options().indentList() + 2; // "- " prefix
            }
        }
    }
}
