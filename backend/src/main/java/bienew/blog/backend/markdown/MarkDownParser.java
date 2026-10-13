package bienew.blog.backend.markdown;


import bienew.blog.backend.markdown.ast.ASTNode;


public class MarkDownParser {

    public static String toHtml(String markdown) {

        // 0. html escape 처리
        markdown = markdown.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");

        ASTNode document = BlockParser.INSTANCE.parse(markdown);

        StringBuilder sb = new StringBuilder();

        while (document != null) {
            sb.append(document.toHtml());
            document = document.getNext();
        }

        return sb.toString();
    }

}
