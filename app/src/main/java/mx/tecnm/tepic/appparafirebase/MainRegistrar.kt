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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main_registrar.*

class MainRegistrar : AppCompatActivity() {

    private lateinit var nombre:EditText
    private lateinit var apellido:EditText
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var progress:ProgressBar
    private lateinit var dbReference: DatabaseReference
    private lateinit var database:FirebaseDatabase
    private lateinit var auth:FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_registrar)
        nombre=findViewById(R.id.txtnombre)
        apellido=findViewById(R.id.txtapellido)
        email=findViewById(R.id.registraremail)
        password=findViewById(R.id.registrarpass)

        progress=findViewById(R.id.progressBar)

        database = FirebaseDatabase.getInstance()
        auth=FirebaseAuth.getInstance()

        dbReference = database.reference.child("User")

        buttonSalir.setOnClickListener {
            finish()
        }
        buttonregistrar.setOnClickListener {
            createNewAccount()
        }
    }

    private fun createNewAccount(){//funcion para crear nuevo usuario.
        //obtenemos todos los datos.
        var name:String=nombre.text.toString()
        var lastname:String=apellido.text.toString()
        var correo:String=email.text.toString()
        var contra:String=password.text.toString()
        //verificamos que no este vacio ningun campo
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastname) && !TextUtils.isEmpty(correo) && !TextUtils.isEmpty(contra)){
            progress.visibility=View.VISIBLE

            auth.createUserWithEmailAndPassword(correo,contra)
                .addOnCompleteListener(this){
                    task ->

                    if(task.isComplete){//VERIFICAMOS SI SE REGISTRO.
                        val user:FirebaseUser?=auth.currentUser //obtenemos el usuario
                        verifyEmail(user)
                        //registramos los demas datos
                        val userBD = dbReference.child(user?.uid!!)
                        userBD.child("Name").setValue(name)
                        userBD.child("Lastname").setValue(lastname)
                        finish()
                    }
                }
        }
    }

    private fun action(){//funcion que se realizara cuando se realice correctamente
        var intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    private fun verifyEmail(user: FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                task ->

                if(task.isComplete){
                    Toast.makeText(this, "Email Enviado",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this, "Error: Email no Enviado",Toast.LENGTH_LONG).show()
                }
            }
    }

}