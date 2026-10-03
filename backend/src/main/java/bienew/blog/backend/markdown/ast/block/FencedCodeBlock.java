package bienew.blog.backend.markdown.ast.block;

import bienew.blog.backend.markdown.ast.ASTNode;
import lombok.Getter;

/**
 * FencedCodeBlock 클래스는 마크다운 문서의 코드 블록 노드를 나타내는 ASTNode의 하위 클래스입니다.
 * 이 클래스는 코드 블록의 내용을 저장하며, 마크다운 문서 내에서 코드 블록을 나타냅니다.
 */
@Getter
public class FencedCodeBlock extends ASTNode {


    private final String language;
    private final String code;

    public FencedCodeBlock(String language, String code) {
        this.language = language;
        this.code = code;
    }
}
