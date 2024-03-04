package com.example.examen01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class CrearTienda : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_crear_tienda)

    val nombreInput = findViewById<EditText>(R.id.input_nombre_tienda)
    val idInput = findViewById<EditText>(R.id.input_id_tienda)
    val direccionInput = findViewById<EditText>(R.id.input_direccion_tienda)
    val botonCrear = findViewById<Button>(R.id.btn_crear_tienda)
    botonCrear
      .setOnClickListener{devolverRespuesta(idInput.text.toString().toInt(), nombreInput.text.toString(), direccionInput.text.toString())}
  }

  fun devolverRespuesta(id: Int, nombre: String, direccion: String){
    val intentDevolverParametros = Intent()
    intentDevolverParametros.putExtra("idModificado", id)
    intentDevolverParametros.putExtra("nombreModificado", nombre)
    intentDevolverParametros.putExtra("direccionModificada", direccion)
    setResult(
      RESULT_OK,
      intentDevolverParametros
    )
    finish()
  }
}