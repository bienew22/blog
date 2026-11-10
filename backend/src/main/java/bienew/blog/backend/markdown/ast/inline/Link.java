package bienew.blog.backend.markdown.ast.inline;

import bienew.blog.backend.markdown.ast.ASTNode;
import bienew.blog.backend.markdown.ast.InlineNode;
import bienew.blog.backend.markdown.renderer.ASTVisitor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Link 클래스는 마크다운 문서의 인라인 링크 노드를 나타내는 InlineNode
 * 이 클래스는 링크 텍스트와 URL을 저장하며, 마크다운 문서 내에서 인라인 링크 블록을 나타냅니다.
 */
@Getter
public class Link  extends InlineNode {
	private final String url;

	public Link(List<? extends InlineNode> children, String url) {
		children.forEach(this::addChild);
		this.url = url;
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
