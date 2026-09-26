package bienew.blog.backend.markdown.extension.attribute;

import org.commonmark.node.Node;

import java.util.Map;

public class AttributeProvider implements org.commonmark.renderer.html.AttributeProvider {
    @Override
    public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
        AttributeBlock attribute = findAttributeNode(node);

        if (attribute == null) {
            return;
        }

        String classes = String.join(" ", attribute.getClasses());

        if (!classes.isEmpty()) {
            attributes.put("class", classes);
        }
    }

    private AttributeBlock findAttributeNode(Node node) {
        Node current = node.getFirstChild();

        while (current != null) {
            if (current instanceof AttributeBlock attributeNode) {
                return attributeNode;
            }
            current = current.getNext();
        }
        return null;
    }
}
