package com.example.examen01

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ESqliteHelper(
  contexto: Context?,
) : SQLiteOpenHelper(
  contexto,
  "examen",
  null,
1
  ) {
  override fun onCreate(db: SQLiteDatabase?) {
    val scriptSQLTableTienda =
      """
        CREATE TABLE Tienda(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        nombre VARCHAR(50),
        telefono VARCHAR(10),
        descripcion VARCHAR(60)
        )
      """.trimIndent()
    val scriptSQLTableProducto =
      """
        CREATE TABLE Producto(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        nombre VARCHAR(50),
        precio Double,
        stock INTEGER,
        FOREIGN KEY (idTienda) REFERENCES Tienda(id)
        )
      """.trimIndent()
    db?.execSQL(scriptSQLTableTienda)
    db?.execSQL(scriptSQLTableProducto)
  }
}