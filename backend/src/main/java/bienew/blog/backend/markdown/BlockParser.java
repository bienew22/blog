package bienew.blog.backend.markdown;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.Default;
import bienew.blog.backend.markdown.ast.Document;
import bienew.blog.backend.markdown.parser.AbstractBlockParser;
import bienew.blog.backend.markdown.parser.HeadingParser;
import bienew.blog.backend.markdown.parser.ParseResult;

import java.util.ArrayList;
import java.util.List;

public class BlockParser {

    private final List<AbstractBlockParser> abstractBlockParsers;

    public BlockParser() {
        abstractBlockParsers = new ArrayList<>();

        abstractBlockParsers.add(new HeadingParser());
    }

    public ASTNode parse(String markdown) {
        // Implement the parsing logic here
        String[] lines = markdown
                .replace("\r\n", "\n")
                .replace("\r", "")
                .split("\n", -1);

        int remainingLines = 0;

        Document document = new Document();

        ASTNode now = document;

        while (remainingLines < lines.length) {
            String line = lines[remainingLines];
            boolean parsed = false;

            for (AbstractBlockParser abstractBlockParser : abstractBlockParsers) {
                if (abstractBlockParser.canParse(line)) {
                    ParseResult result = abstractBlockParser.parse(lines, remainingLines);

                    now.setNext(result.node());
                    now = result.node();
                    remainingLines = result.nextLineIndex();

                    parsed = true;
                    break;
                }
            }

            // 해당하는 파서가 없는 경우 기본 Node으로 처리
            if (!parsed) {
                Default defaultNode = new Default(line);
                now.setNext(defaultNode);
                
                now = defaultNode;
                remainingLines++;
            }
        }

        return document;
    }

}
