package bienew.blog.backend.markdown.ast.block;

import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.ast.InlineNode;

import java.util.List;

/**
 * Paragraph 클래스는 마크다운 문서의 단락 블록을 나타내는 BlockNode 입니다.
 * 단락 내의 인라인 노드들을 자식으로 가질 수 있습니다.
 */
public class Paragraph extends BlockNode {

    public Paragraph(List<? extends InlineNode> children) {
        children.forEach(this::addChild);
    }
}
