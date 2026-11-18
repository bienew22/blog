package bienew.blog.backend.markdown.ast.block;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.renderer.ASTVisitor;

import java.util.List;

/**
 * ListItem 클래스는 마크다운 문서의 리스트 항목 노드를 나타내는 BlockNode.
 * 이 클래스는 마크다운 문서에서 리스트 항목을 표현하는 데 사용됩니다
 */
public class ListItem extends BlockNode {

    public ListItem(List<? extends ASTNode> children) {
        children.forEach(this::addChild);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
