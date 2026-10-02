package bienew.blog.backend.markdown;

import bienew.blog.backend.markdown.ast.ASTNode;
import org.junit.jupiter.api.Test;

class MarkDownParserTest {


    private final BlockParser blockParser = BlockParser.getInstance();

    @Test
    void block_parser_test() {
        ASTNode astNode = blockParser.parse("""
                [TEXT](https://www.naver.com)
                [_TEXT_](https://www.naver.com)
                [__TEXT__](https://www.naver.com)
                [**TEXT**](https://www.naver.com)
                """);

        System.out.println("=============after parse=============");
        while (astNode != null) {
            System.out.println(astNode);
            astNode = astNode.getNext();
        }
        System.out.println("=====================================");
    }
}