package bienew.blog.backend.markdown.parser.inline;

import bienew.blog.backend.markdown.ast.inline.SupScript;
import bienew.blog.backend.markdown.parser.AbstractInlineParser;
import bienew.blog.backend.markdown.parser.ParseResult;

public class SupScriptParser extends AbstractInlineParser {
    @Override
    protected boolean canParse(String text, int startIndex) {
        if (startIndex >= text.length()) {
            return false;
        }

        if (text.startsWith("^{", startIndex)) {
            int endIndex = text.indexOf("}", startIndex + 2);

            return endIndex != -1 && endIndex > startIndex + 2;
        }

        return false;
    }

    @Override
    protected ParseResult parseNode(String text, int startIndex) {
        int startSupScriptIndex = startIndex + 2;
        int endSupScriptIndex = text.indexOf("}", startSupScriptIndex);

        if (endSupScriptIndex != -1) {
            String supScriptText = text.substring(startSupScriptIndex, endSupScriptIndex);
            return new ParseResult(new SupScript(supScriptText), endSupScriptIndex + 1, true);
        }

        return ParseResult.notParsed();
    }
}
