#!/usr/bin/env bash

set -euo pipefail

PR_URL=$(gh pr create \
  --base "$TARGET_BRANCH" \
  --head "$SOURCE_BRANCH" \
  --title "${SOURCE_BRANCH} → ${TARGET_BRANCH} 자동 반영" \
  --body "## 자동 생성 PR

\`${SOURCE_BRANCH}\` 브랜치의 Markdown 변경사항을
\`${TARGET_BRANCH}\` 브랜치에 자동 반영합니다.

### 검사 결과

- 변경 파일: \`.md\`만 허용
- 대상 브랜치: \`${TARGET_BRANCH}\`
- 소스 브랜치: \`${SOURCE_BRANCH}\`

이 PR은 GitHub Actions에 의해 자동 생성되었습니다.")

echo "PR_URL=$PR_URL" >> "$GITHUB_OUTPUT"

echo "PR 생성 완료: $PR_URL"