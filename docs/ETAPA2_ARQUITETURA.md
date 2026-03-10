# ETAPA 2 — Arquitetura do projeto

## 1. Objetivo da arquitetura

Definir uma arquitetura Android escalável, auditável e segura para o SecureKioskV2, separando claramente:
- recursos disponíveis em **modo normal**;
- recursos disponíveis com **Screen Pinning**;
- recursos com **Lock Task**;
- recursos avançados dependentes de **Device Owner/COSU**.

A arquitetura foi desenhada para impedir acoplamento indevido entre UI, regras de negócio e APIs de plataforma, facilitando manutenção, testes e evolução.

---

## 2. Decisões arquiteturais principais

## 2.1 Stack técnica
- **Linguagem:** Kotlin.
- **UI:** Jetpack Compose + Material 3.
- **Arquitetura:** Clean Architecture + MVVM.
- **DI:** Hilt.
- **Persistência:** DataStore (configurações) + Room (auditoria/logs).
- **Criptografia:** Android Keystore + criptografia autenticada para segredos.
- **Concorrência:** Coroutines + Flow.
- **Testes:** JUnit, MockK, Turbine, AndroidX Test, Compose UI Test.

## 2.2 Justificativa de Compose
Compose foi escolhido por:
- reduzir boilerplate de UI;
- facilitar estados complexos de dashboard e diagnóstico;
- melhorar testabilidade com semântica declarativa;
- acelerar evolução de telas administrativas com menor acoplamento.

## 2.3 Princípios de design
- **Single source of truth** por feature.
- **Unidirectional data flow** (State + Event + Effect).
- **Fail-safe defaults:** em dúvida, desativar ação de risco.
- **Security by design:** nenhuma funcionalidade administrativa sem autenticação.
- **Compliance by design:** toda restrição deve mapear para API oficial.

---

## 3. Estrutura de módulos proposta

```text
SecureKioskV2/
  app/                         # composição Android, navegação e DI raiz
  core/common/                 # utilitários puros, Result/Either, extensões seguras
  core/security/               # keystore, hashing, validação de segredo, brute-force guard
  core/platform/               # wrappers de APIs Android (DPM, LockTask, PackageManager)
  feature/onboarding/          # setup inicial admin/perfil
  feature/dashboard/           # estado operacional kiosk
  feature/appselection/        # seleção app alvo + whitelist
  feature/settings/            # edição de políticas e export/import
  feature/adminauth/           # fluxo 7 toques + autenticação
  feature/diagnostics/         # status técnico e logs
  data/                        # impls de repositórios, DataStore, Room, serialização JSON
  domain/                      # entidades e casos de uso
  docs/                        # documentação técnica/operacional/legal
```

## 3.1 Regras de dependência
- `feature/*` depende de `domain` e `core/*`, nunca diretamente de `data`.
- `data` implementa contratos definidos em `domain`.
- `app` faz wiring de DI e navegação.
- `core/platform` concentra APIs Android para facilitar mocks e testes.

---

## 4. Camadas e responsabilidades

## 4.1 Camada Domain
### Entidades
- `KioskProfile`
- `AllowedApp`
- `AdminAuthPolicy`
- `RuntimeStatus`
- `DiagnosticSnapshot`
- `AuditLogEntry`

### Use Cases (exemplos)
- `CreateOrUpdateKioskProfileUseCase`
- `GetInstalledEligibleAppsUseCase`
- `ActivateKioskModeUseCase`
- `DeactivateKioskModeUseCase`
- `AuthenticateAdminUseCase`
- `RegisterFailedAttemptUseCase`
- `ExportConfigurationUseCase`
- `ImportConfigurationUseCase`
- `GetDiagnosticSnapshotUseCase`

## 4.2 Camada Data
### Repositórios
- `KioskConfigRepositoryImpl` (DataStore + validação)
- `AdminCredentialRepositoryImpl` (Keystore + metadados)
- `InstalledAppsRepositoryImpl` (PackageManager wrapper)
- `KioskRuntimeRepositoryImpl` (LockTask/DeviceOwner state)
- `AuditLogRepositoryImpl` (Room)

### Persistência
- DataStore Proto para configuração tipada e versionada.
- Room para trilha de auditoria e diagnóstico histórico.

## 4.3 Camada Presentation (Features)
Cada feature possui:
- `UiState`
- `UiEvent`
- `UiEffect` (navegação/toasts/diálogo)
- `ViewModel`
- `Screen` Compose

Padrão de estado recomendado:
- loading inicial explícito;
- erros com mensagens acionáveis;
- bloqueio de ações críticas enquanto autenticação está pendente.

---

## 5. Arquitetura de segurança

## 5.1 Credenciais administrativas
- PIN/senha nunca em texto puro.
- Hash com salt único por instalação + parâmetros robustos.
- Chaves protegidas no Android Keystore.
- Metadados de tentativas em armazenamento local protegido.

## 5.2 Brute-force protection
- Janela de tentativas falhas progressiva.
- Atraso incremental por falha.
- Bloqueio temporário com contagem regressiva visível.
- Auditoria de todas as falhas de autenticação.

## 5.3 Integridade de configuração
- JSON de exportação com versão de schema.
- Assinatura/HMAC local para detectar adulteração.
- Importação transacional (apply-or-rollback).

