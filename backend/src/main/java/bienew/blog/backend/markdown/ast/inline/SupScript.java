package bienew.blog.backend.markdown.ast.inline;

import bienew.blog.backend.markdown.ast.ASTNode;

/**
 * SupScript 클래스는 마크다운 문서의 위첨자 텍스트 노드를 나타내는 ASTNode의 하위 클래스입니다.
 * 이 클래스는 위첨자로 표시될 텍스트 내용을 저장하며, 마크다운 문서 내에서 위첨자 텍스트 블록을 나타냅니다.
 */
public class SupScript extends ASTNode {
    String text;

    public SupScript(String text) {
        this.text = text;
    }
}
