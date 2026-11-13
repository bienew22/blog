package bienew.blog.backend.markdown;


import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.parser.BlockParser;
import bienew.blog.backend.markdown.renderer.ASTRenderer;
import bienew.blog.backend.markdown.renderer.HtmlRenderer;
import bienew.blog.backend.markdown.renderer.TreeRenderer;


public class MarkdownConverter {

    public static String toAST(String markdown) {
        return convert(markdown, new TreeRenderer());
    }

    public static String toHTML(String markdown) {
        return convert(markdown, new HtmlRenderer());
    }

    private static String convert(String markdown, ASTRenderer renderer) {
        // 0. html escape 처리
        markdown = markdown.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");

        String[] lines = markdown
                .replace("\r\n", "\n")
                .replace("\r", "")
                .split("\n", -1);

        // 1. BlockParser를 사용하여 ASTNode로 변환
        ASTNode rootNode = BlockParser.INSTANCE.parse(lines);

        // 2. ASTRenderer를 사용하여 ASTNode를 문자열로 변환
        return renderer.render(rootNode);
    }

}
