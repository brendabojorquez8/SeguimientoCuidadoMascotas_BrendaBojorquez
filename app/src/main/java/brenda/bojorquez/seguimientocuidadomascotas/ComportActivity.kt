package brenda.bojorquez.seguimientocuidadomascotas

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*

class ComportActivity : AppCompatActivity() {
    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth
    private lateinit var correo: String
    lateinit var mascota: Mascota
    var selectedHambre = 0
    var selectedSueño = 0
    var selectedAnimo = 0
    var selectedLadridos= 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comport)

        storage = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()
        correo = usuario.currentUser?.email.toString()



        val btn_back: ImageView = findViewById(R.id.back) as ImageView

        val nombreMas: TextView = findViewById(R.id.nombreM) as TextView

        val bundle = intent.extras
        if(bundle != null){
            val imageM: de.hdodenhof.circleimageview.CircleImageView = findViewById(R.id.my_image_view)
            imageM.setImageResource(bundle.getInt("image"))
            nombreMas.setText(bundle.getString("nombre").toString())
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

        datosAgregadoHoy()

        val apetito: RadioGroup = findViewById(R.id.apetito)
        val sueño: RadioGroup = findViewById(R.id.Sueño)
        val animo: RadioGroup = findViewById(R.id.animo)
        val ladridos: RadioGroup = findViewById(R.id.ladridos)

        apetito.setOnCheckedChangeListener { group, checkedId ->
            Log.d("Opcion", checkedId.toString())
            if(checkedId > -1){
                if(selectedHambre > 0){
                    val selectedRadioButton = findViewById<RadioButton>(selectedHambre)
                    selectedRadioButton.setBackgroundResource(R.drawable.circle_background)
                }
                apetito.check(checkedId)
                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                selectedHambre = selectedRadioButton.id
                selectedRadioButton.setBackgroundResource(R.drawable.selected)

            }
        }

        sueño.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId > -1) {
                if(selectedSueño > 0){
                    val selectedRadioButton = findViewById<RadioButton>(selectedSueño)
                    selectedRadioButton.setBackgroundResource(R.drawable.circle_background)
                }
                sueño.check(checkedId)
                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                selectedSueño = selectedRadioButton.id
                selectedRadioButton.setBackgroundResource(R.drawable.selected)
            }
        }

        animo.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId > -1){
                if(selectedAnimo > 0){
                    val selectedRadioButton = findViewById<RadioButton>(selectedAnimo)
                    selectedRadioButton.setBackgroundResource(R.drawable.circle_background)
                }
                animo.check(checkedId)
                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                selectedAnimo = selectedRadioButton.id
                selectedRadioButton.setBackgroundResource(R.drawable.selected)
            }
        }

        ladridos.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId > -1){
                if(selectedLadridos > 0){
                    val selectedRadioButton = findViewById<RadioButton>(selectedLadridos)
                    selectedRadioButton.setBackgroundResource(R.drawable.circle_background)
                }
                ladridos.check(checkedId)
                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                selectedLadridos = selectedRadioButton.id
                selectedRadioButton.setBackgroundResource(R.drawable.selected)
            }
        }

        btn_back.setOnClickListener {
            var intento = Intent(this, MascotasperfilActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
            finish()
        }



    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun datosAgregadoHoy(){
        val btn_continuar: Button = findViewById(R.id.btn_confirmar) as Button
        // Obtener la fecha actual
        val currentDate = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale.getDefault()).format(Date())
        storage.collection("Comportamientos")
            .whereEqualTo("email", correo)
            .whereEqualTo("mascota", mascota.nombre)
            .whereEqualTo("fecha", currentDate)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot.size()>0){
                    Toast.makeText(this, "Ya ha registrado el comportamiento hoy", Toast.LENGTH_SHORT).show()
                    btn_continuar.setOnClickListener { Toast.makeText(this, "Ya ha registrado el comportamiento hoy", Toast.LENGTH_SHORT).show() }
                }else{
                    dialog(btn_continuar)
                }

            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dialog(btn_continuar: Button){

        btn_continuar.setOnClickListener {
            if(selectedHambre > 0 && selectedSueño > 0 && selectedAnimo > 0 && selectedLadridos > 0) {
                val horaActual = LocalTime.now()
                val hora = horaActual.hour
                var minutosT:String = horaActual.minute.toString()
                if(horaActual.minute<10){
                    minutosT = "0"+horaActual.minute
                }

                var apetito = obtenerEtiquetaCantidad(selectedHambre)
                var sueño = obtenerEtiquetaCantidad(selectedSueño-4)
                var animo = obtenerEtiquetaAnimo(selectedAnimo-8)
                var ladridos = obtenerEtiquetaCantidad(selectedLadridos-12)

                val currentDate = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale.getDefault()).format(Date())


                val actividad = hashMapOf(
                    "fecha" to currentDate,
                    "hora" to convertirHora24("$hora:$minutosT"),
                    "mascota" to mascota.nombre,
                    "apetito" to apetito,
                    "sueño" to sueño,
                    "animo" to animo,
                    "ladridos" to ladridos,
                    "email" to usuario.currentUser?.email.toString()
                )
                storage.collection("Comportamientos")
                    .add(actividad)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Comportamiento agregado", Toast.LENGTH_SHORT).show()
                        var intento = Intent(this, MascotasperfilActivity::class.java)
                        intento.putExtra("nombre",  mascota.nombre)
                        intento.putExtra("image",  mascota.image)
                        intento.putExtra("edad", mascota.edad)
                        intento.putExtra("uri", mascota.imageUri.toString())
                        this.startActivity(intento)
                        finish()
                        //cargarDatos()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Fallo al guardar" + it.toString(), Toast.LENGTH_SHORT).show()
                    }

            }else{
                Toast.makeText(this, "Ingresar datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun convertirHora24(hora: String): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateObj = sdf.parse(hora)
        return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(dateObj)
    }

    fun obtenerEtiquetaCantidad(cantidad: Int): String {
        return when (cantidad) {
            4 -> "Ninguno"
            3 -> "Poco"
            2 -> "Regular"
            1 -> "Mucho"
            else -> "Desconocido"
        }
    }
    fun obtenerEtiquetaAnimo(cantidad: Int): String {
        return when (cantidad) {
            1 -> "Alegre"
            2 -> "Triste"
            3 -> "Enojado"
            4 -> "Aburrido"
            else -> "Desconocido"
        }
    }
}