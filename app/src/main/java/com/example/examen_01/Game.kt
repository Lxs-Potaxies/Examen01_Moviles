package com.example.examen_01

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class Game : AppCompatActivity() {

    private var montoInicial: Int = 100
    private var montoDisponible: Int = montoInicial
    private var ganadasConsecutivas: Int = 0
    private var cantidadDados: Int = 0
    private lateinit var dadoUnoImageView: ImageView
    private lateinit var dadoDosImageView: ImageView
    private lateinit var dadoTresImageView: ImageView
    private lateinit var radioButtons: List<RadioButton>
    private lateinit var milloImage: ImageView
    private lateinit var montoApostadoTextView: TextView
    private var  montoApostado: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val nombreJugador = intent.getStringExtra("NOMBRE_JUGADOR") ?: "Jugador Anónimo"
        val fondoApuestas = intent.getStringExtra("FONDO_APUESTAS") ?: "0"
        cantidadDados = intent.getIntExtra("CANTIDAD_DADOS", 1)

        montoDisponible = fondoApuestas.toIntOrNull() ?: 0

        val nombreTextView = findViewById<TextView>(R.id.textView5)
        val montoDisponibleTextView = findViewById<TextView>(R.id.textView6)
        dadoUnoImageView = findViewById(R.id.UnoImage)
        dadoDosImageView = findViewById(R.id.DosImage)
        dadoTresImageView = findViewById(R.id.TresImage)
        milloImage = findViewById(R.id.milloImage)
        montoApostadoTextView = findViewById(R.id.textView7)
        val buttonLanzar = findViewById<Button>(R.id.button2)

        radioButtons = listOf(
            findViewById(R.id.radioButton1),
            findViewById(R.id.radioButton2),
            findViewById(R.id.radioButton3),
            findViewById(R.id.radioButton4),
            findViewById(R.id.radioButton5),
            findViewById(R.id.radioButton6),
            findViewById(R.id.radioButton7),
            findViewById(R.id.radioButton8),
            findViewById(R.id.radioButton9),
            findViewById(R.id.radioButton10),
            findViewById(R.id.radioButton11),
            findViewById(R.id.radioButton12),
            findViewById(R.id.radioButton13),
            findViewById(R.id.radioButton14),
            findViewById(R.id.radioButton15),
            findViewById(R.id.radioButton16),
            findViewById(R.id.radioButton17),
            findViewById(R.id.radioButton18)
        )

        nombreTextView.text = "Jugador: $nombreJugador"
        montoDisponibleTextView.text = "Monto Disponible: $montoDisponible"

        mostrarDados(cantidadDados)
        habilitarRadioButtons(cantidadDados)

        buttonLanzar.setOnClickListener {
            lanzarDados(montoDisponibleTextView)
        }
    }

    private fun mostrarDados(cantidad: Int) {
        dadoUnoImageView.visibility = View.GONE
        dadoDosImageView.visibility = View.GONE
        dadoTresImageView.visibility = View.GONE

        when (cantidad) {
            1 -> dadoUnoImageView.visibility = View.VISIBLE
            2 -> {
                dadoUnoImageView.visibility = View.VISIBLE
                dadoDosImageView.visibility = View.VISIBLE
            }
            3 -> {
                dadoUnoImageView.visibility = View.VISIBLE
                dadoDosImageView.visibility = View.VISIBLE
                dadoTresImageView.visibility = View.VISIBLE
            }
        }
    }

    private fun habilitarRadioButtons(cantidad: Int) {
        when (cantidad) {
            1 -> {
                radioButtons[0].isEnabled = false
                radioButtons[1].isEnabled = true
            }
            2 -> {
                radioButtons[0].isEnabled = false
                radioButtons[1].isEnabled = true
            }
            3 -> {
                radioButtons[0].isEnabled = false
                radioButtons[1].isEnabled = false
            }
        }
    }

    private fun lanzarDados(montoDisponibleTextView: TextView) {
        if (montoDisponible <= 0) {
            Toast.makeText(this, "No tienes dinero para jugar.", Toast.LENGTH_LONG).show()
            return
        }

        val numeroApostado = obtenerNumeroApostado() ?: return

        // Sumar el monto apostado de 100,000 al monto total apostado
        montoApostado += 100000

        // Actualizar el TextView con el monto apostado
        montoApostadoTextView.text = "Monto Apostado: $montoApostado"

        // Reducir el monto disponible por el monto apostado solo si el jugador pierde
        val resultado = lanzarYMostrarDados()

        if (resultado == numeroApostado) {
            // Si gana, sumar 200,000 al monto disponible
            montoDisponible += 200000
            ganadasConsecutivas++
            mostrarResultado(true)
            Toast.makeText(this, "¡Ganaste! El número apostado era $numeroApostado.", Toast.LENGTH_LONG).show()
        } else {
            // Si pierde, restar 100,000 del monto disponible
            montoDisponible -= 100000
            ganadasConsecutivas = 0
            mostrarResultado(false)
            Toast.makeText(this, "Perdiste. El número fue $resultado. Apostaste $numeroApostado.", Toast.LENGTH_LONG).show()
        }

        // Actualizar el TextView con el monto disponible
        montoDisponibleTextView.text = "Monto Disponible: $montoDisponible"

        // Desmarcar los radio buttons
        desmarcarRadioButtons()

        // Verificar condiciones de fin de juego
        if (montoDisponible <= 0) {
            Toast.makeText(this, "Te quedaste sin dinero. Fin del juego.", Toast.LENGTH_LONG).show()
            val intent = Intent(this, StatusGame::class.java).apply {
                putExtra("MONTO_INICIAL", montoInicial)
                putExtra("MONTO_FINAL", montoDisponible)
            }
            startActivity(intent)
            finish()
        } else if (ganadasConsecutivas >= 3) {
            Toast.makeText(this, "¡Ganaste tres veces consecutivas! Fin del juego.", Toast.LENGTH_LONG).show()
            val intent = Intent(this, StatusGame::class.java).apply {
                putExtra("MONTO_INICIAL", montoInicial)
                putExtra("MONTO_FINAL", montoDisponible)
            }
            startActivity(intent)
            finish()
        }
    }
    
    private fun obtenerNumeroApostado(): Int? {
        for (radioButton in radioButtons) {
            if (radioButton.isChecked) {
                return radioButton.text.toString().toInt()
            }
        }
        Toast.makeText(this, "Debes seleccionar un número para apostar.", Toast.LENGTH_LONG).show()
        return null
    }

    private fun desmarcarRadioButtons() {
        for (radioButton in radioButtons) {
            radioButton.isChecked = false
        }
    }

    private fun lanzarYMostrarDados(): Int {
        val dadoUno = Random.nextInt(1, 7)
        val dadoDos = if (cantidadDados > 1) Random.nextInt(1, 7) else 0
        val dadoTres = if (cantidadDados > 2) Random.nextInt(1, 7) else 0

        dadoUnoImageView.setImageResource(obtenerImagenDado(dadoUno))
        dadoDosImageView.setImageResource(obtenerImagenDado(dadoDos))
        dadoTresImageView.setImageResource(obtenerImagenDado(dadoTres))

        return dadoUno + dadoDos + dadoTres
    }

    private fun obtenerImagenDado(numero: Int): Int {
        return when (numero) {
            1 -> R.mipmap.ic_1
            2 -> R.mipmap.ic_2
            3 -> R.mipmap.ic_3
            4 -> R.mipmap.ic_4
            5 -> R.mipmap.ic_5
            6 -> R.mipmap.ic_6
            else -> R.mipmap.ic_1
        }
    }

    private fun mostrarResultado(ganador: Boolean) {
        // Mostrar la imagen correspondiente dependiendo del resultado
        val resourceId = if (ganador) R.mipmap.ic_millonario else R.mipmap.ic_bancarrota
        milloImage.setImageResource(resourceId)
        milloImage.visibility = View.VISIBLE
    }
}
