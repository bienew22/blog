package bienew.blog.backend.markdown.ast.inline;

import bienew.blog.backend.markdown.ast.ASTNode;

/**
 * SubScript 클래스는 마크다운 문서의 아래첨자 텍스트 노드를 나타내는 ASTNode의 하위 클래스입니다.
 * 이 클래스는 아래첨자로 표시될 텍스트 내용을 저장하며, 마크다운 문서 내에서 아래첨자 텍스트 블록을 나타냅니다.
 */
public class SubScript extends ASTNode {

    String text;

    public SubScript(String text) {
        this.text = text;
    }

}
