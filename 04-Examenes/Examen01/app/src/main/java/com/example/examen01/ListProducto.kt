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
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

class ListProducto : AppCompatActivity() {
  private lateinit var adaptador: ArrayAdapter<Producto>
  private lateinit var arregloProdutos: ArrayList<Producto>
  var idProducto = 0
  var nombreProducto = ""
  var precioProducto = 0.0
  var idTiendaPertenece: Int = -1
  var posicionItemSeleccionado = 0
  var nombreTiendaPertenece = ""

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
          precioProducto = (data?.getDoubleExtra("precioProducto", 0.0) ?: "") as Double

          anadirProducto(idProducto, nombreProducto, precioProducto, idTiendaPertenece)
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
    idTiendaPertenece = intent.getIntExtra("idTienda", 0)
    nombreTiendaPertenece = intent.getStringExtra("nombreTienda").toString()
    TBaseDatos.tablas = SQLiteHelperCenter(this)
    arregloProdutos = TBaseDatos.tablas!!.consultarProductoTienda(idTiendaPertenece)
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

    val botonCrearPoducto = findViewById<Button>(R.id.btn_crear_producto)
    botonCrearPoducto.setOnClickListener{ abrirActividadconParametros(CrearProducto::class.java) }

    val txtnombreTiendaProducto = findViewById<TextView>(R.id.txt_nombre_tienda_productos)
    txtnombreTiendaProducto.setText(nombreTiendaPertenece)

    registerForContextMenu(listView)

  }

  fun actualizarArreglo() {
    arregloProdutos = TBaseDatos.tablas!!.consultarProductoTienda(idTiendaPertenece)
    adaptador.clear()
    arregloProdutos.forEach {
      adaptador.insert(it, adaptador.count)
    }
  }

  fun mostrarSnackbar(texto:String){
    Snackbar.make(
      findViewById(R.id.id_layout_producto),
      texto,
      Snackbar.LENGTH_LONG
    )
      .show()
  }
  fun anadirProducto(
    idProducto: Int,
    nombreProducto: String,
    precioProducto: Double,
    idTienda: Int,
  ){
    val respuesta = TBaseDatos.tablas?.crearProducto(idProducto, nombreProducto, precioProducto, idTienda)
    if(respuesta == true)  {
      mostrarSnackbar("Producto ${nombreProducto} creado con éxito")
      actualizarArreglo()
    }else{
      mostrarSnackbar("Tienda Error")
    }
  }

  fun actualizarProducto(
    idProducto: Int,
    nombre: String,
    precio: Double
  ){
    val respuesta = TBaseDatos.tablas?.actualizarProducto(nombre,precio,idProducto)
    if(respuesta == true){
      mostrarSnackbar("Producto actualizado con éxito")
      actualizarArreglo()
    }else{
      mostrarSnackbar("Algo salió mal")
    }
  }

  fun eliminarProducto(idEliminar: Int){
    val respuesta = TBaseDatos.tablas?.eliminarProducto(idEliminar)
    if(respuesta == true){
      mostrarSnackbar("Producto eliminado con éxitoooooo")
      actualizarArreglo()
    }else{
      mostrarSnackbar("Algo salió mal")
    }
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

  fun abrirDialogo(nombreProducto: String, idProducto: Int){
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