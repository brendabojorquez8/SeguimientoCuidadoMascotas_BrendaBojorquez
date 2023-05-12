package brenda.bojorquez.seguimientocuidadomascotas

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class GaleriaActivity : AppCompatActivity() {
    var botonesMenuInfo=ArrayList<VacunasMuestra>()
    var adapter: FotosAdaptador? =null
    lateinit var mascota: Mascota

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_galeria)
        val btn_back: ImageView = findViewById(R.id.back) as ImageView

        val bundle = intent.extras
        if(bundle != null){

            var imagenS: String = bundle.getString("uri").toString()
            val imagenUri = Uri.parse(imagenS)
            mascota = Mascota(bundle.getString("nombre").toString(), bundle.getInt("image"), imagenUri, bundle.getString("edad").toString() )        }

        cargarBotones()
        adapter = FotosAdaptador(this, botonesMenuInfo)

        var gridBotones: GridView = findViewById(R.id.gridGaleria)

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
        botonesMenuInfo.add(VacunasMuestra("", R.drawable.foto1,"", mascota))
        botonesMenuInfo.add(VacunasMuestra("", R.drawable.foto2,"", mascota))
        botonesMenuInfo.add(VacunasMuestra("", R.drawable.foto3,"", mascota))
        botonesMenuInfo.add(VacunasMuestra("", R.drawable.foto3,"", mascota))
        botonesMenuInfo.add(VacunasMuestra("", R.drawable.foto5,"", mascota))
        botonesMenuInfo.add(VacunasMuestra("", R.drawable.foto6,"", mascota))
        botonesMenuInfo.add(VacunasMuestra("", R.drawable.foto7,"", mascota))
        botonesMenuInfo.add(VacunasMuestra("", R.drawable.foto8,"", mascota))
        botonesMenuInfo.add(VacunasMuestra("", R.drawable.foto9,"", mascota))
        botonesMenuInfo.add(VacunasMuestra("", R.drawable.foto10,"", mascota))
        botonesMenuInfo.add(VacunasMuestra("", R.drawable.foto11,"", mascota))
        botonesMenuInfo.add(VacunasMuestra("", R.drawable.foto12,"", mascota))
    }

    class FotosAdaptador: BaseAdapter {
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
            var vista = inflador.inflate(R.layout.cell_foto, null)

            val imagen = vista.findViewById(R.id.icono) as ImageView

            imagen.setImageResource(boton.image)

            return vista
        }
    }
}