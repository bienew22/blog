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

    @Test
    void fenced_code_block_parser_test() {
        ASTNode astNode = blockParser.parse("""
                ```java
                public class HelloWorld {
                    public static void main(String[] args) {
                        System.out.println("Hello, World!");
                    }
                }
                ```
                
                ```java
                ```java
                """);

        System.out.println("=============after parse=============");
        while (astNode != null) {
            System.out.println(astNode);
            astNode = astNode.getNext();
        }
        System.out.println("=====================================");
    }

    @Test
    void inline_code_block_parser_test() {
        ASTNode astNode = blockParser.parse("""
                `public class HelloWorld {`
                `public **static** void main(String[] args) {`
                `System.out.println("Hello, World!");`
                `}`
                **`}`**
                """);

        System.out.println("=============after parse=============");
        while (astNode != null) {
            System.out.println(astNode);
            astNode = astNode.getNext();
        }
        System.out.println("=====================================");
    }

    @Test
    void list_parser_test() {
        ASTNode astNode = blockParser.parse("""
                - Item 1
                \t1. Subitem 1.1
                \t2. Subitem 1.2
                \t\t1. Subsubitem 1.2.1 **bold**
                \t\t2. Subsubitem 1.2.2
                - Item 2
                \t- Subitem 2.1
                \t- Subitem 2.2
                - Item 3
                """);
        System.out.println("=============after parse=============");
        while (astNode != null) {
            System.out.println(astNode);
            astNode = astNode.getNext();
        }
        System.out.println("=====================================");
    }
}