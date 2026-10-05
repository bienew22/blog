package bienew.blog.backend.markdown.ast.block;

import bienew.blog.backend.markdown.ast.ASTNode;
import lombok.Getter;

/**
 * ListNode 클래스는 마크다운 문서의 리스트 노드를 나타내는 ASTNode의 하위 클래스입니다.
 * 이 클래스는 마크다운 문서에서 순서 있는 리스트(ordered list) 또는 순서 없는 리스트(unordered list)를 표현하는 데 사용됩니다.
 */
@Getter
public class ListNode extends ASTNode {

    private final boolean ordered;

    private int indent = 0;

    public ListNode(boolean ordered, int indent) {
        this.ordered = ordered;
        this.indent = indent;
    }
}
