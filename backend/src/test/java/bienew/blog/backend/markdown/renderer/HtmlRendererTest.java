package bienew.blog.backend.markdown.renderer;

import bienew.blog.backend.markdown.MarkdownConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HtmlRendererTest {

    @Test
    void testRender() {
        String markdown = "# Heading 1\n\nThis is a paragraph with **bold** text and _italic_ text.\n\n- Item 1\n- Item 2\n- Item 3";

        String expectedHtml = "<h1>Heading 1</h1>\n" +
                "<p>This is a paragraph with <strong>bold</strong> text and <em>italic</em> text.</p>\n" +
                "<ul>\n" +
                "<li><p>Item 1</p></li>\n" +
                "<li><p>Item 2</p></li>\n" +
                "<li><p>Item 3</p></li>\n" +
                "</ul>";

        String actualHtml = MarkdownConverter.parseToHTML(markdown);

        System.out.println("result: " + actualHtml);

        assertEquals(regString(expectedHtml), regString(actualHtml));
    }

    @Test
    void renderTableTest() {
        String markdown = "| Header 1 | Header 2 |\n" +
                "|---|---|\n" +
                "| Cell 1   | Cell 2   |\n" +
                "| Cell 3   | Cell 4   |";

        String expectedHtml = "<table>\n" +
                "<thead>\n" +
                "<tr><th>Header 1</th><th>Header 2</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td>Cell 1</td><td>Cell 2</td></tr>\n" +
                "<tr><td>Cell 3</td><td>Cell 4</td></tr>\n" +
                "</tbody>\n" +
                "</table>";

        String actualHtml = MarkdownConverter.parseToHTML(markdown);

        System.out.println("result: " + actualHtml);

        assertEquals(regString(expectedHtml), regString(actualHtml));
    }

    private String regString(String str) {
        return str.replaceAll("[^a-zA-Z0-9:=.`*<>/]", "");
    }
}