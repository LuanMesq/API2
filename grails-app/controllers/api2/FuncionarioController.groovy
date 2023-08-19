package api2

class FuncionarioController {
    def FuncionarioService

    /* ---------------------------------- INDEX ---------------------------------- */
    def list() {
        if (!request.method.equals("GET")){
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }
        def funcionarios = FuncionarioService.listaFuncionario()
        def mensagemErro
        if (funcionarios) {
            def funcionariosBasicos = funcionarios.collect { funcionario ->
                [
                        id: funcionario.id,
                        nome: funcionario.nome,
                        cidade: [
                                id: funcionario.cidade ? funcionario.cidade.id : null,
                                nome: funcionario.cidade ? funcionario.cidade.nome : null
                        ]
                ]
            }
            mensagemErro = null
            render(status: 200, contentType: 'application/json') {
                JSON(Retorno: funcionariosBasicos)
            }
        } else {
            mensagemErro = "Lista de funcionários não encontrada(s)."
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
        def funcionario = FuncionarioService.buscaFuncionario(id)
        def mensagemErro
        if (funcionario) {
            def funcionarioSimplificado = [
                    id: funcionario.id,
                    nome: funcionario.nome,
                    cidade: [
                            id: funcionario.cidade ? funcionario.cidade.id : null,
                            nome: funcionario.cidade ? funcionario.cidade.nome : null
                    ]
            ]
            mensagemErro = null
            render(status: 200, contentType: 'application/json') {
                JSON(Retorno: funcionarioSimplificado)
            }
        } else {
            mensagemErro = "Funcionário(s) não encontrados(s)."
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

        def funcionarioData = request.JSON
        def funcionarioIncluido = funcionarioService.criaRegFuncionario(funcionarioData)
        if (funcionarioIncluido == 'ERRO') {
            render(contentType: 'application/json') {
                JSON(message: "Erro ao Criar o Funcionario. Por favor, verifique o formato dos registros de entrada.")
            }
        }else{
            render(contentType: 'application/json') {
                JSON(message: "Processamento Concluído com Sucesso. Retorno: $funcionarioIncluido}")
                funcionarioData
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
        def funcionarioData = request.JSON
        def funcionarioAtualizado = funcionarioService.atzFuncionario(id, funcionarioData)
        if (funcionarioAtualizado == 'ERRO') {
            render(contentType: 'application/json') {
                JSON(message: "Erro ao Atualizar o Funcionario. Por favor, verifique o formato dos registros de entrada.")
            }
        }else{
            render(contentType: 'application/json') {
                JSON(message: "Processamento Concluído com Sucesso.. Retorno: ${funcionarioAtualizado}")
                funcionarioAtualizado
            }
        }
    }
    /*  ---------------------------------- DELETE ---------------------------------- */
    def delete(Long id) {
        if (!request.method.equals("DELETE")) {
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }
        def funcionarioData = request.JSON
        def funcionarioExcluido = funcionarioService.excluir(id)
        if (funcionarioExcluido == 'ERRO') {
            render(contentType: 'application/json') {
                JSON(message: "Erro ao Deletar o Funcionario. Por favor, verifique o formato dos registros de entrada.")
            }
        }else{
            render(contentType: 'application/json') {
                JSON(message: "Processamento Concluído com Sucesso.. Retorno: ${funcionarioExcluido} ")
                funcionarioData
            }
        }
    }
}
