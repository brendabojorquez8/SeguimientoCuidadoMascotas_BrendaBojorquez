package brenda.bojorquez.seguimientocuidadomascotas

import brenda.bojorquez.seguimientocuidadomascotas.adapters.VacunasAdaptador
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView

class GastosActivity : AppCompatActivity() {
    var botonesMenuInfo=ArrayList<VacunasMuestra>()
    var adapter: VacunasAdaptador? =null
    lateinit var mascota: Mascota

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gastos)
        val btn_back: ImageView = findViewById(R.id.back) as ImageView
        val btn_continuar: Button = findViewById(R.id.btn_añadir) as Button
        val nombreMas: TextView = findViewById(R.id.nombreMas) as TextView

        val bundle = intent.extras
        if(bundle != null){
            nombreMas.setText(bundle.getString("nombre").toString())
            var imagenS: String = bundle.getString("uri").toString()
            val imagenUri = Uri.parse(imagenS)
            mascota = Mascota(bundle.getString("nombre").toString(), bundle.getInt("image"), imagenUri, bundle.getString("edad").toString() )        }

        cargarBotones()
        adapter = VacunasAdaptador(this, botonesMenuInfo)

        var gridBotones: GridView = findViewById(R.id.gridCardiaca)

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

        btn_continuar.setOnClickListener {
            var intento = Intent(this, AgregagastActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
        }
    }

    fun cargarBotones(){
        botonesMenuInfo.add(VacunasMuestra("Baño", R.drawable.presupuesto,"6 de enero 2023", mascota))
        botonesMenuInfo.add(VacunasMuestra("Desparasitación", R.drawable.presupuesto,"6 de enero 2023", mascota))
    }
}