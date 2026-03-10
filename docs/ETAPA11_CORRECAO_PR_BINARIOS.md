# ETAPA 11 — Correção de falha de Pull Request por binários

## Problema identificado
- O fluxo de criação de PR falhava com a mensagem: **"Arquivos binários não são compatíveis"**.
- Causa principal: inclusão de `gradle/wrapper/gradle-wrapper.jar` no commit.

## Correção aplicada
- Remoção do arquivo binário `gradle/wrapper/gradle-wrapper.jar` do repositório.
- Inclusão do script `scripts/bootstrap_wrapper.sh` para regenerar wrapper localmente de forma reprodutível.
- Manutenção de `gradle/wrapper/gradle-wrapper.properties` com versão pinada (`8.10.2`).

## Como recuperar wrapper local
```bash
./scripts/bootstrap_wrapper.sh
```

## Validação
- `scripts/audit_stages.sh` executado para manter consistência das etapas.
- Verificação de rastreamento Git confirmando remoção do binário.

## Limites legais e técnicos
- Não houve alteração de comportamento de segurança do app.
- Correção limitada à compatibilidade de PR e manutenção de build tooling.
