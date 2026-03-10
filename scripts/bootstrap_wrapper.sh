#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT_DIR"

if [[ -f "gradle/wrapper/gradle-wrapper.jar" ]]; then
  echo "Wrapper jar já presente."
  exit 0
fi

if ! command -v gradle >/dev/null 2>&1; then
  echo "Gradle não encontrado no PATH. Instale Gradle para gerar o wrapper localmente."
  exit 1
fi

export JAVA_HOME="${JAVA_HOME:-$HOME/.local/share/mise/installs/java/17.0.2}"
export PATH="$JAVA_HOME/bin:$PATH"

gradle wrapper --gradle-version 8.10.2 --no-validate-url

echo "Wrapper gerado com sucesso (incluindo gradle/wrapper/gradle-wrapper.jar)."
