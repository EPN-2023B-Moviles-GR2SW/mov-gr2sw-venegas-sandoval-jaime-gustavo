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

    val sumaUno = Suma(1, 1)
    val sumaDos = Suma(null, 1)
    val sumaTres = Suma(1, null)
    val sumaCuatro = Suma(null, null)
    sumaUno.sumar()
    sumaDos.sumar()
    sumaTres.sumar()
    sumaCuatro.sumar()
    println(Suma.pi)
    println(Suma.elevarCuadrado(2))
    println(Suma.historialSumas)

    //Arreglo estático
    val arregloEstatico: Array<Int> = arrayOf<Int>(1, 2, 3)
    println(arregloEstatico)

    //Arreglo Dinámico
    val arregloDinamico: ArrayList<Int> = arrayListOf<Int>(
        1,2,3,4,5,6,7,8,9,10
    )

    println(arregloDinamico)
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    println(arregloDinamico)

    //For each -> Unit
    //iterar un arreglo
    val respuestaForEach: Unit = arregloDinamico
        .forEach{valorActual: Int ->
            println("Valor actual: ${valorActual}")
        }
    //it (en inglés eso) significa el elemento iterado
    arregloDinamico.forEach{ println("Valor actual: ${it}") }

    arregloEstatico
        .forEachIndexed {indice: Int, valorActual: Int ->
            println("Valor ${valorActual} Indice: ${indice}")
        }
    println(respuestaForEach)

    //Map -> Muta el arreglo (Cambia el arreglo)
    //1) Enviemos el nuevo valor de la iteración
    //2) Nos devuelve un NUEVO ARREGLO
    //con los valores modificados

    val respuestaMap: List<Double> = arregloDinamico
        .map{ valorActual: Int ->
            return@map valorActual.toDouble() + 100.00
        }

    println(respuestaMap)
    val respuestaMapDos = arregloDinamico.map {it + 15}

    //Filter -> FILTRAR EL ARREGLO
    //1) Devolver una expresión (TRUE O FALSE)
    //2) Nuevo arreglo filtrado
    val respuestaFilter: List<Int> = arregloDinamico
        .filter { valorActual: Int ->
            val mayorACinco: Boolean = valorActual > 5
            return@filter mayorACinco
        }
    val respuestaFilterDos = arregloDinamico.filter { it<=5 }

    println(respuestaFilter)
    println(respuestaFilterDos)

    // OR y AND
    // OR -> ANY (Alguno cumple?)
    // AND -> ALL (Todos cumplen?)

    val respuestaAny: Boolean = arregloDinamico
        .any{valorActual: Int ->
            return@any (valorActual > 5)
        }
    println(respuestaAny) //true

    val respuestaAll: Boolean = arregloDinamico
        .all{valorActual: Int ->
            return@all (valorActual > 5)
        }
    println(respuestaAll) //false

    //reduce -> valor acumulado
    //Valor acumulado = 0 (en Kotlin)
    //[1,2,3,4,5] -> sumeme todos los valores del arreglo
    //valorIteracion 1 = valorEmpieza + 1 = 0 + 1 = 1 -> Iteracion1
    //valorIteracion 2 = valorIteracion1 + 2 = 1 + 2 = 3 -> Iteracion2
    //valorIteracion 3 = valorIteracion2 + 3 = 3 + 3 = 6 -> Iteracion3
    //valorIteracion 4 = valorIteracion3 + 4 = 6 + 4 = 10 -> Iteracion4
    //valorIteracion 5 = valorIteracion4 + 5 = 10 + 5 = 15 -> Iteracion5

    val respuestaReduce: Int = arregloDinamico
        .reduce{//acumulado = 0 -> siempre empieza en 0
            acumulado: Int, valorActual: Int ->
            return@reduce (acumulado + valorActual) //logica del negocio
        }

    println(respuestaReduce) //78

    //acumulado + (itemCarrito.cantidad * itemCarrito.precio)

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

    constructor( //segundo constructor
        uno: Int?, //parametros
        dos: Int //parametros
    ):this(
        if(uno == null) 0 else uno,
        dos
    )

    constructor( //tercer constructor
        uno: Int, //parametros
        dos: Int? //parametros
    ):this(
        uno,
        if(dos == null) 0 else dos,
    )

    constructor( //cuarto constructor
        uno: Int?, //parametros
        dos: Int? //parametros
    ):this( //llamada constructor primario
        if(uno == null) 0 else uno,
        if(dos == null) 0 else uno
    )

    //public por defecto, o usar private o protected
    public fun sumar(): Int{
        val total = numeroUno + numeroDos
        agregarHistorial(total)
        return total
    }

    //atributos y métodos compartidos
    companion object{
        //entre las instancias
        val pi = 3.14
        fun elevarCuadrado(num: Int): Int{
            return num*num
        }
        val historialSumas = arrayListOf<Int>()
        fun agregarHistorial(valorNuevaSuma: Int){
            historialSumas.add(valorNuevaSuma)
        }
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