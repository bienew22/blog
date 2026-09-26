package bienew.blog.backend.markdown.extension;

import org.commonmark.node.*;

public class AstPrinter extends AbstractVisitor {
    public void print(Node node) {
        print(node, 0);
    }

    private void print(Node node, int indentLevel) {
        if (node == null) return;

        // 1. 현재 노드 들여쓰기 및 설명 출력
        System.out.println("  ".repeat(indentLevel) + getNodeDescription(node));

        // 2. 자식 노드 순회 (들여쓰기 +1 증가)
        Node child = node.getFirstChild();
        while (child != null) {
            print(child, indentLevel + 1);
            child = child.getNext();
        }
    }

    // JDK 17 정식 스펙: Pattern Matching for instanceof 활용
    private String getNodeDescription(Node node) {
        String className = node.getClass().getSimpleName();
        String detail = "";

        if (node instanceof Text n) {
            detail = "literal=\"" + escape(n.getLiteral()) + "\"";
        } else if (node instanceof Code n) {
            detail = "literal=\"" + escape(n.getLiteral()) + "\"";
        } else if (node instanceof FencedCodeBlock n) {
            detail = "info=\"" + n.getInfo() + "\", literal=\"" + escape(n.getLiteral().trim()) + "\"";
        } else if (node instanceof Heading n) {
            detail = "level=" + n.getLevel();
        } else if (node instanceof Link n) {
            detail = "destination=\"" + n.getDestination() + "\", title=\"" + n.getTitle() + "\"";
        } else if (node instanceof Image n) {
            detail = "destination=\"" + n.getDestination() + "\", title=\"" + n.getTitle() + "\"";
        } else if (node instanceof OrderedList n) {
            detail = "start=" + n.getStartNumber() + ", delimiter='" + n.getDelimiter() + "'";
        } else if (node instanceof BulletList n) {
            detail = "bulletMarker='" + n.getBulletMarker() + "'";
        } else if (node instanceof HtmlBlock n) {
            detail = "literal=\"" + escape(n.getLiteral().trim()) + "\"";
        } else if (node instanceof HtmlInline n) {
            detail = "literal=\"" + escape(n.getLiteral()) + "\"";
        } else if (node instanceof CustomBlock n) {
            detail = "customClass=" + n.getClass().getName();
        } else if (node instanceof CustomNode n) {
            detail = "customClass=" + n.getClass().getName();
        }

        return detail.isEmpty() ? className : className + " [" + detail + "]";
    }

    private String escape(String text) {
        if (text == null) return "";
        return text.replace("\n", "\\n").replace("\r", "\\r");
    }
}
