#[cfg(test)]
mod tests {
    use crate::application::use_cases::criar_venda::*;
    use crate::domain::entidades::venda::Venda;
    use crate::domain::erros::erro_dominio::ErroDominio;
    use crate::ports::repositorios::venda_repositorio::VendaRepositorio;

    /// Repositório fake usado para testes unitários
    ///
    /// Objetivo:
    /// - Simular persistência sem acessar banco de dados
    /// - Garantir isolamento do use case
    struct RepoFake;

    impl VendaRepositorio for RepoFake {
        fn salvar(&self, _venda: Venda) -> Result<(), ErroDominio> {
            Ok(())
        }
    }

    /// Testa criação de venda com dados válidos
    ///
    /// Esperado:
    /// - Não deve retornar erro
    /// - Venda deve ser criada com sucesso
    #[test]
    fn deve_criar_venda_com_sucesso() {
        let use_case = CriarVenda::novo(Box::new(RepoFake));

        let input = CriarVendaInput { total: 100.0 };

        let resultado = use_case.executar(input);

        assert!(resultado.is_ok());
    }

    /// Testa falha ao criar venda com valor inválido
    ///
    /// Esperado:
    /// - Deve retornar erro de domínio
    #[test]
    fn deve_falhar_com_valor_invalido() {
        let use_case = CriarVenda::novo(Box::new(RepoFake));

        let input = CriarVendaInput { total: 0.0 };

        let resultado = use_case.executar(input);

        assert!(resultado.is_err());
    }

    /// Testa se o output do caso de uso está correto
    ///
    /// Esperado:
    /// - Campo sucesso deve ser true
    #[test]
    fn deve_retornar_sucesso_no_output() {
        let use_case = CriarVenda::novo(Box::new(RepoFake));

        let input = CriarVendaInput { total: 50.0 };

        let resultado = use_case.executar(input).unwrap();

        assert!(resultado.sucesso);
    }
}
