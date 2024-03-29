package com.example.examen02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val botonIngresar =findViewById<Button>(R.id.btn_ingresar)
    botonIngresar
      .setOnClickListener { irActividad(ListTienda::class.java) }
  }
  fun irActividad(
    clase: Class<*>
  ){
    val intent = Intent(this, clase)
    startActivity(intent)
  }
}