package api2

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

class ReajusteSalario {
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataReajuste
    BigDecimal valorSalario

    static mapping = {
        id generator: 'sequence', params: [sequence: 'SEQ_ID_REAJUSTE']
        version  false
    }

    static belongsTo = [funcionario: Funcionario]

    static constraints = {
        valorSalario nullable: false, maxSize: 6.2
        dataReajuste nullable: false
        id unique: true
    }
}
