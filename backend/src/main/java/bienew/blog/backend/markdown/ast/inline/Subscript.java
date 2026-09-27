package bienew.blog.backend.markdown.ast.inline;

import bienew.blog.backend.markdown.ast.ASTNode;

public class Subscript extends ASTNode {

    String text;

    public Subscript(String text) {
        this.text = text;
    }

}
