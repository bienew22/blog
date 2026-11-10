package bienew.blog.backend.markdown.parser;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.ast.block.Document;
import bienew.blog.backend.markdown.parser.block.*;

import java.util.List;

public enum BlockParser {

    INSTANCE;

    private final List<AbstractBlockParser> parsers;

    BlockParser() {
        parsers = List.of(
                // #{1, 5}로 시작하는 문장을 Parse
                new HeadingParser(),
                // ```로 시작하는 문장을 Parse
                new FencedCodeBlockParser(),
                // -, *, d. 로 시작하는 문장을 Parse
                new ListNodeParser(),
                // 그 외의 문장을 Parse
                new ParagraphParser()
        );
    }

    public ASTNode parse(String[] lines) {

        Document document = new Document();
        int lineIndex = 0;

        while (lineIndex < lines.length) {
            if (lines[lineIndex].isBlank()) {
                lineIndex++;
                continue;
            }

            boolean parsed = false;

            for (AbstractBlockParser parser : parsers) {
                ParseResult<? extends BlockNode> result = parser.parse(lines, lineIndex);

                if (result.isParsed()) {
                    if (result.nextIndex() <= lineIndex) {
                        throw new IllegalStateException("Block parser returned an invalid next index.");
                    }

                    document.addChild(result.node());
                    lineIndex = result.nextIndex();
                    parsed = true;
                    break;
                }
            }

            if (!parsed) {
                throw new IllegalStateException("No block parser accepted line " + lineIndex + ".");
            }
        }

        return document;
    }
}
