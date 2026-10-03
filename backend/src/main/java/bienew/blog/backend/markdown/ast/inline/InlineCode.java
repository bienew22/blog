package bienew.blog.backend.markdown.ast.inline;

import bienew.blog.backend.markdown.ast.ASTNode;
import lombok.Getter;

/**
 * InlineCode 클래스는 인라인 코드 블록을 나타내는 ASTNode의 하위 클래스입니다.
 * 이 클래스는 마크다운 문서에서 백틱(`)으로 감싸진 코드를 표현하는 데 사용됩니다.
 */
@Getter
public class InlineCode extends ASTNode {

    private final String code;

    public InlineCode(String code) {
        this.code = code;
    }
}
