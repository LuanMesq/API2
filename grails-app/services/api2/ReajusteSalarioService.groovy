package api2

import grails.gorm.transactions.Transactional
import org.springframework.dao.DataIntegrityViolationException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import static java.time.LocalDate.parse


@Transactional
class ReajusteSalarioService {

    // Formata a data de entrada dd/mm/yyyy.
    private static LocalDate parseDataReajuste(String dataReajuste) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return parse(dataReajuste, dateFormatter)
    }

    def listaReajuste() {
        def reajusteList = ReajusteSalario.list()

        def reajusteDados = reajusteList.collect { reajusteSalario ->
            [
                    id: reajusteSalario.id,
                    valorSalario: reajusteSalario.valorSalario,
                    dataReajuste: reajusteSalario.dataReajuste.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    funcionario: [
                            id: reajusteSalario.funcionario ? reajusteSalario.funcionario.id : null,
                            nome: reajusteSalario.funcionario ? reajusteSalario.funcionario.nome : null
                    ]
            ]
        }

        if (reajusteList){
            return reajusteDados
        } else{
            return "ERRO"
        }
    }

    def buscaReajuste(Long id) {
        def reajusteId = ReajusteSalario.get(id)

        def reajusteDados = reajusteId.collect { reajusteSalario ->
            [
                    id: reajusteSalario.id,
                    valorSalario: reajusteSalario.valorSalario,
                    dataReajuste: reajusteSalario.dataReajuste.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    funcionario: [
                            id: reajusteSalario.funcionario ? reajusteSalario.funcionario.id : null,
                            nome: reajusteSalario.funcionario ? reajusteSalario.funcionario.nome : null
                    ]
            ]
        }

        if (reajusteId){
            return reajusteDados
        } else{
            return "ERRO"
        }
    }

    def criaRegReajuste(Map reajusteData) {
        Map retorno = [success: true]

        def reajuste = new ReajusteSalario(
                valorSalario: reajusteData.valorSalario,
                dataReajuste: parseDataReajuste(reajusteData.dataReajuste),
                funcionario: reajusteData.funcionario
        )

        if (!reajuste.validate()) {
            retorno.success = false
            return "ERRO"
        }

        reajuste.save(flush: true)
        return retorno
    }

    def atzReajuste(Long id, Map reajusteData) {
        Map retorno = [success: true]

        ReajusteSalario reajuste = ReajusteSalario.get(id as Long)
        reajusteData.dataReajuste = parseDataReajuste(reajusteData.dataReajuste)
        reajuste.properties = reajusteData

        if (!reajuste.validate()) {
            retorno.success = false
            return "ERRO"
        }

        reajuste.save(flush: true)
        return retorno
    }

    def excluir(Long id) {
        Map retorno = [success: true]

        def reajuste = ReajusteSalario.get(id as Long)

        try {
            if (reajuste) {
                reajuste.delete(flush: true)
                retorno.success = true
                return retorno
            } else {
                return "ERRO"
            }
        }catch (DataIntegrityViolationException e) {
            return "ERRO FK"
        }
    }

}