package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.ast.block.ListItem;
import bienew.blog.backend.markdown.parser.BlockParser;
import bienew.blog.backend.markdown.parser.ParseResult;

import java.util.ArrayList;
import java.util.List;

public class ListItemParser extends AbstractBlockParser {
    @Override
    protected boolean canParse(String line) {
        //return line.startsWith("<LIST>") || line.startsWith("<ITEM>");
        // 외부(ListNodeParser 제외)에서 ListItemParser를 직접 호출하지 않도록 수정
        return false;
    }

    @Override
    protected ParseResult<? extends BlockNode> parseNode(String[] lines, int startLineIndex) {
        String content = lines[startLineIndex].substring("<ITEM>".length()).strip();

        int endLineIndex = startLineIndex + 1;

        List<String> contents = new ArrayList<>();
        contents.add(content);

        while (endLineIndex < lines.length) {

            if (lines[endLineIndex].startsWith("<LIST>")) {
                String listContent = lines[endLineIndex].substring("<LIST>".length());
                contents.add(listContent);
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
