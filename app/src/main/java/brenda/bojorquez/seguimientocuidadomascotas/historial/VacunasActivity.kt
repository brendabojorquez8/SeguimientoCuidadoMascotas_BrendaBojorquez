package brenda.bojorquez.seguimientocuidadomascotas.historial

import brenda.bojorquez.seguimientocuidadomascotas.*
import brenda.bojorquez.seguimientocuidadomascotas.adapters.VacunasAdaptador
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class VacunasActivity : AppCompatActivity() {
    var adapter: VacunasAdaptador? =null
    lateinit var mascota: Mascota
    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth
    private lateinit var correo: String
    companion object{
        var listaVacuna = ArrayList<VacunasMuestra>()
        var first = true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacunas)

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
            var intento = Intent(this, AgregarvacunaActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
            finish()
        }
    }


    fun cargarBotones(){
        listaVacuna = ArrayList()
        storage.collection("Vacunas")
            .whereEqualTo("email", correo)
            .whereEqualTo("mascota", mascota.nombre)
            .get()
            .addOnSuccessListener {
                it.forEach{

                    var vacuna: String = it.getString("vacuna").toString()
                    var fecha:String = it.getString("fecha").toString()

                    listaVacuna.add(VacunasMuestra(vacuna, R.drawable.vacuna_icono,fecha, mascota))
                }

                adapter = VacunasAdaptador(this, listaVacuna)

                var gridBotones: GridView = findViewById(R.id.mascotasVacunas) as GridView

                gridBotones.adapter = adapter


            }.addOnFailureListener{
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}