package api2

import org.springframework.transaction.annotation.Transactional
import grails.gorm.annotation.Entity

@Entity
@Transactional
class Funcionario {

    String nome

    static mapping = {
        id generator: 'sequence', params: [sequence: 'SEQ_ID_FUNC']
        version  false
    }

    static belongsTo = [cidade: Cidade]
    static hasMany = [reajustes: ReajusteSalario]

    static constraints = {
        nome nullable: false, maxSize: 50
        id unique: true
    }
}
