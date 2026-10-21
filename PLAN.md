# TODO (2026-10-13 ~ 2026-11-01)
- 개인 일정이 많아지면서 3주로 잡음.
- ci-cd : 배포용 docker-compose 구축 (10.31 ~ 11.01 예정)
- 글쓰기 이동 페이지 추가.
- 미리보기 팝업 추가.
- 게시글 저장 상태 추가 (임시저장, 저장).
- 용어를 관리하는 테이블 추가.


# TODO
- 게시글 수정(임시저장), 삭제, 미리보기
- 관리자 페이지
- oauth 로그인 : 로그인 권한에 따라서 접근 가능 메뉴 분리
- design.md 구축: 디자인 일관성 유지
- markdown to html 컴포넌트 추가 구현
    - 수평선, 개행, 표, 인용, 사진
- 게시글 작성 시 임시저장된 게시글 목록을 보여주고 선택 또는 신규를 고르는 팝업 추가

# Bug Found
- 한글 태그시 조회가 안됨

---

# front 폴더 정리

- views : 하나의 화면
    - Layout을 사용. Layout에 전달할 내용만 다름.

- components/layout : 하나의 구조

- components/blog : 블로그에 사용되는 컴포넌트 모음
    - 사용처: PostView, TagsView, CategoriesView

- components/icon : 공통으로 사용되는 아이콘 모음