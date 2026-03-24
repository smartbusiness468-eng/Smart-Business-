use sqlx::PgPool;
use uuid::Uuid;

use crate::domain::entidades::venda::Venda;
use crate::domain::erros::erro_dominio::ErroDominio;
use crate::ports::repositorios::venda_repositorio::VendaRepositorio;

/// Implementação do repositório usando PostgreSQL
pub struct VendaRepositorioPostgres {
    pool: PgPool,
}

impl VendaRepositorioPostgres {
    pub fn novo(pool: PgPool) -> Self {
        Self { pool }
    }
}

#[async_trait::async_trait]
impl VendaRepositorio for VendaRepositorioPostgres {

    /// Salva uma venda no banco de dados
    async fn salvar(&self, venda: Venda) -> Result<(), ErroDominio> {

        // Gera ID único para a venda
        let id = Uuid::new_v4();

        // Executa query sem validação em compile-time (evita erro com DATABASE_URL)
        let resultado = sqlx::query(
            "INSERT INTO vendas (id, total) VALUES ($1, $2)"
        )
        .bind(id)
        .bind(venda.total())
        .execute(&self.pool)
        .await;

        // Mapeia erro técnico para erro de domínio
        match resultado {
            Ok(_) => Ok(()),
            Err(_) => Err(ErroDominio::ErroPersistencia),
        }
    }
}
