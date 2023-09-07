package api2

class ReajusteSalarioController {


    static responseFormats = ["json"]
    static defaultAction = "get"

    def ReajusteSalarioService

    def list() {
        if (!request.method.equals("GET")){
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }

        def mensagemErro
        def retorno = ReajusteSalarioService.listaReajuste()

        if (retorno == "ERRO"){
            mensagemErro = "Lista de Reajustes não encontrada."
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
        def retorno = ReajusteSalarioService.buscaReajuste(id)

        if (retorno == "ERRO"){
            mensagemErro = "Reajuste de ID ${id} não encontrado."
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

        def reajusteData = request.JSON
        def retorno = ReajusteSalarioService.criaRegReajuste(reajusteData)
        def mensagemErro

        if (retorno == "ERRO"){
            mensagemErro = "Ocorreu um erro ao inserir o reajuste salarial. Contate o ADM."
            render(contentType: 'application/json') {
                JSON(message: mensagemErro)
            }
        } else {
            respond(retorno)
        }
    }

    def update(Long id) {
        if (!request.method.equals("PUT")) {
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }

        def reajusteData = request.JSON
        def retorno = ReajusteSalarioService.atzReajuste(id, reajusteData)
        def mensagemErro

        if (retorno == "ERRO"){
            mensagemErro = "Ocorreu um erro ao atualizar o registro de reajuste. Contate o ADM."
            render(contentType: 'application/json') {
                JSON(message: mensagemErro)
            }
        } else {
            respond(retorno)
        }
    }

    def delete(Long id) {
        if (!request.method.equals("DELETE")) {
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }
        def retorno = ReajusteSalarioService.excluir(id)
        def mensagemErro

        if (retorno == "ERRO"){
            mensagemErro = "Reajuste de ID ${id} não encontrado."
            render(contentType: 'application/json') {
                JSON(message: mensagemErro)
            }
        } else if(retorno == 'ERRO FK') {
            mensagemErro = "Registro Filho Localizado."
            render(contentType: 'application/json') {
                JSON(message: mensagemErro)
            }
        } else {
            respond(retorno)
        }
    }
}
