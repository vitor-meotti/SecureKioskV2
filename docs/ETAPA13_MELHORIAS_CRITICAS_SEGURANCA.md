# ETAPA 13 — Melhorias críticas de segurança e robustez

## Implementado
- Persistência segura de hash/salt/tentativas/lockout em `EncryptedSharedPreferences`.
- `AdminAuthViewModel` com `@HiltViewModel` e injeção real pelo grafo Hilt.
- Persistência de lockout e contador de falhas, sobrevivendo a reinício do app.
- Chamada efetiva de `startLockTask()` via `KioskLockTaskManager` no ciclo da Activity.
- `PinHasher` alterado para operar com `CharArray` e limpeza de memória após uso.

## Limites legais e técnicos
- Não há bypass de proteção Android.
- Persistência usa API pública oficial de segurança AndroidX.
