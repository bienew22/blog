package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.ast.block.Table;
import bienew.blog.backend.markdown.ast.block.TableRow;
import bienew.blog.backend.markdown.parser.ParseResult;

import java.util.ArrayList;
import java.util.List;

public class TableParser extends AbstractBlockParser {

    private final TableRowParser tableRowParser = new TableRowParser();

    @Override
    protected boolean canParse(String line) {
        if  (line == null || line.isEmpty()) {
            return false;
        }

        if (!line.startsWith("|") || !line.endsWith("|") || line.length() <= 2) {
            return false;
        }

        String cells = line.substring(1, line.length() - 1).replace("|", "");
        return !cells.isBlank();
    }

    @Override
    protected ParseResult<? extends BlockNode> parseNode(String[] lines, int startLineIndex) {

        int endLineIndex = startLineIndex + 1;

        // 1. 헤더 라인 찾기
        String headerLine = lines[startLineIndex];

        // 2. 구분자 라인 파싱.
        List<String> delimiters = new ArrayList<>();
        if (endLineIndex < lines.length && canParse(lines[endLineIndex]) && isDelimiterLine(lines[endLineIndex])) {
            String[] delimiterCells = lines[endLineIndex].split("\\|");
            for (String cell : delimiterCells) {
                String trimmedCell = cell.trim();
                if (!trimmedCell.isEmpty()) {
                    switch (trimmedCell) {
                        case "---", ":---:" -> delimiters.add("center");
                        case "---:" -> delimiters.add("right");
                        case ":---" -> delimiters.add("left");
                        default -> {
                            return ParseResult.blockNotParsed();
                        }
                    }
                }
            }
            endLineIndex++;
        } else {
            return ParseResult.blockNotParsed();
        }

        // 3. 테이블 항목 라인 파싱.
        List<String> itemLines = new ArrayList<>();
        while (endLineIndex < lines.length && canParse(lines[endLineIndex])) {
            itemLines.add(lines[endLineIndex]);
            endLineIndex++;
        }

        // 4. 테이블 헤드 노드 생성
        ParseResult<TableRow> headParseResult = tableRowParser.parseNode(new String[]{headerLine}, 0);

        if (!headParseResult.isParsed()) {
            return ParseResult.blockNotParsed();
        }

        // 5. 테이블 바디 노드 생성
        String[] itemLinesArray = itemLines.toArray(String[]::new);

        List<TableRow> blockNodes = new ArrayList<>();
        int dataIndex = 0;

        while (dataIndex < itemLinesArray.length) {
            ParseResult<TableRow> res = tableRowParser.parseNode(itemLinesArray, dataIndex);

            if (!res.isParsed()) {
                return ParseResult.blockNotParsed();
            }

            blockNodes.add(res.node());
            dataIndex = res.nextIndex();
        }

        return new ParseResult<>(
                new Table(delimiters,
                        headParseResult.node(),
                        blockNodes),
                endLineIndex, true);
    }

    private boolean isDelimiterLine(String line) {
        if (line == null || line.isEmpty()) {
            return false;
        }

        String[] cells = line.split("\\|");
        if (cells.length < 2) {
            return false;
        }

        for (String cell : cells) {
            String trimmedCell = cell.trim();
            if (trimmedCell.isEmpty()) {
                continue;
            }
            if (!trimmedCell.matches("^:?---:?$")) {
                return false;
            }
        }

        return true;
    }
}
