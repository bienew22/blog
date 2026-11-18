package bienew.blog.backend.markdown.ast.block;

import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.renderer.ASTVisitor;

/**
 * ThematicBreak 클래스는 마크다운 문서의 수평선을 나타내는 BlockNode 입니다.
 */
public class ThematicBreak extends BlockNode {
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
