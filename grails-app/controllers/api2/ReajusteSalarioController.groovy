package api2

import java.time.format.DateTimeFormatter

class ReajusteSalarioController {
    def ReajusteSalarioService

    /* ---------------------------------- INDEX ---------------------------------- */
    def list() {
        if (!request.method.equals("GET"))  {
            render(contentType: 'application/json') {
                JSON(message: "Método não permitido para esta ação.")
            }
            return
        }
        def reajustes = ReajusteSalarioService.listaReajuste()
        def mensagemErro
        if (reajustes) {
            def reajustesBasicos = reajustes.collect { reajuste ->
                [
                        id: reajuste.id,
                        funcionario: [
                                id: reajuste.funcionario ? reajuste.funcionario.id : null,
                                nome: reajuste.funcionario ? reajuste.funcionario.nome : null
                        ],
                        dataReajuste: reajuste.dataReajuste.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        valorSalario: reajuste.valorSalario
                ]
            }
            mensagemErro = null
            render(status: 200, contentType: 'application/json') {
                JSON(Retorno: reajustesBasicos)
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
        def reajuste = ReajusteSalarioService.buscaReajuste(id)
        def mensagemErro
        if (reajuste) {
            def reajusteSimplificado = [
                    id: reajuste.id,
                    funcionario: [
                            id: reajuste.funcionario ? reajuste.funcionario.id : null,
                            nome: reajuste.funcionario ? reajuste.funcionario.nome : null
                    ],
                    dataReajuste: reajuste.dataReajuste.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    valorSalario: reajuste.valorSalario
            ]
            mensagemErro = null
            render(status: 200, contentType: 'application/json') {
                JSON(Retorno: reajusteSimplificado)
            }
        } else {
            mensagemErro = "Reajuste(s) não encontrados(s)."
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

        def reajusteData = request.JSON
        def reajusteIncluido = ReajusteSalarioService.criaRegReajuste(reajusteData)
        if (reajusteIncluido == 'ERRO') {
            render(contentType: 'application/json') {
                JSON(message: "Erro ao Inserir Reajuste. Por favor, verifique o formato dos registros de entrada.")
            }
        }else{
            render(contentType: 'application/json') {
                JSON(message: "Processamento Concluído com Sucesso. Retorno: $reajusteIncluido}")
                reajusteData
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
        def reajusteData = request.JSON
        def reajusteAtualizado = ReajusteSalarioService.atzReajuste(id, reajusteData)
        if (reajusteAtualizado == 'ERRO') {
            render(contentType: 'application/json') {
                JSON(message: "Erro ao Atualizar o Reajuste Salarial. Por favor, verifique o formato dos registros de entrada.")
            }
        }else{
            render(contentType: 'application/json') {
                JSON(message: "Processamento Concluído com Sucesso.. Retorno: ${reajusteAtualizado}")
                reajusteData
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
        def reajusteData = request.JSON
        def reajusteExcluido = ReajusteSalarioService.excluir(id)
        if (reajusteExcluido == 'ERRO') {
            render(contentType: 'application/json') {
                JSON(message: "Erro ao Deletar o Reajuste Salárial. Por favor, verifique o formato dos registros de entrada.")
            }
        }else{
            render(contentType: 'application/json') {
                JSON(message: "Processamento Concluído com Sucesso.. Retorno: ${reajusteExcluido} ")
                reajusteData
            }
        }
    }
}
