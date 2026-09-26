package bienew.blog.backend.markdown;

import bienew.blog.backend.markdown.extension.AstPrinter;
import bienew.blog.backend.markdown.extension.AttributeExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * convert markdown to html
 * based on commonmark-java
 *
 *
 */
@Component
public class MarkDownParser {

    private final Parser parser;
    private final HtmlRenderer renderer;

    public MarkDownParser() {
        var extensions = List.of(
                AttributeExtension.create()
        );

        this.parser = Parser.builder().extensions(extensions).build();

        this.renderer = HtmlRenderer.builder()
                .extensions(extensions)
                .escapeHtml(true)
                .build();
    }

    public String toHtml(String markdown) {
        Node document = parser.parse(markdown);

        System.out.println("=============after parse=============");
        new AstPrinter().print(document);
        System.out.println("=====================================");

        System.out.println("=============after render=============");
        System.out.println(renderer.render(document));
        System.out.println("======================================");

        return renderer.render(document);
    }
}
