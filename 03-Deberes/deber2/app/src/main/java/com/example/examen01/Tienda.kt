package com.example.examen01

class Tienda(
  var id: Int,
  var nombre: String,
  var direccion: String,
) {
  override fun toString(): String {
    return "${id} - ${nombre} - ${direccion}"
  }
}