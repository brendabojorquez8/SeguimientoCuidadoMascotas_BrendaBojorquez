package brenda.bojorquez.seguimientocuidadomascotas

import brenda.bojorquez.seguimientocuidadomascotas.account.LoginActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        var animacionArriba:Animation = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba)

        var logo: ImageView = findViewById(R.id.logoIV)

        logo.setAnimation(animacionArriba)

        Handler().postDelayed({
            val intentR = Intent(this, LoginActivity::class.java)
            startActivity(intentR)
            finish()
        }, 4000)
    }
}