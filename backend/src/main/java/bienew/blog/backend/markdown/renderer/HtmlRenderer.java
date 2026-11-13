package bienew.blog.backend.markdown.renderer;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.block.*;
import bienew.blog.backend.markdown.ast.inline.*;

import java.util.List;
import java.util.Objects;

public class HtmlRenderer implements ASTRenderer, ASTVisitor {

    private final StringBuilder builder = new StringBuilder();

    @Override
    public String render(ASTNode node) {
        if (node == null) {
            return "";
        }

        node.accept(this);

        return builder.toString();
    }

    @Override
    public void visit(Document document) {
        document.getChildren().forEach(child -> child.accept(this));
    }

    @Override
    public void visit(FencedCodeBlock codeBlock) {
        builder.append("<pre class=\"line-numbers\"><code class=\"language-")
                .append(codeBlock.getLanguage()).append("\">")
                .append(codeBlock.getCode())
                .append("</code></pre>");
    }

    @Override
    public void visit(Heading heading) {
        builder.append("<h").append(heading.getLevel()).append(">");
        heading.getChildren().forEach(child -> child.accept(this));
        builder.append("</h").append(heading.getLevel()).append(">");
    }

    @Override
    public void visit(ListItem listItem) {
        builder.append("<li>");
        listItem.getChildren().forEach(child -> child.accept(this));
        builder.append("</li>");
    }

    @Override
    public void visit(ListNode listNode) {
        String tag = listNode.isOrdered() ? "ol" : "ul";
        builder.append("<").append(tag).append(">");
        listNode.getChildren().forEach(child -> child.accept(this));
        builder.append("</").append(tag).append(">");
    }

    @Override
    public void visit(Paragraph paragraph) {
        if (paragraph.getChildren().isEmpty()) {
            return;
        }

        builder.append("<p>");
        paragraph.getChildren().forEach(child -> child.accept(this));
        builder.append("</p>");
    }

    @Override
    public void visit(Bold bold) {
        builder.append("<strong>");
        bold.getChildren().forEach(child -> child.accept(this));
        builder.append("</strong>");
    }

    @Override
    public void visit(InlineCode inlineCode) {
        builder.append("<code>").append(inlineCode.getCode()).append("</code>");
    }

    @Override
    public void visit(Italic italic) {
        builder.append("<em>");
        italic.getChildren().forEach(child -> child.accept(this));
        builder.append("</em>");
    }

    @Override
    public void visit(Link link) {
        builder.append("<a href=\"")
                .append(link.getUrl())
                .append("\" target=\"_blank\" rel=\"noopener noreferrer\">");
        link.getChildren().forEach(child -> child.accept(this));
        builder.append("</a>");
    }

    @Override
    public void visit(SubScript subScript) {
        builder.append("<sub>");
        subScript.getChildren().forEach(child -> child.accept(this));
        builder.append("</sub>");
    }

    @Override
    public void visit(SupScript superScript) {
        builder.append("<sup>");
        superScript.getChildren().forEach(child -> child.accept(this));
        builder.append("</sup>");
    }

    @Override
    public void visit(Text text) {
        builder.append(text.getText());
    }

    @Override
    public void visit(UnderLine underLine) {
        builder.append("<u>");
        underLine.getChildren().forEach(child -> child.accept(this));
        builder.append("</u>");
    }

    @Override
    public void visit(Table table) {
        builder.append("<table>");
        table.getChildren().forEach(child -> child.accept(this));
        builder.append("</table>");
    }

    @Override
    public void visit(Table.TableHead tableHead) {
        builder.append("<thead>");
        tableHead.getChildren().forEach(child -> child.accept(this));
        builder.append("</thead>");
    }

    @Override
    public void visit(Table.TableBody tableBody) {
        builder.append("<tbody>");
        tableBody.getChildren().forEach(child -> child.accept(this));
        builder.append("</tbody>");
    }

    @Override
    public void visit(TableRow tableRow) {
        builder.append("<tr>");

        String tag = tableRow.getType().getTag();

        for (int i = 0; i < tableRow.getChildren().size(); i++) {
            ASTNode child = tableRow.getChildren().get(i);

            builder.append("<").append(tag);

            if (tableRow.getDelimiters() == null || i >= tableRow.getDelimiters().size()) {
                builder.append(">");
            } else {
                String delimiter = tableRow.getDelimiters().get(i);
                builder.append(" style=\"text-align:").append(delimiter).append(";\">");
            }

            child.accept(this);
            builder.append("</").append(tag).append(">");
        }

        builder.append("</tr>");
    }

    @Override
    public void visit(TableRow.TableItem tableItem) {
        tableItem.getChildren().forEach(child -> child.accept(this));
    }
}
