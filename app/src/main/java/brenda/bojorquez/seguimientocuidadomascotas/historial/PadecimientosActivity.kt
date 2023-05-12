package brenda.bojorquez.seguimientocuidadomascotas.historial

import brenda.bojorquez.seguimientocuidadomascotas.VacunasMuestra
import brenda.bojorquez.seguimientocuidadomascotas.HistorialcActivity
import brenda.bojorquez.seguimientocuidadomascotas.Mascota
import brenda.bojorquez.seguimientocuidadomascotas.R
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PadecimientosActivity : AppCompatActivity() {
    var adapter: AdaptadorPadecimientos? =null
    lateinit var mascota: Mascota
    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth
    private lateinit var correo: String
    companion object{
        var listaPade = ArrayList<VacunasMuestra>()
        var first = true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_padecimientos)

        storage = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()
        correo = usuario.currentUser?.email.toString()

        val bundle = intent.extras
        val btn_back: ImageView = findViewById(R.id.back) as ImageView
        val btn_añadir: Button = findViewById(R.id.btn_añadir) as Button

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

        if(first){
            cargarBotones()
            first = false
        }


        btn_back.setOnClickListener {
            first = true
            var intento = Intent(this, HistorialcActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
            finish()
        }

        btn_añadir.setOnClickListener {
            first = true
            var intento = Intent(this, AgregarpadeActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
            finish()
        }
    }

    fun cargarBotones(){
        listaPade = ArrayList()
        storage.collection("Padecimientos")
            .whereEqualTo("email", correo)
            .whereEqualTo("mascota", mascota.nombre)
            .get()
            .addOnSuccessListener {
                it.forEach{

                    var vacuna: String = it.getString("padecimiento").toString()
                    var fecha:String = it.getString("fechaInicio").toString()

                    listaPade.add(VacunasMuestra(vacuna, R.drawable.padecimientos,fecha, mascota))
                }

                adapter = AdaptadorPadecimientos(this, listaPade)

                var gridBotones: GridView = findViewById(R.id.mascotasPadecimientos) as GridView

                gridBotones.adapter = adapter


            }.addOnFailureListener{
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }

    }

    class AdaptadorPadecimientos: BaseAdapter {
        var botones = ArrayList<VacunasMuestra>()
        var contexto: Context? = null

        constructor(contexto: Context, productos:ArrayList<VacunasMuestra>){
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
            var boton=botones[p0]
            var inflador= LayoutInflater.from(contexto)
            var vista = inflador.inflate(R.layout.cell_perfil, null)

            val imagen = vista.findViewById(R.id.icono) as ImageView
            val nombre = vista.findViewById(R.id.item) as TextView
            val fecha  = vista.findViewById(R.id.item2) as TextView

            imagen.setImageResource(boton.image)
            nombre.setText(boton.name)
            fecha.setText(boton.fecha)


            return vista
        }
    }
}