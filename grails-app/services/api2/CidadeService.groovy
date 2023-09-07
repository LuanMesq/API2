package api2

import javassist.NotFoundException
import org.springframework.dao.DataIntegrityViolationException

class CidadeService {

    def listaCidade() {
        def cidadeLista = Cidade.list()

        if (cidadeLista){
            return cidadeLista
        } else{
            return "ERRO"
        }
    }

    def buscaCidade(Long id) {
        def idCidade = Cidade.get(id)

        if (idCidade){
            return idCidade
        } else{
            return "ERRO"
        }
    }

    def criaRegCidade(Map cidadeData) {
        Map retorno = [success: true]

        Cidade cidade = new Cidade()
        cidade.nome = cidadeData.nome
        cidade.save(flush: true)

        retorno.cidade = cidade
        return retorno
    }

    def atzCidade(Long id, Map cidadeData) {
        Map retorno = [success: true]

        Cidade cidade = Cidade.get(id as Long)
        cidade.nome = cidadeData.nome
        cidade.save(flush: true)

        return retorno
    }

    def excluir(Long id) {
        def retorno = [success: true]

        def cidade = Cidade.get(id)

        try {
            if (cidade) {
                cidade.delete(flush: true)
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