# Smart Business

## Objetivo

O Smart Business é um sistema SaaS para gestão de micro e pequenas empresas.

---

## Problema que resolve

Pequenos negócios geralmente:

- não controlam estoque corretamente
- não sabem o lucro real
- fazem precificação errada
- não possuem histórico financeiro

---

## Solução

O sistema oferece:

- controle de vendas
- controle de estoque
- histórico financeiro
- relatórios mensais
- alerta de estoque baixo
- precificação assistida

---

## Público-alvo

- pequenos comércios
- prestadores de serviço
- empreendedores individuais

---

## Tecnologias (planejadas)

- Backend: Rust + Axum
- Frontend: React + TypeScript
- Banco: PostgreSQL
- Cache: Redis

---

## Arquitetura

O projeto segue Clean Architecture.

Isso permite:

- trocar tecnologia sem reescrever regras
- manter código organizado
- escalar o sistema

---

## Estado atual

- domínio implementado
- caso de uso de venda implementado
- testes funcionando
- sem banco de dados ainda

---

## Próximos passos

1. Infraestrutura (PostgreSQL + SQLx)
2. API HTTP (Axum)
3. Autenticação
4. Frontend
