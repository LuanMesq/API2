package api2

import grails.converters.JSON
import grails.gorm.transactions.Transactional

@Transactional
class CidadeController {

    static responseFormats = ["json"]
    static defaultAction = "get"

    def CidadeService

    def list() {
        if (!request.method.equals("GET")) {
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }
        def mensagemErro
        def retorno = cidadeService.listaCidade()

        if (retorno == "ERRO"){
            mensagemErro = "Lista de cidade(s) não encontradas(s)."
            render(contentType: 'application/json') {
                JSON(message: mensagemErro)
            }
        } else {
            respond(retorno)
        }
    }

    def get(Long id) {
        if (!request.method.equals("GET")) {
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }
        def mensagemErro
        def retorno = cidadeService.buscaCidade(id)

        if (retorno == "ERRO"){
            mensagemErro = "Cidade de ID ${id} não encontrada."
            render(contentType: 'application/json') {
                JSON(message: mensagemErro)
            }
        } else {
            respond(retorno)
        }
    }

    def save() {
        if (!request.method.equals("POST")) {
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }
        def cidadeData = request.JSON
        def retorno = cidadeService.criaRegCidade(cidadeData)
        respond(retorno)
    }


    def update(Long id) {
        if (!request.method.equals("PUT")) {
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }
        def cidadeData = request.JSON
        def retorno = cidadeService.atzCidade(id, cidadeData)
        respond(retorno)
    }

    def delete(Long id) {
        if (!request.method.equals("DELETE")) {
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }
        def retorno = cidadeService.excluir(id)
        def mensagemErro

        if (retorno == "ERRO"){
            mensagemErro = "Cidade de ID ${id} não encontrada."
            render(contentType: 'application/json') {
                JSON(message: mensagemErro)
            }
        }else if(retorno == 'ERRO FK'){
            mensagemErro = "Registro Filho Localizado."
            render(contentType: 'application/json') {
                JSON(message: mensagemErro)
            }
        }else {
            respond(retorno)
        }
    }

}