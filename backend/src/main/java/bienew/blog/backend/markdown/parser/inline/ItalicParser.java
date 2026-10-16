package bienew.blog.backend.markdown.parser.inline;

import bienew.blog.backend.markdown.ast.inline.Italic;
import bienew.blog.backend.markdown.parser.AbstractInlineParser;
import bienew.blog.backend.markdown.parser.ParseResult;

public class ItalicParser extends AbstractInlineParser {

    @Override
    protected boolean canParse(String text, int startIndex) {
        if (startIndex >= text.length() || text.charAt(startIndex) != '_') {
            return false;
        }

        // 앞 문자가 문자, 또는 숫자라면 기울임 텍스트로 인식하지 않음
        if (startIndex > 0 && Character.isLetterOrDigit(text.charAt(startIndex - 1))) {
            return false;
        }

        int endIndex = text.indexOf('_', startIndex + 1);

        // 최소 한 글자 이상이 있어야 기울임 텍스트로 인식
        if (endIndex == -1 || endIndex == startIndex + 1) {
            return false;
        }

        // 뒤 문자가 문자, 또는 숫자라면 기울임 텍스트로 인식하지 않음
        return endIndex + 1 >= text.length() || !Character.isLetterOrDigit(text.charAt(endIndex + 1));
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

        return ParseResult.notParsed();
    }
}
