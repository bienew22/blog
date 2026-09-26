package bienew.blog.backend.markdown.ast;

/**
 * Text 클래스는 마크다운 문서의 텍스트 노드를 나타내는 ASTNode의 하위 클래스입니다.
 * 이 클래스는 단순히 텍스트 내용을 저장하며, 마크다운 문서 내에서 텍스트 블록을 나타냅니다.
 */
public class Text extends ASTNode {
    String text;

    public Text(String text) {
        this.text = text;
    }
}
