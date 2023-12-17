import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

fun main() {
  while(true){
    
  }
}

class Producto(
  val id: Int,
  val nombre: String,
  val precio: Double,
  val fechaCaducidad: Date,
  val stock: Int,
)

class Tienda(
  val ruc: String,
  val nombre: String,
  val telefono: String,
  val direccion: String,
  val estaAbierto: Boolean,
  val productos: MutableList<Producto> = mutableListOf()
)

class Archivos {
  companion object {
    private const val archivoTiendas = "tiendas.txt"
    private const val archivoProductos = "productos.txt"
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    fun guardarProductos(productos: List<Producto>) {
      File(archivoProductos).printWriter().use { out ->
        productos.forEach {
          out.println("${it.id}, ${it.nombre}, ${it.precio}, ${it.fechaCaducidad}, ${it.stock}")
        }
      }
    }

    fun guardarTiendas(tiendas: List<Tienda>) {
      File(archivoTiendas).printWriter().use { out ->
        tiendas.forEach {
          out.println("${it.ruc}, ${it.nombre}, ${it.telefono}, ${it.direccion}, ${it.estaAbierto}")
        }
      }
    }

    fun leerProductos(): List<Producto> {
      val listaProductos = mutableListOf<Producto>()
      File(archivoProductos).forEachLine {
        val datos = it.split(",")
        if(datos.size == 5){
          listaProductos.add(
            Producto(
              datos[0].toInt(),
              datos[1],
              datos[2].toDouble(),
              dateFormat.parse(datos[3]),
              datos[4].toInt()
            )
          )
        }
      }
      return listaProductos
    }

    fun leerTiendas(): List<Tienda>{
      val listaTiendas = mutableListOf<Tienda>()
      File(archivoTiendas).forEachLine {
        val datos = it.split(",")
        if(datos.size == 5){
          listaTiendas.add(
            Tienda(
              datos[0],
              datos[1],
              datos[2],
              datos[3],
              datos[4].toBooleanStrict()
            )
          )
        }
      }
      return listaTiendas
    }
  }
}

fun crearTienda():Tienda{
  println("******Nueva Tienda*******")
  println("Ingresa el ruc de la Tienda: ")
  val ruc = readLine() ?: ""
  println("Ingresa el nombre de la Tienda: ")
  val nombre = readLine() ?: ""
  println("Ingresa el telefono de la Tienda: ")
  val telefono = readLine() ?: ""
  println("Ingresa el dirección de la Tienda: ")
  val direccion = readLine() ?: ""
  println("Estado de la tienda abierto o cerrado: ")
  val estado = readLine() ?.toBoolean()?: false

  val tienda = Tienda(ruc, nombre, telefono, direccion, estado)
  Archivos.guardarTiendas(Archivos.leerTiendas() + tienda)
  return tienda
}

fun crearProducto(tienda: int):Producto{
  println("*******Nuevo Producto*******")
  println("Ingresa el nombre del producto: ")
  val nombre = readLine() ?: ""
  println("Ingresa el precio del producto: ")
  val precio = readLine() ?.toDoubleOrNull()?: 0.0
  println("Ingresa la fecha de caducidad del producto(dd/MM/yyyy): ")
  val StringfechaCaducidad = readLine() ?: ""
  val fechaCaducidad: Date = SimpleDateFormat("dd/MM/yyyy").parse(StringfechaCaducidad)
  println("Ingresa el stock o unidades del producto: ")
  val stock = readLine() ?.toInt()?: 0

  val producto = Producto(Archivos.leerProductos().size+1, nombre, precio, fechaCaducidad, stock)
  val tiendasCreadas = Archivos.leerTiendas().map{
    if(it.ruc == tienda){
      it.productos.add(producto)
      it
    }else it
  }
  Archivos.guardarTiendas(tiendasCreadas)
  Archivos.guardarProductos(Archivos.leerProductos() + producto)
  return producto
}

fun listarTiendas(){
  println("Tiendas existentes: ")
  Archivos.leerTiendas().forEach{
    println("${it.ruc} - ${it.nombre}")
  }
}

fun listarProductosPorTienda(rucTienda: String){
  val tienda = Archivos.leerTiendas().find{it.ruc == rucTienda}
  if(tienda != null){
    println("Productos de la tienda ${tienda.nombre}: ")
    tienda.productos.forEach{
      println("${it.id} - ${it.nombre} - ${it.precio}")
    }
  }else{
    println("Tienda no encontrada")
  }
}