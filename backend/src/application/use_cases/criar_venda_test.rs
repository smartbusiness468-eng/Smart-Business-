#[cfg(test)]
mod tests {
    use crate::application::use_cases::criar_venda::*;
    use crate::domain::entidades::venda::Venda;
    use crate::domain::erros::erro_dominio::ErroDominio;
    use crate::ports::repositorios::venda_repositorio::VendaRepositorio;

    struct RepoFake;

    impl VendaRepositorio for RepoFake {
        fn salvar(&self, _venda: Venda) -> Result<(), ErroDominio> {
            Ok(())
        }
    }

    #[test]
    fn deve_criar_venda_com_sucesso() {
        let use_case = CriarVenda::novo(Box::new(RepoFake));

        let input = CriarVendaInput { total: 100.0 };

        let resultado = use_case.executar(input);

        assert!(resultado.is_ok());
    }

    #[test]
    fn deve_falhar_com_valor_invalido() {
        let use_case = CriarVenda::novo(Box::new(RepoFake));

        let input = CriarVendaInput { total: 0.0 };

        let resultado = use_case.executar(input);

        assert!(resultado.is_err());
    }
}
