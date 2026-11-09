package bienew.blog.backend.markdown.renderer;

import bienew.blog.backend.markdown.ast.ASTNode;

public class ASTRenderer {

    public static String render(ASTNode node) {
        if (node == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(node.getClass().getSimpleName());

        if (node.getClass().getDeclaredFields().length > 0) {
            sb.append(": ");
            for (var field : node.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    sb.append(field.getName()).append("=").append(field.get(node)).append(" ");
                } catch (IllegalAccessException e) {
                    sb.append("access denied ");
                }
            }
        }

        if (node.getChildren() != null && !node.getChildren().isEmpty()) {
            for (ASTNode child : node.getChildren()) {
                sb.append("\n\t").append(render(child).replace("\n", "\n\t"));
            }
        }
        return sb.toString();
    }
}
