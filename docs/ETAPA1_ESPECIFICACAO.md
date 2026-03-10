# ETAPA 1 — Especificação funcional e técnica detalhada

## 1. Visão do produto

**Nome de trabalho:** SecureKioskV2  
**Objetivo:** transformar dispositivos Android corporativos/autorizados em terminais de uso restrito (kiosk), operando exclusivamente com APIs oficiais do Android Enterprise, com foco em segurança, previsibilidade operacional e conformidade legal.

### 1.1 Princípios mandatórios
- Uso **somente de APIs públicas** e suportadas.
- Operação apenas em dispositivos **próprios ou explicitamente autorizados**.
- Transparência total do modo de administração (sem canais ocultos).
- Segurança por camadas (autenticação admin, criptografia, logs locais, validações).
- UX operacional clara para equipes não técnicas.

### 1.2 Cenários suportados
1. **Modo normal (sem privilégios corporativos):** recursos básicos de configuração e monitoramento.
2. **Screen Pinning (usuário):** restrição limitada via funcionalidade nativa do sistema.
3. **Lock Task com app permitido:** restrição avançada quando o app pode iniciar lock task conforme políticas.
4. **Device Owner / COSU (Android Enterprise):** cenário recomendado para restrições robustas e controladas.

---

## 2. Escopo funcional detalhado

## 2.1 Onboarding e setup administrativo

### Funcionalidades
- Cadastro inicial de perfil kiosk:
  - nome do perfil;
  - definição de PIN/senha administrativa;
  - configuração opcional de biometria para desbloqueio administrativo;
  - seleção inicial de app alvo.
- Checklist de conformidade no primeiro uso:
  - status Device Owner;
  - suporte a Lock Task;
  - disponibilidade de biometria;
  - permissões e restrições aplicáveis.

### Regras
- Sem PIN/senha admin definida, modo kiosk **não pode ser ativado**.
- Todos os termos de uso e consentimento devem ser aceitos explicitamente.

## 2.2 Seleção de app alvo e whitelist

### Funcionalidades
- Lista de apps instalados elegíveis (label, ícone, package).
- Busca/filtro por nome/package.
- Seleção de:
  - app principal alvo;
  - apps auxiliares permitidos (whitelist) quando suportado no cenário.

### Regras de elegibilidade
- Ignorar apps sem launcher visível para operador comum (exceto se admin habilitar modo avançado documentado).
- Exibir alerta quando app selecionado não for adequado para lock task/cenário atual.

## 2.3 Dashboard operacional

### Funcionalidades
- Exibir estado atual:
  - kiosk ativo/inativo;
  - app alvo atual;
  - modo de operação detectado (normal/screen pinning/lock task/device owner);
  - última falha operacional.
- Ações rápidas:
  - ativar/desativar kiosk (mediante autenticação admin);
  - lançar app alvo;
  - abrir diagnóstico.

## 2.4 Configurações editáveis

### Funcionalidades
- Perfil kiosk (nome, descrição, operador responsável).
- Auto-launch do app alvo ao iniciar serviço/app.
- Política ao reiniciar dispositivo (quando permitido):
  - relançar kiosk automaticamente;
  - exigir autenticação admin antes de ativar.
- Tela e energia (apenas parâmetros legalmente permitidos pela API e perfil do dispositivo).
- Exportação/importação local de configuração em JSON validado.
- Log administrativo local com filtros.

### Regras
- Importação só com validação de schema + assinatura/hmac local.
- Configuração inválida deve falhar de forma segura (não ativar kiosk até correção).

## 2.5 Acesso administrativo seguro

### Fluxo obrigatório
1. Operador faz gesto explícito de **7 toques** em área visível/documentada.
2. Sistema abre tela de autenticação admin.
3. Autenticação via PIN/senha (obrigatória) + biometria opcional.

### Defesas
- Rate limiting com atraso progressivo.
- Bloqueio temporário após N tentativas falhas.
- Registro de tentativas em log local (sem armazenar segredo).

### Regras de reset
- Redefinição do PIN apenas por fluxo autenticado.
- Sem “senha mestra” ou bypass oculto.

## 2.6 Modo kiosk legítimo

### Funcionalidades
- Inicializar app alvo automaticamente quando kiosk ativo.
- Aplicar Lock Task quando disponível e autorizado.
- Watchdog de sessão do app alvo:
  - detectar fechamento inesperado;
  - relançar com backoff controlado.
- Recuperação segura:
  - rota de saída admin documentada;
  - fallback em caso de erro crítico.

### Restrições
- Nenhum bloqueio fora dos mecanismos oficiais Android.
- Sem interceptação maliciosa de eventos do sistema.

## 2.7 Diagnóstico e observabilidade local

### Campos mínimos
- versão do app;
- versão Android e fabricante/modelo;
- package alvo;
- status Device Owner;
- status Lock Task;
- permissões relevantes;
- último erro;
- timestamp de eventos administrativos.

### Exportação
- Exportar relatório local em JSON/CSV para suporte interno autorizado.
- Sem telemetria invasiva por padrão.

