package com.example.examen01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class EditarProducto : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_editar_producto)

    val idAnterior = intent.getStringExtra("id")
    val nombreAnterior = intent.getStringExtra("nombre")
    val precioAnterior = intent.getStringExtra("precio")
    val nombreTienda = intent.getStringExtra("nombreTienda")

    val txtNombre = findViewById<TextView>(R.id.txt_nombre_t_producto_a)
    txtNombre.setText(nombreTienda)
    val titulo = findViewById<TextView>(R.id.txt_nombre_producto)
    titulo.setText(nombreAnterior)
    val nombreActualizado = findViewById<EditText>(R.id.input_n_nombre_pro)
    nombreActualizado.setText(nombreAnterior)
    val precioActualizado = findViewById<EditText>(R.id.input_n_precio)
    precioActualizado.setText(precioAnterior)
    val botonActualizar = findViewById<Button>(R.id.btn_actualizar_producto)
    botonActualizar.setOnClickListener{
      devolverRespuesta(idAnterior!!.toInt(), nombreActualizado.text.toString(), precioActualizado.text.toString().toDouble())
    }
  }

  fun devolverRespuesta(id: Int, nombre: String, precio: Double){
    val intentDevolverParametros = Intent()
    intentDevolverParametros.putExtra("idModificado", id)
    intentDevolverParametros.putExtra("nombreNuevo", nombre)
    intentDevolverParametros.putExtra("precioNuevo", precio)
    setResult(
      RESULT_OK,
      intentDevolverParametros
    )
    finish()
  }
}