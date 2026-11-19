package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.MarkdownConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageParserTest {
    @Test
    void parseImage() {
        String markdown = "![alt text](https://example.com/image.png)";

        String document = MarkdownConverter.toAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tParagraph\n" +
                "\t\tText: text=! \n" +
                "\t\tLink: url=https://example.com/image.png \n" +
                "\t\t\tText: text=alt text ";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parseImageWithEmptyAltText() {
        String markdown = "![](/api/v1/images/astr)";

        String document = MarkdownConverter.toAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tImage: url=/api/v1/images/astr altText= \n";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parseImageWithList() {
        String markdown = "- Item 1\n- ![alt text](/api/v1/images/astr)\n- Item 3";

        String document = MarkdownConverter.toAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tListNode: ordered=false \n" +
                "\t\tListItem\n" +
                "\t\t\tParagraph\n" +
                "\t\t\t\tText: text=Item 1 \n" +
                "\t\tListItem\n" +
                "\t\t\tImage: url=/api/v1/images/astr altText=alt text \n" +
                "\t\tListItem\n" +
                "\t\t\tParagraph\n" +
                "\t\t\t\tText: text=Item 3 ";

        assertEquals(regString(expected), regString(document));
    }

    private String regString(String str) {
        return str.replaceAll("[^a-zA-Z0-9:=.`*]", "");
    }
}