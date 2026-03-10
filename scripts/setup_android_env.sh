#!/usr/bin/env bash
set -euo pipefail

export ANDROID_HOME="${ANDROID_HOME:-/usr/lib/android-sdk}"
export ANDROID_SDK_ROOT="${ANDROID_SDK_ROOT:-$ANDROID_HOME}"

if [[ -z "${JAVA_HOME:-}" ]]; then
  JAVA_BIN="$(command -v javac || true)"
  if [[ -n "$JAVA_BIN" ]]; then
    export JAVA_HOME="$(dirname "$(dirname "$JAVA_BIN")")"
  fi
fi

mkdir -p "$ANDROID_HOME"

add_path() {
  local dir="$1"
  if [[ -d "$dir" ]]; then
    case ":$PATH:" in
      *":$dir:"*) ;;
      *) export PATH="$dir:$PATH" ;;
    esac
  fi
}

add_path "$ANDROID_HOME/cmdline-tools/latest/bin"
add_path "$ANDROID_HOME/platform-tools"
add_path "$ANDROID_HOME/build-tools/34.0.0"
add_path "$ANDROID_HOME/tools"

if ! command -v sdkmanager >/dev/null 2>&1; then
  echo "ERRO: sdkmanager não encontrado. Instale cmdline-tools ou use android-actions/setup-android no CI." >&2
  exit 1
fi

yes | sdkmanager --sdk_root="$ANDROID_HOME" --licenses >/dev/null
yes | sdkmanager --sdk_root="$ANDROID_HOME" \
  "platform-tools" \
  "platforms;android-34" \
  "build-tools;34.0.0" \
  "cmdline-tools;latest"

echo "ANDROID_HOME=$ANDROID_HOME"
echo "ANDROID_SDK_ROOT=$ANDROID_SDK_ROOT"
echo "JAVA_HOME=${JAVA_HOME:-not-set}"

echo "Android toolchain configurada com sucesso."
