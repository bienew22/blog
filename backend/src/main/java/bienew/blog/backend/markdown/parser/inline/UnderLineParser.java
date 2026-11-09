package bienew.blog.backend.markdown.parser.inline;

import bienew.blog.backend.markdown.ast.InlineNode;
import bienew.blog.backend.markdown.ast.inline.UnderLine;
import bienew.blog.backend.markdown.parser.InlineParser;
import bienew.blog.backend.markdown.parser.ParseResult;

public class UnderLineParser extends AbstractInlineParser {
    @Override
    protected boolean canParse(String text, int startIndex) {

        if (startIndex >= text.length()) {
            return false;
        }

        if (text.startsWith("__", startIndex)) {
            int endIndex = text.indexOf("__", startIndex + 2);

            // 최소 한 글자 이상이 있어야 밑줄 텍스트로 인식
            return endIndex != -1 && endIndex > startIndex + 1;
        }

        return false;
    }

    @Override
    protected ParseResult<? extends InlineNode> parseNode(String text, int startIndex) {

        int startUnderLineIndex = startIndex + 2;
        int endUnderLineIndex = text.indexOf("__", startUnderLineIndex);

        String underLineText = text.substring(startUnderLineIndex, endUnderLineIndex);
        return new ParseResult<>(new UnderLine(InlineParser.INSTANCE.parse(underLineText)),
                endUnderLineIndex + 2, true);
    }
}
