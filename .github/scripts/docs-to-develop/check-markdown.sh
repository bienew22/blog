#!/usr/bin/env bash

set -euo pipefail

echo "=== ${SOURCE_BRANCH} → ${TARGET_BRANCH} 변경 파일 ==="

CHANGED_FILES=$(git diff \
  --name-only \
  "origin/${TARGET_BRANCH}...origin/${SOURCE_BRANCH}")

echo "$CHANGED_FILES"

echo ""

# 변경사항 없음
if [ -z "$CHANGED_FILES" ]; then
  echo "변경사항이 없습니다."
  echo "has_changes=false" >> "$GITHUB_OUTPUT"
  exit 0
fi

# .md가 아닌 파일 검색
INVALID_FILES=$(echo "$CHANGED_FILES" | grep -v '\.md$' || true)

if [ -n "$INVALID_FILES" ]; then
  echo "❌ .md 이외의 파일이 포함되어 있습니다."
  echo ""
  echo "$INVALID_FILES"

  exit 1
fi

echo "has_changes=true" >> "$GITHUB_OUTPUT"

echo ""
echo "✅ 모든 변경 파일이 .md입니다."