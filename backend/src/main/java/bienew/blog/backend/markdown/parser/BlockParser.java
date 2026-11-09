package bienew.blog.backend.markdown.parser;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.ast.block.Document;
import bienew.blog.backend.markdown.parser.block.HeadingParser;
import bienew.blog.backend.markdown.parser.block.ParagraphParser;

import java.util.ArrayList;
import java.util.List;

public enum BlockParser {

    INSTANCE;

    private final List<AbstractBlockParser> abstractBlockParsers;

    BlockParser() {
        abstractBlockParsers = new ArrayList<>();

        // #{1, 5}로 시작하는 문장을 Parse
        abstractBlockParsers.add(new HeadingParser());

        // 그 외의 문장을 Parse
        abstractBlockParsers.add(new ParagraphParser());
    }

    public ASTNode parse(String markdown) {
        String[] lines = markdown
                .replace("\r\n", "\n")
                .replace("\r", "")
                .split("\n", -1);

        Document document = new Document();
        int lineIndex = 0;

        while (lineIndex < lines.length) {
            if (lines[lineIndex].isBlank()) {
                lineIndex++;
                continue;
            }

            boolean parsed = false;

            for (AbstractBlockParser abstractBlockParser : abstractBlockParsers) {
                ParseResult<? extends BlockNode> result = abstractBlockParser.parse(lines, lineIndex);

                if (result.isParsed()) {
                    if (result.nextIndex() <= lineIndex || result.nextIndex() > lines.length) {
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
