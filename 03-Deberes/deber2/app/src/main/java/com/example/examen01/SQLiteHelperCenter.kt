package com.example.examen01

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelperCenter (
  contexto: Context?,
) : SQLiteOpenHelper(
  contexto,
  "deber",
  null,
  1
){
  override fun onCreate(db: SQLiteDatabase?) {
    val scriptSQLCrearTablaTienda =
      """
        CREATE TABLE Tienda(
        id INTEGER PRIMARY KEY,
        nombre VARCHAR(50),
        direccion VARCHAR(50)
        )
        
        """.trimIndent()
    val scriptSQLCreateTablaProducto =
      """
        CREATE TABLE Producto(
        id INTEGER PRIMARY KEY,
        nombre VARCHAR(50),
        precio DOUBLE,
        idTienda INTEGER,
        FOREIGN KEY (idTienda) REFERENCES Tienda(id)
        )
      """.trimIndent()
    db?.execSQL(scriptSQLCrearTablaTienda)
    db?.execSQL(scriptSQLCreateTablaProducto)
  }

  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { }

  fun crearTienda(
    id: Int,
    nombre: String,
    direccion: String
  ): Boolean{
    val baseDatosEscritura = writableDatabase
    val valoresAGuardar = ContentValues()
    valoresAGuardar.put("id", id)
    valoresAGuardar.put("nombre", nombre)
    valoresAGuardar.put("descripcion", direccion)
    val resultadoGuardar = baseDatosEscritura
        .insert(
          "Tienda",
          null,
          valoresAGuardar
        )
    baseDatosEscritura.close()
    return if(resultadoGuardar.toInt() == -1) false else true
  }

  fun actualizarTienda(
    nombre: String,
    descripcion: String,
    id: Int,
  ):Boolean{
    val conexionEscritura = writableDatabase
    val valoresActualizar = ContentValues()
    valoresActualizar.put("nombre", nombre)
    valoresActualizar.put("descripcion", descripcion)
    val parametrosConsultaActualizar = arrayOf(id.toString())
    val resultadoActualizacion = conexionEscritura
      .update(
        "Tienda",
        valoresActualizar,
        "id=?",
        parametrosConsultaActualizar
      )
    conexionEscritura.close()
    return if(resultadoActualizacion.toInt() == -1) false else true
  }

  fun actualizarProducto(
    nombre: String,
    precio: Double,
    id: Int,
  ):Boolean{
    val conexionEscritura = writableDatabase
    val valoresActualizar = ContentValues()
    valoresActualizar.put("nombre", nombre)
    valoresActualizar.put("precio", precio)
    val parametrosConsultaActualizar = arrayOf(id.toString())
    val resultadoActualizacion = conexionEscritura
      .update(
        "Producto",
        valoresActualizar,
        "id=?",
        parametrosConsultaActualizar
      )
    conexionEscritura.close()
    return if(resultadoActualizacion.toInt() == -1) false else true
  }

  fun crearProducto(
    id: Int,
    nombre: String,
    precio: Double,
    idTienda: Int,
  ) : Boolean {
    val baseDatosEscritura = writableDatabase
    val valoresAGuardar = ContentValues()
    valoresAGuardar.put("id", id)
    valoresAGuardar.put("nombre", nombre)
    valoresAGuardar.put("precio", precio)
    valoresAGuardar.put("idTienda", idTienda)
    val resultadoGuardar = baseDatosEscritura
      .insert(
        "Producto",
        null,
        valoresAGuardar
      )
    baseDatosEscritura.close()
    return if(resultadoGuardar.toInt() == -1) false else true
  }

  fun eliminarTienda(id: Int) : Boolean {
    val conexionEscritura = writableDatabase
    val parametrosConsultaDelete = arrayOf(id.toString())

    val resultadoEliminacionProductos = conexionEscritura
      .delete(
        "Producto",
        "idTienda=?",
        parametrosConsultaDelete
      )
    val resultadoEliminacion = conexionEscritura
      .delete(
        "Tienda",
        "id=?",
        parametrosConsultaDelete
      )
    conexionEscritura.close()
    return if(resultadoEliminacion.toInt() == -1) false else true
  }

  fun eliminarProducto(id: Int) : Boolean {
    val conexionEscritura = writableDatabase
    val parametrosConsultaDelete = arrayOf(id.toString())
    val resultadoEliminacion = conexionEscritura
      .delete(
        "Producto",
        "id=?",
        parametrosConsultaDelete
      )
    conexionEscritura.close()
    return if(resultadoEliminacion.toInt() == -1) false else true
  }

  fun consultarTiendaPorID(id: Int) : Tienda{
    val baseDatosLectura = readableDatabase
    val scriptConsultaLectura = """
      SELECT * FROM Tienda WHERE id = ?
      """.trimIndent()
    val parametrosConsultaLectura = arrayOf(id.toString())
    val resultadoConsultaLectura = baseDatosLectura.rawQuery(
      scriptConsultaLectura,
      parametrosConsultaLectura,
    )

    val existeUsuario = resultadoConsultaLectura.moveToFirst()
    val tiendaEncontrada = Tienda(0, "","")
    if(existeUsuario){
      do{
        val id = resultadoConsultaLectura.getInt(0)
        val nombre = resultadoConsultaLectura.getString(1)
        val direccion = resultadoConsultaLectura.getString(2)
        if(id != null){
          tiendaEncontrada.id = id
          tiendaEncontrada.nombre = nombre
          tiendaEncontrada.direccion = direccion
        }
      }while (resultadoConsultaLectura.moveToNext())
    }
    resultadoConsultaLectura.close()
    baseDatosLectura.close()
    return tiendaEncontrada
  }

  fun consultarTiendas() : ArrayList<Tienda>{
    val baseDatosLectura = readableDatabase
    val scriptConsultaLectura = """
      SELECT * FROM Tienda
      """.trimIndent()
    val resultadoConsultaLectura = baseDatosLectura.rawQuery(
      scriptConsultaLectura,
      null
    )

    val existeUsuario = resultadoConsultaLectura.moveToFirst()
    val tiendasEncontradas = ArrayList<Tienda>()
    if(existeUsuario){
      do{
        val id = resultadoConsultaLectura.getInt(0)
        val nombre = resultadoConsultaLectura.getString(1)
        val direccion = resultadoConsultaLectura.getString(2)
        val tienda = Tienda(id, nombre, direccion)
        tiendasEncontradas.add(tienda)
      }while (resultadoConsultaLectura.moveToNext())
    }
    resultadoConsultaLectura.close()
    baseDatosLectura.close()
    return tiendasEncontradas
  }

  fun consultarProductoTienda(idTienda: Int) : ArrayList<Producto>{
    val baseDatosLectura = readableDatabase
    val scriptConsultaLectura = """
      SELECT * FROM Producto WHERE idTienda = ?
      """.trimIndent()
    val parametrosConsultaLectura = arrayOf(idTienda.toString())
    val resultadoConsultaLectura = baseDatosLectura.rawQuery(
      scriptConsultaLectura,
      parametrosConsultaLectura
    )

    val existeTienda = resultadoConsultaLectura.moveToFirst()
    val productosEncontrados = ArrayList<Producto>()
    if(existeTienda){
      do{
        val id = resultadoConsultaLectura.getInt(0)
        val nombre = resultadoConsultaLectura.getString(1)
        val precio = resultadoConsultaLectura.getDouble(2)
        val idTienda = resultadoConsultaLectura.getInt(3)
        val producto = Producto(id,nombre,precio,idTienda)
        productosEncontrados.add(producto)
      }while (resultadoConsultaLectura.moveToNext())
    }
    resultadoConsultaLectura.close()
    baseDatosLectura.close()
    return productosEncontrados
  }
}