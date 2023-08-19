package api2

 /*
 Versão I. Para facilitar processos e validações no banco de dados Oracle,
 Foi criada a PACK_DESAFIO_DEV, no owner "ALUNO9", responsável pelas validações
 dos registros de reajustes de salários e retornando o "p_retorno in out" a resposta do banco.
 */

import org.springframework.jdbc.datasource.DataSourceUtils
import java.sql.CallableStatement
import java.sql.ResultSet
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReajusteSalarioService {

    private static LocalDate parseDataNascimento(String dataReajuste) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return parse(dataReajuste, dateFormatter)
    }

    def listaReajuste() {
        ReajusteSalario.list()
    }

    def buscaReajuste(Long id) {
        ReajusteSalario.get(id)
    }

    def dataSource

    def criaRegReajuste(Map reajusteData) {
        def conn = DataSourceUtils.getConnection(dataSource)
        CallableStatement callStmt = null

        try {
            callStmt = conn.prepareCall("{call PACK_DESAFIO_DEV.PROC_INCLUI_REAJUSTE(?, ?, ?, ?, ?)}")

            def seq_id_reajuste
            def CampoObrigatorio

            if (reajusteData.funcionario == null){
                CampoObrigatorio = 'Obrigatório Informar o ID do funcionário.'
                return CampoObrigatorio
            }
            if (reajusteData.valorSalario == null){
                CampoObrigatorio = 'Obrigatório Informar o Valor do Reajuste Salarial.'
                return CampoObrigatorio
            }
            if (reajusteData.dataReajuste == null){
                CampoObrigatorio = 'Obrigatório Informar a Data do do Reajuste Salarial.'
                return CampoObrigatorio
            }

            def sequenceQuery = "SELECT seq_id_reajuste.nextval FROM DUAL"
            ResultSet sequenceResult = conn.createStatement().executeQuery(sequenceQuery)
            if (sequenceResult.next()) {
                seq_id_reajuste = sequenceResult.getLong(1)
                sequenceResult.close()
            } else {
                log.error("Não foi possível obter o valor da sequência.")
            }
            log.error("seq_id_reajuste: ${seq_id_reajuste}")
            callStmt.setLong(1, seq_id_reajuste)
            callStmt.setLong(2, reajusteData.funcionario)
            callStmt.setDate(3, java.sql.Date.valueOf(LocalDate.parse(reajusteData.dataReajuste, DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
            callStmt.setBigDecimal(4, reajusteData.valorSalario)
            callStmt.registerOutParameter(5, java.sql.Types.VARCHAR)

            callStmt.execute()

            def returnValue = callStmt.getString(5)
            log.error("Retorno da procedure: ${returnValue}")
            return returnValue
            return
        } catch (Exception e) {
            log.error("Erro ao criar registro de Reajuste Salarial:", e)
            return "ERRO"
        } finally {
            if (callStmt) {
                callStmt.close()
            }
            DataSourceUtils.releaseConnection(conn, dataSource)
        }
    }

    def atzReajuste(Long id, Map reajusteData) {

        def conn = DataSourceUtils.getConnection(dataSource)
        CallableStatement callStmt = null

        try {
            callStmt = conn.prepareCall("{call PACK_DESAFIO_DEV.PROC_ATZ_REAJUSTE(?, ?, ?, ?, ?)}")
            callStmt.setLong(1, id)
            callStmt.setLong(2, reajusteData.funcionario)
            callStmt.setDate(3, java.sql.Date.valueOf(LocalDate.parse(reajusteData.dataReajuste, DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
            callStmt.setBigDecimal(4, reajusteData.valorSalario)
            callStmt.registerOutParameter(5, java.sql.Types.VARCHAR)

            callStmt.execute()

            def returnValue = callStmt.getString(5)
            log.error("Retorno da procedure: ${returnValue}")
            return returnValue
            return
        } catch (Exception e) {
            log.error("Erro ao alterar registro de Reajuste Salarial:", e)
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
            callStmt = conn.prepareCall("{call PACK_DESAFIO_DEV.PROC_DELETA_REAJUSTE(?, ?)}")
            callStmt.setLong(1, id)
            callStmt.registerOutParameter(2, java.sql.Types.VARCHAR)

            callStmt.execute()

            def returnValue = callStmt.getString(2)
            log.error("Retorno da procedure: ${returnValue}")
            return returnValue
        } catch (Exception e) {
            log.error("Erro ao deletar registro de Reajuste Salarial:", e)
            return "ERRO"
        } finally {
            if (callStmt) {
                callStmt.close()
            }
            DataSourceUtils.releaseConnection(conn, dataSource)
        }
    }
}
