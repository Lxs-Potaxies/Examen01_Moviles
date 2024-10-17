package com.example.examen_01

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var nombreJugador: EditText
    private lateinit var cantidadDadosEditText: EditText // Campo EditText para la cantidad de dados
    private lateinit var fondoApuestas: EditText
    private var fechaNacimiento: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar componentes
        calendarView = findViewById(R.id.calendarView)
        nombreJugador = findViewById(R.id.editTextText)
        fondoApuestas = findViewById(R.id.editTextNumber3)
        cantidadDadosEditText = findViewById(R.id.editTextNumber2)
        val buttonIngresar = findViewById<Button>(R.id.button)

        // Guardar la fecha de nacimiento seleccionada
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            fechaNacimiento = calendar.timeInMillis
        }

        buttonIngresar.setOnClickListener {
            if (isMayorDeEdad(fechaNacimiento)) {
                // Obtener el valor del fondo de apuestas y el nombre del jugador
                val fondoDeApuestas = fondoApuestas.text.toString()
                val nombreDelJugador = nombreJugador.text.toString()
                val cantidadDeDadosTexto = cantidadDadosEditText.text.toString()

                if (fondoDeApuestas.isNotEmpty() && nombreDelJugador.isNotEmpty() && cantidadDeDadosTexto.isNotEmpty()) {
                    // Convertir la cantidad de dados a un entero
                    val cantidadDeDados = cantidadDeDadosTexto.toIntOrNull()

                    // Validar que la cantidad de dados esté entre 1 y 3
                    if (cantidadDeDados != null && cantidadDeDados in 1..3) {
                        // Verifica si el fondo de apuestas es mayor o igual a 2 millones
                        if (montoMinimo(fondoDeApuestas)) {
                            // Iniciar la actividad de Game si el monto es válido
                            val intent = Intent(this, Game::class.java)
                            intent.putExtra("FONDO_APUESTAS", fondoDeApuestas)
                            intent.putExtra("NOMBRE_JUGADOR", nombreDelJugador)
                            intent.putExtra("CANTIDAD_DADOS", cantidadDeDados)  // Envía la cantidad de dados seleccionada
                            startActivity(intent)
                        } else {
                            // Mostrar mensaje si el monto es menor a 2 millones
                            Toast.makeText(this, "El monto de apuestas debe ser mayor o igual a 2 millones.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        // Mostrar un mensaje si la cantidad de dados no es válida
                        Toast.makeText(this, "Por favor, ingresa una cantidad de dados válida (1 a 3).", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Mostrar un mensaje si alguno de los campos está vacío
                    Toast.makeText(this, "Por favor, ingresa el nombre, el fondo de apuestas y la cantidad de dados.", Toast.LENGTH_LONG).show()
                }
            } else {
                // Si el jugador no tiene 21 años, mostramos un mensaje
                Toast.makeText(this, "Debes tener al menos 21 años para jugar.", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Método para verificar si el monto de apuesta es mayor o igual a 2 millones
    private fun montoMinimo(fondoDeApuestas: String): Boolean {
        val monto = fondoDeApuestas.toLongOrNull() // Convierte el fondo de apuestas a Long

        return if (monto != null && monto >= 2000000) {
            true // El monto es válido
        } else {
            false // El monto es menor a dos millones o no es válido
        }
    }

    // Método para verificar si el jugador es mayor de 21 años
    private fun isMayorDeEdad(fechaNacimiento: Long): Boolean {
        val calendarNacimiento = Calendar.getInstance()
        calendarNacimiento.timeInMillis = fechaNacimiento

        val hoy = Calendar.getInstance()
        val edad = hoy.get(Calendar.YEAR) - calendarNacimiento.get(Calendar.YEAR)

        // Verifica si el cumpleaños ya ha pasado este año
        if (hoy.get(Calendar.DAY_OF_YEAR) < calendarNacimiento.get(Calendar.DAY_OF_YEAR)) {
            return edad - 1 >= 21
        }
        return edad >= 21
    }
}
