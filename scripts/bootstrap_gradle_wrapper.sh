#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
WRAPPER_JAR="$ROOT_DIR/gradle/wrapper/gradle-wrapper.jar"
WRAPPER_PROPERTIES="$ROOT_DIR/gradle/wrapper/gradle-wrapper.properties"

if [[ -f "$WRAPPER_JAR" ]]; then
  echo "Gradle wrapper jar já existe em $WRAPPER_JAR"
  exit 0
fi

if [[ ! -f "$WRAPPER_PROPERTIES" ]]; then
  echo "ERRO: arquivo $WRAPPER_PROPERTIES não encontrado." >&2
  exit 1
fi

distribution_url="$(awk -F= '/^distributionUrl=/{print $2}' "$WRAPPER_PROPERTIES" | sed 's#\\:#:#g')"
version="$(basename "$distribution_url" | sed -E 's/gradle-([0-9.]+)-bin\.zip/\1/')"

if [[ -z "$version" ]]; then
  echo "ERRO: não foi possível derivar versão do Gradle a partir de $distribution_url" >&2
  exit 1
fi

TMP_DIR="$(mktemp -d)"
trap 'rm -rf "$TMP_DIR"' EXIT

if command -v gradle >/dev/null 2>&1; then
  echo "Gerando gradle-wrapper.jar com Gradle local..."
  if gradle -p "$ROOT_DIR" wrapper --gradle-version "$version" >/dev/null 2>&1 && [[ -f "$WRAPPER_JAR" ]]; then
    echo "Gradle wrapper jar gerado via gradle local em $WRAPPER_JAR"
    exit 0
  fi
  echo "AVISO: falha ao gerar wrapper via gradle local; tentando download..." >&2
fi

if [[ -n "${GRADLE_WRAPPER_JAR_URL:-}" ]]; then
  download_url="$GRADLE_WRAPPER_JAR_URL"
else
  download_url="https://raw.githubusercontent.com/gradle/gradle/v${version}/gradle/wrapper/gradle-wrapper.jar"
fi

echo "Baixando gradle-wrapper.jar de: $download_url"
if ! curl -fsSL "$download_url" -o "$TMP_DIR/gradle-wrapper.jar"; then
  echo "ERRO: falha ao baixar gradle-wrapper.jar. Configure GRADLE_WRAPPER_JAR_URL apontando para um mirror interno." >&2
  exit 1
fi

if ! unzip -p "$TMP_DIR/gradle-wrapper.jar" META-INF/MANIFEST.MF 2>/dev/null | grep -q '^Main-Class: org.gradle.wrapper.GradleWrapperMain'; then
  echo "ERRO: o gradle-wrapper.jar baixado é inválido (sem Main-Class do Wrapper)." >&2
  echo "Defina GRADLE_WRAPPER_JAR_URL para um artefato válido do seu mirror interno." >&2
  exit 1
fi

install -m 0644 "$TMP_DIR/gradle-wrapper.jar" "$WRAPPER_JAR"
echo "Gradle wrapper jar instalado em $WRAPPER_JAR"
