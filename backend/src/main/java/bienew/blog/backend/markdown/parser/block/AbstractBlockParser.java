package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.parser.ParseResult;

/**
 * AbstractBlockParser 인터페이스는 마크다운 문서의 블록 요소를 파싱하는 기능을 정의합니다.
 * 각 block ast node는 해당 parser을 구현해야함.
 */
public abstract class AbstractBlockParser {

    public final ParseResult<? extends BlockNode> parse(String[] lines, int startLineIndex) {
        if (!canParse(lines[startLineIndex])) {
            return ParseResult.blockNotParsed();
        }
        return parseNode(lines, startLineIndex);

    }

    protected abstract boolean canParse(String line);

    protected abstract ParseResult<? extends BlockNode> parseNode(String[] lines, int startLineIndex);
}
