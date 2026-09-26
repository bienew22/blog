package bienew.blog.backend.markdown.ast;

public class Heading extends ASTNode {
    int level;
    String text;

    public Heading(int level, String text) {
        this.level = level;
        this.text = text;
    }

    public int getLevel() {
        return level;
    }

    public String getText() {
        return text;
    }
}
