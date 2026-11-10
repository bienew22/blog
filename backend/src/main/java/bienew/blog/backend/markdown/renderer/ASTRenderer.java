package bienew.blog.backend.markdown.renderer;

import bienew.blog.backend.markdown.ast.ASTNode;

public interface ASTRenderer {
    String render(ASTNode node);
}
