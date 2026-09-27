package bienew.blog.backend.markdown.parser;

/**
 * AbstractBlockParser 인터페이스는 마크다운 문서의 블록 요소를 파싱하는 기능을 정의합니다.
 * 각 block ast node는 해당 parser을 구현해야함.
 */
public abstract class AbstractBlockParser {

    public final ParseResult parse(String[] lines, int startLineIndex) {
        if (!canParse(lines[startLineIndex])) {
            return new ParseResult(null, startLineIndex, false);
        }
        return parseNode(lines, startLineIndex);

    }

    protected abstract boolean canParse(String line);

    protected abstract ParseResult parseNode(String[] lines, int startLineIndex);
}
