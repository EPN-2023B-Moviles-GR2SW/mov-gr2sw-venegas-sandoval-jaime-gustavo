package com.example.examen02

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
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList
class ListTienda : AppCompatActivity() {
  val arreglo: ArrayList<Tienda> = arrayListOf()
  private lateinit var adaptador: ArrayAdapter<Tienda>
  var query: Query? = null
  var idModificado = 0
  var nombreModificado = ""
  var direccionModificada = ""
  var posicionTiendaSeleccionada = 0

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
          crearTienda(idModificado, nombreModificado, direccionModificada)
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
          val id = data?.getIntExtra("idModificado", 1) ?: -1
          val nombreNuevo = data?.getStringExtra("nombreNuevo").toString()
          val direccionNueva = data?.getStringExtra("direccionNueva").toString()
          actualizarTienda(id, nombreNuevo, direccionNueva)
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
    adaptador.notifyDataSetChanged()

    consultarTienda()

    val botonCrearTienda = findViewById<Button>(R.id.btn_anadir_tienda)
    botonCrearTienda
      .setOnClickListener { abrirActividadconParametros(CrearTienda::class.java) }

    registerForContextMenu(listView)
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
    val idTienda = arreglo[posicionTiendaSeleccionada].id.toString()
    val nombreTienda = arreglo[posicionTiendaSeleccionada].nombre

    intentExplicito.putExtra("idTiendaPertenece", idTienda)
    intentExplicito.putExtra("nombreTienda", nombreTienda)
    System.out.println("esto mando a la actividad producto ${idTienda}")
    startActivity(intentExplicito)
  }


  fun mostrarSnackbar(texto:String){
    Snackbar.make(
      findViewById(R.id.id_layout_tiendas),
      texto,
      Snackbar.LENGTH_LONG
    )
      .show()
  }
  fun crearTienda(
    id: Int,
    nombre: String,
    direccion: String
  ){
    val db = Firebase.firestore
    val tiendas = db.collection("tiendas")

    val data = hashMapOf(
      "id" to id,
      "nombre" to nombre,
      "direccion" to direccion
    )
    tiendas.document("${id}").set(data)
      .addOnSuccessListener { mostrarSnackbar("Tienda ${nombre} creada con éxito") }
    consultarTienda()
  }

  fun actualizarTienda(
    id: Int,
    nombre: String,
    direccion: String,
  ){
    val db = Firebase.firestore
    val tiendas = db.collection("tiendas")

    val data = hashMapOf(
      "id" to id,
      "nombre" to nombre,
      "direccion" to direccion
    )

    tiendas.document("${id}").set(data)
      .addOnSuccessListener { mostrarSnackbar("Tienda actualizada con éxito") }
    consultarTienda()
  }

  fun limpiarArreglo(){
    arreglo.clear()
  }
  fun anadiraArreglo(
    tienda: QueryDocumentSnapshot
  ){
    val nuevaTienda = Tienda(
      tienda.data.get("id") as Long,
      tienda.data.get("nombre") as String,
      tienda.data.get("direccion") as String,
    )
    arreglo.add(nuevaTienda)
  }

  fun consultarTienda( ){
    val db = Firebase.firestore
    val tiendaRef =db.collection("tiendas")
    var tarea: Task<QuerySnapshot>? = null
    tarea = tiendaRef.get()
    limpiarArreglo()
    if(tarea != null){
      tarea
        .addOnSuccessListener { documentSnapshots ->
          guardarQuery(documentSnapshots, tiendaRef)
          for(tienda in documentSnapshots){
            anadiraArreglo(tienda)
          }
          adaptador.notifyDataSetChanged()
        }
        .addOnFailureListener{}
    }
  }

  fun guardarQuery(
    documentSnapshots: QuerySnapshot,
    ref: Query
  ) {
    if (documentSnapshots.size() > 0) {
      val ultimoDocumento = documentSnapshots
        .documents[documentSnapshots.size() - 1]
      query = ref
        // Start After nos ayuda a paginar
        .startAfter(ultimoDocumento)
    }
  }

  fun eliminarRegistro(
    id: Long
  ) {
    val db = Firebase.firestore
    val tiendaRef = db
      .collection("tiendas")

    tiendaRef
      .document("${id}")
      .delete() // elimina
      .addOnCompleteListener { mostrarSnackbar("Tienda eliminada") }
      .addOnFailureListener { /* Si algo salio mal*/ }
    consultarTienda()
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

  fun abrirDialogo(nombreTienda: String, idTienda: Long){
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Eliminar Tienda")
    builder.setPositiveButton(
      "Aceptar",
      DialogInterface.OnClickListener{ dialog, which ->
        eliminarRegistro(idTienda)
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