import java.util.*

fun main() {
    println("Hello World!")

    //inmutables (no se reasignan "=")
    val inmutable: String = "Adrian";
    //inmutable = "Vicente";

    //Mutables (re asignar)
    var mutable: String = "Vicente";
    mutable = "Adrian"

    //val > var
    //duck typing
    var ejemploVariable = "Gustavo Venegas"
    val edadEjemplo: Int = 12
    ejemploVariable.trim()

    //variable primitiva
    val nombreProfesor: String = "Adrian Eguez"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'C'
    val mayorEdad: Boolean = true
    //clases Java
    val fechaNacimiento: Date = Date()

    //switch
    val estadoCivilWhen = "C"
    when (estadoCivilWhen) {
        "C" -> {
            println("Casado")
        }
        "S" -> {
            println("Soltero")
        }
        else -> {
            println("No sabemos")
        }
    }

    val esSoltero = (estadoCivilWhen == "S")
    val coqueteo = if (esSoltero) "Si" else "No"

    calcularSueldo(10.00)
    calcularSueldo(10.00, 15.00, 20.00)
    calcularSueldo(10.00, bonoEspecial = 20.00)
    calcularSueldo(bonoEspecial = 20.00, sueldo = 10.00, tasa = 14.00 )
}

fun imprimirNombre(nombre: String): Unit{
    println("Nombre: ${nombre}")
}


abstract class NumerosJava{
    protected val numeroUno: Int
    private val numeroDos: Int

    constructor(
        uno: Int,
        dos: Int
    ){ //bloque de código del constructor
        this.numeroUno = uno
        this.numeroDos = dos
        println("Inicializando")
    }
}

abstract class Numeros( //Constructor PRIMARIO
    //Ejemplo:
    // uno:Int, (Parametro (sin modificador de acceso))
    // private var uno: Int, //Propiedad publica Clase numeros.uno
    // var uno: Int, //Propiedad de la clase (por defecto es PUBLIC)
    // public var uno: Int,
    protected val numeroUno: Int, // Propiedad de la clase protected numeros.numeroUno
    protected val numeroDos: Int, // propedad de la clase protected numeros.numeroDos
){
    // var cedula: string = "" (public por defecto)
    // private valorCalculado: Int = 0 (private)
    init{//bloque constructor primario
        this.numeroUno; this.numeroUno; //this es opcional
        numeroUno; numeroDos; //sin el "this" es lo mismo
        println("inicializando")
    }
}

class Suma( //Constructor primario suma
    unoParametro: Int, // Parametro
    dosParametro: Int, // Parametro
): Numeros(unoParametro, dosParametro){//Extendiendo y mandando los parametros (super)
    init{
        this.numeroUno
        this.numeroDos
    }
}

fun calcularSueldo(
    sueldo: Double, //requerido
    tasa: Double = 12.00, //opcional (defecto)
    bonoEspecial: Double? = null //Opcion null -> nullable
):Double{
    //int -> Int? (nullable)
    //String -> String? (nullable)
    if(bonoEspecial == null){
        return sueldo * (100/tasa)
    }else{
        return sueldo * (100/tasa) + bonoEspecial
    }
}