package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.ast.block.ListNode;
import bienew.blog.backend.markdown.parser.BlockParser;
import bienew.blog.backend.markdown.parser.ParseResult;

import java.util.ArrayList;
import java.util.List;

public class ListNodeParser extends AbstractBlockParser {

    private static final int MAX_NESTING_DEPTH = 5;
    private final ListItemParser listItemParser = new ListItemParser();


    @Override
    protected boolean canParse(String line) {
        return line.startsWith("- ")
                || line.startsWith("* ")
                || line.matches("^\\d+\\.\\s.*");
    }

    @Override
    protected ParseResult<? extends BlockNode> parseNode(String[] lines, int startLineIndex) {
        boolean isOrdered = lines[startLineIndex].matches("^(\\t)*\\d+\\.\\s.*");

        List<String> listLines = new ArrayList<>();
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

        // 2. 데이터 정리
        List<String> convertedLines = new ArrayList<>();

        for (String line: listLines) {
            // 구조 평탄화 : 최대 5단계까지 중첩 리스트를 허용하고, 그 이상은 상위 리스트로 병합
            int indentCount = 0;
            int indentLength = 0;
            while (true) {
                if (line.startsWith("\t", indentLength)) {
                    indentLength += 1;
                } else if (line.startsWith("    ", indentLength)) {
                    indentLength += 4;
                } else {
                    break;
                }
                indentCount++;
            }

            if (indentCount > MAX_NESTING_DEPTH) {
                line = "\t".repeat(MAX_NESTING_DEPTH) + line.substring(indentLength);
            }

            // 리스트 항목이면 리스트 표시 제거 후 <ITEM> prefix을 추가
            // 중첩 리스트인 경우 indent 한개 제거 후 <LIST> prefix을 추가
            String convertedLine = line.replaceFirst("^(?:\\d+\\.\\s|[*-]\\s)", "<ITEM>")
                    .replaceFirst("^(\\t| {4})", "<LIST>"); // 중첩 리스트의 경우 indent 제거
            convertedLines.add(convertedLine);
        }

        // 3. ListItemParser를 사용하여 각 항목을 파싱
        List<BlockNode> result = new ArrayList<>();

        String[] data = convertedLines.toArray(String[]::new);
        int dataIndex = 0;
        while (dataIndex < data.length) {
            ParseResult<? extends BlockNode> res = listItemParser.parseNode(data, dataIndex);

            if (!res.isParsed()) {
                throw new IllegalStateException("ListItemParser failed to parse line " + dataIndex + ".");
            }

            result.add(res.node());
            dataIndex = res.nextIndex();
        }

       return new ParseResult<>(
               new ListNode(isOrdered, result),
               startLineIndex, true);
    }
}
