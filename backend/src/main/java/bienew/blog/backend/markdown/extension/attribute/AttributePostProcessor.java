package bienew.blog.backend.markdown.extension.attribute;

import bienew.blog.backend.markdown.extension.AstPrinter;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.CustomBlock;
import org.commonmark.node.Node;
import org.commonmark.parser.PostProcessor;

public class AttributePostProcessor implements PostProcessor {

    @Override
    public Node process(Node node) {

        System.out.println("=============before process=============");
        new AstPrinter().print(node);
        System.out.println("========================================");


        node.accept(new AbstractVisitor() {
            @Override
            public void visit(CustomBlock nowBlock) {

                if (nowBlock instanceof AttributeBlock) {
                    // 1. 바로 앞의 형제 노드를 가져옴.
                    Node previous = nowBlock.getPrevious();

                    if (previous != null) {
                        // 2. 이전 노드가 존재하면
                        // 를 이전 노드의 자식으로 이동시킴.
                        previous.appendChild(nowBlock);
                    }
                }

                visitChildren(nowBlock);
            }
        });

        return node;
    }
}
