package bienew.blog.backend.markdown.ast.inline;

import bienew.blog.backend.markdown.ast.ASTNode;
import lombok.Getter;

/**
 * Link 클래스는 마크다운 문서의 인라인 링크 노드를 나타내는 클래스입니다.
 * 이 클래스는 링크 텍스트와 URL을 저장하며, 마크다운 문서 내에서 인라인 링크 블록을 나타냅니다.
 */
@Getter
public class Link  extends ASTNode {
	private final String url;

	public Link(String text, String url) {
		this.source = text;
		this.url = url;
	}

}
