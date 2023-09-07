package api2

class FuncionarioController {

    static responseFormats = ["json"]
    static defaultAction = "get"

    def funcionarioService

    def list() {
        if (!request.method.equals("GET")){
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }

        def mensagemErro
        def retorno = funcionarioService.listaFuncionario()

        if (retorno == "ERRO"){
            mensagemErro = "Lista de funcionario(s) não encontrada."
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
        def retorno = funcionarioService.buscaFuncionario(id)

        if (retorno == "ERRO"){
            mensagemErro = "Funcionario de ID ${id} não encontrado."
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

        def funcionarioData = request.JSON
        def retorno = funcionarioService.criaRegFuncionario(funcionarioData)
        def mensagemErro

        if (retorno == "ERRO"){
            mensagemErro = "Ocorreu um erro ao inserir o funcionário. Contate o ADM."
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

        def funcionarioData = request.JSON
        def retorno = funcionarioService.atzFuncionario(id, funcionarioData)
        def mensagemErro

        if (retorno == "ERRO"){
            mensagemErro = "Ocorreu um erro ao atualizar o funcionário. Contate o ADM."
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
        def retorno = funcionarioService.excluir(id)
        def mensagemErro

        if (retorno == "ERRO"){
            mensagemErro = "Funcionário de ID ${id} não encontrado."
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
