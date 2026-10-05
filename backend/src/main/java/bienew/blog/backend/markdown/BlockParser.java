package bienew.blog.backend.markdown;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.block.Default;
import bienew.blog.backend.markdown.ast.block.Document;
import bienew.blog.backend.markdown.parser.AbstractBlockParser;
import bienew.blog.backend.markdown.parser.ParseResult;
import bienew.blog.backend.markdown.parser.block.FencedCodeBlockParser;
import bienew.blog.backend.markdown.parser.block.HeadingParser;
import bienew.blog.backend.markdown.parser.block.ListNodeParser;

import java.util.ArrayList;
import java.util.List;

public enum BlockParser {

    INSTANCE;

    private final List<AbstractBlockParser> abstractBlockParsers;

    BlockParser() {
        abstractBlockParsers = new ArrayList<>();

        // #{1, 6} 시작하는 HeadingParser를 추가
        abstractBlockParsers.add(new HeadingParser());

        // ``` 시작하는 FencedCodeBlockParser를 추가
        abstractBlockParsers.add(new FencedCodeBlockParser());

        // - 또는 * 시작하는 ListParser를 추가
        abstractBlockParsers.add(new ListNodeParser());
    }

    public static BlockParser getInstance() {
        return INSTANCE;
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
                ParseResult result = abstractBlockParser.parse(lines, remainingLines);

                if (result.isParsed()) {
                    now.setNext(result.node());
                    now = result.node();
                    remainingLines = result.nextIndex();

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
        processInlineParser(document);
        return document;
    }

    private void processInlineParser(ASTNode document) {

        ASTNode now = document.getNext();

        while (now != null) {
            InlineParser.getInstance().parse(now);
            now = now.getNext();
        }
    }
}
