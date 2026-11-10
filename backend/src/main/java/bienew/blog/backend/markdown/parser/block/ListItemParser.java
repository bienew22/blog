package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.ast.block.ListItem;
import bienew.blog.backend.markdown.parser.BlockParser;
import bienew.blog.backend.markdown.parser.ParseResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListItemParser extends AbstractBlockParser {
    @Override
    protected boolean canParse(String line) {
        return line.startsWith("LIST_ITEM");
    }

    @Override
    protected ParseResult<? extends BlockNode> parseNode(String[] lines, int startLineIndex) {
        String content = lines[startLineIndex].substring("LIST_ITEM".length()).strip();

        int endLineIndex = startLineIndex + 1;

        List<String> contents = new ArrayList<>();
        contents.add(content);

        ListNodeParser listNodeParser = new ListNodeParser();
        while (endLineIndex < lines.length) {
            String stripLine = lines[endLineIndex].replace("LIST_ITEM", "").strip();

            if (listNodeParser.canParse(stripLine)) {
                contents.add(stripLine);
                endLineIndex++;
            } else {
                break;
            }
        }

        ASTNode result = BlockParser.INSTANCE.parse(contents.toArray(String[]::new));

        return new ParseResult<>(
                new ListItem(result.getChildren()),
                endLineIndex, true);
    }
}
