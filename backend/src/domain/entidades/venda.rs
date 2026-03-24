use crate::domain::erros::erro_dominio::ErroDominio;

#[derive(Debug, Clone)]
pub struct Venda {
    pub total: f64,
}

impl Venda {
    pub fn nova(total: f64) -> Result<Self, ErroDominio> {
        if total <= 0.0 {
            return Err(ErroDominio::ValorInvalido);
        }

        Ok(Self { total })
    }
}
