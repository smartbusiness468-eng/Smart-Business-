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

    /// Implementação fake do repositório
    ///
    /// Importante:
    /// - Agora é async para acompanhar o contrato real
    /// - Sempre retorna sucesso (não simula erro ainda)
    #[async_trait::async_trait]
    impl VendaRepositorio for RepoFake {
        async fn salvar(&self, _venda: Venda) -> Result<(), ErroDominio> {
            Ok(())
        }
    }

    /// Testa criação de venda com dados válidos
    ///
    /// Esperado:
    /// - Não deve retornar erro
    /// - Venda deve ser criada com sucesso
    #[tokio::test]
    async fn deve_criar_venda_com_sucesso() {
        let use_case = CriarVenda::novo(Box::new(RepoFake));

        let input = CriarVendaInput { total: 100.0 };

        // Agora precisa de .await pois o use case é async
        let resultado = use_case.executar(input).await;

        assert!(resultado.is_ok());
    }

    /// Testa falha ao criar venda com valor inválido
    ///
    /// Esperado:
    /// - Deve retornar erro de domínio
    #[tokio::test]
    async fn deve_falhar_com_valor_invalido() {
        let use_case = CriarVenda::novo(Box::new(RepoFake));

        let input = CriarVendaInput { total: 0.0 };

        let resultado = use_case.executar(input).await;

        assert!(resultado.is_err());
    }

    /// Testa se o output do caso de uso está correto
    ///
    /// Esperado:
    /// - Campo sucesso deve ser true
    #[tokio::test]
    async fn deve_retornar_sucesso_no_output() {
        let use_case = CriarVenda::novo(Box::new(RepoFake));

        let input = CriarVendaInput { total: 50.0 };

        // unwrap após await
        let resultado = use_case.executar(input).await.unwrap();

        assert!(resultado.sucesso);
    }
}
