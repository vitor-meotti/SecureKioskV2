# ETAPA 4 — Testes e hardening inicial

## Implementações desta etapa
- Fluxo de autenticação admin dedicado (`AdminAuthScreen` + `AdminAuthViewModel`).
- Hash seguro de PIN com PBKDF2 (`PinHasher`).
- Integração do fluxo 7 toques com autenticação obrigatória no dashboard.
- Testes unitários adicionais para hashing e autenticação.
- Ajuste de tema para modo claro/escuro automático.

## Validação minuciosa
### Verificações realizadas
- Garantia de ausência de backdoor: o acesso admin só ocorre com autenticação explícita.
- Garantia de que segredos não são persistidos em texto puro na lógica implementada.
- Revisão da UX de bloqueio temporário para brute force.

### Bugs corrigidos
- Corrigido comportamento anterior de detecção de 7 toques sem fluxo completo de autenticação.
- Corrigido tema fixo em modo claro.

### Limitações atuais
- Armazenamento seguro persistente do PIN ainda depende de integração com Keystore/DataStore nas próximas etapas.
- Device Owner / Lock Task avançado segue para ETAPA 5 em diante.

## Limites legais e técnicos
- Não há uso de API oculta, root ou bypass de proteção Android.
- Não há mecanismo secreto de acesso administrativo.
- Capacidades de restrição avançada continuam dependentes de Device Owner legítimo.


## Correções adicionais pós-revisão
- Removido PIN hardcoded de desenvolvimento.
- Removido botão de limpeza de bloqueio que poderia ser interpretado como bypass operacional.
- Setup inicial passa a exigir configuração explícita de PIN administrativo.
