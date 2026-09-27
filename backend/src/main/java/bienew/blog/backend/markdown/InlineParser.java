package bienew.blog.backend.markdown;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.inline.Text;
import bienew.blog.backend.markdown.parser.AbstractInlineParser;
import bienew.blog.backend.markdown.parser.ParseResult;
import bienew.blog.backend.markdown.parser.inline.BoldParser;

import java.util.ArrayList;
import java.util.List;

public enum InlineParser {
    INSTANCE;

    private final List<AbstractInlineParser> parsers;

    InlineParser() {
        parsers = new ArrayList<>();
        parsers.add(new BoldParser());
    }

    public static InlineParser getInstance() {
        return INSTANCE;
    }

    public void parse(ASTNode node) {
        if (node == null) {
            return;
        }

        parse(node.getSource()).forEach(node::addChild);
    }

    public List<ASTNode> parse(String text) {
        List<ASTNode> nodes = new ArrayList<>();


        int index = 0;
        StringBuilder sb = new StringBuilder();

        while (index < text.length()) {
            boolean matched = false;
            ParseResult result = null;

            // Iterate through the parsers to find a match
            for (AbstractInlineParser parser : parsers) {
                ParseResult parseResult = parser.parse(text, index);

                if (parseResult.isParsed()) {
                    result = parseResult;
                    matched = true;
                    break;
                }
            }

            // 해당 하는 파서가 없는 경우 : TextNode으로 변환
            if (!matched) {
                sb.append(text.charAt(index));
                index++;
            } else {
                // If there is accumulated text, create a Text node and add it to the list
                if (!sb.isEmpty()) {
                    nodes.add(new Text(sb.toString()));
                    sb.setLength(0);
                }

                nodes.add(result.node());
                index = result.nextIndex();
            }
        }

        // If there is any remaining text, create a Text node and add it to the list
        if (!sb.isEmpty()) {
            nodes.add(new Text(sb.toString()));
        }

        return nodes;
    }
}
