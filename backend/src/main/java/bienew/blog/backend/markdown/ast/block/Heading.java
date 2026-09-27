package bienew.blog.backend.markdown.ast.block;

import bienew.blog.backend.markdown.ast.ASTNode;
import lombok.Getter;

/**
 * Heading 클래스는 마크다운 문서의 제목(Heading) 블록을 나타내는 ASTNode의 하위 클래스입니다.
 * 이 클래스는 제목의 레벨을 저장하며, 마크다운 문서 내에서 제목 블록을 나타냅니다.
 */
@Getter
public class Heading extends ASTNode {
    int level;

    public Heading(int level) {
        this.level = level;
    }

}
