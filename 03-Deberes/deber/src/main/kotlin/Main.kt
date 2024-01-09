import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun main() {
  Archivos.inicializarArchivos()
  while(true){
    println("************\nMenú*************")
    println("1. Crear Tienda")
    println("2. Crear Producto")
    println("3. Listar Tiendas")
    println("4. Listar productos por Tienda")
    println("5. Actualizar Producto")
    println("6. Eliminar Producto")
    println("7. Actualizar Tienda")
    println("8. Eliminar Tienda")
    println("9. Salir")
    println("Elige una opción: ")
    when (readLine()?.toIntOrNull() ?:0){
      1 ->{
        val tienda = crearTienda()
        println("Tienda ${tienda.nombre} creada con exito")
      }
      2 ->{
        println ("Elige la tienda para agregar el producto:")
        listarTiendas()
        print("Ingresa el RUC de la tienda a la cuál quieres agregar e producto:")
        val rucTienda = readLine()?.toString() ?: ""
        if (rucTienda != ""){
          val producto = crearProducto(rucTienda)
          println("Producto cread: ${producto.nombre}")
        }else{
          println("RUC no válido. No existe la tienda")
        }
      }
      3 ->{
        listarTiendas()
      }
      4 ->{
        println("Elige la tienda para listar sus productos:")
        listarTiendas()
        print("Ingresa el RUC de la tienda: ")
        val rucTienda = readLine()?.toString() ?: ""
        if(rucTienda != ""){
          listarProductosPorTienda(rucTienda)
        }else{
          println("RUC no válido. No existe la tienda")
        }
      }
      5 ->{
        println("Elige el producto para actualizar")
        Archivos.leerProductos().forEach(){ println("${it.id} - ${it.nombre}") }
        print("Ingresa el id del producto: ")
        val productoID = readLine()?.toIntOrNull() ?: 0
        if (productoID != 0){
          actualizarProducto(productoID)
        }else{
          println("ID de producto inválido")
        }
      }
      6 ->{
        println("Elige el producto a eliminar: ")
        Archivos.leerProductos().forEach{ println("${it.id} - ${it.nombre}") }
        print("Ingresa el ID del producto: ")
        val productoID = readLine()?.toIntOrNull() ?:0
        if(productoID != 0){
          eliminarProducto(productoID)
        }else{
          println("ID de producto inválido")
        }
      }
      7 ->{
        println("Elige la tienda para actualizar")
        Archivos.leerTiendas().forEach(){ println("${it.ruc} - ${it.nombre}") }
        print("Ingresa el ruec de la tienda: ")
        val rucTienda = readLine()?: ""
        if (rucTienda != ""){
          actualizarTienda(rucTienda)
        }else{
          println("ID de tienda inválido")
        }
      }
      8 ->{
        println("Elige la tienda a eliminar: ")
        Archivos.leerTiendas().forEach{ println("${it.ruc} - ${it.nombre}") }
        print("Ingresa el ID de la tienda: ")
        val rucTienda = readLine()?: ""
        if(rucTienda != ""){
          eliminarTienda(rucTienda)
        }else{
          println("ID de tienda inválido")
        }
      }
      9 -> return
        else -> println("Opción inválida. Intenta de nuevo")
    }
  }
}
class Producto(
  val id: Int,
  val nombre: String,
  val precio: Double,
  val fechaCaducidad: Date,
  val stock: Int,
  val tienda: Tienda?
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
    private const val archivoTiendas = "C:/Users/Gustavo/Documents/Gustavo/7semestre/AplicacionesMoviles/mov-gr2sw-venegas-sandoval-jaime-gustavo/03-Deberes/deber/tiendas.txt"
    private const val archivoProductos = "C:/Users/Gustavo/Documents/Gustavo/7semestre/AplicacionesMoviles/mov-gr2sw-venegas-sandoval-jaime-gustavo/03-Deberes/deber/productos.txt"
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    fun inicializarArchivos() {
      val archivoProductos = File(archivoProductos)
      val archivoTiendas = File(archivoTiendas)

      if (!archivoProductos.exists()) {
        archivoProductos.createNewFile()
      }

      if (!archivoTiendas.exists()) {
        archivoTiendas.createNewFile()
      }
    }
    fun guardarProductos(productos: List<Producto>) {
      File(archivoProductos).printWriter().use { out ->
        productos.forEach {
          out.println("${it.id},${it.nombre},${it.precio},${it.fechaCaducidad},${it.stock},${it.tienda?.ruc}")
        }
      }
    }

    fun guardarTiendas(tiendas: List<Tienda>) {
      File(archivoTiendas).printWriter().use { out ->
        tiendas.forEach {
          out.println("${it.ruc},${it.nombre},${it.telefono},${it.direccion},${it.estaAbierto}")
        }
      }
    }

    fun leerProductosPorTienda(rucTienda: String): List<Producto> {
      val listaProductos = mutableListOf<Producto>()
      File(archivoProductos).forEachLine {
        val datos = it.split(",")
        if(datos.size == 6){
          val rucTien = datos[5]
          val formatoFecha = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
          val fechaCaducidad: Date = formatoFecha.parse(datos[3].trim())
          if(rucTien == rucTienda){
            listaProductos.add(
              Producto(
                datos[0].toInt(),
                datos[1],
                datos[2].toDouble(),
                fechaCaducidad,
                datos[4].trim().toInt(),
                Tienda("","","","",false)
              )
            )
          }

        }
      }
      return listaProductos
    }
    fun leerProductos(): List<Producto> {
      val listaProductos = mutableListOf<Producto>()
      File(archivoProductos).forEachLine {
        val datos = it.split(",")
        if(datos.size == 6){
          val rucTienda = datos[5]
          val tienda = leerTiendas().find { it.ruc == rucTienda }
          val formatoFecha = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
          val fechaCaducidad: Date = formatoFecha.parse(datos[3].trim())
          listaProductos.add(
            Producto(
              datos[0].toInt(),
              datos[1],
              datos[2].toDouble(),
              fechaCaducidad,
              datos[4].trim().toInt(),
              tienda
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

          val estaAbierto = when (val estado = datos[4].trim().lowercase()) {
            "true" -> true
            "false" -> false
            else -> {
              println("Valor inválido para 'estaAbierto': $estado. Se ignorará esta entrada.")
              return@forEachLine  // Salir del forEachLine si el valor no es "true" o "false"
            }
          }

          val tienda = Tienda(
            datos[0],
            datos[1],
            datos[2],
            datos[3],
            estaAbierto
          )

          val productosAsociados = leerProductosPorTienda(tienda.ruc)
          tienda.productos.addAll(productosAsociados)

          listaTiendas.add(tienda)
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

  var condicion: Boolean? = null
  while (condicion == null) {
    println("Estado de la tienda (abierto/cerrado): ")
    val estado = readLine()?.lowercase() ?: ""
    condicion = when (estado) {
      "abierto" -> true
      "cerrado" -> false
      else -> {
        println("Valor ingresado incorrecto. Por favor, ingresa 'abierto' o 'cerrado'.")
        continue
      }
    }
  }

  val tienda = Tienda(ruc, nombre, telefono, direccion, condicion!!)
  Archivos.guardarTiendas(Archivos.leerTiendas() + tienda)
  return tienda
}

fun crearProducto(rucTienda: String):Producto{
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
  val tienda = Archivos.leerTiendas().find{it.ruc == rucTienda}

  if(tienda != null){
    val producto = Producto(Archivos.leerProductos().size+1, nombre, precio, fechaCaducidad, stock, tienda)
    tienda.productos.add(producto)
    println(tienda.productos)
    println(producto)
    Archivos.guardarTiendas(Archivos.leerTiendas());
    val productos = Archivos.leerProductos() + producto
    Archivos.guardarProductos(productos)

    return producto
  }else{
    println("Tienda no encontrada")
    return Producto(0,"",0.0,fechaCaducidad, 0, null)
  }

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
    if (tienda.productos.isNotEmpty()) {
      tienda.productos.forEach { producto ->
        println("${producto.id} - ${producto.nombre} - ${producto.precio}")
      }
    } else {
      println("No hay productos en esta tienda.")
    }
  }else{
    println("Tienda no encontrada")
  }
}

fun actualizarProducto(idProducto: Int){
  val producto = Archivos.leerProductos().find{it.id == idProducto}
  if(producto != null){
    println("****Editando producto: ${producto.nombre}*****")
    print("Ingresa el nuevo nombre (vacío para mantener el anterior)")
    val nuevoNombre = readLine() ?: ""


    print("Ingresa el nuevo precio (0.0 para mantener el anterior): ")
    val nuevoPrecioInput = readLine()
    val nuevoPrecio = if (!nuevoPrecioInput.isNullOrBlank()) {
      nuevoPrecioInput.toDoubleOrNull() ?: producto.precio
    } else {
      producto.precio
    }

    print("Ingresa el nuevo stock (0 para mantener el anterior): ")
    val nuevoStockInput = readLine()
    val nuevoStock = if (!nuevoStockInput.isNullOrBlank()) {
      nuevoStockInput.toIntOrNull() ?: producto.stock
    } else {
      producto.stock
    }

    val productoActualizado = Archivos.leerProductos().map {
      if (it.id == idProducto) {
        Producto(
          it.id,
          if (nuevoNombre.isNotBlank()) nuevoNombre else it.nombre,
          nuevoPrecio,
          it.fechaCaducidad,
          nuevoStock,
          it.tienda
        )
      } else it
    }
    Archivos.guardarProductos(productoActualizado)
    println("Producto Actualizado")
  }else{
    println("Producto no encontrado")
  }
}

fun actualizarTienda(rucTienda: String){
  val tienda = Archivos.leerTiendas().find { it.ruc == rucTienda }
  if(tienda != null){
    println("***** Editando la tienda: ${tienda.nombre} ******")
    print("Nuevo nombre (vacío para mantener el actual)")
    val nuevoNombre = readLine() ?: ""
    print("Nuevo telefono (vacío para mantener el actual)")
    val nuevoTelefono = readLine() ?: ""
    print("Nueva dirección (vacío para mantener el actual)")
    val nuevoDireccion = readLine() ?: ""
    println("Abrir/Cerrar Tienda (true:abierto / false: cerrado")
    val nuevaCondicion = readLine()?.toBoolean() ?: false

    val tiendaActualizada = Archivos.leerTiendas().map{
      if(it.ruc == rucTienda){
        Tienda(
          it.ruc,
          if(nuevoNombre.isNotBlank()) nuevoNombre else it.nombre,
          if(nuevoTelefono.isNotBlank()) nuevoTelefono else it.nombre,
          if(nuevoDireccion.isNotBlank()) nuevoDireccion else it.nombre,
          nuevaCondicion
        )
      }else it
    }
    Archivos.guardarTiendas(tiendaActualizada)
    println("Tienda actualizada")
  }else{
    println("Tienda no encontrada")
  }
}

fun eliminarProducto(idProducto: Int){
  val producto = Archivos.leerProductos().find { it.id == idProducto }
  if (producto != null){
    val productosActualizados = Archivos.leerProductos().filterNot { it.id == idProducto }
    Archivos.guardarProductos(productosActualizados)

    val tiendasActualizadas = Archivos.leerTiendas().map{
      if(it.productos.removeIf{prod -> prod.id == idProducto}){
        it
      }else{
        it
      }
    }
    Archivos.guardarTiendas(tiendasActualizadas)
    println("Producto eliminado")
  }else{
    println("Producto no encontrado")
  }
}

fun eliminarTienda(rucTienda: String){
  val tienda = Archivos.leerTiendas().find{ it.ruc == rucTienda}
  if(tienda != null){
    val productosActualizados = Archivos.leerProductos().filterNot { it.tienda?.ruc == rucTienda }
    Archivos.guardarProductos(productosActualizados)

    val tiendasActualizadas = Archivos.leerTiendas().filterNot { it.ruc == rucTienda }
    Archivos.guardarTiendas(tiendasActualizadas)

    println("Tienda eliminada")
  }else{
    println("Tienda no encontrada")
  }
}