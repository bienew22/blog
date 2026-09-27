package bienew.blog.backend.markdown.ast.inline;

import bienew.blog.backend.markdown.ast.ASTNode;

/**
 * Italic 클래스는 마크다운 문서의 기울임꼴 텍스트 노드를 나타내는 ASTNode의 하위 클래스입니다.
 * 이 클래스는 기울임꼴로 표시될 텍스트 내용을 저장하며, 마크다운 문서 내에서 기울임꼴 텍스트 블록을 나타냅니다.
 */
public class Italic extends ASTNode {

    public Italic(String source) {
        this.source = source;
    }
}
