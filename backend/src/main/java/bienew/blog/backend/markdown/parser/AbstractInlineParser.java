package bienew.blog.backend.markdown.parser;

/**
 * AbstractInlineParser 인터페이스는 마크다운 문서의 인라인 요소를 파싱하는 기능을 정의합니다.
 * 각 inline ast node는 해당 parser을 구현해야함.
 */
public abstract class AbstractInlineParser {

    public final ParseResult parse(String text, int startIndex) {
        if (!canParse(text, startIndex)) {
            return new ParseResult(null, startIndex, false);
        }
        return parseNode(text, startIndex);
    }

    protected abstract boolean canParse(String text, int startIndex);

    protected abstract ParseResult parseNode(String text, int startIndex);
}
