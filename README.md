# SecureKioskV2

Baseline Android kiosk com foco em segurança, compliance e uso legítimo.

## Melhorias críticas implementadas
1. Persistência segura de credencial admin e lockout com EncryptedSharedPreferences.
2. Injeção Hilt completa nos ViewModels e telas via `hiltViewModel()`.
3. Lockout persistente entre reinícios.
4. Ativação de Lock Task no `onResume` quando permitido oficialmente.
5. Fluxo de PIN usando `CharArray` na autenticação/configuração e limpeza em memória no hasher.

## Limites legais e técnicos
- Somente APIs oficiais Android.
- Sem root/exploit/API oculta/bypass.


## Revisão contínua
- ETAPA 14 concluída: análise crítica robusta, aperfeiçoamento e validação de segurança/lockout/lock-task.
