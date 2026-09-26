package bienew.blog.backend.markdown;

import bienew.blog.backend.markdown.ast.ASTNode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MarkDownParserTest {

    private final MarkDownParser parser = new MarkDownParser();

    private final BlockParser blockParser = new BlockParser();

    @Test
    void block_parser_test() {
        ASTNode astNode = blockParser.parse("#Hello\nhello world\nis there");

        System.out.println("=============after parse=============");
        while (astNode != null) {
            System.out.println(astNode);
            astNode = astNode.getNext();
        }
        System.out.println(astNode);
        System.out.println("=====================================");
    }

    @Test
    void heading에_class를_추가한다() {

        String markdown = """
                # Hello
                { .title }
                hello
                world
                is
                there
                """;

        String html = parser.toHtml(markdown);

        assertThat(html)
                .isEqualTo(
                        "<h1 class=\"title\">Hello</h1>\n"
                );
    }

    @Test
    void 여러_class를_추가한다() {

        String markdown = """
                # Hello
                { .title .text-3xl .font-bold }
                """;

        String html = parser.toHtml(markdown);

        assertThat(html)
                .isEqualTo(
                        "<h1 class=\"title text-3xl font-bold\">Hello</h1>\n"
                );
    }

    @Test
    void 일반_문단에도_class를_추가한다() {

        String markdown = """
                Hello World
                { .text-gray-500 }
                """;

        String html = parser.toHtml(markdown);

        assertThat(html)
                .isEqualTo(
                        "<p class=\"text-gray-500\">Hello World</p>\n"
                );
    }

    @Test
    void 빈줄이_있으면_속성을_적용하지_않는다() {

        String markdown = """
                # Hello
                <button onclick="alert('Button clicked!')">Click me</button>
                { .title }
                """;

        String html = parser.toHtml(markdown);

        assertThat(html)
                .doesNotContain(
                        "class=\"title\""
                );
    }
}