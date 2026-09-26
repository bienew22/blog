package bienew.blog.backend.markdown.parser;

import bienew.blog.backend.markdown.ast.ASTNode;

/**
 * ParseResult 클래스는 마크다운 문서의 특정 블록을 파싱한 결과를 나타냅니다.
 * 이 클래스는 파싱된 ASTNode와 다음에 파싱할 라인의 인덱스를 포함합니다.
 */
public record ParseResult(
        ASTNode node,
        int nextLineIndex
) {
}
