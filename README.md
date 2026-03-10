# SecureKioskV2

Projeto Android kiosk mode com foco em conformidade legal, segurança e operação corporativa.

## Status atual
- ✅ ETAPA 1 concluída: especificação funcional e técnica detalhada.
- ✅ ETAPA 2 concluída: arquitetura detalhada do projeto.
- ✅ ETAPA 3 concluída: baseline de implementação Android (Compose + segurança inicial + testes unitários).
- ✅ ETAPA 4 concluída: hardening inicial de autenticação admin + novos testes.
- ✅ ETAPA 5 concluída: revisão de segurança, correções e melhorias do fluxo admin.
- ✅ ETAPA 6 concluída: estratégia de ambiente/pipeline para resolução de dependências em rede corporativa.
- ✅ ETAPA 7 concluída: revisão final de código com correções de segurança, estado e testabilidade.
- ✅ ETAPA 8 concluída: auditoria transversal das etapas e validação automática de conformidade.
- ✅ ETAPA 9 concluída: organização e otimização para Android Studio Panda 2 (2025.3.2).
- ✅ ETAPA 10 concluída: testes minuciosos e correção do bug de lockout administrativo.
- ✅ ETAPA 11 concluída: correção de falha de PR por incompatibilidade com arquivo binário.

## Documentação
- [Setup Android Studio Panda 2 (2025.3.2)](docs/ANDROID_STUDIO_PANDA2_SETUP.md)
- [ETAPA 1 — Especificação funcional e técnica](docs/ETAPA1_ESPECIFICACAO.md)
- [ETAPA 2 — Arquitetura do projeto](docs/ETAPA2_ARQUITETURA.md)
- [ETAPA 3 — Implementação baseline](docs/ETAPA3_IMPLEMENTACAO.md)
- [ETAPA 4 — Testes e hardening inicial](docs/ETAPA4_TESTES_E_HARDENING.md)
- [ETAPA 5 — Revisão, correções e melhorias](docs/ETAPA5_REVISAO_E_MELHORIAS.md)
- [ETAPA 6 — Ambiente, pipeline e resolução de dependências](docs/ETAPA6_AMBIENTE_E_PIPELINE.md)
- [ETAPA 7 — Revisão final de código, correções e otimizações](docs/ETAPA7_REVISAO_FINAL_CODIGO.md)
- [ETAPA 8 — Auditoria transversal e validação das etapas](docs/ETAPA8_AUDITORIA_TRANSVERSAL.md)
- [ETAPA 9 — Organização e otimização para Android Studio Panda 2](docs/ETAPA9_PANDA2_OTIMIZACAO.md)
- [ETAPA 10 — Testes minuciosos e correções de bugs](docs/ETAPA10_TESTES_MINUCIOSOS_E_CORRECOES.md)
- [ETAPA 11 — Correção de falha de PR por binários](docs/ETAPA11_CORRECAO_PR_BINARIOS.md)

## Build local
```bash
./gradlew test
```

## Aviso de conformidade
Este projeto implementa somente recursos legais e oficiais do Android para cenários de dispositivos próprios/autorizados.
