package com.example.b2023gr2sw

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ESqliteHelperEntrenador(
  contexto: Context?, //This
) : SQLiteOpenHelper(
    contexto,
    "moviles", //nombre BDD
    null,
    1
        ){
  override fun onCreate(db: SQLiteDatabase?) {
    val scriptSQLCrearTablaEntrenador =
      """
        CREATE TABLE Entrenador(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        nombre VARCHAR(50),
        descripcion VARCHAR(50)
        )
      """.trimIndent()
    db?.execSQL(scriptSQLCrearTablaEntrenador)
  }

  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { }

  fun crearEntrenador(
    nombre: String,
    descripcion: String
  ): Boolean{
    val basedatosEscritura = writableDatabase
    val valoresAGuardar = ContentValues()
    valoresAGuardar.put("nombre", nombre)
    valoresAGuardar.put("descripcion", descripcion)
    val resultadoGuardar = basedatosEscritura
      .insert(
        "Entreandor", //Nombre tabla
        null,
        valoresAGuardar //valores
      )
    basedatosEscritura.close()
    return if(resultadoGuardar.toInt() == -1) false else true
  }

  fun eliminarEntrenadorFormulario(id:Int):Boolean{
    val conexionEscritura = writableDatabase
    val parametrosConsultaDelete = arrayOf(id.toString())
    val resultadoEliminacion = conexionEscritura
      .delete(
        "Entrenador", //nombre tabla
        "id=?", //consulta where
        parametrosConsultaDelete
      )
    conexionEscritura.close()
    return if(resultadoEliminacion.toInt()==  -1) false else true
  }

  fun actualizarEntrenadorFormulario(
    nombre: String,
    descripcion: String,
    id: Int,
  ):Boolean{
    val conexionEscritura = writableDatabase
    val valoresaActualizar = ContentValues()
    valoresaActualizar.put("nombre", nombre)
    valoresaActualizar.put("descripcion", descripcion)
    //where ID = ?
    val parametrosConsultaActualizar = arrayOf(id.toString())
    val resultadoActualizacion = conexionEscritura
      .update(
        "Entrenador", //nombre tabla
        valoresaActualizar, //valores
        "id=?", //consulta where
        parametrosConsultaActualizar
      )
    conexionEscritura.close()
    return if(resultadoActualizacion.toInt() == -1) false else true
  }

  fun consultarEntrenadorPorID(id: Int): BEntrenador{
    val baseDatosLectura = readableDatabase
    val scrpitConsultaLectura = """
    SELECT * FROM ENTRENADOR WHERE ID = ?
    """.trimIndent()
    val parametrosConsultaLectura = arrayOf(id.toString())
    val resultadoConsultaLectura = baseDatosLectura.rawQuery(
      scrpitConsultaLectura, //Consulta
      parametrosConsultaLectura, //Parametros
    )

    //logica busqueda
    val existeUsuario = resultadoConsultaLectura.moveToFirst()
    val usuarioEncontrado = BEntrenador(0,"","")
    //se puede almacenar un arreglo
    // val arreglo = arrayListOf<BEntrenador>()
    if(existeUsuario){
      do {
        val id = resultadoConsultaLectura.getInt(0) //indice 0
        val nombre = resultadoConsultaLectura.getString(1)
        val descripcion = resultadoConsultaLectura.getString(2)
        if(id != null){
          //llenar el arreglo con un nuevo BEntrenador
          usuarioEncontrado.id = id
          usuarioEncontrado.nombre = nombre
          usuarioEncontrado.descripcion = descripcion
        }
      }while (resultadoConsultaLectura.moveToNext())
    }
    resultadoConsultaLectura.close()
    baseDatosLectura.close()
    return usuarioEncontrado
  }
}