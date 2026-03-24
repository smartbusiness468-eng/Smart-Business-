use crate::domain::entidades::venda::Venda;
use crate::domain::erros::erro_dominio::ErroDominio;

/// Trait que define o contrato de persistência de vendas.
///
/// Responsabilidade:
/// - Abstrair a forma como os dados são salvos
/// - Permitir troca de banco sem impactar o domínio
///
/// Importante:
/// - Não deve conter lógica de negócio
/// - Apenas persistência
///
/// Observação:
/// - Agora é async para suportar operações reais com banco de dados
#[async_trait::async_trait]
pub trait VendaRepositorio: Send + Sync {

    /// Salva uma venda no sistema
    ///
    /// Fluxo esperado:
    /// - Recebe uma entidade já validada
    /// - Persiste no banco
    ///
    /// Possíveis erros:
    /// - ErroPersistencia (falha no banco)
    async fn salvar(&self, venda: Venda) -> Result<(), ErroDominio>;
}
