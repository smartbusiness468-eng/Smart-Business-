use crate::domain::entidades::venda::Venda;
use crate::domain::erros::erro_dominio::ErroDominio;
use crate::ports::repositorios::venda_repositorio::VendaRepositorio;

/// Caso de uso responsável por criar uma venda.
///
/// Responsabilidade:
/// - Orquestrar o fluxo de criação
/// - Garantir validação via domínio
/// - Delegar persistência ao repositório
///
/// Importante:
/// - Não conhece banco de dados
/// - Não conhece HTTP
pub struct CriarVenda {
    repo: Box<dyn VendaRepositorio>,
}

/// Dados de entrada para criação da venda
///
/// Possíveis melhorias:
/// - adicionar cliente_id
/// - adicionar itens
pub struct CriarVendaInput {
    pub total: f64,
}

/// Resultado da operação
///
/// Possíveis melhorias:
/// - retornar ID da venda
pub struct CriarVendaOutput {
    pub sucesso: bool,
}

impl CriarVenda {
    /// Cria instância do caso de uso com injeção de dependência
    pub fn novo(repo: Box<dyn VendaRepositorio>) -> Self {
        Self { repo }
    }

    /// Executa o fluxo de criação de venda
    ///
    /// Fluxo:
    /// 1. Cria entidade (validação)
    /// 2. Persiste no banco
    pub async fn executar(
        &self,
        input: CriarVendaInput,
    ) -> Result<CriarVendaOutput, ErroDominio> {

        // Cria a entidade validando regras de negócio
        let venda = Venda::nova(input.total)?;

        // Persiste no banco via interface
        self.repo.salvar(venda).await?;

        Ok(CriarVendaOutput { sucesso: true })
    }
}
