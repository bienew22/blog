# TODO (2026-10-09 ~ 2026-10-11)
- 게시글 작성
    - front : 게시글 작성 화면, 작성 후 요청
        - 게시글 작성 디자인 (완)
        - 게시글 저장 요청
        - 태그 생성 요청
        - 태그 목록 조회
    - back : 게시글 저장, 태그 생성, 카테고리(설계)
        - 게시글 저장
        - 태그 저장
- ci-cd : 배포용 docker-compse 구축

# TODO (발생)
- 게시글 수정(임시저장), 삭제
- (고민) 임시저장 상태의 게시글은 어디서 보여줄것인가?
- AST -> HTML 번역기 구현
- 관리자 페이지 -> 태그 관리

---

# front 폴더 정리

- views : 하나의 화면
    - Layout을 사용. Layout에 전달할 내용만 다름.

- components/layout : 하나의 구조

- components/blog : 블로그에 사용되는 컴포넌트 모음
    - 사용처: PostView, TagsView, CategoriesView

- components/icon : 공통으로 사용되는 아이콘 모음