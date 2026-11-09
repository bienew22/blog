package bienew.blog.backend.markdown;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarkDownParserTest {

    @Test
    void parsesParagraphAsTextChild() {
        String markdown = "This is a paragraph.\nHere is another line of the paragraph.";

        String document = ASTRenderer.render(MarkDownParser.parse(markdown));

        System.out.println(document);
    }
}