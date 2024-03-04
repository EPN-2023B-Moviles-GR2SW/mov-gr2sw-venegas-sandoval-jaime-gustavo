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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.QueryDocumentSnapshot


class ListTienda : AppCompatActivity() {
  private lateinit var arreglo: ArrayList<Tienda>
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
          actualizarTienda(nombreNuevo, direccionNueva)
          mostrarSnackbar("Tienda ${nombreNuevo} actualizada con éxito")
        }
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    TBaseDatos.tablas = SQLiteHelperCenter(this)
    arreglo = TBaseDatos.tablas!!.consultarTiendas()
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_list_tienda)

    val listView = findViewById<ListView>(R.id.lv_list_tiendas)
    adaptador = ArrayAdapter(
      this,
      android.R.layout.simple_list_item_1,
      arreglo
    )
    listView.adapter = adaptador
    adaptador.notifyDataSetChanged()

    val botonCrearTienda = findViewById<Button>(R.id.btn_anadir_tienda)
    botonCrearTienda
      .setOnClickListener { abrirActividadconParametros(CrearTienda::class.java) }

    registerForContextMenu(listView)
  }

  fun mostrarSnackbar(texto:String){
    Snackbar.make(
      findViewById(R.id.id_layout_tiendas),
      texto,
      Snackbar.LENGTH_LONG
    )
      .show()
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

  fun abrirACtividadProducto(
    clase: Class<*>
  ){
    val intentExplicito = Intent(this, clase)
    val idTienda = arreglo[posicionTiendaSeleccionada].id
    val nombreTienda = arreglo[posicionTiendaSeleccionada].nombre

    intentExplicito.putExtra("idTienda", idTienda)
    intentExplicito.putExtra("nombreTienda", nombreTienda)

    startActivity(intentExplicito)
  }

  fun actualizarArreglo() {
    arreglo = TBaseDatos.tablas!!.consultarTiendas()
    adaptador.clear()
    arreglo.forEach {
      adaptador.insert(it, adaptador.count)
    }
  }
  fun anadirTienda(
    id: Int,
    nombre: String,
    direccion: String,
  ){
    val respuesta = TBaseDatos.tablas?.crearTienda(id, nombre, direccion)
    if(respuesta == true)  {
      mostrarSnackbar("Tienda ${nombreModificado} creada con éxito")
      actualizarArreglo()
    }else{
      mostrarSnackbar("Tienda Error")
    }
  }

  fun actualizarTienda(
    nombre: String,
    direccion: String
  ){
    val idTienda = arreglo[posicionTiendaSeleccionada].id
    val respuesta = TBaseDatos.tablas?.actualizarTienda(nombre,direccion,idTienda)
    if(respuesta == true){
      mostrarSnackbar("Tienda actualizada con éxito")
      actualizarArreglo()
    }else{
      mostrarSnackbar("Algo salió mal")
    }


  }

  fun eliminarTienda(idEliminar: Int){
    val respuesta = TBaseDatos.tablas?.eliminarTienda(idEliminar)
    if (respuesta == true){
      mostrarSnackbar("Tienda eliminada con éxito")
      actualizarArreglo()
    }else{
      mostrarSnackbar("Algo salió mal")
    }
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
        abrirDialogo(tiendaSeleccionada.nombre, tiendaSeleccionada.id)
        return true
      }
      R.id.mi_ver_productos -> {
        abrirACtividadProducto(ListProducto::class.java)
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