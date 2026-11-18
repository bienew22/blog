package bienew.blog.backend.markdown.ast.inline;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.InlineNode;
import bienew.blog.backend.markdown.renderer.ASTVisitor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Bold 클래스는 마크다운 문서의 굵은 텍스트 노드를 나타내는 InlineNode.
 */
public class Bold extends InlineNode {

    public Bold(List<? extends InlineNode> children) {
        children.forEach(this::addChild);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
