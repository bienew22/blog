package bienew.blog.backend.markdown.ast.inline;

import bienew.blog.backend.markdown.ast.ASTNode;

public class SubScript extends ASTNode {

    String text;

    public SubScript(String text) {
        this.text = text;
    }

}
