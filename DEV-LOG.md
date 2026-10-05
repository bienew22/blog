# 2026-10-05 개발 로그
- front
    - 게시글 상세(`post/{slug}`) 화면 추가
    - 코드 블럭은 Prismjs을 통하여 parsing하고 디자인 직접 추가
- back
    - slug기반 게시글 조회 API 구현
- html-rendeerer (markdown to AST)
    - V1 구현 완료
    - block : ol, ul, code-block
    - inline : inline-code, link 

# 2026-09-27 개발 로그
- front
    - view에서 공통으로 사용되던 상단 header을 component으로 관리되도록 수정
    - tag's Post 목록 화면 구현
    - 404 페이지 추가
- back
    - DB 마이그레이션 도구 flywaydb 추가
    - tag 목록 조회 API 구현
    - post 목록 조회 API 수정
        - tag 관련 하드 코딩에서 조회되도록 수정
- html-renderer (markdown to html)
    - markdown to AST 기본 구조 구현
    - block: Heading 추가
    - inline: Text, Bold, Italic, Strikethrough, Bold&Italic, All bold and italic, Subscript, Superscript, Underline 

# 2026-09-20 개발 로그
- Tag 목록 화면 구현

# 2026-09-13 개발 로그

- POSTS 화면 구현
    - 화면(PostsView) 디자인

- 반응형 구조 구현

- 공통 기능
    - icon 별도 컴포넌트로 관리
    - 스크롤시 최상단으로 이동하는 버튼 추가
    - BlogLayout 디자인 수정 