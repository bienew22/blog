## blog
my blog

---

## Git 형상관리 프로세스

#### main (운영 브랜치)
- 운영유지 마스터 브랜치
- 권한자에 의해서만 관리 됨.
- develop, ci-cd 브랜치만 PR을 생성할 수 있음.

#### develop
- 개발 브랜치
- main으로 부터 파생됨.
- 배포 대상인 feature 브랜치들의 merge 대상이 됨.
- 향후 : PR 만 허용

#### ci-cd
- CI/CD 작업 브랜치
- main으로 부터 파생됨.
- 관리자에 의해서 관리 됨.
- 변경 사항은 `main` 및 `develop` 브랜치에 적용 됨.

#### feature
- 기능 개발 브랜치
- develop으로 부터 파생되어야 햠.
- 원격 저장소에 해당 브랜치를 `push` 해야함.
- 개발 완료 후에 develop branch에 merge 함. (브랜치는 삭제 X)
- 이름 규칙 "feature/기능 이름"

#### b-feature
- 백엔드 기능 개발 브랜치
- feature으로 부터 파생되어야 함.
- 이는 로컬 브랜치으로 레포지토리에 push X.
- 개발 완료 후에 feature 브랜치에 merge 되어야 함. (no fastforword)

#### f-feature
- 프론트 기능 개발 브랜치
- feature으로 부터 파생되어야 함.
- 이는 로컬 브랜치으로 레포지토레어 push X
- 개발 완료 후에 feature 브랜치에 merge 되어야 함. (no fastforword)

#### docs
- 문서 작업 브랜치
- 변경 사항이 자동으로 develop 브랜치에 자동으로 merge 됨.
- `.md` 파일의 수정만 허용함.