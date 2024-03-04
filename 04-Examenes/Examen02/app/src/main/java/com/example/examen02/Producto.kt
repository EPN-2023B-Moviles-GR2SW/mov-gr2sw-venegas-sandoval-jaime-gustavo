package com.example.examen02

class Producto(
  var id: Long,
  var nombre: String,
  var precio: Double,
) {
  override fun toString(): String {
    return "${nombre} - $${precio}"
  }
}