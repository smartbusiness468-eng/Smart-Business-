# Rust no Projeto

## Result

Usamos Result para tratamento de erro:

Result<T, ErroDominio>

## Traits

Traits são usados como interfaces:

trait VendaRepositorio

## Box<dyn>

Usado para injeção de dependência:

Box<dyn VendaRepositorio>

Permite trocar implementação em runtime.
