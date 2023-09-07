package api2

class Funcionario {

    String nome

    static mapping = {
        id generator: "increment"
        version  false
    }

    static belongsTo = [cidade: Cidade]

    static constraints = {
        nome nullable: false, maxSize: 50
        cidade nullable: false
        id unique: true
    }
}
