package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.MarkdownConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListNodeParserTest {
    @Test
    void parseOrderedList() {
        String markdown = "1. Item 1\n2. Item 2\n3. Item 3";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tListNode: ordered=true \n" +
                "\t\tListItem\n" +
                "\t\t\tParagraph\n" +
                "\t\t\t\tText: text=Item 1 \n" +
                "\t\tListItem\n" +
                "\t\t\tParagraph\n" +
                "\t\t\t\tText: text=Item 2 \n" +
                "\t\tListItem\n" +
                "\t\t\tParagraph\n" +
                "\t\t\t\tText: text=Item 3";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parseNestedList() {
        String markdown = "- Item 1\n\t- Nested Item 1.1\n\t- Nested Item 1.2\n- Item 2";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tListNode: ordered=false \n" +
                "\t\tListItem\n" +
                "\t\t\tParagraph\n" +
                "\t\t\t\tText: text=Item 1 \n" +
                "\t\t\tListNode: ordered=false \n" +
                "\t\t\t\tListItem\n" +
                "\t\t\t\t\tParagraph\n" +
                "\t\t\t\t\t\tText: text=Nested Item 1.1 \n" +
                "\t\t\t\tListItem\n" +
                "\t\t\t\t\tParagraph\n" +
                "\t\t\t\t\t\tText: text=Nested Item 1.2 \n" +
                "\t\tListItem\n" +
                "\t\t\tParagraph\n" +
                "\t\t\t\tText: text=Item 2";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parseNestedOrderedList() {
        String markdown = "1. Item 1\n\t1. Nested Item 1.1\n\t2. Nested Item 1.2\n2. Item 2";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tListNode: ordered=true \n" +
                "\t\tListItem\n" +
                "\t\t\tParagraph\n" +
                "\t\t\t\tText: text=Item 1 \n" +
                "\t\t\tListNode: ordered=true \n" +
                "\t\t\t\tListItem\n" +
                "\t\t\t\t\tParagraph\n" +
                "\t\t\t\t\t\tText: text=Nested Item 1.1 \n" +
                "\t\t\t\tListItem\n" +
                "\t\t\t\t\tParagraph\n" +
                "\t\t\t\t\t\tText: text=Nested Item 1.2 \n" +
                "\t\tListItem\n" +
                "\t\t\tParagraph\n" +
                "\t\t\t\tText: text=Item 2";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parseNestedListWithDifferentTypes() {
        String markdown = "- Item 1\n\t1. Nested Item 1.1\n\t2. Nested Item 1.2\n- Item 2";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tListNode: ordered=false \n" +
                "\t\tListItem\n" +
                "\t\t\tParagraph\n" +
                "\t\t\t\tText: text=Item 1 \n" +
                "\t\t\tListNode: ordered=true \n" +
                "\t\t\t\tListItem\n" +
                "\t\t\t\t\tParagraph\n" +
                "\t\t\t\t\t\tText: text=Nested Item 1.1 \n" +
                "\t\t\t\tListItem\n" +
                "\t\t\t\t\tParagraph\n" +
                "\t\t\t\t\t\tText: text=Nested Item 1.2 \n" +
                "\t\tListItem\n" +
                "\t\t\tParagraph\n" +
                "\t\t\t\tText: text=Item 2";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parseWeirdNestedList() {
        String markdown = "1. Item 1\n\t\t1. Nested Item 1.1\n\t- weird\n- Item 2";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tListNode: ordered=true \n" +
                "\t\tListItem\n" +
                "\t\t\tParagraph\n" +
                "\t\t\t\tText: text=Item 1 \n" +
                "\t\t\tParagraph \n" +
                "\t\t\t\tText: text=\t1. Nested Item 1.1 \n" +
                "\t\t\tListNode: ordered=false\n" +
                "\t\t\t\tListItem\n" +
                "\t\t\t\t\tParagraph\n" +
                "\t\t\t\t\t\tText: text=weird \n" +
                "\tListItem\n" +
                "\t\tParagraph\n" +
                "\t\t\tText: text=Item 2";

        assertEquals(regString(expected), regString(document));
    }

    private String regString(String str) {
        return str.replaceAll("[^a-zA-Z0-9:=.`*]", "");
    }
}