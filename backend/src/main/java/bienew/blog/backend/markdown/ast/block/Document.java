package bienew.blog.backend.markdown.ast.block;

import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.renderer.ASTVisitor;

/**
 * Markdown 문서의 최상위 루트 노드를 나타내는 클래스입니다.
 * 이 노드는 문서 트리의 다른 모든 노드를 포함하는 컨테이너 역할을 합니다.
 */
public class Document extends BlockNode {
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
