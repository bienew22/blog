package bienew.blog.backend.markdown.ast.block;

import bienew.blog.backend.markdown.ast.ASTNode;

import java.util.stream.Collectors;

/**
 * Paragraph 클래스는 마크다운 문서의 단락 블록을 나타내는 ASTNode의 하위 클래스입니다.
 * 이 클래스는 특별한 기능을 가지지 않으며, 단락 블록을 나타내는 데 사용됩니다.
 * blockParser에서 처리되지 않은 블록을 나타낼 때 사용될 수 있습니다.
 */
public class Paragraph extends ASTNode {

    public Paragraph(String source) {
        this.source = source;
    }

    @Override
    public String toHtml() {

        if (children.isEmpty()) {
            return "";
        }

        return "<p>"
                + children.stream()
                    .map(ASTNode::toHtml)
                    .collect(Collectors.joining())
                + "</p>";
    }
}
