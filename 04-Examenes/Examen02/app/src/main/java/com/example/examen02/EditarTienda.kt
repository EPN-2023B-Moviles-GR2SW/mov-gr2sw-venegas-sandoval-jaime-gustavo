package com.example.examen02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class EditarTienda : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_editar_tienda)

    val idAnterior = intent.getStringExtra("id")
    val nombreAnterior = intent.getStringExtra("nombre")
    val direccionAnterior = intent.getStringExtra("direccion")
    System.out.println("Esto llega a la acutalización ${direccionAnterior}")

    val titulo = findViewById<TextView>(R.id.txt_nombre)
    titulo.setText(nombreAnterior)
    val nombreActualizado = findViewById<EditText>(R.id.input_n_nombre_tienda)
    nombreActualizado.setText(nombreAnterior)
    val direccionAcutalizada = findViewById<EditText>(R.id.input_n_direccion_tienda)
    direccionAcutalizada.setText(direccionAnterior)

    val botonEditarTienda = findViewById<Button>(R.id.btn_editar_tienda)
    botonEditarTienda.setOnClickListener{
      System.out.println("Nombre nuevo ${nombreActualizado}")
      System.out.println("Direccion nueva ${direccionAcutalizada}")

      devolverRespuesta(idAnterior.toString().toInt(), nombreActualizado.text.toString(), direccionAcutalizada.text.toString())
    }
  }

  fun devolverRespuesta(id: Int, nombre: String, direccion: String){
    val intentDevolverParametros = Intent()
    intentDevolverParametros.putExtra("idModificado", id)
    intentDevolverParametros.putExtra("nombreNuevo", nombre)
    intentDevolverParametros.putExtra("direccionNueva", direccion)
    setResult(
      RESULT_OK,
      intentDevolverParametros
    )
    finish()
  }
}