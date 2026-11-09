package bienew.blog.backend.markdown;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarkdownConverterTest {

    @Test
    void parsesParagraph() {
        String markdown = "This is a paragraph.\nHere is another line of the paragraph.";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tParagraph\n" +
                "\t\tText: text=This is a paragraph.\n" +
                "\tParagraph\n" +
                "\t\tText: text=Here is another line of the paragraph.";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parsesHeading() {
        String markdown = "# This is a heading\n## this is a level 2 heading\n### this is a level 3 heading" +
                "\n#### this is a level 4 heading\n##### this is a level 5 heading" +
                "\n###### this is a level 6 heading\n####### this is a level 7 heading";

        String document = MarkdownConverter.parseToAST(markdown);

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

        String document = MarkdownConverter.parseToAST(markdown);

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

    @Test
    void parsesItalicText() {
        String markdown = "This is _italic_ text.\n# Heading with _italic_ text";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tParagraph\n" +
                "\t\tText: text=This is \n" +
                "\t\tItalic\n" +
                "\t\t\tText: text=italic \n" +
                "\t\tText: text=text.\n" +
                "\tHeading: level=1 \n" +
                "\t\tText: text=Heading with \n" +
                "\t\tItalic\n" +
                "\t\t\tText: text=italic \n" +
                "\t\tText: text=text";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parsesBoldAndItalicText() {
        String markdown = "This is _**italic after bold**_ and **_bold after italic_**";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tParagraph\n" +
                "\t\tText: text=This is \n" +
                "\t\tItalic\n" +
                "\t\t\tBold\n" +
                "\t\t\t\tText: text=italic after bold \n" +
                "\t\tText: text=and \n" +
                "\t\tBold\n" +
                "\t\t\tItalic\n" +
                "\t\t\t\tText: text=bold after italic";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parsesNotItalicText() {
        String markdown = "This is not _italic text.\n# Heading with not _italic text a_b_c is not italic";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tParagraph\n" +
                "\t\tText: text=This is not _italic text.\n" +
                "\tHeading: level=1 \n" +
                "\t\tText: text=Heading with not _italic text a_b_c is not italic";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parsesInlineCode() {
        String markdown = "This is `inline code` text.\n# Heading with `inline code` text";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tParagraph\n" +
                "\t\tText: text=This is \n" +
                "\t\tInlineCode: code=inline code \n" +
                "\t\tText: text=text.\n" +
                "\tHeading: level=1 \n" +
                "\t\tText: text=Heading with \n" +
                "\t\tInlineCode: code=inline code \n" +
                "\t\tText: text=text";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parsesNotInlineCode() {
        String markdown = "This is not ````inline code text.\n# Heading with not `inline code text is not inline code";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tParagraph\n" +
                "\t\tText: text=This is not ````inline code text.\n" +
                "\tHeading: level=1 \n" +
                "\t\tText: text=Heading with not `inline code text is not inline code";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parseBoldAndInlineCode() {
        String markdown = "This is **`bold and inline`** and only `**inline code**`";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tParagraph\n" +
                "\t\tText: text=This is \n" +
                "\t\tBold\n" +
                "\t\t\tInlineCode: code=bold and inline \n" +
                "\t\tText: text=and only \n" +
                "\t\tInlineCode: code=**inline code**";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parseFencedCodeBlock() {
        String markdown = "```java\n" +
                "public class HelloWorld {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Hello, World!\");\n" +
                "    }\n" +
                "}\n" +
                "```";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tFencedCodeBlock: language=java \n" +
                "\t\tcode=public class HelloWorld {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(&quot;Hello, World!&quot;);\n" +
                "    }\n" +
                "}";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parseUnClosedFencedCodeBlock() {
        String markdown = "# heading 1\n```java\n" +
                "public class HelloWorld {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Hello, World!\");\n" +
                "    }\n" +
                "}\n" +
                "# heading 1";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tHeading: level=1 \n" +
                "\t\tText: text=heading 1\n" +
                "\tFencedCodeBlock: language=java code=public class HelloWorld {\n" +
                "\t    public static void main(String[] args) {\n" +
                "\t        System.out.println(&quot;Hello, World!&quot;);\n" +
                "\t    }\n" +
                "\t}\n" +
                "\t# heading 1 ";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parseLink() {
        String markdown = "This is a [link](https://example.com) in a paragraph.\n" +
                "# Heading with a [link](https://example.com)";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tParagraph\n" +
                "\t\tText: text=This is a \n" +
                "\t\tLink: url=https://example.com \n" +
                "\t\t\tText: text=link \n" +
                "\t\tText: text=in a paragraph.\n" +
                "\tHeading: level=1 \n" +
                "\t\tText: text=Heading with a \n" +
                "\t\tLink: url=https://example.com \n" +
                "\t\t\tText: text=link";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parseNotLink() {
        String markdown = "This is not a [link]https://example.com in a paragraph.\n" +
                "Heading with a [link(https://example.com) is not a link\n" +
                "This is not a [link](https://example.com in a paragraph.)";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tParagraph\n" +
                "\t\tText: text=This is not a [link]https://example.com in a paragraph.\n" +
                "\tParagraph\n" +
                "\t\tText: text=Heading with a [link(https://example.com) is not a link\n" +
                "\tParagraph\n" +
                "\t\tText: text=This is not a" +
                "\t\tLink:url=https:example.cominaparagraph.Text:text=link";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parsesUnderLineText() {
        String markdown = "This is __underline__ text.\n# Heading with __underline__ text";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tParagraph\n" +
                "\t\tText: text=This is \n" +
                "\t\tUnderLine\n" +
                "\t\t\tText: text=underline \n" +
                "\t\tText: text=text.\n" +
                "\tHeading: level=1 \n" +
                "\t\tText: text=Heading with \n" +
                "\t\tUnderLine\n" +
                "\t\t\tText: text=underline \n" +
                "\t\tText: text=text";

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void parseUnderLineAndOtherText() {
        String markdown = "This is __**underline and bold**__\n[_link_](https://example.com)";

        String document = MarkdownConverter.parseToAST(markdown);

        System.out.println(document);

        String expected = "Document\n" +
                "\tParagraph\n" +
                "\t\tText: text=This is \n" +
                "\t\tUnderLine\n" +
                "\t\t\tBold\n" +
                "\t\t\t\tText: text=underline and bold \n" +
                "\tParagraph\n" +
                "\t\tLink: url=https://example.com \n" +
                "\t\t\tItalic\n" +
                "\t\t\t\tText: text=link";

        assertEquals(regString(expected), regString(document));
    }

    private String regString(String str) {
        return str.replaceAll("[^a-zA-Z0-9:=.`*]", "");
    }
}