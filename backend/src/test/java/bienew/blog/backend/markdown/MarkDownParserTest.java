package bienew.blog.backend.markdown;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarkDownParserTest {

    @Test
    void parsesParagraph() {
        String markdown = "This is a paragraph.\nHere is another line of the paragraph.";

        String document = ASTRenderer.render(MarkDownParser.parse(markdown));

        System.out.println(document);

        String expected = "Document\n" +
                "  Paragraph\n" +
                "    Text: text=This is a paragraph.\n" +
                "  Paragraph\n" +
                "    Text: text=Here is another line of the paragraph.";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parsesHeading() {
        String markdown = "# This is a heading\n## this is a level 2 heading\n### this is a level 3 heading" +
                "\n#### this is a level 4 heading\n##### this is a level 5 heading" +
                "\n###### this is a level 6 heading\n####### this is a level 7 heading";

        String document = ASTRenderer.render(MarkDownParser.parse(markdown));

        System.out.println(document);

        String expected = "Document\n" +
                "\tHeading: level=1 \n" +
                "\t\tText: text=This is a heading \n" +
                "\tHeading: level=2 \n" +
                "\t\tText: text=this is a level 2 heading \n" +
                "\tHeading: level=3 \n" +
                "\t\tText: text=this is a level 3 heading \n" +
                "\tHeading: level=4 \n" +
                "\t\tText: text=this is a level 4 heading \n" +
                "\tHeading: level=5 \n" +
                "\t\tText: text=this is a level 5 heading \n" +
                "\tParagraph\n" +
                "\t\tText: text=###### this is a level 6 heading \n" +
                "\tParagraph\n" +
                "\t\tText: text=####### this is a level 7 heading";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parsesBoldText() {
        String markdown = "This is **bold** text.\n# Heading with **bold** text";

        String document = ASTRenderer.render(MarkDownParser.parse(markdown));

        System.out.println(document);

        String expected = "Document\n" +
                "\tParagraph\n" +
                "\t\tText: text=This is \n" +
                "\t\tBold\n" +
                "\t\t\tText: text=bold \n" +
                "\t\tText: text=text.\n" +
                "\tHeading: level=1 \n" +
                "\t\tText: text=Heading with \n" +
                "\t\tBold\n" +
                "\t\t\tText: text=bold \n" +
                "\t\tText: text=text";

        assertEquals(regString(expected), regString(document));
    }

    private String regString(String str) {
        return str.replaceAll("[^a-zA-Z0-9:=.]", "");
    }
}