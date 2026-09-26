package bienew.blog.backend.markdown.ast;

/**
 * Default 클래스는 마크다운 문서의 기본 블록을 나타내는 ASTNode의 하위 클래스입니다.
 * 이 클래스는 특별한 기능을 가지지 않으며, 기본 블록을 나타내는 데 사용됩니다.
 * blockParser에서 처리되지 않은 블록을 나타낼 때 사용될 수 있습니다.
 */
public class Default extends ASTNode {
    String content;

    public Default(String content) {
        this.content = content;
    }
}
