# ETAPA 5 — Revisão, correções e melhorias

## Melhorias implementadas
- Eliminação de credencial administrativa hardcoded.
- Fluxo de onboarding atualizado para configuração explícita de PIN admin.
- Fluxo de autenticação ajustado para depender de credencial configurada.
- Remoção de ação de debug que permitia limpar lockout manualmente.

## Validação minuciosa
### Pontos revisados
- Segurança operacional do acesso administrativo.
- Redução de risco de bypass involuntário em ambiente de produção.
- Clareza de UX para configuração inicial obrigatória.

### Limitações atuais
- `AdminCredentialStore` ainda está em memória (não persistente entre reinícios).
- Persistência em Keystore/DataStore permanece planejada para etapa seguinte.

## Limites legais e técnicos
- Sem mecanismos ocultos de acesso.
- Sem técnicas proibidas (root, exploit, API oculta).
- Todas as melhorias mantêm uso de APIs públicas oficiais.
