package brenda.bojorquez.seguimientocuidadomascotas

import brenda.bojorquez.seguimientocuidadomascotas.adapters.BotonesAdaptador
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.bumptech.glide.Glide

class HistorialcActivity : AppCompatActivity() {
    var botonesMenuSignosH=ArrayList<BotonesMenu>()
    var adapter: BotonesAdaptador? =null
    lateinit var mascota: Mascota
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historialc)
        val bundle = intent.extras



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
        adapter = BotonesAdaptador(this, botonesMenuSignosH)

        var gridBotones: GridView = findViewById(R.id.mascotasBotonesH)

        gridBotones.adapter = adapter

        val btn_menu: ImageView = findViewById(R.id.menu) as ImageView

        btn_menu.setOnClickListener {
            var intento = Intent(this, MenuActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
            finish()
        }
    }

    fun cargarBotones(){
        botonesMenuSignosH.add(BotonesMenu("Vacunación", R.drawable.vacuna_icono, mascota))
        botonesMenuSignosH.add(BotonesMenu("Padecimientos", R.drawable.padecimientos, mascota))
        botonesMenuSignosH.add(BotonesMenu("Enfermedades", R.drawable.enfermedades, mascota))
    }
}