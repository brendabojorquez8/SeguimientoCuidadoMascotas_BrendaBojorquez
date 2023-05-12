package brenda.bojorquez.seguimientocuidadomascotas.frecres

import brenda.bojorquez.seguimientocuidadomascotas.*
import brenda.bojorquez.seguimientocuidadomascotas.adapters.DatosAdaptador
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView

class RespiradActivity : AppCompatActivity() {
    var botonesMenuSignosH=ArrayList<Datos>()
    var adapter: DatosAdaptador? =null
    lateinit var mascota: Mascota
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_respirad)

        val btn_back: ImageView = findViewById(R.id.back) as ImageView
        val btn_manual: Button = findViewById(R.id.btn_manual) as Button
        val bundle = intent.extras
        if(bundle != null){

            val nombreM: TextView = findViewById(R.id.nombreMas)
            nombreM.setText(bundle.getString("nombre").toString())

            var imagenS: String = bundle.getString("uri").toString()
            val imagenUri = Uri.parse(imagenS)
            mascota = Mascota(bundle.getString("nombre").toString(), bundle.getInt("image"), imagenUri, bundle.getString("edad").toString() )        }

        cargarDatos()
        adapter = DatosAdaptador(this, botonesMenuSignosH)

        var gridBotones: GridView = findViewById(R.id.gridCardiaca)
        gridBotones.adapter = adapter

        btn_back.setOnClickListener {
            var intento = Intent(this, SignosvActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
            finish()
        }

        btn_manual.setOnClickListener {
            var intento = Intent(this, MedicionMRespActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
        }
    }
    fun cargarDatos(){
        botonesMenuSignosH.add(Datos("Frecuencia Reposo", "15xmin", mascota))
        botonesMenuSignosH.add(Datos("Frecuencia máximo", "19xmin", mascota))
        botonesMenuSignosH.add(Datos("Frecuencia minimo", "10xmin", mascota))
    }
}