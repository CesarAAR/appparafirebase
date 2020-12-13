package mx.tecnm.tepic.appparafirebase

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main_personas.*
import java.util.*

class MainPersonas : AppCompatActivity() {
    var baseRemota = FirebaseFirestore.getInstance()
    var datos= ArrayList<String>()
    var ListaEventosIDs= ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_personas)

        baseRemota.collection("personas")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException!=null){
                    mensaje("ERROR! No se pudo recuperar data desde NUBE")
                    return@addSnapshotListener
                }
                datos.clear()
                ListaEventosIDs.clear()
                var cadena = ""
                for(registro in querySnapshot!!){
                    cadena = "Nombre: ${registro.getString("NOMBRE")}\nApellido: ${registro.getString("APELLIDO")}\n" +
                            "Edad: ${registro.getString("EDAD")}"
                    datos.add(cadena)
                    ListaEventosIDs.add(registro.id)
                }
                var adaptador = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,datos)
                listaPersonas.adapter = adaptador

            }
        buttonInsertar.setOnClickListener {
            insertarTabla()
        }

        button2.setOnClickListener {
            finish()
        }

    }


   /* var selectPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

       if(requestCode==0 && resultCode== Activity.RESULT_OK && data!=null){
           selectPhotoUri=data.data
           val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectPhotoUri)
           val bitmapDrawable = BitmapDrawable(bitmap)

           imageViewPersona.setBackgroundDrawable(bitmapDrawable)

       }
    }*/


    private fun insertarTabla() {
        var datosInsertar = hashMapOf(
            "NOMBRE" to nombrePersona.text.toString(),
            "APELLIDO" to apellidoPersona.text.toString(),
            "EDAD" to edadPersona.text.toString()
        )
        baseRemota.collection("personas")
            .add(datosInsertar as Any)
            .addOnSuccessListener {
                Toast.makeText(this,"SE INSERTO CORRECTAMENTE CON ID ${it.id}", Toast.LENGTH_LONG)
                    .show()
                nombrePersona.setText("")
                apellidoPersona.setText("")
                edadPersona.setText("")
            }
            .addOnFailureListener {
                mensaje("NO SE PUDO INSERTAR:\n${it.message!!}")
            }
    }


    private fun mensaje(s: String) {
        AlertDialog.Builder(this)
            .setMessage(s)
            .setTitle("ATENCION CHAMACO")
            .setPositiveButton("Ok"){d,i-> d.dismiss()}
            .show()
    }
    /*private fun uploadImageToFirebaseStorage() {
        if(selectPhotoUri==null)return

        val filename = UUID.randomUUID().toString()
        val ref=FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectPhotoUri!!)
            .addOnSuccessListener {
                Toast.makeText(this,"Se ha guardado la foto con: ${it.metadata?.path}",Toast.LENGTH_LONG).show()
            }
    }*/
}