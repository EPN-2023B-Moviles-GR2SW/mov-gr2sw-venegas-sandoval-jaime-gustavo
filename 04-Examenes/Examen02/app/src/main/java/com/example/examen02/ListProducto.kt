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
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListProducto : AppCompatActivity() {
  private lateinit var adaptador: ArrayAdapter<Producto>
  val arregloProdutos: ArrayList<Producto> = arrayListOf()
  var query: Query? = null
  var idProducto = 0
  var nombreProducto = ""
  var precioProducto = 0.0
  var posicionItemSeleccionado = 0
  var nombreTiendaPertenece = ""
  var idTiendaPertenece = ""

  val callbackContenidoIntentExplicito =
    registerForActivityResult(
      ActivityResultContracts.StartActivityForResult()
    ){
        result->
      if(result.resultCode == Activity.RESULT_OK){
        if(result.data != null){
          val data = result.data

          idProducto = data?.getIntExtra("idProducto",-1) ?: -1
          nombreProducto = data?.getStringExtra("nombreProducto") ?: ""
          precioProducto = data?.getDoubleExtra("precioProducto", 0.0) ?: 0.0
          crearProducto(idProducto,nombreProducto,precioProducto)
        }
      }
    }

  val respuestaProductoActualizado =
    registerForActivityResult(
      ActivityResultContracts.StartActivityForResult()
    ){
        result ->
      if(result.resultCode == Activity.RESULT_OK){
        if(result.data != null){
          val data = result.data
          val idProducto = data?.getIntExtra("idModificado", -1) ?: -1
          val nombreNuevo = data?.getStringExtra("nombreNuevo").toString()
          val precioNuevo = data?.getDoubleExtra("precioNuevo", 0.0) ?: 0.0
          actualizarProducto(idProducto, nombreNuevo, precioNuevo)
        }
      }
    }
  override fun onCreate(savedInstanceState: Bundle?) {
    idTiendaPertenece = intent.getStringExtra("idTiendaPertenece").toString()
    nombreTiendaPertenece = intent.getStringExtra("nombreTienda").toString()
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_list_producto)

    val listView = findViewById<ListView>(R.id.lv_list_productos)
    System.out.println("esto llega a la actividad Productos ${idTiendaPertenece}")

    // Crear el adaptador con la lista de productos
    adaptador = ArrayAdapter(
      this,
      android.R.layout.simple_list_item_1,
      arregloProdutos
    )
    listView.adapter = adaptador

    consultarProducto()


    val botonCrearPoducto = findViewById<Button>(R.id.btn_crear_producto)
    botonCrearPoducto.setOnClickListener{ abrirActividadconParametros(CrearProducto::class.java) }

    val txtnombreTiendaProducto = findViewById<TextView>(R.id.txt_nombre_tienda_productos)
    txtnombreTiendaProducto.setText(nombreTiendaPertenece)

    registerForContextMenu(listView)
  }

  fun mostrarSnackbar(texto:String){
    Snackbar.make(
      findViewById(R.id.id_layout_producto),
      texto,
      Snackbar.LENGTH_LONG
    )
      .show()
  }
  fun abrirActividadconParametros(
    clase: Class<*>
  ){
    val intentExplicito = Intent(this, clase)
    intentExplicito.putExtra("idProducto", idProducto)
    intentExplicito.putExtra("nombre", nombreProducto)
    intentExplicito.putExtra("precio", precioProducto)
    intentExplicito.putExtra("nombreTienda", nombreTiendaPertenece)

    callbackContenidoIntentExplicito.launch(intentExplicito)
  }

  fun abrirActividadItem(
    clase: Class<*>
  ){
    val intentExplicito = Intent(this,clase)
    intentExplicito.putExtra("id", arregloProdutos[posicionItemSeleccionado].id.toString())
    intentExplicito.putExtra("nombre", arregloProdutos[posicionItemSeleccionado].nombre)
    intentExplicito.putExtra("precio", arregloProdutos[posicionItemSeleccionado].precio.toString())
    intentExplicito.putExtra("nombreTienda", nombreTiendaPertenece)

    respuestaProductoActualizado.launch(intentExplicito)
  }
  fun limpiarArreglo(){
    arregloProdutos.clear()
  }

  fun anadiraArreglo(
    producto: QueryDocumentSnapshot
  ){
    val nuevoProducto = Producto(
      producto.data.get("id") as Long,
      producto.data.get("nombre") as String,
      producto.data.get("precio") as Double,
    )
    arregloProdutos.add(nuevoProducto)
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
  fun consultarProducto( ){
    val db = Firebase.firestore
    val tiendaRef =db.collection("tiendas/${idTiendaPertenece}/productos")
    var tarea: Task<QuerySnapshot>? = null
    tarea = tiendaRef.get()
    limpiarArreglo()
    if(tarea != null){
      tarea
        .addOnSuccessListener { documentSnapshots ->
          guardarQuery(documentSnapshots, tiendaRef)
          for(producto in documentSnapshots){
            anadiraArreglo(producto)
          }
          adaptador.notifyDataSetChanged()
        }
        .addOnFailureListener{}
    }
  }
  fun crearProducto(
    id: Int,
    nombre: String,
    precio: Double
  ){
    val db = Firebase.firestore
    val productos = db.collection("tiendas/${idTiendaPertenece}/productos")

    val data = hashMapOf(
      "id" to id,
      "nombre" to nombre,
      "precio" to precio
    )
    productos.document("${id}").set(data)
      .addOnSuccessListener { mostrarSnackbar("Tienda ${nombre} creada con éxito") }
    consultarProducto()
  }

  fun actualizarProducto(
    id: Int,
    nombre: String,
    precio: Double
  ){
    val db = Firebase.firestore
    val tiendas = db.collection("tiendas/${idTiendaPertenece}/productos")

    val data = hashMapOf(
      "id" to id,
      "nombre" to nombre,
      "precio" to precio
    )

    tiendas.document("${id}").set(data)
      .addOnSuccessListener { mostrarSnackbar("Producto actualizado con éxito") }
    consultarProducto()
  }

  fun eliminarProducto(
    id: Long
  ){
    val db = Firebase.firestore
    val tiendaRef = db
      .collection("tiendas/${idTiendaPertenece}/productos")

    tiendaRef
      .document("${id}")
      .delete() // elimina
      .addOnCompleteListener { mostrarSnackbar("Tienda eliminada") }
      .addOnFailureListener { /* Si algo salio mal*/ }
    consultarProducto()
  }

  override fun onCreateContextMenu(
    menu: ContextMenu?,
    v: View?,
    menuInfo: ContextMenu.ContextMenuInfo?
  ) {
    super.onCreateContextMenu(menu, v, menuInfo)
    val inflater = menuInflater
    inflater.inflate(R.menu.menuproducto, menu)
    val info = menuInfo as AdapterView.AdapterContextMenuInfo
    val posicion = info.position
    posicionItemSeleccionado = posicion
  }
  override fun onContextItemSelected(item: MenuItem): Boolean {
    val productoSeleccionado = arregloProdutos[posicionItemSeleccionado]
    return when(item.itemId){
      R.id.m_editar_prod ->{
        abrirActividadItem(EditarProducto::class.java)
        return true
      }
      R.id.m_eliminar_pro ->{
        abrirDialogo(productoSeleccionado.nombre, productoSeleccionado.id)
        return true
      }
      else -> super.onContextItemSelected(item)
    }
  }

  fun abrirDialogo(nombreProducto: String, idProducto: Long){
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Eliminar Producto")
    builder.setPositiveButton(
      "Aceptar",
      DialogInterface.OnClickListener{ dialog, which ->
        eliminarProducto(idProducto)
      }
    )
    builder.setNegativeButton(
      "Cancelar",
      null
    )

    builder.setMessage("Estas seguro de eliminar el producto ${nombreProducto}")
    val dialogo =builder.create()
    dialogo.show()
  }
}