package bienew.blog.backend.markdown.extension.attribute;


import org.commonmark.node.CustomBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttributeBlock extends CustomBlock {

    private final List<String> classes = new ArrayList<>();

    public void addClass(String className) {
        classes.add(className);
    }

    public List<String> getClasses() {
        return Collections.unmodifiableList(classes);
    }
}