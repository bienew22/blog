package bienew.blog.backend.markdown.ast.inline;

import bienew.blog.backend.markdown.ast.InlineNode;
import bienew.blog.backend.markdown.renderer.ASTVisitor;
import lombok.Getter;

/**
 * Text 클래스는 마크다운 문서의 텍스트 노드를 나타내는 ASTNode의 하위 클래스입니다.
 * 이 클래스는 인라인 텍스트 내용을 저장합니다.
 */
@Getter
public class Text extends InlineNode {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