---

## 3. Matriz de conformidade legal/técnica

| Recurso | API oficial | Exige Device Owner | Suporte por versão | Observação legal |
|---|---|---:|---|---|
| Screen Pinning | Sim | Não | Android 5+ | Saída depende do sistema/usuário; restrição limitada |
| Lock Task básico | Sim | Parcial (depende do cenário) | Android 5+ | Para restrição robusta, preferir Device Owner |
| Lock Task com políticas amplas | Sim (DPM) | Sim | Melhor em Android 8+ | Requer provisioning corporativo legítimo |
| Auto-start pós-boot | Sim (BOOT_COMPLETED) | Não/Parcial | Android 5+ | Restrições de background podem impactar comportamento |
| Bloqueio total de configurações do sistema | Parcial | Sim | Varia por OEM/versão | Só o que DPM permitir oficialmente |
| Ajustes de rede do sistema | Limitado | Geralmente sim | Varia | Não implementar bypass de configurações do usuário |
| Controle por acessibilidade | Não para kiosk | Não aplicável | - | **Proibido** para bypass/controle indevido |

---

## 4. Requisitos não funcionais

## 4.1 Segurança
- Segredos em Android Keystore (chaves) + armazenamento cifrado para metadados sensíveis.
- Hash seguro de PIN/senha (com salt + parâmetros modernos).
- Logs sem dados sensíveis.
- Validação estrita de entradas (UI/import JSON/intents).

## 4.2 Arquitetura e qualidade
- Kotlin + Clean Architecture + MVVM.
- Módulos desacoplados (core, domain, data, app).
- DI com Hilt.
- Persistência com DataStore e Room (logs).
- Testes unitários e instrumentados cobrindo fluxos críticos.

## 4.3 UX operacional
- Material Design 3, alto contraste e acessibilidade.
- Linguagem simples para operador e detalhada para admin.
- Mensagens de erro acionáveis.

## 4.4 Conformidade
- Política de privacidade mínima e transparente.
- Termos de uso com cláusulas de autorização do dispositivo.
- Evidência clara de que o app não contorna proteções do SO.

---

## 5. Modelo de dados inicial (alto nível)

- **KioskProfile**
  - id, nome, appAlvoPackage, whitelistPackages, autoLaunch, comportamentoBoot, createdAt, updatedAt
- **AdminCredentialMetadata**
  - tipoCredencial, hash, salt, tentativasFalhas, bloqueadoAte
- **RuntimeStatus**
  - kioskAtivo, lockTaskAtivo, deviceOwnerAtivo, ultimoErro
- **AuditLogEntry**
  - timestamp, ator (admin/sistema), evento, severidade, detalhesNaoSensiveis

---

## 6. Fluxos principais (resumo técnico)

1. **Setup inicial** → cria perfil + credencial admin + valida pré-condições.
2. **Ativação kiosk** → autentica admin → valida app alvo → entra em lock task quando permitido → inicia watchdog.
3. **Acesso admin (7 toques)** → autenticação → painel protegido.
4. **Falha do app alvo** → captura evento → registra log → relança com backoff.
5. **Import config** → valida assinatura/schema/versão → aplica transacionalmente.

---

## 7. Critérios de aceite da ETAPA 1

- Especificação cobre todos os itens do escopo solicitado.
- Limitações legais/técnicas estão explicitadas sem ambiguidades.
- Dependências de Device Owner estão separadas das funções em modo normal.
- Fluxos de segurança admin e recuperação estão claros e auditáveis.

---

## 8. Análise minuciosa da ETAPA 1 (auto-revisão)

### Verificações executadas
- Cobertura de requisitos funcionais 1–15 solicitados pelo cliente.
- Revisão de aderência a Android Enterprise sem APIs ocultas.
- Revisão de riscos de abuso (backdoor, bypass, persistência indevida).

### Ajustes aplicados após revisão
- Inclusão explícita de bloqueio progressivo contra brute force.
- Reforço de que acessibilidade não será usada para controle indevido.
- Matriz de compatibilidade separando “possível” de “permitido oficialmente”.

### Bugs/inconsistências identificados na especificação
- Nenhum conflito lógico crítico encontrado nesta etapa.
- Ponto de atenção para etapas seguintes: comportamento de auto-start em OEMs com restrições agressivas de bateria deve ser tratado como limitação documentada.

---

## 9. Próximo passo (ETAPA 2)

Na próxima etapa será definida a arquitetura detalhada de módulos, camadas, contratos, fluxos de navegação, modelo de permissões e estratégia de testes para implementação completa.

## Limites legais e técnicos
- Não é permitido nem será implementado qualquer bypass de segurança do Android.
- Sem root/exploit/APIs ocultas/acessibilidade abusiva.
- Sem desativar FRP, Play Protect ou políticas do fabricante.
- Sem senha mestra oculta ou canal remoto secreto.
- Restrições avançadas dependem de provisioning legítimo Device Owner/COSU.
- Em dispositivos pessoais sem gestão corporativa, o nível de bloqueio é naturalmente limitado pelo Android.
