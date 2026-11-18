package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.ast.block.Paragraph;
import bienew.blog.backend.markdown.ast.block.TableRow;
import bienew.blog.backend.markdown.parser.InlineParser;
import bienew.blog.backend.markdown.parser.ParseResult;

import java.util.ArrayList;
import java.util.List;

public class TableRowParser extends AbstractBlockParser {

    @Override
    protected boolean canParse(String line) {
        return false;
    }

    @Override
    protected ParseResult<TableRow> parseNode(String[] lines, int startLineIndex) {

        String[] tableColumns = lines[startLineIndex].split("\\|");

        List<TableRow.TableItem> blockNodes = new ArrayList<>();

        for (String column : tableColumns) {
            String trimmedColumn = column.trim();
            if (!trimmedColumn.isEmpty()) {
                blockNodes.add(new TableRow.TableItem(InlineParser.INSTANCE.parse(trimmedColumn)));
            }
        }

        return new ParseResult<>(
                new TableRow(blockNodes),
                startLineIndex + 1, true
        );
    }
}
