package bienew.blog.backend.markdown.ast.block;


import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.renderer.ASTVisitor;
import lombok.Getter;

/**
 * Image 클래스는 마크다운 문서의 이미지(Image) 블록을 나타내는 BlockNode.
 */
@Getter
public class Image extends BlockNode {

    String url;
    String altText;

    public Image(String altText, String url) {
        this.url = url;
        this.altText = altText;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
