package brenda.bojorquez.seguimientocuidadomascotas

import brenda.bojorquez.seguimientocuidadomascotas.adapters.BotonesAdaptador
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.bumptech.glide.Glide

class SignosvActivity : AppCompatActivity() {
    var botonesMenuSignos=ArrayList<BotonesMenu>()
    var adapter: BotonesAdaptador? =null
    lateinit var mascota: Mascota

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signosv)

        val bundle = intent.extras
        val btn_back: ImageView = findViewById(R.id.back) as ImageView

        if(bundle != null){

            val imageM: de.hdodenhof.circleimageview.CircleImageView = findViewById(R.id.my_image_view)
            val nombreM: TextView = findViewById(R.id.nombreM)
            val edadM: TextView = findViewById(R.id.edadM)

            imageM.setImageResource(bundle.getInt("image"))
            nombreM.setText(bundle.getString("nombre").toString())
            edadM.setText(bundle.getString("edad").toString())

            var imagenS: String = bundle.getString("uri").toString()
            val imagenUri = Uri.parse(imagenS)
            mascota = Mascota(bundle.getString("nombre").toString(), bundle.getInt("image"), imagenUri, bundle.getString("edad").toString() )
            if (mascota.imageUri.toString() != "") {
                Glide.with(this)
                    .load(mascota.imageUri)
                    .into(imageM)
            } else {
                imageM.setImageResource(mascota.image)
            }
        }

        cargarBotones()
        adapter = BotonesAdaptador(this, botonesMenuSignos)

        var gridBotones: GridView = findViewById(R.id.mascotasBotonesS)

        gridBotones.adapter = adapter

        btn_back.setOnClickListener {
            var intento = Intent(this, MascotasperfilActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
            finish()
        }
    }

    fun cargarBotones(){
        botonesMenuSignos.add(BotonesMenu("Frecuencia cardiaca", R.drawable.frecuenciacardiaca, mascota))
        botonesMenuSignos.add(BotonesMenu("Frecuencia respiratoria", R.drawable.frecuenciarespiratoria, mascota))
        botonesMenuSignos.add(BotonesMenu("Temperatura", R.drawable.temperatura, mascota))
        botonesMenuSignos.add(BotonesMenu("Peso", R.drawable.peso, mascota))
    }
}