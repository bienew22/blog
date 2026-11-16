package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.ast.block.ThematicBreak;
import bienew.blog.backend.markdown.parser.ParseResult;

/**
 * ThematicBreakParser 클래스는 마크다운 문서에서 수평선을 파싱하는 역할을 담당하는 블록 파서입니다.
 */
public class ThematicBreakParser extends AbstractBlockParser {
    @Override
    protected boolean canParse(String line) {
        return line != null && line.strip().equals("---");
    }

    @Override
    protected ParseResult<ThematicBreak> parseNode(String[] lines, int startLineIndex) {
        return new ParseResult<>(new ThematicBreak(), startLineIndex + 1, true);
    }
}
