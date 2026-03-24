use crate::domain::erros::erro_dominio::ErroDominio;
use crate::domain::value_objects::dinheiro::Dinheiro;

/// Entidade de domínio que representa uma venda.
///
/// Responsabilidade:
/// - Garantir consistência dos dados de uma venda
/// - Aplicar regras de negócio relacionadas à venda
///
/// Regras atuais:
/// - O valor total deve ser válido (maior que zero)
///
/// Possíveis melhorias:
/// - Adicionar lista de itens (produtos)
/// - Calcular total automaticamente com base nos itens
/// - Aplicar descontos e impostos
/// - Associar cliente à venda
#[derive(Debug, Clone)]
pub struct Venda {
    total: Dinheiro, // agora encapsulado e seguro
}

impl Venda {
    /// Cria uma nova venda validando regras de negócio.
    ///
    /// Validações:
    /// - total > 0 (delegado ao Value Object Dinheiro)
    ///
    /// Possíveis erros:
    /// - ErroDominio::ValorInvalido
    ///
    /// Observação:
    /// - Toda criação deve passar por este método para garantir consistência
    pub fn nova(total: f64) -> Result<Self, ErroDominio> {
        let total = Dinheiro::novo(total)?;

        Ok(Self { total })
    }

    /// Retorna o valor total da venda
    pub fn total(&self) -> f64 {
        self.total.valor()
    }
}
