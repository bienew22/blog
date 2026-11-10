package bienew.blog.backend.markdown.ast.inline;

import bienew.blog.backend.markdown.ast.InlineNode;
import bienew.blog.backend.markdown.renderer.ASTVisitor;

import java.util.List;

/**
 * UnderLine 클래스는 마크다운 문서의 밑줄 텍스트 노드를 나타내는 InlineNode.
 * 이 클래스는 밑줄로 표시될 텍스트 내용을 저장하며, 마크다운 문서 내에서 밑줄 텍스트 블록을 나타냅니다.
 */
public class UnderLine extends InlineNode {

    public UnderLine(List<? extends InlineNode> children) {
        children.forEach(this::addChild);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
