Smart Business

Plataforma SaaS de gestão empresarial voltada para micro e pequenas empresas (MPEs), desenvolvida no contexto do curso de Engenharia de Computação.

---

Descrição

O Smart Business é um sistema web que centraliza as principais operações administrativas de um negócio em um único ambiente digital. A plataforma foi projetada para resolver problemas recorrentes enfrentados por microempreendedores, como controle financeiro ineficiente, gestão manual de estoque e falta de visibilidade sobre o desempenho da empresa.

A solução permite organizar, automatizar e monitorar atividades essenciais, contribuindo para a tomada de decisão e sustentabilidade do negócio. Estudos indicam que uma parcela significativa das micro e pequenas empresas encerra suas atividades por falhas na gestão, cenário que o sistema busca mitigar .

---

Objetivo

Desenvolver uma plataforma SaaS que:

- Centralize a gestão empresarial
- Automatize processos operacionais
- Forneça indicadores para tomada de decisão
- Reduza a complexidade da gestão para pequenos negócios

---

Funcionalidades principais

- Cadastro e gestão de clientes
- Controle de produtos e estoque
- Registro de vendas (PDV)
- Controle financeiro (receitas e despesas)
- Dashboard com indicadores de desempenho
- Relatórios gerenciais
- Alertas de estoque baixo
- Precificação assistida
- Emissão de notas fiscais (NF-e)

O sistema cobre as principais necessidades operacionais de micro e pequenas empresas, funcionando como um ERP simplificado .

---

Arquitetura

O sistema segue uma arquitetura em camadas com separação clara de responsabilidades:

- Frontend: React + TypeScript
- Backend: Rust + Axum
- Banco de dados: PostgreSQL
- Cache: Redis
- Infraestrutura: Docker + Nginx

A aplicação adota o modelo SaaS multi-tenant, garantindo isolamento lógico dos dados por empresa e permitindo escalabilidade horizontal .

---

Tecnologias

Frontend

- React
- TypeScript
- Tailwind CSS
- Zustand
- Recharts

Backend

- Rust
- Axum
- SQLx
- Tokio

Banco de dados e cache

- PostgreSQL
- Redis

Infraestrutura

- Docker
- Docker Compose
- Nginx
- GitHub Actions

Segurança

- JWT (autenticação)
- Argon2id (hash de senha)
- HTTPS (SSL)

---

Fluxo básico do sistema

1. Uma venda é registrada no sistema
2. O estoque é atualizado automaticamente
3. A movimentação financeira é registrada
4. O dashboard exibe indicadores atualizados

Esse fluxo garante integração entre os módulos do sistema .

---

Público-alvo

- Microempreendedores individuais (MEI)
- Pequenos comércios
- Prestadores de serviço
- Restaurantes e negócios locais

---

Estrutura do projeto

/backend
/frontend
/docs
/docker

---

Status

Em desenvolvimento. MVP em construção conforme roadmap definido no projeto.

---

Licença

Este projeto está licenciado sob a MIT License.

---

Equipe

Projeto desenvolvido por equipe de Engenharia de Computação (UNIVESP).

---
