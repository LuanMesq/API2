package api2
/* ---------------------------------- Versão I ---------------------------------- */

import grails.converters.JSON
import grails.gorm.transactions.Transactional

@Transactional
class CidadeController {

    def CidadeService

    /* ---------------------------------- INDEX ---------------------------------- */
    def list() {
        if (!request.method.equals("GET")) {
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }
        def cidades = CidadeService.listaCidade()
        def mensagemErro
        if (cidades) {
            def cidadesComDadosBasicos = cidades.collect { cidade ->
                [
                        id: cidade.id,
                        nome: cidade.nome
                ]
            }
            mensagemErro = null
            render cidadesComDadosBasicos as JSON
        } else {
            mensagemErro = "Lista de cidade(s) não encontradas(s)."
        }
        if (mensagemErro) {
            render(status: 500, contentType: 'application/json') {
                JSON(error: mensagemErro)
            }
        } else {
            return
        }
    }
    /*  ---------------------------------- SHOW ---------------------------------- */
    def get(Long id) {
        if (!request.method.equals("GET")) {
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }
        def cidade = CidadeService.buscaCidade(id)
        def mensagemErro

        if (cidade) {
            def cidadeSimplificada = [
                      id: cidade.id,
                      nome: cidade.nome
            ]
            mensagemErro = null
            render cidadeSimplificada as JSON
        } else {
            mensagemErro = "Cidade(s) não encontradas(s)."
        }
        if (mensagemErro) {
            render(status: 500, contentType: 'application/json') {
                JSON(error: mensagemErro)
            }
        } else {
            return
        }
    }
    /* ---------------------------------- SAVE ---------------------------------- */
    def save() {

        if (!request.method.equals("POST")) {
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }

        def cidadeData = request.JSON
        def cidadeIncluida = cidadeService.criaRegCidade(cidadeData)
        if (cidadeIncluida == 'ERRO') {
            render(contentType: 'application/json') {
                JSON(message: "Erro ao Criar o Cidade. Por favor, verifique o formato dos registros de entrada.")
            }
        }else{
            render(contentType: 'application/json') {
                JSON(message: "Atualização de Dados Processada com Sucesso. Retorno: $cidadeIncluida}")
                cidadeData
            }
        }
    }
    /* ---------------------------------- UPDATE ---------------------------------- */
    def update(Long id) {
        if (!request.method.equals("PUT")) {
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }
        def cidadeData = request.JSON
        def cidadeAtualizada = cidadeService.atzCidade(id, cidadeData)
        if (cidadeAtualizada == 'ERRO') {
            render(contentType: 'application/json') {
                JSON(message: "Erro ao Atualizar Cidade. Por favor, verifique o formato dos registros de entrada.")
            }
        }else{
            render(contentType: 'application/json') {
                JSON(message: "Atualização de Dados Processada com Sucesso. Retorno: ${cidadeAtualizada}")
                cidadeData
            }
        }
    }
    /*  ---------------------------------- DELETE ---------------------------------- */
    def delete(Long id) {
        if (!request.method.equals("DELETE")){
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }
        def cidadeData = request.JSON
        def cidadeExcluida = cidadeService.excluir(id)
        if (cidadeExcluida == 'ERRO') {
            render(contentType: 'application/json') {
                JSON(message: "Erro ao Deletar o Funcionario. Por favor, verifique o formato dos registros de entrada.")
            }
        }else{
            render(contentType: 'application/json') {
                JSON(message: "Delete de Dados Processado com Sucesso. Retorno: ${cidadeExcluida} ")
                cidadeData
            }
        }
    }
}