package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.MarkdownConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThematicBreakParserTest {

    @Test
    void parsesThematicBreak() {
        String markdown = "---";

        String document = MarkdownConverter.toAST(markdown);

        String expected = "Document\n" +
                "\tThematicBreak";

        System.out.println(document);

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void doesNotParseNonThematicBreak() {
        String markdown = "----";

        String document = MarkdownConverter.toAST(markdown);

        String expected = "Document\n" +
                "\tParagraph\n" +
                "\t\tText: text=----";

        System.out.println(document);

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void doParseThematicBreaks() {
        String markdown = "---\n" +
                "---\n" +
                "---";

        String document = MarkdownConverter.toAST(markdown);

        String expected = "Document\n" +
                "\tThematicBreak\n" +
                "\tThematicBreak\n" +
                "\tThematicBreak";

        System.out.println(document);

        assertEquals(regString(expected), regString(document));
    }

    private String regString(String str) {
        return str.replaceAll("[^a-zA-Z0-9:=.`*]", "");
    }
}