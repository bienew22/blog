# TODO (2026-10-13 ~ 2026-11-01)
- 개인 일정이 많아지면서 3주로 잡음.
- ci-cd : 배포용 docker-compose 구축
- AST -> HTML 번역기 구현


# TODO (발생)
- 게시글 수정(임시저장), 삭제
- (고민) 임시저장 상태의 게시글은 어디서 보여줄것인가?
- 관리자 페이지 -> 태그 관리
- oauth 로그인 필요
- 로그인한 상태에서만 게시글 작성에 접근 가능하게 설정
- 로그인 페이지, 
- desige.md 구축하여 디자인 일관성있게 수정
---

# front 폴더 정리

- views : 하나의 화면
    - Layout을 사용. Layout에 전달할 내용만 다름.

- components/layout : 하나의 구조

- components/blog : 블로그에 사용되는 컴포넌트 모음
    - 사용처: PostView, TagsView, CategoriesView

- components/icon : 공통으로 사용되는 아이콘 모음