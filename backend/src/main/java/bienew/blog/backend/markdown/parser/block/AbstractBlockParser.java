package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.parser.ParseResult;

/**
 * AbstractBlockParser 인터페이스는 마크다운 문서의 블록 요소를 파싱하는 기능을 정의합니다.
 * 각 block ast node는 해당 parser을 구현해야함.
 */
public abstract class AbstractBlockParser {

    /**
     * (외부용) 마크다운 문서의 특정 라인이 해당 블록 요소를 파싱하는지 확인 후 가능하면 파싱
     * @param lines 마크다운 문서의 라인 배열
     * @param startLineIndex 파싱을 시작할 라인 인덱스
     * @return 파싱 결과
     */
    public final ParseResult<? extends BlockNode> parse(String[] lines, int startLineIndex) {
        if (!canParse(lines[startLineIndex])) {
            return ParseResult.blockNotParsed();
        }
        return parseNode(lines, startLineIndex);

    }

    protected abstract boolean canParse(String line);

    /**
     * (내부용) 마크다운 문서의 특정 라인에서 블록 요소를 파싱하는 기능을 구현해야함.
     * @param lines 마크다운 문서의 라인 배열
     * @param startLineIndex 파싱을 시작할 라인 인덱스
     * @return 파싱 결과
     */
    protected abstract ParseResult<? extends BlockNode> parseNode(String[] lines, int startLineIndex);
}
