package api2

import grails.gorm.transactions.Transactional
import org.springframework.dao.DataIntegrityViolationException

@Transactional
class FuncionarioService {

    def listaFuncionario() {
        def funcionarioList = Funcionario.list()

        def funcionariosBasicos = funcionarioList.collect { funcionario ->
            [
                    id: funcionario.id,
                    nome: funcionario.nome,
                    cidade: [
                            id: funcionario.cidade ? funcionario.cidade.id : null,
                            nome: funcionario.cidade ? funcionario.cidade.nome : null
                    ]
            ]
        }

        if (funcionarioList){
            return funcionariosBasicos
        } else{
            return "ERRO"
        }
    }

    def buscaFuncionario(Long id) {
        def ifFuncionario = Funcionario.get(id)

        def funcionariosBasicos = ifFuncionario.collect { funcionario ->
            [
                    id: funcionario.id,
                    nome: funcionario.nome,
                    cidade: [
                            id: funcionario.cidade ? funcionario.cidade.id : null,
                            nome: funcionario.cidade ? funcionario.cidade.nome : null
                    ]
            ]
        }

        if (ifFuncionario){
            return funcionariosBasicos
        } else{
            return "ERRO"
        }
    }

    def criaRegFuncionario(Map funcionarioData) {
        Map retorno = [success: true]

        def funcionario = new Funcionario(
                nome: funcionarioData.nome,
                cidade: funcionarioData.cidade
        )

        if (!funcionario.validate()) {
            retorno.success = false
            return "ERRO"
        }

        funcionario.save(flush: true)
        return retorno
    }

    def atzFuncionario(Long id, Map funcionarioData) {
        Map retorno = [success: true]

        Funcionario funcionario = Funcionario.get(id as Long)

        try {
            if (!funcionario.validate()) {
                retorno.success = false
                return "ERRO"
            }

            if (funcionario) {
                funcionario.properties = funcionarioData
                funcionario.save(flush: true)
                return retorno
            } else {
                retorno.success = false
                return "ERRO"
            }
        }catch(Exception e){
            retorno.success = false
            return "ERRO"
        }
    }

    def excluir(Long id) {
        Map retorno = [success: true]

        def funcionario = Funcionario.get(id as Long)

        try {
            if (funcionario) {
                funcionario.delete(flush: true)
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