package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.ast.block.Paragraph;
import bienew.blog.backend.markdown.parser.AbstractBlockParser;
import bienew.blog.backend.markdown.parser.InlineParser;
import bienew.blog.backend.markdown.parser.ParseResult;

public class ParagraphParser extends AbstractBlockParser {

    @Override
    protected boolean canParse(String line) {
        return !line.isBlank();
    }

    @Override
    protected ParseResult<Paragraph> parseNode(String[] lines, int startLineIndex) {
        String source = lines[startLineIndex];

        Paragraph paragraph = new Paragraph(InlineParser.INSTANCE.parse(source));
        return new ParseResult<>(paragraph, startLineIndex + 1, true);
    }
}
