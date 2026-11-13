package bienew.blog.backend.markdown.ast.block;

import bienew.blog.backend.markdown.ast.BlockNode;
import bienew.blog.backend.markdown.renderer.ASTVisitor;

import java.util.List;

/**
 * Table 클래스는 마크다운 문서의 테이블 블록을 나타내는 BlockNode 입니다.
 * 테이블은 TableHead, TableBody를 자식 노드로 가질수 있습니다.
 */
public class Table extends BlockNode {

    public Table(List<String> delimiters, TableRow head, List<TableRow> body) {

        addChild(new TableHead(head, delimiters));
        addChild(new TableBody(body, delimiters));
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * TableHead 클래스는 마크다운 문서의 테이블 헤드 블록을 나타내는 BlockNode 입니다.
     * 테이블 헤드는 테이블의 첫 번째 행으로, 열의 제목을 나타내는 데 사용됩니다.
     */
    public static class TableHead extends BlockNode {

        public TableHead(TableRow node, List<String> delimiters) {
            node.setType(TableRow.TableRowType.TH);
            node.setDelimiters(delimiters);
            addChild(node);
        }

        @Override
        public void accept(ASTVisitor visitor) {
            visitor.visit(this);
        }
    }

    /**
     * TableBody 클래스는 마크다운 문서의 테이블 바디 블록을 나타내는 BlockNode 입니다.
     * 테이블 바디는 테이블의 나머지 행으로, 실제 데이터를 나타내는 데 사용됩니다.
     */
    public static class TableBody extends BlockNode {


        public TableBody(List<TableRow> blockNodes, List<String> delimiters) {

            blockNodes.forEach(node -> {
                node.setDelimiters(delimiters);
                node.setType(TableRow.TableRowType.TD);

                addChild(node);
            });
        }

        @Override
        public void accept(ASTVisitor visitor) {
            visitor.visit(this);
        }
    }
}
