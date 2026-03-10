#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT_DIR"

required_docs=(
  "docs/ETAPA1_ESPECIFICACAO.md"
  "docs/ETAPA2_ARQUITETURA.md"
  "docs/ETAPA3_IMPLEMENTACAO.md"
  "docs/ETAPA4_TESTES_E_HARDENING.md"
  "docs/ETAPA5_REVISAO_E_MELHORIAS.md"
  "docs/ETAPA6_AMBIENTE_E_PIPELINE.md"
  "docs/ETAPA7_REVISAO_FINAL_CODIGO.md"
  "docs/ETAPA8_AUDITORIA_TRANSVERSAL.md"
  "docs/ETAPA9_PANDA2_OTIMIZACAO.md"
  "docs/ETAPA10_TESTES_MINUCIOSOS_E_CORRECOES.md"
  "docs/ETAPA11_CORRECAO_PR_BINARIOS.md"
)

for doc in "${required_docs[@]}"; do
  if [[ ! -f "$doc" ]]; then
    echo "FALHA: documentação ausente: $doc"
    exit 1
  fi
done

echo "OK: documentação das etapas 1-11 presente."

if rg -n "123456|senha mestra|backdoor|bypass oculto" app/src/main >/tmp/stage_audit_hits.txt; then
  echo "FALHA: padrões proibidos encontrados em código principal:"
  cat /tmp/stage_audit_hits.txt
  exit 1
fi

echo "OK: nenhum padrão proibido encontrado em app/src/main."

if ! rg -n "Limites legais e técnicos" docs/ETAPA*.md >/tmp/stage_audit_limits.txt; then
  echo "FALHA: seção 'Limites legais e técnicos' ausente em um ou mais documentos de etapa."
  exit 1
fi

echo "OK: seção de limites legais/técnicos encontrada nas etapas."

echo "AUDITORIA DE ETAPAS CONCLUÍDA"
