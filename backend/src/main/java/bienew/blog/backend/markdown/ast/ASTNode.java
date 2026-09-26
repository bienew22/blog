package bienew.blog.backend.markdown.ast;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * ASTNode는 추상 구문 트리(Abstract Syntax Tree)의 노드를 나타내는 추상 클래스입니다.
 * 이 클래스는 트리 구조를 형성하기 위해 부모 노드, 자식 노드, 이전 노드 및 다음 노드에 대한 참조를 관리합니다.
 */
@Getter
@Setter
public abstract class ASTNode {
    protected ASTNode parent;
    protected List<ASTNode> children;
    protected ASTNode prev;
    protected ASTNode next;

    public void addChild(ASTNode child) {
        if (children == null) {
            children = new ArrayList<>();
        }

        if (!children.isEmpty()) {
            ASTNode lastChild = children.get(children.size() - 1);
            lastChild.setNext(child);
            child.setPrev(lastChild);
        }

        children.add(child);
        child.setParent(this);
    }

    public void setNext(ASTNode next) {
        this.next = next;
        next.setPrev(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());

        if (this.getClass().getDeclaredFields().length > 0) {
            sb.append(": ");
            for (var field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    sb.append(field.getName()).append("=").append(field.get(this)).append(" ");
                } catch (IllegalAccessException e) {
                    sb.append("access denied ");
                }
            }
        }

        if (children != null && children.size() > 0) {
            for (ASTNode child : children) {
                sb.append("\n\t").append(child.toString().replace("\n", "\n\t"));
            }
        }
        return sb.toString();
    }
}
