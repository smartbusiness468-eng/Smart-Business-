/// Enum que representa erros de domínio do sistema.
///
/// Responsabilidade:
/// - Centralizar erros relacionados às regras de negócio
/// - Padronizar retorno de falhas no domínio
///
/// Importante:
/// - Não deve conter erros técnicos (ex: SQL, HTTP)
/// - Deve representar apenas problemas de regra de negócio
///
/// Possíveis melhorias:
/// - Adicionar mensagens customizadas
/// - Separar erros por contexto (Venda, Produto, etc)
#[derive(Debug, PartialEq)]
pub enum ErroDominio {
    /// Valor inválido para regras de negócio
    ValorInvalido,

    /// Falha ao persistir dados (erro de infraestrutura mapeado)
    ErroPersistencia,
}
