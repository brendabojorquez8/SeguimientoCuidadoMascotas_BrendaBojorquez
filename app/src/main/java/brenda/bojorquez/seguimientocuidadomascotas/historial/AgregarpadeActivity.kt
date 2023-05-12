package brenda.bojorquez.seguimientocuidadomascotas.historial

import brenda.bojorquez.seguimientocuidadomascotas.Mascota
import brenda.bojorquez.seguimientocuidadomascotas.R
import brenda.bojorquez.seguimientocuidadomascotas.VacunasMuestra
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

class AgregarpadeActivity : AppCompatActivity() {
    lateinit var mascota: Mascota
    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregarpade)

        storage = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()

        val btn_back: ImageView = findViewById(R.id.back) as ImageView
        val btn_continuar: Button = findViewById(R.id.btn_continuar) as Button

        val editText = findViewById<EditText>(R.id.et_fechaInicio)
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
            var intento = Intent(this, PadecimientosActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
            finish()
        }

        btn_continuar.setOnClickListener {
            val et_padecimiento: EditText = findViewById(R.id.et_enfermedad)
            val et_fechaInicio: EditText = findViewById(R.id.et_fechaInicio)
            //val et_fechaFin: EditText = findViewById(R.id.et_fechaFin)
            val et_tratamiento: EditText = findViewById(R.id.et_tratamiento)
            val et_notas: EditText = findViewById(R.id.et_notas)

            val padecimiento: String = et_padecimiento.text.toString()
            val fechaInicio: String = et_fechaInicio.text.toString()
            //val fechaFin: String = et_fechaFin.text.toString()
            val tratamiento: String = et_tratamiento.text.toString()
            val notas: String = et_notas.text.toString()

            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault())
            val date = inputFormat.parse(fechaInicio)

            if(!padecimiento.isNullOrBlank() && !fechaInicio.isNullOrBlank() && !tratamiento.isNullOrBlank() &&
                !notas.isNullOrBlank()){
                    val actividad = hashMapOf(
                        "padecimiento" to padecimiento,
                        "fechaInicio" to outputFormat.format(date),
                        "tratamiento" to tratamiento,
                        "notas" to notas,
                        "mascota" to mascota.nombre,
                        "email" to usuario.currentUser?.email.toString()
                    )

                    storage.collection("Padecimientos")
                        .add(actividad)
                        .addOnSuccessListener {
                            Toast.makeText(this, "padecimiento agregada", Toast.LENGTH_SHORT).show()
                            var act = VacunasMuestra(padecimiento, R.drawable.padecimientos,outputFormat.format(date), mascota)
                            PadecimientosActivity.listaPade.add(act)
                            val intent = Intent(this, PadecimientosActivity::class.java)
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