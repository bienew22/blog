package bienew.blog.backend.markdown.ast.block;

import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.ast.CompositeNode;
import bienew.blog.backend.markdown.ast.InlineNode;
import bienew.blog.backend.markdown.renderer.ASTVisitor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TableRow extends BlockNode {

    @Getter
    public enum TableRowType {
        TH("th"), TD("td");

        private final String tag;

        TableRowType(String tag) {
            this.tag = tag;
        }

    }

    private TableRowType type;

    public TableRow(List<TableItem> children) {
        children.forEach(this::addChild);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }


    public static class TableItem extends BlockNode {

        public TableItem(List<? extends InlineNode> children) {
            children.forEach(this::addChild);
        }

        @Override
        public void accept(ASTVisitor visitor) {
            visitor.visit(this);
        }
    }
}
