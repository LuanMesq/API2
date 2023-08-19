package api2

  /*
   Versão I. Para facilitar processos e validações no banco de dados Oracle,
   Foi criada a PACK_DESAFIO_DEV, no owner "ALUNO9", responsável pelas validações
   dos registros de funcionários e retornando o "p_retorno in out" a resposta do banco.
  */

import org.springframework.jdbc.datasource.DataSourceUtils
import java.sql.CallableStatement
import java.sql.ResultSet

class FuncionarioService {

    def listaFuncionario() {
        Funcionario.list()
    }

    def buscaFuncionario(Long id) {
        Funcionario.get(id)
    }

    def dataSource

    def criaRegFuncionario(Map funcionarioData) {

        def CampoObrigatorio
        if (funcionarioData.cidade == null){
            CampoObrigatorio = 'Obrigatório Informar o ID da Cidade.'
            return CampoObrigatorio
            return
        }

        def conn = DataSourceUtils.getConnection(dataSource)
        CallableStatement callStmt = null

        try {
            callStmt = conn.prepareCall("{call PACK_DESAFIO_DEV.PROC_INCLUI_REG_FUNCIONARIO(?, ?, ?, ?)}")
            def seq_id_func
            def sequenceQuery = "SELECT seq_id_func.nextval FROM DUAL"
            ResultSet sequenceResult = conn.createStatement().executeQuery(sequenceQuery)
            if (sequenceResult.next()) {
                seq_id_func = sequenceResult.getLong(1)
                sequenceResult.close()
            } else {
                log.error("Não foi possível obter o valor da sequência.")
            }
            log.error("seq_id_func: ${seq_id_func}")
            callStmt.setLong(1, seq_id_func)
            callStmt.setString(2, funcionarioData.nome)
            callStmt.setLong(3, funcionarioData.cidade)
            callStmt.registerOutParameter(4, java.sql.Types.VARCHAR)

            callStmt.execute()

            def returnValue = callStmt.getString(4)
            log.error("Retorno da procedure: ${returnValue}")
            return returnValue
            return
        } catch (Exception e) {
            log.error("Erro ao criar registro de funcionário:", e)
            return "ERRO"
        } finally {
            if (callStmt) {
                callStmt.close()
            }
            DataSourceUtils.releaseConnection(conn, dataSource)
        }
    }

    def atzFuncionario(Long id, Map funcionarioData) {

        def conn = DataSourceUtils.getConnection(dataSource)
        CallableStatement callStmt = null

        try {
            callStmt = conn.prepareCall("{call PACK_DESAFIO_DEV.PROC_ATZ_REG_FUNCIONARIO(?, ?, ?, ?)}")
            callStmt.setLong(1, id)
            callStmt.setString(2, funcionarioData.nome)
            callStmt.setLong(3, funcionarioData.cidade)
            callStmt.registerOutParameter(4, java.sql.Types.VARCHAR)

            callStmt.execute()

            def returnValue = callStmt.getString(4)
            log.error("Retorno da procedure: ${returnValue}")
            log.error("id ${id} funcionarioData.nome ${funcionarioData.nome}  funcionarioData.cidade ${funcionarioData.cidade}")
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
            callStmt = conn.prepareCall("{call PACK_DESAFIO_DEV.PROC_DELETA_FUNCIONARIO(?, ?)}")
            callStmt.setLong(1, id)
            callStmt.registerOutParameter(2, java.sql.Types.VARCHAR)

            callStmt.execute()

            def returnValue = callStmt.getString(2)
            log.error("Retorno da procedure: ${returnValue}")
            return returnValue
        } catch (Exception e) {
            log.error("Erro ao deletar registro de funcionário:", e)
            return "ERRO"
        } finally {
            if (callStmt) {
                callStmt.close()
            }
            DataSourceUtils.releaseConnection(conn, dataSource)
        }
    }
}
