package bienew.blog.backend.markdown.ast;

import java.util.*;

public abstract class CompositeNode<T extends ASTNode> implements ASTNode {
    protected final List<T> children = new ArrayList<>();

    public void addChild(T child) {
        children.add(child);
    }

    @Override
    public List<T> getChildren() {
        return Collections.unmodifiableList(children);
    }
}
