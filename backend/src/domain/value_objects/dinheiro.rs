use crate::domain::erros::erro_dominio::ErroDominio;

/// Value Object que representa um valor monetário.
///
/// Responsabilidade:
/// - Garantir que valores financeiros sejam válidos
/// - Evitar uso direto de f64 no domínio
///
/// Regras:
/// - Valor deve ser maior que zero
///
/// Possíveis melhorias:
/// - Suporte a moeda (BRL, USD)
/// - Precisão com Decimal (evitar problemas de ponto flutuante)
#[derive(Debug, Clone, Copy)]
pub struct Dinheiro(f64);

impl Dinheiro {
    /// Cria um novo valor monetário validado
    pub fn novo(valor: f64) -> Result<Self, ErroDominio> {
        if valor <= 0.0 {
            return Err(ErroDominio::ValorInvalido);
        }

        Ok(Self(valor))
    }

    /// Retorna o valor interno
    pub fn valor(&self) -> f64 {
        self.0
    }
}
