# TODO (2026-11-02 ~ 2026-11-08)
- 태그 생성 시 팝업 추가, 한 줄 설명 필수 입력 (완)
- 카테고리 추가 : 디자인 선정 및 db 설계 -> 삭제 고려 중 (완)
    - 다시 설계 필요

# TODO
- ci-cd : 배포용 docker-compose 구축 (연기 : 개인 휴식)
- 관리자 페이지
- markdown to html 컴포넌트 추가 구현
    - 수평선, 개행, 표, 인용, 사진
- 에러 처리 획일화
- id 키 생성 규칙 변경 : 10진수 기준
    - YYYYMMDDHHMMSS * 100000 + {SEQ:0 ~ 99} * 1000 + {RAND:0 ~ 999}
    - 수용가능 요청 : 9222년까지 사용 가능, 초당 100 건의 생성 요청 수용 가능 
- WriteTab.vue : 태그 입력 부분 고도화
    1. 정렬 수정, 선택된 태그는 최하단으로 이동
    2. 입력 후 enter시 조회된 목록 중 첫 번째 태그를 자동으로 선택
- 태그 목록 조회 부분을 카테고리 처럼 분리
- 각 조회의 정렬은 백에서 수행 할 것
---

# front 폴더 정리

- views : 하나의 화면
    - Layout을 사용. Layout에 전달할 내용만 다름.

- components/layout : 하나의 구조

- components/blog : 블로그에 사용되는 컴포넌트 모음
    - 사용처: PostView, TagsView, CategoriesView

- components/icon : 공통으로 사용되는 아이콘 모음