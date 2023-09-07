package api2

import org.springframework.transaction.annotation.Transactional

@Transactional
class BootStrap {

    def InicializaMockupService

    def init = { servletContext ->
        log.info("Iniciando a inicialização do serviço...")
        InicializaMockupService.insereDados()
    }

    def destroy() {
        Cidade.deleteAll()
    }
}