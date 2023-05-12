package brenda.bojorquez.seguimientocuidadomascotas.freccard

import brenda.bojorquez.seguimientocuidadomascotas.Mascota
import brenda.bojorquez.seguimientocuidadomascotas.R
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class ComoCardActivity : AppCompatActivity() {
    lateinit var mascota: Mascota
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_como_card)

        val btn_back: ImageView = findViewById(R.id.back) as ImageView

        val bundle = intent.extras
        if(bundle != null){

            var imagenS: String = bundle.getString("uri").toString()
            val imagenUri = Uri.parse(imagenS)
            mascota = Mascota(bundle.getString("nombre").toString(), bundle.getInt("image"), imagenUri, bundle.getString("edad").toString() )        }

        btn_back.setOnClickListener {
            var intento = Intent(this, MedicionmActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
            finish()
        }
    }
}