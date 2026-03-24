use crate::domain::entidades::venda::Venda;
use crate::domain::erros::erro_dominio::ErroDominio;

pub trait VendaRepositorio {
    fn salvar(&self, venda: Venda) -> Result<(), ErroDominio>;
}
