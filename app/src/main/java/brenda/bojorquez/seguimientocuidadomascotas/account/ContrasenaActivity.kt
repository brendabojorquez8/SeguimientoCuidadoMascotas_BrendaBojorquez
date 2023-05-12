package brenda.bojorquez.seguimientocuidadomascotas.account

import brenda.bojorquez.seguimientocuidadomascotas.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ContrasenaActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contrasena)
        auth = Firebase.auth

        val btn_back: ImageView = findViewById(R.id.back) as ImageView
        val btn_continuar: Button = findViewById(R.id.btn_continuar) as Button

        btn_back.setOnClickListener {
            var intento = Intent(this, LoginActivity::class.java)
            this.startActivity(intento)
        }

        btn_continuar.setOnClickListener {
            val et_correo: EditText = findViewById(R.id.et_correoOC)

            var correo: String = et_correo.text.toString()

            if(!correo.isNullOrBlank()){

                auth.sendPasswordResetEmail(correo)
                    .addOnCompleteListener{ task ->

                        if (task.isSuccessful){
                            Toast.makeText(this, "Se envió un códico a tu correo para reestablecer la contraseña",
                                Toast.LENGTH_SHORT).show()
                            var intento = Intent(this, CodigoActivity::class.java)
                            intento.putExtra("correo",  correo)
                            this.startActivity(intento)
                        }else{
                            Toast.makeText(this, "Error al enviar correo",
                                Toast.LENGTH_SHORT).show()
                        }

                    }

            }else{
                Toast.makeText(this,"Ingresar correo",
                    Toast.LENGTH_SHORT).show()
            }


        }
    }
}