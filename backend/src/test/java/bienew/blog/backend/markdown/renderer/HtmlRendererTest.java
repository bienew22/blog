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
                "<tr><th style=\"text-align:center;\">Header 1</th><th style=\"text-align:center;\">Header 2</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td style=\"text-align:center;\">Cell 1</td><td style=\"text-align:center;\">Cell 2</td></tr>\n" +
                "<tr><td style=\"text-align:center;\">Cell 3</td><td style=\"text-align:center;\">Cell 4</td></tr>\n" +
                "</tbody>\n" +
                "</table>";

        String actualHtml = MarkdownConverter.parseToHTML(markdown);

        System.out.println("result: " + actualHtml);

        assertEquals(regString(expectedHtml), regString(actualHtml));
    }

    @Test
    void renderTableWithOnlyHeaderTest() {
        String markdown = "| Header 1 | Header 2 |\n" +
                "|---|---|";

        String expectedHtml = "<table>\n" +
                "<thead>\n" +
                "<tr><th style=\"text-align:center;\">Header 1</th><th style=\"text-align:center;\">Header 2</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "</tbody>\n" +
                "</table>";

        String actualHtml = MarkdownConverter.parseToHTML(markdown);

        System.out.println("result: " + actualHtml);

        assertEquals(regString(expectedHtml), regString(actualHtml));
    }

    @Test
    void renderTableWithAlignTest() {
        String markdown = "| Header 1 | Header 2 | Header 3 |\n" +
                "|:---|:---:|---:|\n" +
                "| Cell 1   | Cell 2   | Cell 3   |\n" +
                "| Cell 4   | Cell 5   | Cell 6   |";

        String expectedHtml = "<table>\n" +
                "<thead>\n" +
                "<tr><th style=\"text-align:left;\">Header 1</th><th style=\"text-align:center;\">Header 2</th><th style=\"text-align:right;\">Header 3</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td style=\"text-align:left;\">Cell 1</td><td style=\"text-align:center;\">Cell 2</td><td style=\"text-align:right;\">Cell 3</td></tr>\n" +
                "<tr><td style=\"text-align:left;\">Cell 4</td><td style=\"text-align:center;\">Cell 5</td><td style=\"text-align:right;\">Cell 6</td></tr>\n" +
                "</tbody>\n" +
                "</table>";

        String actualHtml = MarkdownConverter.parseToHTML(markdown);

        System.out.println("result: " + actualHtml);

        assertEquals(regString(expectedHtml), regString(actualHtml));
    }

    @Test
    void renderTableWithMoreHeadersThanCellsTest() {
        String markdown = "| Header 1 | Header 2 | Header 3 |\n" +
                "|---|---|---|\n" +
                "| Cell 1   | Cell 2   |";

        String expectedHtml = "<table>\n" +
                "<thead>\n" +
                "<tr><th style=\"text-align:center;\">Header 1</th><th style=\"text-align:center;\">Header 2</th><th style=\"text-align:center;\">Header 3</th></tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr><td style=\"text-align:center;\">Cell 1</td><td style=\"text-align:center;\">Cell 2</td></tr>\n" +
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