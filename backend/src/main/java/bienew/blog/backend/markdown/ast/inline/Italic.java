package bienew.blog.backend.markdown.ast.inline;

import bienew.blog.backend.markdown.ast.ASTNode;

public class Italic extends ASTNode {
    String text;

    public Italic(String text) {
        this.text = text;
    }
}
