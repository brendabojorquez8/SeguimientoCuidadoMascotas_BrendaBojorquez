package brenda.bojorquez.seguimientocuidadomascotas.account

import brenda.bojorquez.seguimientocuidadomascotas.R
import android.content.Intent
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class CrearcuentaActivity : AppCompatActivity() {
    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth
    private lateinit var auth: FirebaseAuth
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crearcuenta)
        auth = Firebase.auth

        val btn_back: ImageView = findViewById(R.id.back) as ImageView
        val btn_ingresar: Button = findViewById(R.id.btn_ingresarReg) as Button

        val editText = findViewById<EditText>(R.id.et_nacimientoReg)
        val editText2 = findViewById<EditText>(R.id.et_telefonoReg)
        editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))
        editText2.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))

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

        btn_back.setOnClickListener {
            var intento = Intent(this, LoginActivity::class.java)
            this.startActivity(intento)
        }

        btn_ingresar.setOnClickListener {
            valida_registro()
            var intento = Intent(this, LoginActivity::class.java)
            this.startActivity(intento)

        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun valida_registro(){
        val et_ombre: EditText = findViewById(R.id.et_nombreReg)
        val et_telefono: EditText = findViewById(R.id.et_telefonoReg)
        val et_ciudad: EditText = findViewById(R.id.et_ciudadReg)
        val et_estado: EditText = findViewById(R.id.et_estadoReg)
        val et_pais: EditText = findViewById(R.id.et_paisReg)
        val et_nacimiento: EditText = findViewById(R.id.et_nacimientoReg)

        val et_correo: EditText = findViewById(R.id.et_correoReg)
        val et_contra1: EditText = findViewById(R.id.et_contraReg)
        val et_contra2: EditText = findViewById(R.id.et_contra2Reg)

        val nombre: String = et_ombre.text.toString()
        val telefono: String = et_telefono.text.toString()
        val ciudad: String = et_ciudad.text.toString()
        val estado: String = et_estado.text.toString()
        val pais: String = et_pais.text.toString()
        val fecha_nacimiento: String = et_nacimiento.text.toString()

        var correo: String = et_correo.text.toString()
        var contra1: String = et_contra1.text.toString()
        var contra2: String = et_contra2.text.toString()

        storage = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()

        if(!correo.isNullOrBlank() && !contra1.isNullOrBlank() && !nombre.isNullOrBlank() && !telefono.isNullOrBlank() && !ciudad.isNullOrBlank() &&
            !contra2.isNullOrBlank() && !estado.isNullOrBlank() && !pais.isNullOrBlank() && !fecha_nacimiento.isNullOrBlank()){
                if(contra1.length>=6 && contra2.length>=6) {
                    if (contra1 == contra2) {
                        storage.collection("usuarios").document(correo).set(
                            hashMapOf("email" to correo, "nombre" to nombre, "telefono" to telefono,
                                "ciudad" to ciudad, "estado" to estado, "pais" to pais, "fecha nacimiento" to fecha_nacimiento)
                        ).addOnSuccessListener {
                            Toast.makeText(this, "Guardado con exito", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener{
                            Toast.makeText(this, "Fallo al guardar"+it.toString(), Toast.LENGTH_SHORT).show()
                        }
                        registrarFirebase(correo, contra1)
                    } else {
                        Toast.makeText(this, "Las contraseña no coinciden", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Las contraseña debe tener por lo menos 6 caracteres", Toast.LENGTH_SHORT).show()
                }
        }else{
            Toast.makeText(this, "Ingresar datos",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun registrarFirebase(email: String, password: String){

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "${user?.email}se ha creado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext, "Autenticación fallida.", Toast.LENGTH_SHORT).show()
                }
            }
    }

}