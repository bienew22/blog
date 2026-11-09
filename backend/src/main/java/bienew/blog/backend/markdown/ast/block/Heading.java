package bienew.blog.backend.markdown.ast.block;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.BlockNode;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Heading 클래스는 마크다운 문서의 제목(Heading) 블록을 나타내는 BlockNode.
 * 이 클래스는 제목의 레벨을 저장하며, 마크다운 문서 내에서 제목 블록을 나타냅니다.
 */
@Getter
public class Heading extends BlockNode {
    int level;

    public Heading(int level, List<? extends ASTNode> children) {
        this.level = level;

        children.forEach(this::addChild);
    }
}
