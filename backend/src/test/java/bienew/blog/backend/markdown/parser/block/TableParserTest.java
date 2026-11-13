package bienew.blog.backend.markdown.parser.block;

import bienew.blog.backend.markdown.MarkdownConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TableParserTest {

    @Test
    void rejectsLineWithOnlyPipeCharacters() {
        String markdown = "||||";

        String document = MarkdownConverter.parseToAST(markdown);

        String expected = "Document\n" +
                "\tParagraph\n" +
                "\t\tText: text=||||";

        System.out.println(document);

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void rejectsLineWithCellContent() {
        String markdown = "|header|";

        String document = MarkdownConverter.parseToAST(markdown);

        String expected = "Document\n" +
                "\tParagraph\n" +
                "\t\tText: text=|header|";

        System.out.println(document);

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void onlyTitleLineIsNotParsedAsTable() {
        String markdown = "|header1|header2|\n" +
                "|---|---|";

        String document = MarkdownConverter.parseToAST(markdown);

        String expected = "Document\n" +
                "\tTable\n" +
                "\t\tTableHead: delimiters=[center, center] \n" +
                "\t\t\tTableRow: TH_TYPE=1 TD_TYPE=2 type=1 \n" +
                "\t\t\t\tTableItem\n" +
                "\t\t\t\t\tText: text=header1 \n" +
                "\t\t\t\tTableItem\n" +
                "\t\t\t\t\tText: text=header2 \n" +
                "\t\tTableBody: delimiters=[center, center] ";

        System.out.println(document);

        assertEquals(regString(expected), regString(document));
    }

    @Test
    void fullTableParseTest() {
        String markdown = "|header1|header2|\n" +
                "|---|---|\n" +
                "|cell1|cell2|\n" +
                "|cell3|cell4|";

        String document = MarkdownConverter.parseToAST(markdown);

        String expected = "Document\n" +
                "\tTable\n" +
                "\t\tTableHead: delimiters=[center, center] \n" +
                "\t\t\tTableRow: TH_TYPE=1 TD_TYPE=2 type=1 \n" +
                "\t\t\t\tTableItem\n" +
                "\t\t\t\t\tText: text=header1 \n" +
                "\t\t\t\tTableItem\n" +
                "\t\t\t\t\tText: text=header2 \n" +
                "\t\tTableBody: delimiters=[center, center] \n" +
                "\t\t\tTableRow: TH_TYPE=1 TD_TYPE=2 type=2 \n" +
                "\t\t\t\tTableItem\n" +
                "\t\t\t\t\tText: text=cell1 \n" +
                "\t\t\t\tTableItem\n" +
                "\t\t\t\t\tText: text=cell2 \n" +
                "\t\t\tTableRow: TH_TYPE=1 TD_TYPE=2 type=2 \n" +
                "\t\t\t\tTableItem\n" +
                "\t\t\t\t\tText: text=cell3 \n" +
                "\t\t\t\tTableItem\n" +
                "\t\t\t\t\tText: text=cell4 \n";

        System.out.println(document);

        assertEquals(regString(expected), regString(document));
    }

    private String regString(String str) {
        return str.replaceAll("[^a-zA-Z0-9:=.`*]", "");
    }
}
