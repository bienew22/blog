package bienew.blog.backend.markdown;


import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.block.Document;
import bienew.blog.backend.markdown.parser.BlockParser;


public class MarkDownParser {

    public static ASTNode parse(String markdown) {

        // 0. html escape 처리
        markdown = markdown.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");

        // 1. BlockParser를 사용하여 ASTNode로 변환
        return BlockParser.INSTANCE.parse(markdown);
    }

}
