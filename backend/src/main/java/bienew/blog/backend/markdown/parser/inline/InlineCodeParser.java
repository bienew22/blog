package bienew.blog.backend.markdown.parser.inline;

import bienew.blog.backend.markdown.ast.inline.InlineCode;
import bienew.blog.backend.markdown.parser.AbstractInlineParser;
import bienew.blog.backend.markdown.parser.ParseResult;

public class InlineCodeParser extends AbstractInlineParser {
    @Override
    protected boolean canParse(String text, int startIndex) {
        if (startIndex < text.length() && text.charAt(startIndex) == '`') {
            int endIndex = text.indexOf('`', startIndex + 1);
            return endIndex != -1;
        }
        return false;
    }

    @Override
    protected ParseResult parseNode(String text, int startIndex) {
        int endIndex = text.indexOf('`', startIndex + 1);
        if (endIndex != -1) {
            String code = text.substring(startIndex + 1, endIndex);
            return new ParseResult(new InlineCode(code), endIndex + 1, true);
        }
        return ParseResult.notParsed();
    }
}
