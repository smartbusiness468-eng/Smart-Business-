use crate::domain::entidades::venda::Venda;
use crate::domain::erros::erro_dominio::ErroDominio;

/// Contrato (interface) responsável pela persistência de vendas.
///
/// Este trait define como o sistema salva uma venda,
/// sem expor detalhes de implementação (banco, API, etc).
///
/// Implementações possíveis:
/// - PostgreSQL (SQLx)
/// - MongoDB
/// - Redis
/// - Em memória (para testes)
///
/// Importante:
/// - Esta camada NÃO deve conter lógica de negócio
/// - Apenas persistência e recuperação de dados
pub trait VendaRepositorio {

    /// Persiste uma venda no sistema.
    ///
    /// Fluxo esperado:
    /// - Recebe uma entidade já validada
    /// - Salva no banco de dados
    ///
    /// Possíveis erros:
    /// - Falha de conexão com banco
    /// - Erro de persistência
    ///
    /// Possíveis melhorias:
    /// - Retornar ID da venda criada
    /// - Retornar entidade completa persistida
    fn salvar(&self, venda: Venda) -> Result<(), ErroDominio>;
}
