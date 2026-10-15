package bienew.blog.backend.markdown.parser.inline;

import bienew.blog.backend.markdown.ast.inline.Link;
import bienew.blog.backend.markdown.parser.AbstractInlineParser;
import bienew.blog.backend.markdown.parser.ParseResult;

public class LinkParser  extends AbstractInlineParser {

    @Override
    protected boolean canParse(String text, int startIndex) {
        if (startIndex >= text.length()) {
            return false;
        }

        if (text.charAt(startIndex) == '[') {

            int endIndex = text.indexOf(']', startIndex);
            if (endIndex == -1 || endIndex + 1 >= text.length() || text.charAt(endIndex + 1) != '(') {
                return false;
            }

            int urlEndIndex = text.indexOf(')', endIndex + 2);

            // 최소 한 글자 이상이 있어야 링크로 인식
            return urlEndIndex != -1 && urlEndIndex > endIndex + 2;
        }


        return false;
    }

    @Override
    protected ParseResult parseNode(String text, int startIndex) {

        int startTextIndex = startIndex + 1;
        int endTextIndex = text.indexOf(']', startIndex);

        if (endTextIndex != -1 && endTextIndex + 1 < text.length() && text.charAt(endTextIndex + 1) == '(') {
            int startUrlIndex = endTextIndex + 2;
            int endUrlIndex = text.indexOf(')', startUrlIndex);

            if (endUrlIndex != -1) {
                String linkText = text.substring(startTextIndex, endTextIndex);
                String linkUrl = text.substring(startUrlIndex, endUrlIndex);
                return new ParseResult(new Link(linkText, linkUrl), endUrlIndex + 1, true);
            }
        }


        return ParseResult.notParsed();
    }
}