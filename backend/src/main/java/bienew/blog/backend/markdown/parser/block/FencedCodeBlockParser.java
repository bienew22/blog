package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.ast.block.FencedCodeBlock;
import bienew.blog.backend.markdown.parser.AbstractBlockParser;
import bienew.blog.backend.markdown.parser.ParseResult;

public class FencedCodeBlockParser extends AbstractBlockParser {
    @Override
    public boolean canParse(String line) {
        return line.startsWith("```");
    }

    @Override
    protected ParseResult parseNode(String[] lines, int startLineIndex) {

        int endLineIndex = startLineIndex + 1;
        while (endLineIndex < lines.length && !"```".equals(lines[endLineIndex])) {
            endLineIndex++;
        }

        if (endLineIndex < lines.length) {
            StringBuilder codeContent = new StringBuilder();
            for (int i = startLineIndex + 1; i < endLineIndex; i++) {
                codeContent.append(lines[i]);

                if (i < endLineIndex - 1) {
                    codeContent.append("\n");
                }
            }

            return new ParseResult(new FencedCodeBlock(lines[startLineIndex].substring(3), codeContent.toString()), endLineIndex + 1, true);
        }

        return ParseResult.notParsed();
    }
}
