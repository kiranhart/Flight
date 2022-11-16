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

package ca.tweetzy.flight.config.yaml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.events.CommentEvent;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.representer.Representer;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The original author of this code is SpraxDev, the original is from SongodaCore,
 * the following code below, may not reflect the original version.
 */public class YamlCommentRepresenter extends Representer {
    private final Map<String, Supplier<String>> nodeComments;

    public YamlCommentRepresenter(DumperOptions dumperOptions, Map<String, Supplier<String>> nodeComments) {
        super(dumperOptions);
        this.nodeComments = nodeComments;
    }

    @Override
    public Node represent(Object data) {
        Node rootNode = super.represent(data);

        if (!(rootNode instanceof MappingNode)) {
            return rootNode;
        }

        for (NodeTuple nodeTuple : ((MappingNode) rootNode).getValue()) {
            if (!(nodeTuple.getKeyNode() instanceof ScalarNode)) {
                continue;
            }

            applyComment((ScalarNode) nodeTuple.getKeyNode(), ((ScalarNode) nodeTuple.getKeyNode()).getValue());

            if (nodeTuple.getValueNode() instanceof MappingNode) {
                String key = ((ScalarNode) nodeTuple.getKeyNode()).getValue();

                resolveSubNodes(((MappingNode) nodeTuple.getValueNode()), key);
            }
        }

        return rootNode;
    }

    protected void resolveSubNodes(MappingNode mappingNode, String key) {
        for (NodeTuple nodeTuple : mappingNode.getValue()) {
            if (!(nodeTuple.getKeyNode() instanceof ScalarNode)) {
                continue;
            }

            String newKey = key + "." + ((ScalarNode) nodeTuple.getKeyNode()).getValue();

            applyComment((ScalarNode) nodeTuple.getKeyNode(), newKey);

            if (nodeTuple.getValueNode() instanceof MappingNode) {
                resolveSubNodes(((MappingNode) nodeTuple.getValueNode()), newKey);
            }
        }
    }

    protected void applyComment(ScalarNode scalarNode, String key) {
        Supplier<String> innerValue = this.nodeComments.get(key);

        if (innerValue != null) {
            scalarNode.setBlockComments(Collections.singletonList(new CommentLine(new CommentEvent(CommentType.BLOCK, " " + innerValue.get(), null, null))));
        }
    }
}
