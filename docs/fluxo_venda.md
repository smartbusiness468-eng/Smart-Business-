# Fluxo: Criar Venda

## Visão geral

Este fluxo representa a criação de uma venda no sistema.

---

## Etapas

1. Requisição HTTP (futuro)
2. Handler recebe os dados
3. Handler chama o use case
4. Use case cria a entidade
5. Entidade valida regras
6. Use case chama o repositório
7. Repositório salva no banco

---

## Fluxo simplificado

request → handler → use case → entidade → repositório → banco

---

## Estado atual

Atualmente o fluxo está implementado até:

use case → repositório (mock)

Ainda não há:
- banco de dados
- API HTTP

---

## Possíveis melhorias

- adicionar itens na venda
- calcular total automaticamente
- aplicar descontos
- vincular cliente
- registrar data/hora
