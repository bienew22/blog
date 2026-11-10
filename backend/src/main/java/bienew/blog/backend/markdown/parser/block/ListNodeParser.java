package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.ast.block.ListNode;
import bienew.blog.backend.markdown.parser.BlockParser;
import bienew.blog.backend.markdown.parser.ParseResult;

import java.util.ArrayList;

public class ListNodeParser extends AbstractBlockParser {
    @Override
    protected boolean canParse(String line) {
        return line.startsWith("- ")
                || line.startsWith("* ")
                || line.matches("^\\d+\\.\\s.*");
    }

    @Override
    protected ParseResult<? extends BlockNode> parseNode(String[] lines, int startLineIndex) {
        boolean isOrdered = lines[startLineIndex].matches("^(\\t)*\\d+\\.\\s.*");

        java.util.List<String> listLines = new ArrayList<>();
        listLines.add(lines[startLineIndex++]);

        // 1. 현재 줄이 리스트 항목인지 확인하고, 리스트 항목이면 listLines에 추가
        while (startLineIndex < lines.length) {
            String line = lines[startLineIndex];
            String strippedLine = line.strip();

            if (canParse(strippedLine)) {
                listLines.add(line);
                startLineIndex++;
            } else {
                break;
            }
        }

        // 2. ListItem인 경우 ~ 으로 변경하고, 중첩 리스트인 경우 indent를 한개 제거
        java.util.List<String> convertedLines = new ArrayList<>();

        for (String line: listLines) {
            String convertedLine = line.replaceFirst("^(?:\\d+\\.\\s|[*-]\\s)", "<ITEM>")
                    .replaceFirst("^(\\t| {4})", "<LIST>"); // 중첩 리스트의 경우 indent 제거
            convertedLines.add(convertedLine);
        }

        ASTNode document = BlockParser.INSTANCE.parse(convertedLines.toArray(String[]::new));

       return new ParseResult<>(
               new ListNode(isOrdered, document.getChildren()),
               startLineIndex, true);
    }
}
