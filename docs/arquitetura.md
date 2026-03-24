# Arquitetura do Projeto

Este projeto segue o padrão Clean Architecture.

## Camadas

- domain → regras de negócio
- application → casos de uso
- ports → contratos
- infrastructure → banco, cache, APIs
- presentation → HTTP

## Regra principal

O domínio NÃO depende de nenhuma outra camada.

## Fluxo

presentation → application → domain → ports → infrastructure
