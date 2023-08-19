package api2

import org.springframework.transaction.annotation.Transactional
import grails.gorm.annotation.Entity

@Entity
@Transactional
class Cidade {

    String nome

    static mapping = {
        id generator: 'sequence', params: [sequence: 'SEQ_ID_CIDADE']
        version false
    }

    static hasMany = [funcionarios: Funcionario]

    static constraints = {
        nome nullable: false, maxSize: 50
        id  unique: true
    }
}