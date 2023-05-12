package brenda.bojorquez.seguimientocuidadomascotas

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NuevapetActivity : AppCompatActivity() {
    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth
    var mascotasPerfilD=ArrayList<Mascota>()
    var adapter: AdaptadorMascotas? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevapet)
        cargarBotones()
        adapter = AdaptadorMascotas(this, mascotasPerfilD)

        var gridMascotas: GridView = findViewById(R.id.mascotas)

        gridMascotas.adapter = adapter

        val btn_back: ImageView = findViewById(R.id.back) as ImageView

        btn_back.setOnClickListener {
            val intent = Intent(this, DuenoperfilActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun cargarBotones(){
        mascotasPerfilD.add(Mascota("Canino", R.drawable.dog, Uri.EMPTY,""))
        mascotasPerfilD.add(Mascota("Felino", R.drawable.cat, Uri.EMPTY,""))
        mascotasPerfilD.add(Mascota("Ave", R.drawable.bird, Uri.EMPTY,""))
        mascotasPerfilD.add(Mascota("Pez", R.drawable.pez, Uri.EMPTY,""))
        mascotasPerfilD.add(Mascota("Reptil", R.drawable.lizard, Uri.EMPTY,""))
        mascotasPerfilD.add(Mascota("Insecto", R.drawable.insecto, Uri.EMPTY,""))
    }

    class AdaptadorMascotas: BaseAdapter {
        var botones = ArrayList<Mascota>()
        var contexto: Context?=null

        constructor(contexto: Context, productos:ArrayList<Mascota>){
            this.botones = productos
            this.contexto = contexto
        }

        override fun getCount(): Int {
            return botones.size
        }

        override fun getItem(p0: Int): Any {
            return botones[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var mascota=botones[p0]
            var inflador= LayoutInflater.from(contexto)
            var vista = inflador.inflate(R.layout.cell_especie, null)

            var imagen = vista.findViewById(R.id.image_cell) as ImageView
            var especie = vista.findViewById(R.id.especie_cell) as TextView

            imagen.setImageResource(mascota.image)
            especie.setText(mascota.nombre)

            imagen.setOnClickListener {
                var intento = Intent(contexto, AgregarmascotaActivity::class.java)
                intento.putExtra("image", mascota.image)
                intento.putExtra("especie", mascota.nombre)
                contexto!!.startActivity(intento)

            }

            return vista
        }
    }
}