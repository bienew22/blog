# TODO (2026-10-13 ~ 2026-11-01)
- 개인 일정이 많아지면서 3주로 잡음.
- ci-cd : 배포용 docker-compose 구축 (10.31 ~ 11.01 예정)
- Oauth 로그인 구현 및 로그인 권한에 따라서 접근 가능 메뉴 분리
- 로그인 화면 구현

# TODO
- 게시글 수정(임시저장), 삭제, 미리보기
- 관리자 페이지
- markdown to html 컴포넌트 추가 구현
    - 수평선, 개행, 표, 인용, 사진
- 태그 생성시 : 태그 이름이랑 설명 작성란 추가
- 카테고리 추가
    - 메인 분류 > 하위 분류
- 게시글 작성 시 카테고리 설정

---

# front 폴더 정리

- views : 하나의 화면
    - Layout을 사용. Layout에 전달할 내용만 다름.

- components/layout : 하나의 구조

- components/blog : 블로그에 사용되는 컴포넌트 모음
    - 사용처: PostView, TagsView, CategoriesView

- components/icon : 공통으로 사용되는 아이콘 모음