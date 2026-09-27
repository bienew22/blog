package bienew.blog.backend.markdown.ast.inline;

import bienew.blog.backend.markdown.ast.ASTNode;

public class SupScript extends ASTNode {
    String text;

    public SupScript(String text) {
        this.text = text;
    }
}