## 5.4 Logs e privacidade
- Sem registrar segredo, PIN, token, conteúdo sensível.
- Logging por evento com severidade e contexto mínimo.
- Exportação local sob autenticação admin.

---

## 6. Arquitetura de políticas kiosk por modo

## 6.1 Modo normal
**Permitido:**
- configurar perfil;
- selecionar app alvo;
- autenticação admin;
- diagnóstico;
- abrir app alvo manualmente.

**Limitação:**
- sem garantias fortes de retenção no app alvo.

## 6.2 Screen Pinning
**Permitido:**
- orientar usuário a fixar app oficialmente;
- monitorar estado e instruir saída segura.

**Limitação:**
- saída depende do comportamento do sistema e método de desbloqueio.

## 6.3 Lock Task (não-DO e DO)
**Permitido:**
- iniciar lock task quando condição oficial for atendida;
- relançar app alvo em quedas acidentais;
- aplicar políticas limitadas/avançadas conforme papel de administrador.

**Dependência:**
- para políticas robustas, Device Owner é recomendado/necessário.

## 6.4 Device Owner / COSU
**Permitido (API oficial DPM):**
- whitelisting de pacotes lock task;
- políticas adicionais de UX kiosk conforme versão Android;
- inicialização corporativa controlada.

**Limitação:**
- provisioning obrigatório e legítimo antes do uso em produção.

---

## 7. Navegação e fluxos

## 7.1 Grafo principal
- `OnboardingRoute`
- `DashboardRoute`
- `AppSelectionRoute`
- `SettingsRoute`
- `AdminAuthRoute`
- `DiagnosticsRoute`
- `RecoveryRoute`

## 7.2 Guarda de navegação
- Rotas administrativas exigem sessão admin válida.
- Sessão expira por inatividade configurável.
- Reautenticação para ações destrutivas (desativar kiosk, trocar credencial, importar config).

## 7.3 Fluxo “7 toques”
- Área visível marcada em tela kiosk.
- Detector de sequência com janela temporal.
- Ao completar 7 toques: abre `AdminAuthRoute`.
- Se autenticação falhar: aplica política anti-brute-force.

---

## 8. Observabilidade e diagnóstico

## 8.1 DiagnosticSnapshot
Campos:
- versão app;
- build type;
- versão SDK;
- fabricante/modelo;
- package alvo;
- estado lock task;
- estado device owner;
- permissões relevantes;
- último erro normalizado.

## 8.2 Auditoria administrativa
Eventos mínimos:
- ativação/desativação kiosk;
- alterações de configuração;
- tentativas de autenticação;
- import/export de configuração;
- falhas críticas de execução do app alvo.

---

## 9. Estratégia de testes por camada

## 9.1 Unitários (Domain/Core)
- regras de ativação kiosk por cenário.
- validação de credencial e backoff.
- parse/validação de JSON de configuração.

## 9.2 Integração (Data)
- DataStore Proto migrations.
- Room DAO para logs com filtros.
- wrappers de platform com mocks de API level.

## 9.3 UI (Compose)
- onboarding completo;
- autenticação admin com erros;
- fluxo 7 toques;
- dashboard com estados degradados.

## 9.4 Instrumentados
- comportamento em APIs alvo mínimas/recomendadas.
- validação de lock task no cenário compatível.

---

## 10. Pipeline de qualidade (pré-ETAPA 3)

- `./gradlew lint detekt test` em PR.
- `./gradlew connectedAndroidTest` em pipeline dedicado.
- coverage mínimo em domain/core.
- verificação de dependências e licença.

---

## 11. Plano de implementação incremental

1. Base do projeto (Gradle, módulos, DI, navegação).
2. Segurança admin (credencial + brute-force + biometria opcional).
3. Gestão de perfil kiosk e seleção de apps.
4. Runtime kiosk (lock task, watchdog, recuperação).
5. Diagnóstico/logs/exportação.
6. Hardening, testes completos e documentação final.

---

## 12. Análise minuciosa da ETAPA 2 (auto-revisão)

### Verificações realizadas
- Separação explícita entre recursos por modo (normal/pinning/lock task/device owner).
- Revisão de dependências de módulo para evitar acoplamento circular.
- Revisão de superfícies de risco de segurança (auth, logs, import/export).

### Ajustes aplicados
- Inclusão de guardas de navegação por sessão admin.
- Fortalecimento do fluxo de importação com transação e rollback.
- Detalhamento de anti-brute-force com atraso incremental e bloqueio temporário.

### Bugs/inconsistências encontrados
- Nenhum bug arquitetural bloqueante identificado.
- Risco conhecido para ETAPA 3: variações OEM no Lock Task devem ser encapsuladas em camada `core/platform` com fallback e mensagens claras.

---

## 13. Critérios de aceite da ETAPA 2
- Estrutura modular clara e escalável.
- Regras de dependência e responsabilidades definidas.
- Estratégia de segurança e testes rastreável.
- Mapeamento explícito de limitações Android e dependências de Device Owner.

## Limites legais e técnicos
- Não será implementada qualquer técnica de bypass de segurança do Android.
- Sem uso de root, exploit, API oculta ou acessibilidade para controle indevido.
- Restrições “máximas” só dentro do que DPM/Lock Task suportarem oficialmente.
- Device Owner exige provisioning corporativo legítimo; sem isso, capacidades são limitadas.
- Não haverá canal oculto, backdoor ou credencial secreta não documentada.
