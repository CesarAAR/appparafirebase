package mx.tecnm.tepic.appparafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var usuario: EditText
    private lateinit var contra: EditText
    private lateinit var progre: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usuario=findViewById(R.id.txtcorreoL)
        contra=findViewById(R.id.txtpasswordL)
        progre=findViewById(R.id.progressBar3)
        auth=FirebaseAuth.getInstance()


        btnRegistrarLogin.setOnClickListener {
            limpiarCampos()
            var intent = Intent(this, MainRegistrar::class.java)
            startActivity(intent)

        }

        btnLogear.setOnClickListener {
            loginUser()
            limpiarCampos()
        }

        limpiarCampos()
    }

    private fun limpiarCampos() {
        txtcorreoL.setText("")
        txtpasswordL.setText("")
    }

    private fun loginUser(){
        val user:String=usuario.text.toString()
        val pass:String=contra.text.toString()

        if(!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pass)){
            progre.visibility=View.VISIBLE

            auth.signInWithEmailAndPassword(user,pass)
                .addOnCompleteListener(this){
                    task ->
                    if(task.isSuccessful){
                        action()
                    }
                    else{
                        Toast.makeText(this,"Error en la Autenticacion",Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
    private fun action(){
        startActivity(Intent(this,MainPersonas::class.java))
    }
}