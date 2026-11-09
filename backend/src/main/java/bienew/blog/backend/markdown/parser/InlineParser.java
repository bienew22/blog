package bienew.blog.backend.markdown.parser;

import bienew.blog.backend.markdown.ast.InlineNode;
import bienew.blog.backend.markdown.ast.inline.Text;
import bienew.blog.backend.markdown.parser.inline.*;

import java.util.ArrayList;
import java.util.List;

public enum InlineParser {
    INSTANCE;

    private final List<AbstractInlineParser> parsers;

    InlineParser() {
        parsers = List.of(
                // **some** 구조의 문장을 parse
                new BoldParser(),
                // _{some} 구조의 문장을 parse
                new SubScriptParser(),
                // ^{some} 구조의 문장을 parse
                new SupScriptParser(),
                // __some__ 구조의 문장을 parse
                new UnderLineParser(),
                // _some_ 구조의 문장을 parse
                new ItalicParser(),
                // [some](url) 구조의 문장을 parse
                new LinkParser(),
                // `some` 구조의 문장을 parse
                new InlineCodeParser()
        );
    }
    public List<InlineNode> parse(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }

        List<InlineNode> nodes = new ArrayList<>();

        int index = 0;
        StringBuilder sb = new StringBuilder();

        while (index < text.length()) {
            boolean matched = false;
            ParseResult<? extends InlineNode> result = null;

            // Iterate through the parsers to find a match
            for (AbstractInlineParser parser : parsers) {
                ParseResult<? extends InlineNode> parseResult = parser.parse(text, index);

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