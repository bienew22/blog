package bienew.blog.backend.markdown.parser;

/**
 * AbstractBlockParser 인터페이스는 마크다운 문서의 블록 요소를 파싱하는 기능을 정의합니다.
 * 각 구현체는 특정 블록 유형(예: 제목, 목록, 코드 블록 등)을 처리할 수 있습니다.
 */
public interface AbstractBlockParser {

    boolean canParse(String line);

    ParseResult parse(String[] lines, int startLineIndex);
}
