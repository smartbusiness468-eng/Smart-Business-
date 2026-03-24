# Como Rodar o Projeto

## Pré-requisitos

- Rust instalado (https://rustup.rs)
- Git
- (futuro) Docker e Docker Compose

---

## Clonar o projeto

git clone <url-do-repositorio>
cd smart-business/backend

---

## Rodar o projeto

cargo run

---

## Rodar testes

cargo test

---

## Estrutura atual

O projeto ainda não possui banco de dados configurado.
Atualmente roda apenas lógica de domínio e testes.

---

## Futuro (infraestrutura)

Será necessário:

docker-compose up

Para subir:
- PostgreSQL
- Redis
