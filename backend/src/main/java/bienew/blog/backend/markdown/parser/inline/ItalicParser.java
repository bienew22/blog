package bienew.blog.backend.markdown.parser.inline;

import bienew.blog.backend.markdown.ast.inline.Italic;
import bienew.blog.backend.markdown.parser.AbstractInlineParser;
import bienew.blog.backend.markdown.parser.ParseResult;

public class ItalicParser extends AbstractInlineParser {

    @Override
    protected boolean canParse(String text, int startIndex) {
        if (startIndex >= text.length()) {
            return false;
        }

        if (text.charAt(startIndex) == '_') {
            int endIndex = text.indexOf('_', startIndex + 1);

            // 최소 한 글자 이상이 있어야 기울임 텍스트로 인식
            return endIndex != -1 && endIndex > startIndex + 1;
        }

        return false;
    }

    @Override
    protected ParseResult parseNode(String text, int startIndex) {

        int startItalicIndex = startIndex + 1;
        char delimiter = text.charAt(startIndex);
        int endItalicIndex = text.indexOf(delimiter, startItalicIndex);

        if (endItalicIndex != -1) {
            String italicText = text.substring(startItalicIndex, endItalicIndex);
            return new ParseResult(new Italic(italicText), endItalicIndex + 1, true);
        }

        return new ParseResult(null, startIndex, false);
    }
}
