package bienew.blog.backend.markdown.ast;

import bienew.blog.backend.markdown.renderer.ASTVisitor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * ASTNode는 추상 구문 트리(Abstract Syntax Tree)의 노드를 나타내는 인터페이스입니다.
 */
public interface ASTNode {
     List<? extends ASTNode> getChildren();

     void accept(ASTVisitor visitor);
}
