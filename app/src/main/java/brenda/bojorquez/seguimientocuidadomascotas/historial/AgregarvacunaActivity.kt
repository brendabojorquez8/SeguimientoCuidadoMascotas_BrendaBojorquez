package brenda.bojorquez.seguimientocuidadomascotas.historial

import brenda.bojorquez.seguimientocuidadomascotas.Mascota
import brenda.bojorquez.seguimientocuidadomascotas.R
import brenda.bojorquez.seguimientocuidadomascotas.VacunasMuestra
import brenda.bojorquez.seguimientocuidadomascotas.historial.VacunasActivity.Companion.listaVacuna
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class AgregarvacunaActivity : AppCompatActivity() {
    lateinit var mascota: Mascota
    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregarvacuna)

        storage = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()

        val btn_back: ImageView = findViewById(R.id.back) as ImageView
        val btn_continuar: Button = findViewById(R.id.btn_continuar) as Button

        val editText = findViewById<EditText>(R.id.et_fecha)
        editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No es necesario implementar este método
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Este método se llama cada vez que se cambia el texto del EditText
                val text = s.toString()

                if (text.length == 2 && before == 0) {
                    // Si el usuario acaba de escribir el día, agregamos el primer '/'
                    editText.setText("$text/")
                    editText.setSelection(editText.text.length)
                } else if (text.length == 5 && before == 0) {
                    // Si el usuario acaba de escribir el mes, agregamos el segundo '/'
                    editText.setText("$text/")
                    editText.setSelection(editText.text.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No es necesario implementar este método
            }
        })

        val bundle = intent.extras
        if(bundle != null){

            var imagenS: String = bundle.getString("uri").toString()
            val imagenUri = Uri.parse(imagenS)
            mascota = Mascota(bundle.getString("nombre").toString(), bundle.getInt("image"), imagenUri, bundle.getString("edad").toString() )        }

        btn_back.setOnClickListener {
            var intento = Intent(this, VacunasActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intent.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
            finish()
        }

        btn_continuar.setOnClickListener {
            val et_vacuna: EditText = this.findViewById(R.id.et_vacuna)
            val et_fecha: EditText = this.findViewById(R.id.et_fecha)
            val et_dosis: EditText = this.findViewById(R.id.et_dosis)
            val et_lote: EditText = this.findViewById(R.id.et_lote)

            val vacuna: String = et_vacuna.text.toString()
            val fecha: String = et_fecha.text.toString()
            val dosis: String = et_dosis.text.toString()
            val lote: String = et_lote.text.toString()

            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault())
            val date = inputFormat.parse(fecha)

            if(!vacuna.isNullOrBlank() && !fecha.isNullOrBlank() && !dosis.isNullOrBlank() &&
                !lote.isNullOrBlank()){
                    val actividad = hashMapOf(
                        "vacuna" to vacuna,
                        "fecha" to outputFormat.format(date),
                        "dosis" to dosis,
                        "lote" to lote,
                        "mascota" to mascota.nombre,
                        "email" to usuario.currentUser?.email.toString()
                    )

                    storage.collection("Vacunas")
                        .add(actividad)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Vacuna agregada", Toast.LENGTH_SHORT).show()
                            var act = VacunasMuestra(vacuna, R.drawable.vacuna_icono,fecha, mascota)
                            listaVacuna.add(act)
                            val intent = Intent(this, VacunasActivity::class.java)
                            intent.putExtra("nombre",  mascota.nombre)
                            intent.putExtra("image",  mascota.image)
                            intent.putExtra("edad", mascota.edad)
                            intent.putExtra("uri", mascota.imageUri.toString())
                            startActivity(intent)
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Fallo al guardar" + it.toString(), Toast.LENGTH_SHORT).show()
                        }
            }else{
                Toast.makeText(this, "Ingresar datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

}