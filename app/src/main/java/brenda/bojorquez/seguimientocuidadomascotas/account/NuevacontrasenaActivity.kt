package brenda.bojorquez.seguimientocuidadomascotas.account

import brenda.bojorquez.seguimientocuidadomascotas.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NuevacontrasenaActivity : AppCompatActivity() {
    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevacontrasena)
        val btn_back: ImageView = findViewById(R.id.back) as ImageView
        val btn_confirmar: Button = findViewById(R.id.btn_confirmar) as Button

        btn_back.setOnClickListener {
            var intento = Intent(this, ContrasenaActivity::class.java)
            this.startActivity(intento)
        }

        btn_confirmar.setOnClickListener {


        }
    }
}