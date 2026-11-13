# TODO (2026-11-09 ~ 2026-11-15)
- Markdown to HTML V2 개발
- 구조 리팩토링
- 표(테이블) 추가

# TODO
- ci-cd : 배포용 docker-compose 구축 (연기 : 개인 휴식)
- 관리자 페이지
- markdown to html 컴포넌트 추가 구현
    - 수평선, 개행, 인용, 사진
- 에러 처리 획일화
- id 키 생성 규칙 변경 : 10진수 기준
    - YYYYMMDDHHMMSS * 100000 + {SEQ:0 ~ 99} * 1000 + {RAND:0 ~ 999}
    - 수용가능 요청 : 9222년까지 사용 가능, 초당 100 건의 생성 요청 수용 가능 
- WriteTab.vue : 태그 입력 부분 고도화
    1. 정렬 수정, 선택된 태그는 최하단으로 이동
    2. 입력 후 enter시 조회된 목록 중 첫 번째 태그를 자동으로 선택
- 태그 목록 조회 부분을 카테고리 처럼 분리 (백엔드)
    - 게시글 작성에서 태그별 게시글 개수 값을 사용하지 않고 있음.
- 각 조회의 정렬은 백에서 수행 할 것
- Markdown converter에서 `___some___`인 경우에는 `UnderLine: Text= _some`으로 파싱됨.
    - UnderLine 안에 Italic 또는 Italic 안에 UnderLine 추가되게 하기.
---

# front 폴더 정리

- views : 하나의 화면
    - Layout을 사용. Layout에 전달할 내용만 다름.

- components/layout : 하나의 구조

- components/blog : 블로그에 사용되는 컴포넌트 모음
    - 사용처: PostView, TagsView, CategoriesView

- components/icon : 공통으로 사용되는 아이콘 모음