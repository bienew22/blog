package bienew.blog.backend.markdown.renderer;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.block.Document;

public class HtmlRenderer implements ASTRenderer {

    @Override
    public String render(ASTNode node) {
        if (node == null) {
            return "";
        }
        return "hello";
    }

    private String renderChildren(ASTNode node) {
        StringBuilder sb = new StringBuilder();
        for (ASTNode child : node.getChildren()) {
            sb.append(render(child));
        }
        return sb.toString();
    }
}
