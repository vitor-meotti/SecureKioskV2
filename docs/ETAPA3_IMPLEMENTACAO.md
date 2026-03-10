# ETAPA 3 — Implementação completa (baseline funcional)

## Entrega realizada nesta etapa
Foi implementada a baseline compilável do app Android com:
- configuração Gradle Kotlin DSL;
- app Compose com navegação inicial;
- telas base: onboarding, dashboard, diagnóstico;
- estrutura inicial de domínio/modelos;
- componente de segurança `BruteForceGuard` com testes unitários;
- manifest com parâmetros de segurança mínimos e sem permissões abusivas.

## Itens implementados
1. **Projeto Android inicial** (`settings.gradle.kts`, `build.gradle.kts`, `app/build.gradle.kts`).
2. **App shell + navegação** (`MainActivity`, `NavHost`).
3. **Fluxo de 7 toques** em estado inicial no dashboard (detecção explícita).
4. **Camada de segurança inicial** para atraso progressivo e bloqueio temporário.
5. **Teste unitário real** cobrindo regras de brute force.

## Limitações conhecidas desta implementação
- Lock Task / Device Owner ainda serão ligados de forma completa na ETAPA 4.
- Persistência DataStore/Room será aprofundada na ETAPA 4.
- Biometria e PIN seguro serão finalizados na ETAPA 4.

## Análise minuciosa da ETAPA 3
### Verificações
- Revisão de uso exclusivo de APIs públicas.
- Revisão de permissões para evitar coleta indevida.
- Revisão de clareza de fluxo admin e não existência de bypass.

### Bugs corrigidos durante a etapa
- Ajuste para evitar bloqueio oculto: mensagem explícita de que autenticação real será exigida na etapa seguinte.
- Ajuste de delay com teto para evitar esperas infinitas.

### Riscos remanescentes
- Necessidade de testes instrumentados quando o pipeline Android estiver configurado no ambiente CI.

## Limites legais e técnicos
- Não há implementação de root/exploit/APIs ocultas.
- Não há canal secreto de administração.
- Recursos avançados de restrição dependem de Device Owner legítimo.


## Revisão complementar (ponte para ETAPA 4)
- Fluxo de 7 toques evoluído para redirecionar obrigatoriamente para autenticação administrativa.
- Inclusão de hashing PBKDF2 para PIN e testes unitários específicos de autenticação.
- Correção de tema para respeitar modo escuro do sistema.
