package bienew.blog.backend.markdown;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.renderer.HtmlRenderer;
import bienew.blog.backend.markdown.renderer.TreeRenderer;
import bienew.blog.backend.markdown.utils.MarkdownImageExtractor;

import java.util.List;

public class MarkdownDocument {

    private final ASTNode rootNode;

    public MarkdownDocument(ASTNode rootNode) {
        this.rootNode = rootNode;
    }

    public String toTree() {
        return new TreeRenderer().render(rootNode);
    }

    public String toHTML() {
        return new HtmlRenderer().render(rootNode);
    }

    public List<String> getImageUrls() {
        return MarkdownImageExtractor.extractImageUrls(rootNode);
    }
}
