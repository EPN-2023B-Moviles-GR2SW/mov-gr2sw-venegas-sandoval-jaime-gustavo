package com.example.examen01

import java.io.Serializable

class Producto(
  var id: Int,
  var nombre: String,
  var precio: Double,
  var idTienda: Int,
) {
  override fun toString(): String {
    return "${nombre} - $${precio}"
  }
}