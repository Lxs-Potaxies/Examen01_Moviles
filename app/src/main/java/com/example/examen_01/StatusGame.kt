package com.example.examen_01

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StatusGame : AppCompatActivity() {
    private var montoInicial: Int = 0
    private var montoFinal: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_game)

        // Obtener los montos enviados desde Game
        montoInicial = intent.getIntExtra("MONTO_INICIAL", 0)
        montoFinal = intent.getIntExtra("MONTO_FINAL", 0)

        // Mostrar el mensaje de estado del juego
        val mensaje = determinarMensajeEstado()
        val textViewDescription = findViewById<TextView>(R.id.textViewDescription)
        textViewDescription.text = mensaje
    }

    private fun determinarMensajeEstado(): String {
        return when {
            montoFinal > montoInicial -> "Eres un ganador"
            montoFinal == montoInicial -> "Te salvaste..."
            montoFinal < montoInicial && montoFinal > 0 -> "No deberías de jugar…Retírate"
            montoFinal == 0 -> "Lo perdiste todo….No vuelvas a jugar!"
            else -> "Estado desconocido"
        }
    }
}
