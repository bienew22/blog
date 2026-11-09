package bienew.blog.backend.markdown.parser;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.ast.InlineNode;

/**
 * ParseResult 클래스는 마크다운 문서의 특정 블록을 파싱한 결과를 나타냅니다.
 * 이 클래스는 파싱된 ASTNode와 다음에 파싱할 인덱스를 포함합니다.
 */
public record ParseResult<T extends ASTNode>(
        T node,
        int nextIndex,
        boolean isParsed
) {
    private static final ParseResult<BlockNode> BLOCK_NOT_PARSED =
            new ParseResult<>(null, -1, false);

    private static final ParseResult<InlineNode> INLINE_NOT_PARSED =
            new ParseResult<>(null, -1, false);

    public static ParseResult<BlockNode> blockNotParsed() {
        return BLOCK_NOT_PARSED;
    }

    public static ParseResult<InlineNode> inlineNotParsed() {
        return INLINE_NOT_PARSED;
    }
}
