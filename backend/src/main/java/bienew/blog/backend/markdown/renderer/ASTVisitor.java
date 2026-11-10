package bienew.blog.backend.markdown.renderer;

import bienew.blog.backend.markdown.ast.block.*;
import bienew.blog.backend.markdown.ast.inline.*;

public interface ASTVisitor {
    void visit(Document document);
    void visit(FencedCodeBlock codeBlock);
    void visit(Heading heading);
    void visit(ListItem listItem);
    void visit(ListNode listNode);
    void visit(Paragraph paragraph);

    void visit(Bold bold);
    void visit(InlineCode inlineCode);
    void visit(Italic italic);
    void visit(Link link);
    void visit(SubScript subScript);
    void visit(SupScript superScript);
    void visit(Text text);
    void visit(UnderLine underLine);
}
