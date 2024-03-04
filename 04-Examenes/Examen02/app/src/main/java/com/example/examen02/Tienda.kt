package com.example.examen02

class Tienda(
  var id: Long,
  var nombre: String,
  var direccion: String,
) {
  override fun toString(): String {
    return "${id} - ${nombre} - ${direccion}"
  }
}