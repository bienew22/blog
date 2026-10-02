#!/usr/bin/env bash

set -euo pipefail

PR_NUMBER="$1"

echo "PR #$PR_NUMBER 자동 Merge 설정"

gh pr merge \
  "$PR_NUMBER" \
  --auto \
  --merge