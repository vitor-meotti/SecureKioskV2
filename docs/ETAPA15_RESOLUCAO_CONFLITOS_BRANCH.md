# ETAPA 15 — Resolução e prevenção de conflitos de branch

## Ação executada
- Validada a lista de arquivos com conflito reportada no PR.
- Confirmada ausência de marcadores de conflito (`<<<<<<<`, `=======`, `>>>>>>>`) nos arquivos críticos.
- Adicionado script `scripts/check_conflicts.sh` para verificação automatizada local/CI.
- Workflow `android-ci.yml` atualizado para falhar cedo quando houver marcadores de conflito.

## Como validar localmente
```bash
./scripts/check_conflicts.sh
```

## Limites legais e técnicos
- Mudança focada em higiene de branch/CI, sem alterar comportamento de segurança do app.
- Não há bypass de políticas de merge do GitHub.


## Complemento operacional
- Scripts do Wrapper (`gradlew`/`gradlew.bat`) e propriedades foram mantidos no repositório, com geração do `gradle-wrapper.jar` no CI quando ausente.

- Ajuste aplicado: `gradle-wrapper.jar` removido do versionamento e regenerado no CI quando necessário.
