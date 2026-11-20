package bienew.blog.backend.markdown;


import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.parser.BlockParser;
import bienew.blog.backend.markdown.renderer.ASTRenderer;
import bienew.blog.backend.markdown.renderer.HtmlRenderer;
import bienew.blog.backend.markdown.renderer.TreeRenderer;


public class MarkdownConverter {

    private final String markdown;

    private MarkdownConverter(String markdown) {
        this.markdown = markdown;
    }

    public static MarkdownConverter of (String markdown) {
        return new MarkdownConverter(markdown);
    }

    public MarkdownDocument parse() {
        // 0. html escape 처리
        String filteredMarkdown = markdown.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");

        String[] lines = filteredMarkdown
                .replace("\r\n", "\n")
                .replace("\r", "")
                .split("\n", -1);

        // 1. BlockParser를 사용하여 ASTNode로 변환
        ASTNode rootNode = BlockParser.INSTANCE.parse(lines);

        // 2. ASTRenderer를 사용하여 ASTNode를 문자열로 변환
        return new MarkdownDocument(rootNode);
    }

}
