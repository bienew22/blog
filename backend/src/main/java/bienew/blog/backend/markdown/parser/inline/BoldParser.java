package bienew.blog.backend.markdown.parser.inline;

import bienew.blog.backend.markdown.ast.inline.Bold;
import bienew.blog.backend.markdown.parser.AbstractInlineParser;
import bienew.blog.backend.markdown.parser.ParseResult;

public class BoldParser extends AbstractInlineParser {
    @Override
    protected boolean canParse(String text, int startIndex) {
        if (text.startsWith("**", startIndex)) {
            int endIndex = text.indexOf("**", startIndex + 2);
            return endIndex != -1 && endIndex > startIndex + 2;
        }
        return false;
    }

    @Override
    protected ParseResult parseNode(String text, int startIndex) {

        int startBoldIndex = startIndex + 2;
        int endBoldIndex = text.indexOf("**", startBoldIndex);

        System.out.println("BoldParser.parseNode: startIndex=" + startIndex + ", startBoldIndex=" + startBoldIndex + ", endBoldIndex=" + endBoldIndex);
        // parsing
        if (endBoldIndex != -1) {
            String boldText = text.substring(startBoldIndex, endBoldIndex);
            return new ParseResult(new Bold(boldText), endBoldIndex + 2, true);
        }

        return new ParseResult(null, startIndex, false);
    }
}
