package bienew.blog.backend.markdown;

import bienew.blog.backend.markdown.ast.ASTNode;
import org.junit.jupiter.api.Test;

class MarkDownParserTest {


    private final BlockParser blockParser = BlockParser.getInstance();

    @Test
    void block_parser_test() {
        ASTNode astNode = blockParser.parse("""
                __**_a_**__
                ___**a**___
                """);

        System.out.println("=============after parse=============");
        while (astNode != null) {
            System.out.println(astNode);
            astNode = astNode.getNext();
        }
        System.out.println("=====================================");
    }
}