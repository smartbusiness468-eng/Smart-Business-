use crate::domain::entidades::venda::Venda;
use crate::domain::erros::erro_dominio::ErroDominio;
use crate::ports::repositorios::venda_repositorio::VendaRepositorio;

/// Caso de uso responsável por criar uma venda.
///
/// Responsabilidade:
/// - Orquestrar o fluxo de criação de venda
/// - Garantir que a entidade seja validada
/// - Delegar persistência ao repositório
///
/// Fluxo:
/// 1. Recebe dados de entrada
/// 2. Cria entidade de domínio (validação)
/// 3. Persiste no repositório
///
/// Importante:
/// - Não conhece banco de dados
/// - Não conhece HTTP
/// - Depende apenas de abstrações (trait)
pub struct CriarVenda {
    repo: Box<dyn VendaRepositorio>,
}

/// DTO de entrada do caso de uso.
///
/// Representa os dados necessários para criar uma venda.
///
/// Possíveis melhorias:
/// - Adicionar cliente_id
/// - Adicionar lista de itens
pub struct CriarVendaInput {
    pub total: f64,
}

/// DTO de saída do caso de uso.
///
/// Representa o resultado da operação.
///
/// Possíveis melhorias:
/// - Retornar ID da venda
/// - Retornar data/hora da venda
pub struct CriarVendaOutput {
    pub sucesso: bool,
}

impl CriarVenda {
    /// Cria uma nova instância do caso de uso.
    ///
    /// Recebe a implementação do repositório via injeção de dependência.
    /// Isso permite trocar banco de dados sem alterar o use case.
    pub fn novo(repo: Box<dyn VendaRepositorio>) -> Self {
        Self { repo }
    }

    /// Executa o fluxo de criação de venda.
    ///
    /// Possíveis erros:
    /// - ErroDominio::ValorInvalido (validação da entidade)
    /// - ErroDominio::ErroPersistencia (falha ao salvar)
    pub fn executar(
        &self,
        input: CriarVendaInput,
    ) -> Result<CriarVendaOutput, ErroDominio> {
        // Criação da entidade (já valida regras de negócio)
        let venda = Venda::nova(input.total)?;

        // Persistência via abstração (não depende de banco específico)
        self.repo.salvar(venda)?;

        Ok(CriarVendaOutput { sucesso: true })
    }
}
