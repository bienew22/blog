package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.ast.block.Default;
import bienew.blog.backend.markdown.ast.block.ListNode;
import bienew.blog.backend.markdown.parser.AbstractBlockParser;
import bienew.blog.backend.markdown.parser.ParseResult;

public class ListNodeParser extends AbstractBlockParser {
    @Override
    protected boolean canParse(String line) {
        return line.startsWith("- ")
                || line.startsWith("* ")
                || line.matches("^\\d+\\.\\s.*");
    }

    @Override
    protected ParseResult parseNode(String[] lines, int startLineIndex) {
        boolean isOrdered = lines[startLineIndex].matches("^\\d+\\.\\s.*");

        int currentIndent = countIndent(lines[startLineIndex]);

        // 부모 리스트
        ListNode parent = new ListNode(isOrdered, currentIndent);

        while (startLineIndex < lines.length) {
            String line = lines[startLineIndex];
            String strippedLine = line.strip();

            if (canParse(strippedLine)) {
                // 현재 줄의 들여쓰기 수준을 계산
                int indent = countIndent(line);

                if (indent > currentIndent) {
                    // 현재 줄이 부모 노드보다 더 깊은 들여쓰기 수준이면, 재귀적으로 하위 노드를 파싱
                    ParseResult childResult = parseNode(lines, startLineIndex);
                    if (childResult.isParsed()) {
                        parent.addChild(childResult.node());
                        startLineIndex = childResult.nextIndex();
                        continue;
                    }
                } else if (indent == currentIndent) {
                    // 현재 줄이 부모 노드와 같은 수준이면, 새로운 ListNode를 생성하고 부모 노드에 추가
                    boolean ordered = strippedLine.matches("^\\d+\\.\\s.*");

                    String content = strippedLine.replaceFirst("^(?:\\d+\\.\\s|[*-]\\s)", "");

                    parent.addChild(new Default(content));
                } else {
                    // 현재 줄이 부모 노드보다 덜 들여쓰기 수준이면, 파싱을 종료
                    break;
                }
            } else {
                break;
            }

            startLineIndex++;
        }

        if (parent.getChildren().size() > 0) {
            return new ParseResult(parent, startLineIndex, true);
        }

        return ParseResult.notParsed();
    }

    private int countIndent(String line) {
        int indent = 0;

        while (indent < line.length() && line.charAt(indent) == '\t') {
            indent++;
        }

        return indent;
    }
}
