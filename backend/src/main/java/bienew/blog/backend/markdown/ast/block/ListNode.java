package bienew.blog.backend.markdown.ast.block;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.renderer.ASTVisitor;
import lombok.Getter;

import java.util.List;

/**
 * ListNode 클래스는 마크다운 문서의 리스트 노드를 나타내는 BlockNode.
 * 이 클래스는 마크다운 문서에서 순서 있는 리스트(ordered list) 또는 순서 없는 리스트(unordered list)를 표현하는 데 사용됩니다.
 */
@Getter
public class ListNode extends BlockNode {

    private final boolean ordered;

    public ListNode(boolean ordered, List<? extends BlockNode> children) {
        this.ordered = ordered;
        children.forEach(this::addChild);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
