# Markdown to HTML Converter

블로그 내부에서 사용하고 있는 Markdown to HTML 변환기입니다.
해당 파일에는 변환기의 구조와 사용법이 포함되어 있습니다.

---

## 처리 과정

1. Markdown Input
    - 마크다운 문서 문자열을 입력으로 주어짐.
2. Markdown Parser
   - 입력된 마크 다운 분석하고 AST(Abstract Syntax Tree) 생성.
3. AST
   - Markdown 문서의 구조를 나타내는 트리 구조.
4.  HTML Renderer
   - AST를 기반으로 HTML 문서를 생성.

- 각 구성요소는 명확하게 분리된 책임을 가짐.

---

## 구성 요소

### Parser
- Markdown 문서를 분석하여 AST를 생성하는 역할을 담당.
- Block Parser와 Inline Parser로 구성되어 있음.

#### Block Parser
- 문서의 블록 단위(문단, 제목, 리스트 등)를 분석.
- 각 블록의 시작과 끝을 식별하고, 해당 블록에 대한 AST 노드를 생성.

#### Inline Parser
- 블록 내의 인라인 요소(링크, 이미지, 강조 등)를 분석.
- 각 인라인 요소에 대한 AST 노드를 생성.

### AST Node
- Markdown 문서의 구조를 나타내는 트리 구조의 노드.
- 각 노드는 블록 또는 인라인 요소를 나타냄.
- 블록 노드는 자식 노드으로 블록 노드와 인라인 노드를 가질 수 있음.
- 인라인 노드는 자식 노드으로 인라인 노드만 가질 수 있음.

### HTML Renderer
- AST를 기반으로 HTML 문서를 생성하는 역할을 담당.
- 각 ASTNode가 서로 다르게 렌더링할 때 Visitor 패턴 기반으로 동작을 수행.

---

## 개발 규칙

### 각 계층은 독립적인 책임을 가진다.

- Parser, HTML Renderer는 서로 독립적인 책임을 가지며, 각 계층은 다른 계층에 의존하지 않음.
- Parser 내부에서 HTML Renderer를 호출하지 않음.
- HTML Renderer 내부에서 Parser를 호출하지 않음.

### 중첩 구조를 가진다.

- BlockNode와 InlineNode는 서로 다른 계층에 속하며, BlockNode는 InlineNode를 포함할 수 있음.

### Parser는 독립적이야 한다.

- 개별 Parser는 다른 Parser를 직접 호출하지 않음.
  - 단, 독립적으로 존재할 수 없는 하위 요소를 파싱하는 Parser는 상위 요소의 Parser에 의해 호출되어야 함.
  - 예를 들어, ListItem은 독립적으로 존재하지 않고 ListNode 내부에 포함되므로 ListNodeParser가 ListItemParser를 호출하여 파싱함.
- 전체 파싱 흐름은 BlockParser와 InlineParser가 관리함.
- 예를들어 BlockquoteParser는 ListParser를 직접 호출하지 않고 BlockParser를 통해 내부 내용의 파싱을 요청 함.

---

## 추가 시 규칙 

### 신규 Node를 추가할 때는 다음과 같은 규칙을 따라야 함.
- ASTVisitor에 해당 Node를 처리하는 visit 메서드를 등록해야 함.
- 독립적으로 존재하는 Node인 경우 이를 처리하는 Parser를 구현해야 함.
- 신규 Node의 타입에 맞게 BlockParser 또는 InlineParser에 등록해야 함.
- Parser 파일 기준으로 테스트 파일을 작성해야 함.

### 시균 Parser 를 추가할 때는 다음과 같은 규칙을 따라야 함.
- Parser는 BlockParser 또는 InlineParser 에서만 new 되어야 함.
- Parser는 싱글톤 객체 취급되므로 상태를 가지지 않아야 함.

### 신규 Renderer를 추가할 때는 다음과 같은 규칙을 따라야 함.
- ASTRenderer을 구현해야 함.
- 각 노드를 다른 로직으로 렌더링할 때 ASTVisitor 도 구현해야함.
- MarkdownRenderer에 해당 메소드를 생성해야함.
  - `public static {type} parseTo{name}(String markdown, ...)` 형식의 함수를 생성해야함.

### 테스트 파일 규칙
- 테스트 파일은 Parser 파일과 동일한 패키지에 위치해야 함.
- 테스트의 진입점은 `MarkdownConverter.parseTo{name}`의 함수를 호출하여 테스트해야 함.

--- 

## 사용법

- 마크다운을 변화하려고 하는 타입에 맞게 `MarkdownConverter`의 함수를 호출하면 됨.