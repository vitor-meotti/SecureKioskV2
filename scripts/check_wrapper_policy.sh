#!/usr/bin/env bash
set -euo pipefail

if git check-ignore -q gradle/wrapper/gradle-wrapper.jar; then
  echo "FALHA: gradle-wrapper.jar está sendo ignorado pelo .gitignore."
  exit 1
fi

if [[ ! -f gradle/wrapper/gradle-wrapper.jar ]]; then
  echo "FALHA: gradle-wrapper.jar ausente."
  exit 1
fi

echo "OK: gradle-wrapper.jar versionado e não ignorado."
