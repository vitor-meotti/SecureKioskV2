# ETAPA 6 — Ambiente, pipeline e resolução de dependências

## Objetivo
Eliminar o bloqueio de build causado por indisponibilidade de acesso direto aos repositórios públicos de plugins Android/dependências.

## Melhorias implementadas
- `settings.gradle.kts` atualizado para suportar **mirror corporativo** via variável `SECUREKIOSK_MAVEN_MIRROR`.
- Resolução de plugins explicitada via `resolutionStrategy` para reduzir ambiguidades de artifact.
- Script `scripts/validate_build_env.sh` adicionado para diagnosticar conectividade com repositórios.

## Como usar no ambiente corporativo
1. Disponibilize um repositório proxy/mirror interno que replique Google Maven e Maven Central.
2. Exporte a variável:
   ```bash
   export SECUREKIOSK_MAVEN_MIRROR="https://seu-mirror-corporativo/maven"
   ```
3. Valide conectividade:
   ```bash
   ./scripts/validate_build_env.sh
   ```
4. Execute build/test:
   ```bash
   gradle test --no-daemon
   ```

## Validação minuciosa
- Confirmado que o ambiente atual responde `403` para URLs públicas de repositório.
- Confirmado que o projeto agora permite injetar mirror sem alterar código-fonte adicional.

## Limites legais e técnicos
- Não há qualquer bypass de segurança de rede/sistema.
- A solução segue padrão enterprise: uso de mirror/proxy autorizado.
- Sem uso de mecanismos ocultos para obter dependências.
