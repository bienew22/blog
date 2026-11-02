# TODO (2026-11-02 ~ 2026-11-08)
- 로그인 화면 구현
- 태그 생성 시 팝업 추가, 한 줄 설명 필수 입력
- 카테고리 추가 : 디자인 선정 및 db 설계 -> 삭제 고려 중
    - 다시 설계 필요

# TODO
- ci-cd : 배포용 docker-compose 구축 (연기 : 개인 휴식)
- 관리자 페이지
- markdown to html 컴포넌트 추가 구현
    - 수평선, 개행, 표, 인용, 사진
- 에러 처리 획일화
---

# front 폴더 정리

- views : 하나의 화면
    - Layout을 사용. Layout에 전달할 내용만 다름.

- components/layout : 하나의 구조

- components/blog : 블로그에 사용되는 컴포넌트 모음
    - 사용처: PostView, TagsView, CategoriesView

- components/icon : 공통으로 사용되는 아이콘 모음