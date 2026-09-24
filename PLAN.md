# TODO
- 백엔드으로 부터 게시글 목록 데이터 조회하기
- Tags 화면 구현

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
    - 

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

--- 

# 폴더 정리

- views : 하나의 화면
    - Layout을 사용. Layout에 전달할 내용만 다름.

- components/layout : 하나의 구조

- components/blog : 블로그에 사용되는 컴포넌트 모음
    - 사용처: PostView, TagsView, CategoriesView

- components/icon : 공통으로 사용되는 아이콘 모음