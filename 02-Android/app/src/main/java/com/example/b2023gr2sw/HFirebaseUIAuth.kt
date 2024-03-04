package com.example.b2023gr2sw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class HFirebaseUIAuth : AppCompatActivity() {
  private val respuestaLoginAuthUi = registerForActivityResult(
    FirebaseAuthUIActivityResultContract()
  ){res: FirebaseAuthUIAuthenticationResult ->
    if(res.resultCode === RESULT_OK){
      if(res.idpResponse != null){
        seLogeo(res.idpResponse!!)
      }
    }
  }

  fun seLogeo(
    res: IdpResponse
  ){
    val btnLogin = findViewById<Button>(R.id.btn_login_firebase)
    val btnLogout = findViewById<Button>(R.id.btn_logout_firebase)
    val tvtBienvenido = findViewById<TextView>(R.id.tv_bienvenido)
    tvtBienvenido.text = FirebaseAuth.getInstance().currentUser?.displayName
    btnLogout.visibility = View.VISIBLE
    btnLogin.visibility = View.INVISIBLE
    if(res.isNewUser == true){
      registrarUsuarioPorPrimeraVez(res)
    }
  }

  fun registrarUsuarioPorPrimeraVez(usuario: IdpResponse){
    /*
    usuario.email;
    usuario.phoneNumber;
    usuario.user.name;
     */
  }

  fun seDeslogeo(){
    val btnLogin = findViewById<Button>(R.id.btn_login_firebase)
    val btnLogout = findViewById<Button>(R.id.btn_logout_firebase)
    val tvtBienvenido = findViewById<TextView>(R.id.tv_bienvenido)
    tvtBienvenido.text = "Bienvenido"
    btnLogout.visibility = View.INVISIBLE
    btnLogin.visibility = View.VISIBLE
    FirebaseAuth.getInstance().signOut()
  }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hfirebase_uiauth)

    val btnLogin = findViewById<Button>(R.id.btn_login_firebase)
    btnLogin.setOnClickListener {
      val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build()
      )

      val logearseIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()

      respuestaLoginAuthUi.launch(logearseIntent)
    }

    val btnLogout = findViewById<Button>(R.id.btn_logout_firebase)
    btnLogout.setOnClickListener { seDeslogeo() }

    val usuario = FirebaseAuth.getInstance().currentUser
    if(usuario != null){
      val tvtBienvenido = findViewById<TextView>(R.id.tv_bienvenido)
      val btnLogin: Button = findViewById<Button>(R.id.btn_login_firebase)
      val btnLogout = findViewById<Button>(R.id.btn_logout_firebase)
      btnLogout.visibility = View.VISIBLE
      btnLogin.visibility = View.INVISIBLE
      tvtBienvenido.text = usuario.displayName
    }
  }
}

