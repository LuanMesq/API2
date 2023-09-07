package api2

import grails.gorm.transactions.Transactional
import java.time.LocalDate

@Transactional
class InicializaMockupService {
    def insereDados() {
        Cidade cidade = new Cidade()
        cidade.setNome("Sapiranga")
        cidade.save(flush: true)

        Cidade cidade2 = new Cidade()
        cidade2.setNome("Taquara")
        cidade2.save(flush: true)

        Cidade cidade3 = new Cidade()
        cidade3.setNome("Novo Hamburgo")
        cidade3.save(flush: true)

        Funcionario funcionario = new Funcionario()
        funcionario.setNome("Luan Mesquita")
        funcionario.setCidade(cidade2)
        funcionario.save(flush: true)

        Funcionario funcionario2 = new Funcionario()
        funcionario2.setNome("Ricardo Severo")
        funcionario2.setCidade(cidade)
        funcionario2.save(flush: true)

        ReajusteSalario reajusteSalario = new ReajusteSalario()
        reajusteSalario.setDataReajuste(LocalDate.now())
        reajusteSalario.setValorSalario(3200 as BigDecimal)
        reajusteSalario.setFuncionario(funcionario)
        reajusteSalario.save(flush: true)
    }
}

