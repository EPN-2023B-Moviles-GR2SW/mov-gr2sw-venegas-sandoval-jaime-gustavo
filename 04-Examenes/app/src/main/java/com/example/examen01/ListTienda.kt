package com.example.examen01

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts

class ListTienda : AppCompatActivity() {
  var arreglo = BBaseDatosMemoria.arregloTienda
  var posicionTiendaSeleccionada = 0
  private lateinit var adaptador: ArrayAdapter<Tienda>
  var idModificado = 0
  var nombreModificado = ""
  var direccionModificada = ""

  val callbackContenidoIntentExplicito =
    registerForActivityResult(
      ActivityResultContracts.StartActivityForResult()
    ){
      result->
      if(result.resultCode == Activity.RESULT_OK){
        if(result.data != null){
          val data = result.data

          idModificado = data?.getIntExtra("idModificado",-1) ?: -1
          nombreModificado = data?.getStringExtra("nombreModificado") ?: ""
          direccionModificada = data?.getStringExtra("direccionModificada") ?: ""
          anadirTienda(idModificado, nombreModificado, direccionModificada)
        }
      }
    }

  val respuestaActividadActualizacion =
    registerForActivityResult(
      ActivityResultContracts.StartActivityForResult()
    ){
        result->
      if(result.resultCode == Activity.RESULT_OK){
        if(result.data != null){
          val data = result.data
          val nombreNuevo = data?.getStringExtra("nombreNuevo").toString()
          val direccionNueva = data?.getStringExtra("direccionNueva").toString()
          System.out.println("Esto llega a nombre: ${nombreNuevo}")
          System.out.println("Esto llega a direccion: ${direccionNueva}")
          actualizarTienda(nombreNuevo, direccionNueva)
        }
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_list_tienda)

    val listView = findViewById<ListView>(R.id.lv_list_tiendas)
    adaptador = ArrayAdapter(
      this,
      android.R.layout.simple_list_item_1,
      arreglo
    )
    listView.adapter = adaptador

    val botonCrearTienda = findViewById<Button>(R.id.btn_anadir_tienda)
    botonCrearTienda
      .setOnClickListener { abrirActividadconParametros(CrearTienda::class.java) }

    registerForContextMenu(listView)
  }

  fun irActividad(
    clase: Class<*>
  ){
    val intent = Intent(this, clase)
    startActivity(intent)
  }

  fun abrirActividadconParametros(
    clase: Class<*>
  ){
    val intentExplicito = Intent(this, clase)
    intentExplicito.putExtra("id", idModificado)
    intentExplicito.putExtra("nombre", nombreModificado)
    intentExplicito.putExtra("direccion", direccionModificada)

    callbackContenidoIntentExplicito.launch(intentExplicito)
  }

  fun abrirActividadItem(
    clase: Class<*>
  ){
    val intentExplicito = Intent(this, clase)
    System.out.println("entre a esta función")
    System.out.println("${posicionTiendaSeleccionada}")
    intentExplicito.putExtra("id", arreglo[posicionTiendaSeleccionada].id.toString())
    intentExplicito.putExtra("nombre", arreglo[posicionTiendaSeleccionada].nombre)
    intentExplicito.putExtra("direccion", arreglo[posicionTiendaSeleccionada].direccion)

    respuestaActividadActualizacion.launch(intentExplicito)
  }

  fun anadirTienda(
    id: Int,
    nombre: String,
    direccion: String,
  ){
    arreglo.add(
      Tienda(
        id,
        nombre,
        direccion,
      )
    )
    adaptador.notifyDataSetChanged()
  }

  fun actualizarTienda(
    nombre: String,
    direccion: String
  ){
    val tiendaActual = arreglo[posicionTiendaSeleccionada]

    if(tiendaActual.nombre != nombre){
      tiendaActual.nombre = nombre
    }
    if(tiendaActual.direccion != direccion){
      tiendaActual.direccion = direccion
    }

    adaptador.notifyDataSetChanged()

  }

  fun eliminarTienda(posicionEliminar: Int){
    arreglo.removeAt(posicionEliminar)
    adaptador.notifyDataSetChanged()
  }

  override fun onCreateContextMenu(
    menu: ContextMenu?,
    v: View?,
    menuInfo: ContextMenu.ContextMenuInfo?
  ) {
    super.onCreateContextMenu(menu, v, menuInfo)
    val inflater = menuInflater
    inflater.inflate(R.menu.menu, menu)
    val info = menuInfo as AdapterView.AdapterContextMenuInfo
    val posicion = info.position
    posicionTiendaSeleccionada = posicion
    System.out.println("esta es la posicion que envío")
    System.out.println(posicion)
  }

  override fun onContextItemSelected(item: MenuItem): Boolean {
    val tiendaSeleccionada = arreglo[posicionTiendaSeleccionada]
    return when(item.itemId){
      R.id.mi_editar -> {
        abrirActividadItem(EditarTienda::class.java)
        return true
      }
      R.id.mi_eliminar -> {
        abrirDialogo(tiendaSeleccionada.nombre, posicionTiendaSeleccionada)
        return true
      }
      R.id.mi_eliminar -> {
        return true
      }
      else -> super.onContextItemSelected(item)
    }
  }

  fun abrirDialogo(nombreTienda: String, idTienda: Int){
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Eliminar Tienda")
    builder.setPositiveButton(
      "Aceptar",
      DialogInterface.OnClickListener{ dialog, which ->
        eliminarTienda(idTienda)
      }
    )
    builder.setNegativeButton(
      "Cancelar",
      null
    )

    builder.setMessage("Estas seguro de eliminar la tienda ${nombreTienda}")
    val dialogo =builder.create()
    dialogo.show()
  }





}