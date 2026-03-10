#!/usr/bin/env bash
set -euo pipefail

DEFAULT_ANDROID_SDK_DIR="$HOME/android-sdk"
ANDROID_HOME_WAS_SET=${ANDROID_HOME+x}
ANDROID_SDK_ROOT_WAS_SET=${ANDROID_SDK_ROOT+x}
export ANDROID_HOME="${ANDROID_HOME:-$DEFAULT_ANDROID_SDK_DIR}"
export ANDROID_SDK_ROOT="${ANDROID_SDK_ROOT:-$ANDROID_HOME}"

if [[ -z "${JAVA_HOME:-}" ]]; then
  JAVA_BIN="$(command -v javac || true)"
  if [[ -n "$JAVA_BIN" ]]; then
    export JAVA_HOME="$(dirname "$(dirname "$JAVA_BIN")")"
  fi
fi

ensure_sdk_root_writable() {
  if mkdir -p "$ANDROID_HOME" 2>/dev/null; then
    return
  fi

  if [[ -n "${ANDROID_HOME_WAS_SET:-}" || -n "${ANDROID_SDK_ROOT_WAS_SET:-}" ]]; then
    echo "ERRO: sem permissão para criar ANDROID_HOME em $ANDROID_HOME. Defina um diretório gravável." >&2
    exit 1
  fi

  echo "AVISO: sem permissão em $ANDROID_HOME. Usando fallback em $DEFAULT_ANDROID_SDK_DIR" >&2
  export ANDROID_HOME="$DEFAULT_ANDROID_SDK_DIR"
  export ANDROID_SDK_ROOT="$DEFAULT_ANDROID_SDK_DIR"
  mkdir -p "$ANDROID_HOME"
}

ensure_sdk_root_writable

ensure_java_compatible() {
  if ! command -v java >/dev/null 2>&1; then
    echo "ERRO: java não encontrado no PATH." >&2
    exit 1
  fi

  local version_raw major
  version_raw="$(java -version 2>&1 | awk -F\" '/version/{print $2; exit}')"
  major="$(echo "$version_raw" | cut -d. -f1)"

  if [[ -z "$major" ]]; then
    echo "ERRO: não foi possível identificar versão do Java." >&2
    exit 1
  fi

  if (( major < 17 || major > 21 )); then
    echo "ERRO: Java $version_raw incompatível. Use JDK 17-21 para AGP/Kotlin deste projeto." >&2
    exit 1
  fi
}

add_path() {
  local dir="$1"
  if [[ -d "$dir" ]]; then
    case ":$PATH:" in
      *":$dir:"*) ;;
      *) export PATH="$dir:$PATH" ;;
    esac
  fi
}

bootstrap_sdkmanager_from_mirror() {
  if [[ -z "${ANDROID_SDK_MIRROR_URL:-}" ]]; then
    return 1
  fi

  if ! command -v curl >/dev/null 2>&1 || ! command -v unzip >/dev/null 2>&1; then
    echo "ERRO: curl/unzip são necessários para bootstrap via ANDROID_SDK_MIRROR_URL." >&2
    return 1
  fi

  local mirror_url="${ANDROID_SDK_MIRROR_URL%/}"
  local cmdline_zip_url
  if [[ "$mirror_url" == *.zip ]]; then
    cmdline_zip_url="$mirror_url"
  else
    cmdline_zip_url="$mirror_url/commandlinetools-linux-11076708_latest.zip"
  fi

  local tmp_dir
  tmp_dir="$(mktemp -d)"
  trap 'rm -rf "$tmp_dir"' RETURN

  echo "Bootstrapping cmdline-tools from mirror: $cmdline_zip_url"
  curl -fsSL "$cmdline_zip_url" -o "$tmp_dir/cmdline-tools.zip"
  unzip -q -o "$tmp_dir/cmdline-tools.zip" -d "$tmp_dir/extracted"

  mkdir -p "$ANDROID_HOME/cmdline-tools/latest"
  cp -r "$tmp_dir/extracted/cmdline-tools"/* "$ANDROID_HOME/cmdline-tools/latest/"

  add_path "$ANDROID_HOME/cmdline-tools/latest/bin"
}

ensure_java_compatible

normalize_cmdline_tools_layout() {
  local latest_dir="$ANDROID_HOME/cmdline-tools/latest"
  local alt_latest_dir="$ANDROID_HOME/cmdline-tools/latest-2"

  if [[ -d "$alt_latest_dir" ]]; then
    rm -rf "$latest_dir"
    mv "$alt_latest_dir" "$latest_dir"
    add_path "$latest_dir/bin"
  fi
}

add_path "$ANDROID_HOME/cmdline-tools/latest/bin"
add_path "$ANDROID_HOME/platform-tools"
add_path "$ANDROID_HOME/build-tools/35.0.0"
add_path "$ANDROID_HOME/tools"

if ! command -v sdkmanager >/dev/null 2>&1; then
  bootstrap_sdkmanager_from_mirror || {
    echo "ERRO: sdkmanager não encontrado. Defina ANDROID_SDK_MIRROR_URL ou use android-actions/setup-android no CI." >&2
    exit 1
  }
fi

run_sdkmanager_with_auto_yes() {
  local -a args=("$@")
  set +o pipefail
  yes | sdkmanager "${args[@]}"
  local sdk_status=$?
  set -o pipefail
  if [[ $sdk_status -ne 0 ]]; then
    echo "ERRO: sdkmanager falhou com código $sdk_status para args: ${args[*]}" >&2
    exit "$sdk_status"
  fi
}

run_sdkmanager_with_auto_yes --sdk_root="$ANDROID_HOME" --licenses >/dev/null
run_sdkmanager_with_auto_yes --sdk_root="$ANDROID_HOME" \
  "platform-tools" \
  "platforms;android-35" \
  "build-tools;35.0.0" \
  "cmdline-tools;latest"

normalize_cmdline_tools_layout

echo "ANDROID_HOME=$ANDROID_HOME"
echo "ANDROID_SDK_ROOT=$ANDROID_SDK_ROOT"
echo "JAVA_HOME=${JAVA_HOME:-not-set}"
echo "ANDROID_SDK_MIRROR_URL=${ANDROID_SDK_MIRROR_URL:-not-set}"

echo "Android toolchain configurada com sucesso."
