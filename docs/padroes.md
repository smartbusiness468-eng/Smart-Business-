# Padrões do Projeto

## Arquitetura

Seguimos Clean Architecture.

Camadas:

- domain → regras de negócio
- application → casos de uso
- ports → contratos (interfaces)
- infrastructure → banco, cache, APIs
- presentation → HTTP

---

## Regras obrigatórias

### 1. Domínio não depende de nada

NÃO pode importar:
- banco
- framework
- HTTP

---

### 2. Use cases não conhecem tecnologia

Devem usar apenas traits (interfaces)

---

### 3. Infra implementa ports

Exemplo:

VendaRepositorio → VendaRepositorioPostgres

---

### 4. Controllers não têm regra de negócio

Apenas recebem request e chamam use case

---

## Nomeação

- Entidades: Venda, Produto
- Use cases: CriarVenda, AtualizarEstoque
- Traits: VendaRepositorio
- Implementações: VendaRepositorioPostgres

---

## Tratamento de erro

Sempre usar:

Result<T, ErroDominio>

---

## Comentários

Devem explicar:
- regras de negócio
- decisões
- possíveis melhorias

Evitar comentar o óbvio
