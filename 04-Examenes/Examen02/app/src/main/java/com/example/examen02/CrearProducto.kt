package com.example.examen02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class CrearProducto : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_crear_producto)

    val idProducto = findViewById<EditText>(R.id.input_id_producto)
    val nombreProducto = findViewById<EditText>(R.id.input_nombre_producto)
    val precioProducto = findViewById<EditText>(R.id.input_precio_producto)
    val botonAnadirProducto = findViewById<Button>(R.id.btn_anadir_producto)
    val nombreTiendaPertenece = intent.getStringExtra("nombreTienda")
    val txtNombreTienda = findViewById<TextView>(R.id.txt_nombre_t_pro)
    txtNombreTienda.setText(nombreTiendaPertenece)

    botonAnadirProducto.setOnClickListener{
      devolverRespuesta(
        idProducto.text.toString().toInt(), nombreProducto.text.toString(), precioProducto.text.toString().toDouble())}
  }

  fun devolverRespuesta(idProducto: Int, nombreProducto: String, precioProducto: Double){
    val intentDevolverParametros = Intent()
    intentDevolverParametros.putExtra("idProducto", idProducto)
    intentDevolverParametros.putExtra("nombreProducto", nombreProducto)
    intentDevolverParametros.putExtra("precioProducto", precioProducto)
    setResult(
      RESULT_OK,
      intentDevolverParametros
    )
    finish()
  }
}