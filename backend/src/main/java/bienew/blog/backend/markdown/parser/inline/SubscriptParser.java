package bienew.blog.backend.markdown.parser.inline;

import bienew.blog.backend.markdown.ast.inline.Subscript;
import bienew.blog.backend.markdown.parser.AbstractInlineParser;
import bienew.blog.backend.markdown.parser.ParseResult;

public class SubscriptParser extends AbstractInlineParser {
    @Override
    protected boolean canParse(String text, int startIndex) {
        if (startIndex >= text.length()) {
            return false;
        }

        if (text.startsWith("_{", startIndex)) {
            int endIndex = text.indexOf("}", startIndex + 2);
            // 최소 한 글자 이상이 있어야 아래 첨자 텍스트로 인식
            return endIndex != -1 && endIndex > startIndex + 2;
        }

        return false;
    }

    @Override
    protected ParseResult parseNode(String text, int startIndex) {

        int startSubscriptIndex = startIndex + 2;
        int endSubscriptIndex = text.indexOf("}", startSubscriptIndex);

        // parsing
        if (endSubscriptIndex != -1) {
            String subscriptText = text.substring(startSubscriptIndex, endSubscriptIndex);
            return new ParseResult(new Subscript(subscriptText), endSubscriptIndex + 1, true);
        }

        return ParseResult.notParsed();
    }
}
