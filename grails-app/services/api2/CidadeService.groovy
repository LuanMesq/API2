package api2
 /*
 Versão I. Para facilitar processos e validações no banco de dados Oracle,
 Foi criada a PACK_DESAFIO_DEV, no owner "ALUNO9", responsável pelas validações
 dos registros de cidades e retornando o "p_retorno in out" a resposta do banco.
*/

import org.springframework.jdbc.datasource.DataSourceUtils
import java.sql.CallableStatement
import java.sql.ResultSet

class CidadeService {
    def dataSource

    def listaCidade() {
        Cidade.list()
    }

    def buscaCidade(Long id) {
        Cidade.get(id)
    }

    def criaRegCidade(Map cidadeData) {
        def conn = DataSourceUtils.getConnection(dataSource)
        CallableStatement callStmt = null

        try {
            callStmt = conn.prepareCall("{call PACK_DESAFIO_DEV.PROC_INCLUI_CIDADE(?, ?, ?)}")

            def seq_id_cidade
            def sequenceQuery = "SELECT seq_id_cidade.nextval FROM DUAL"
            ResultSet sequenceResult = conn.createStatement().executeQuery(sequenceQuery)
            if (sequenceResult.next()) {
                seq_id_cidade = sequenceResult.getLong(1)
                sequenceResult.close()
            } else {
                log.error("Não foi possível obter o valor da sequência.")
            }
            log.error("seq_id_func: ${seq_id_cidade}")
            callStmt.setLong(1, seq_id_cidade)
            callStmt.setString(2, cidadeData.nome)
            callStmt.registerOutParameter(3, java.sql.Types.VARCHAR)

            callStmt.execute()

            def returnValue = callStmt.getString(3)
            log.error("Retorno da procedure: ${returnValue}")
            return returnValue
            return
        } catch (Exception e) {
            log.error("Erro ao criar registro de cidade:", e)
            return "ERRO"
        } finally {
            if (callStmt) {
                callStmt.close()
            }
            DataSourceUtils.releaseConnection(conn, dataSource)
        }
    }

    def atzCidade(Long id, Map cidadeData) {
        def conn = DataSourceUtils.getConnection(dataSource)
        CallableStatement callStmt = null

        try {
            callStmt = conn.prepareCall("{call PACK_DESAFIO_DEV.PROC_ATZ_CIDADE(?, ?, ?)}")
            callStmt.setLong(1, id)
            callStmt.setString(2, cidadeData.nome)
            callStmt.registerOutParameter(3, java.sql.Types.VARCHAR)

            callStmt.execute()

            def returnValue = callStmt.getString(3)
            log.error("Retorno da procedure: ${returnValue}")
            log.error("id ${id} funcionarioData.nome ${cidadeData.nome}")
            return returnValue
        } catch (Exception e) {
            log.error("Erro ao alterar registro de funcionário:", e)
            return "ERRO"
        } finally {
            if (callStmt) {
                callStmt.close()
            }
            DataSourceUtils.releaseConnection(conn, dataSource)
        }
    }

    def excluir(Long id) {
        def conn = DataSourceUtils.getConnection(dataSource)
        CallableStatement callStmt = null

        try {
            callStmt = conn.prepareCall("{call PACK_DESAFIO_DEV.PROC_DELETA_CIDADE(?, ?)}")
            callStmt.setLong(1, id)
            callStmt.registerOutParameter(2, java.sql.Types.VARCHAR)

            callStmt.execute()

            def returnValue = callStmt.getString(2)
            log.error("Retorno da procedure: ${returnValue}")
            return returnValue
        } catch (Exception e) {
            log.error("Erro ao deletar registro de cidade:", e)
            return "ERRO"
        } finally {
            if (callStmt) {
                callStmt.close()
            }
            DataSourceUtils.releaseConnection(conn, dataSource)
        }
    }
}