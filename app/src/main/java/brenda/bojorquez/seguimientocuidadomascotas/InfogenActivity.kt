package brenda.bojorquez.seguimientocuidadomascotas

import brenda.bojorquez.seguimientocuidadomascotas.adapters.BotonesAdaptador
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class InfogenActivity : AppCompatActivity() {
    var botonesMenuInfo=ArrayList<BotonesMenu>()
    var adapter: BotonesAdaptador? =null
    lateinit var mascota: Mascota
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infogen)
        val bundle = intent.extras

        if(bundle != null){
            var imagenS: String = bundle.getString("uri").toString()
            val imagenUri = Uri.parse(imagenS)
            mascota = Mascota(bundle.getString("nombre").toString(), bundle.getInt("image"), imagenUri, bundle.getString("edad").toString() )        }

        cargarBotones()
        adapter = BotonesAdaptador(this, botonesMenuInfo)

        var gridBotones: GridView = findViewById(R.id.mascotasBotonesIn)

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
        botonesMenuInfo.add(BotonesMenu("Artículos", R.drawable.articulo, mascota))
        botonesMenuInfo.add(BotonesMenu("Galeria", R.drawable.galeria, mascota))
        botonesMenuInfo.add(BotonesMenu("Gastos", R.drawable.presupuesto, mascota))
        botonesMenuInfo.add(BotonesMenu("Calendario", R.drawable.calendario, mascota))
        botonesMenuInfo.add(BotonesMenu("Directorio veterinarios", R.drawable.directorio, mascota))
    }

}