# TODO (2026-10-09 ~ 2026-10-11)
- 게시글 작성
    - front : 게시글 작성 화면, 작성 후 요청
    - back : 게시글 저장, 태그 생성, 카테고리(설계)
- ci-cd : 배포용 docker-compse 구축

# TODO
- 게시글 조회 (백엔드 API 제공)
- 게시글 화면 (프론트 화면 및 렌더링 css 정리)
- 마크다운 -> AST -> HTML 번역기 구현
    - 마크다운 -> AST : V1 구현 완료

--- 

# 고민거리
- 프론트와 백에서 각각 어떻게 처리할 것인가?
- 404 페이지으로 가는제 언제 적당할까?

---

# front 폴더 정리

- views : 하나의 화면
    - Layout을 사용. Layout에 전달할 내용만 다름.

- components/layout : 하나의 구조

- components/blog : 블로그에 사용되는 컴포넌트 모음
    - 사용처: PostView, TagsView, CategoriesView

- components/icon : 공통으로 사용되는 아이콘 모음