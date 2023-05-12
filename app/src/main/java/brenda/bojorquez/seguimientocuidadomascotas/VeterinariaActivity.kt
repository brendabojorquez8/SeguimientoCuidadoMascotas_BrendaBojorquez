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

class VeterinariaActivity : AppCompatActivity() {
    var botonesMenuInfo=ArrayList<VacunasMuestra>()
    var adapter: VeterinariasAdaptador? =null
    lateinit var mascota: Mascota

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinaria)
        val bundle = intent.extras
        val btn_back: ImageView = findViewById(R.id.back) as ImageView

        if(bundle != null){
            var imagenS: String = bundle.getString("uri").toString()
            val imagenUri = Uri.parse(imagenS)
            mascota = Mascota(bundle.getString("nombre").toString(), bundle.getInt("image"), imagenUri, bundle.getString("edad").toString() )        }

        cargarBotones()
        adapter = VeterinariasAdaptador(this, botonesMenuInfo)

        var gridBotones: GridView = findViewById(R.id.mascotasBotonesIn)

        gridBotones.adapter = adapter

        btn_back.setOnClickListener {
            var intento = Intent(this, InfogenActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
            finish()
        }
    }

    fun cargarBotones(){
        botonesMenuInfo.add(VacunasMuestra("Clinica Vet. Medicans", R.drawable.medicans,"Ignacio Ramirez 198 3, Ciudad Obregón, México\n644 412 9000", mascota))
        botonesMenuInfo.add(VacunasMuestra("Hospital veterinario\nDr Hiram.", R.drawable.hiram,"C. Coahuila 173 Norte, Centro, 85000 Cd Obregón\n644 415 0715", mascota))
        botonesMenuInfo.add(VacunasMuestra("ALPHA PETH", R.drawable.alpha,"Jesus Garcia #2210A, Ciudad Obregón, México\n644 412 0882", mascota))
        botonesMenuInfo.add(VacunasMuestra("El ranchero", R.drawable.none, "C California 293, Centro, 85000 Cd Obregón\n644 413 8847",mascota))
        botonesMenuInfo.add(VacunasMuestra("D'Knes", R.drawable.none, "C Quintana Roo 702, Noroeste, 85100 Cd Obregón\n644 416 1611",mascota))
        botonesMenuInfo.add(VacunasMuestra("Pets Blue Home", R.drawable.petsblue, "Avenida Vicente Guerrero, Cd Obregón\n644 415 8960",mascota))
        botonesMenuInfo.add(VacunasMuestra("Pets & Grooming", R.drawable.pyg, "Tetabiate #307, Ciudad Obregón, México\n644 414 7534",mascota))
    }

    class VeterinariasAdaptador: BaseAdapter {
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
            var vista = inflador.inflate(R.layout.cell_veterinarias, null)

            val imagen = vista.findViewById(R.id.icono) as ImageView
            val nombre = vista.findViewById(R.id.item) as TextView
            val direccion = vista.findViewById(R.id.item2) as TextView

            nombre.setText(boton.name)
            imagen.setImageResource(boton.image)
            direccion.setText(boton.fecha)

            return vista
        }
    }
}