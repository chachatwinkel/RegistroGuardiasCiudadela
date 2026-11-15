package com.isengard.registroguardiasciudadela

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class RegistroGuardiaActivity : AppCompatActivity() {

    //@Samah
    // Constantes que utilizamos para guardar los datos del registro.
    // PREFS_NAME indica el nombre del archivo XML que se creará.
    // Las KEY sirven para identificar cada dato que queremos guardar.
    companion object {
        const val PREFS_NAME = "RegistroGuardia"   // genera RegistroGuardia.xml
        const val KEY_SOLDADO = "KEY_SOLDADO"
        const val KEY_RANGO = "KEY_RANGO"
        const val KEY_ESTADO = "KEY_ESTADO"
    }
     //@Eva
    // Variables que representan los elementos del xml.
    // lateinit indica que se inicializarán más adelante, dentro de onCreate().
    private lateinit var Soldado: EditText
    private lateinit var Rango: RadioGroup
    private lateinit var Estado: SwitchCompat
    private lateinit var btnGuardar: Button


    // Metodo que se ejecuta al crear la actividad.
   // Aquí cargamos el diseño XML y relacionamos las variables
   // con los elementos de la interfaz mediante findViewById().
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registroguardia)

        //@Samah
        // Relacionamos cada variable con su elemento del XML.
        Soldado = findViewById(R.id.nombreSoldado)
        Rango = findViewById(R.id.rgRango)
        Estado = findViewById(R.id.estado)
        btnGuardar = findViewById(R.id.btnGuardarGuardia)

        btnGuardar.setOnClickListener { guardarRegistro() }
    }

    //@Eva
    // Se ejecuta cada vez que la actividad vuelve a estar visible.
    // Llamamos a cargarRegistro() para recuperar los datos guardados
    // y mostrar nuevamente la información en la interfaz.
    override fun onResume() {
        super.onResume()
        cargarRegistro()
    }

    //@Samah
    // metodo encargado de recuperar los datos guardados en SharedPreferences
   // y mostrarlos en los elementos correspondientes de la interfaz.
    private fun guardarRegistro() {
        val soldado = Soldado.text.toString().trim()

        // Control defensivo: nombre no vacío
        if (soldado.isEmpty()) {
            Soldado.error = "El nombre no puede estar vacío"
            return
        }

        val rango = when (Rango.checkedRadioButtonId) {
            R.id.opcCapitan -> 1
            R.id.opcVeterano -> 2
            else -> 0
        }
        val enGuardia = Estado.isChecked

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_SOLDADO, soldado)
            .putInt(KEY_RANGO, rango)
            .putBoolean(KEY_ESTADO, enGuardia)
            .apply()

        // Solo tras lanzar la transacción se limpia el EditText
        Soldado.text.clear()
        Toast.makeText(this, "Registro de $soldado guardado", Toast.LENGTH_SHORT).show()
    }


    //@Eva
    // Metodo encargado de guardar los datos del soldado en SharedPreferences,
    // comprobando previamente que el nombre no esté vacío.
    private fun cargarRegistro() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        Soldado.setText(prefs.getString(KEY_SOLDADO, ""))

        // Control defensivo: forzamos el rango al intervalo válido 0..2
        val rango = prefs.getInt(KEY_RANGO, 0).coerceIn(0, 2)
        Rango.check(
            when (rango) {
                1 -> R.id.opcCapitan
                2 -> R.id.opcVeterano
                else -> R.id.opcRecluta
            }
        )

        Estado.isChecked = prefs.getBoolean(KEY_ESTADO, false)
    }
}