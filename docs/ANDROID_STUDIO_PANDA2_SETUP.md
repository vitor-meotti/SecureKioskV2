# Setup otimizado para Android Studio Panda 2 (2025.3.2)

## Versões recomendadas neste repositório
- Android Studio: **Panda 2 (2025.3.2)**
- Android Gradle Plugin (AGP): **8.8.2**
- Gradle Wrapper: **8.10.2**
- Kotlin: **1.9.24**
- KSP: **1.9.24-1.0.20**
- Java/JDK: **17**
- compileSdk/targetSdk: **35**

## Passos no Android Studio
1. Abra a pasta raiz do projeto.
2. Confirme JDK 17 em `Settings > Build Tools > Gradle`.
3. Sincronize o projeto com Gradle.
4. Rode os testes locais (`testDebugUnitTest`) pelo painel Gradle.

## Ambientes corporativos com mirror
Se sua rede bloqueia Google Maven/Maven Central:
1. Configure um mirror corporativo autorizado.
2. Exporte:
   ```bash
   export SECUREKIOSK_MAVEN_MIRROR="https://seu-mirror-corporativo/maven"
   ```
3. Valide:
   ```bash
   ./scripts/validate_build_env.sh
   ```

## Checklist rápido de validação
- Wrapper presente (`gradlew`, `gradlew.bat`, `gradle/wrapper/*`).
- `build.gradle.kts` e `app/build.gradle.kts` em versões compatíveis com Panda 2.
- Scripts de auditoria e conformidade executando sem erros de lógica.

## Limites legais e técnicos
- Ajustes de ambiente/build não adicionam qualquer bypass do Android.
- O app mantém restrições apenas por APIs públicas e fluxos documentados.
