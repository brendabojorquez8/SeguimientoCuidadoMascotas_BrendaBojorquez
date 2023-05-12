package brenda.bojorquez.seguimientocuidadomascotas.account

import brenda.bojorquez.seguimientocuidadomascotas.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CodigoActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_codigo)
        auth = Firebase.auth

        val btn_back: ImageView = findViewById(R.id.back) as ImageView
        val btn_cotinuar: Button = findViewById(R.id.btn_cotinuar) as Button
        val btn_reenviar: Button = findViewById(R.id.btn_reenviar) as Button

        btn_back.setOnClickListener {
            var intento = Intent(this, LoginActivity::class.java)
            this.startActivity(intento)
        }

        btn_reenviar.setOnClickListener {
            val et_correo: EditText = findViewById(R.id.et_correoOC)

            var correo: String = et_correo.text.toString()

            if(!correo.isNullOrBlank()){

                auth.sendPasswordResetEmail(correo)
                    .addOnCompleteListener{ task ->

                        if (task.isSuccessful){
                            Toast.makeText(this, "Se reenvió el código a $correo",
                                Toast.LENGTH_SHORT).show()
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
        btn_cotinuar.setOnClickListener {
            val et_codigo: TextView = findViewById(R.id.et_codigo) as TextView
            val auth = FirebaseAuth.getInstance()
            val codigo : String = et_codigo.text.toString()

            auth.verifyPasswordResetCode(codigo)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // El código de verificación es correcto, permitir al usuario restablecer la contraseña.
                    } else {
                        // El código de verificación es incorrecto.
                    }
                }
        }
    }
}