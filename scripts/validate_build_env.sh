#!/usr/bin/env bash
set -euo pipefail

check_url() {
  local url="$1"
  local code
  code=$(curl -s -o /dev/null -w "%{http_code}" "$url" || true)
  echo "$code"
}

ANDROID_PLUGIN_URL="https://dl.google.com/dl/android/maven2/com/android/tools/build/gradle/8.4.2/gradle-8.4.2.pom"
MAVEN_CENTRAL_URL="https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.pom"

android_code=$(check_url "$ANDROID_PLUGIN_URL")
central_code=$(check_url "$MAVEN_CENTRAL_URL")

echo "Android Maven status: $android_code"
echo "Maven Central status: $central_code"

if [[ "$android_code" != "200" || "$central_code" != "200" ]]; then
  echo "Ambiente com bloqueio de repositórios externos."
  echo "Defina SECUREKIOSK_MAVEN_MIRROR com um mirror corporativo acessível."
  exit 2
fi

echo "Ambiente apto para resolver plugins/dependências."
