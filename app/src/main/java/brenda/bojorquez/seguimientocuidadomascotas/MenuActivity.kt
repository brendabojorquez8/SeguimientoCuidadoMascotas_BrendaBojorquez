package brenda.bojorquez.seguimientocuidadomascotas

import brenda.bojorquez.seguimientocuidadomascotas.adapters.BotonesAdaptador
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView

class MenuActivity : AppCompatActivity() {
    var botonesMenuInfo=ArrayList<BotonesMenu>()
    var adapter: BotonesAdaptador? =null
    lateinit var mascota: Mascota

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val bundle = intent.extras

        if(bundle != null){
            var imagenS: String = bundle.getString("uri").toString()
            val imagenUri = Uri.parse(imagenS)
            mascota = Mascota(bundle.getString("nombre").toString(), bundle.getInt("image"), imagenUri, bundle.getString("edad").toString() )        }

        cargarBotones()
        adapter = BotonesAdaptador(this, botonesMenuInfo)

        var gridBotones: GridView = findViewById(R.id.mascotasBotonesIn)

        gridBotones.adapter = adapter
    }

    fun cargarBotones(){
        botonesMenuInfo.add(BotonesMenu("Perfil dueño", R.drawable.crearcuenta, mascota))
        botonesMenuInfo.add(BotonesMenu("Signos vitales", R.drawable.frecuenciacardiaca, mascota))
        botonesMenuInfo.add(BotonesMenu("Frecuencia cardiaca", R.drawable.frecuenciacardiaca, mascota))
        botonesMenuInfo.add(BotonesMenu("Frecuencia respiratoria", R.drawable.frecuenciarespiratoria, mascota))
        botonesMenuInfo.add(BotonesMenu("Temperatura", R.drawable.temperatura, mascota))
        botonesMenuInfo.add(BotonesMenu("Peso", R.drawable.peso, mascota))
        botonesMenuInfo.add(BotonesMenu("Comportamiento", R.drawable.comportamiento, mascota))
        botonesMenuInfo.add(BotonesMenu("Historial Clinico", R.drawable.historial, mascota))
        botonesMenuInfo.add(BotonesMenu("Vacunación", R.drawable.vacuna_icono, mascota))
        botonesMenuInfo.add(BotonesMenu("Padecimientos", R.drawable.padecimientos, mascota))
        botonesMenuInfo.add(BotonesMenu("Enfermedades", R.drawable.enfermedades, mascota))
        botonesMenuInfo.add(BotonesMenu("Información General", R.drawable.informacion, mascota))
        botonesMenuInfo.add(BotonesMenu("Artículos", R.drawable.articulo, mascota))
        botonesMenuInfo.add(BotonesMenu("Galeria", R.drawable.galeria, mascota))
        botonesMenuInfo.add(BotonesMenu("Gastos", R.drawable.presupuesto, mascota))
        botonesMenuInfo.add(BotonesMenu("Calendario", R.drawable.calendario, mascota))
        botonesMenuInfo.add(BotonesMenu("Directorio veterinarios", R.drawable.directorio, mascota))
    }
}