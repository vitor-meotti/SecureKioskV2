#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT_DIR"

FILES=(
  ".github/workflows/android-ci.yml"
  ".gitignore"
  "README.md"
  "app/build.gradle.kts"
  "app/src/main/AndroidManifest.xml"
  "app/src/main/java/com/securekioskv2/app/AppSessionViewModel.kt"
  "app/src/main/java/com/securekioskv2/app/MainActivity.kt"
  "app/src/main/java/com/securekioskv2/core/security/BruteForceGuard.kt"
  "app/src/main/java/com/securekioskv2/core/security/PinHasher.kt"
  "app/src/main/java/com/securekioskv2/feature/adminauth/AdminAuthScreen.kt"
  "app/src/main/java/com/securekioskv2/feature/adminauth/AdminAuthState.kt"
  "app/src/main/java/com/securekioskv2/feature/adminauth/AdminAuthViewModel.kt"
  "app/src/main/java/com/securekioskv2/feature/dashboard/DashboardScreen.kt"
  "app/src/main/java/com/securekioskv2/feature/diagnostics/DiagnosticsScreen.kt"
  "app/src/main/java/com/securekioskv2/feature/onboarding/OnboardingScreen.kt"
  "app/src/main/java/com/securekioskv2/ui/theme/Theme.kt"
  "app/src/test/java/com/securekioskv2/core/security/BruteForceGuardTest.kt"
  "app/src/test/java/com/securekioskv2/core/security/PinHasherTest.kt"
  "app/src/test/java/com/securekioskv2/feature/adminauth/AdminAuthViewModelTest.kt"
  "gradle.properties"
  "settings.gradle.kts"
)

for f in "${FILES[@]}"; do
  if [[ ! -f "$f" ]]; then
    echo "FALHA: arquivo esperado ausente: $f"
    exit 2
  fi
done

if rg -n "^(<<<<<<<|=======|>>>>>>>)" "${FILES[@]}" >/tmp/conflict_hits.txt; then
  echo "FALHA: marcadores de conflito encontrados:"
  cat /tmp/conflict_hits.txt
  exit 1
fi

echo "OK: sem marcadores de conflito nos arquivos críticos."
