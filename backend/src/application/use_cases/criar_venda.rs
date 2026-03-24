use crate::domain::entidades::venda::Venda;
use crate::domain::erros::erro_dominio::ErroDominio;
use crate::ports::repositorios::venda_repositorio::VendaRepositorio;

pub struct CriarVenda {
    repo: Box<dyn VendaRepositorio>,
}

pub struct CriarVendaInput {
    pub total: f64,
}

pub struct CriarVendaOutput {
    pub sucesso: bool,
}

impl CriarVenda {
    pub fn novo(repo: Box<dyn VendaRepositorio>) -> Self {
        Self { repo }
    }

    pub fn executar(
        &self,
        input: CriarVendaInput,
    ) -> Result<CriarVendaOutput, ErroDominio> {
        let venda = Venda::nova(input.total)?;

        self.repo.salvar(venda)?;

        Ok(CriarVendaOutput { sucesso: true })
    }
}
