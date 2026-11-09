package bienew.blog.backend.markdown;


import bienew.blog.backend.markdown.parser.BlockParser;
import bienew.blog.backend.markdown.renderer.ASTRenderer;


public class MarkdownConverter {

    public static String parseToAST(String markdown) {

        // 0. html escape 처리
        markdown = markdown.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");

        // 1. BlockParser를 사용하여 ASTNode로 변환
        return ASTRenderer.render(BlockParser.INSTANCE.parse(markdown));
    }

}
