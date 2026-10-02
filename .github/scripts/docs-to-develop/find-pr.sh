#!/usr/bin/env bash

set -euo pipefail

PR_NUMBER=$(gh pr list \
  --base "$TARGET_BRANCH" \
  --head "$SOURCE_BRANCH" \
  --state open \
  --json number \
  --jq '.[0].number')

echo "PR_NUMBER=$PR_NUMBER" >> "$GITHUB_OUTPUT"

if [ -n "$PR_NUMBER" ]; then
  echo "기존 PR 발견: #$PR_NUMBER"
else
  echo "기존 PR이 없습니다."
fi