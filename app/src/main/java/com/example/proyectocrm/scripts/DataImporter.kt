package com.example.proyectocrm.scripts

import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

/**
 * Clase que maneja la importación automatizada de datos a Firebase Firestore.
 */
object DataImporter {

    // Instancia de Firebase Firestore
    private val db = FirebaseFirestore.getInstance()

    /**
     * Función para importar datos automatizados en las colecciones de Firebase.
     */
    fun importarDatosAutomatizados() {
        // Función para generar datos aleatorios con variaciones dinámicas
        fun generarDatosAleatorios(): List<Pair<Float, Float>> {
            val datos = mutableListOf<Pair<Float, Float>>()
            var previousY = Random.nextInt(2000, 8000).toFloat() // Valor inicial aleatorio
            for (i in 1..14) {
                val variacion = Random.nextInt(-1000, 1000).toFloat() // Variación aleatoria
                val nuevoY = (previousY + variacion).coerceAtLeast(0f) // Asegura valores no negativos
                datos.add(i.toFloat() to nuevoY)
                previousY = nuevoY // Actualiza el valor previo
            }
            return datos
        }

        // Función para agregar datos sin duplicados a una colección
        fun agregarDatosSinDuplicados(chartName: String, data: List<Pair<Float, Float>>) {
            db.collection(chartName).get().addOnSuccessListener { snapshot ->
                // Obtén los valores existentes en Firebase para evitar duplicados
                val existentes = snapshot.documents.mapNotNull { it.getDouble("x")?.toFloat() }

                // Filtra datos y limita la cantidad a 10 elementos
                val nuevosDatos = data.filter { pair -> pair.first !in existentes }.take(10)

                // Agrega los datos nuevos a Firebase
                nuevosDatos.forEach { (x, y) ->
                    val docData = hashMapOf("x" to x, "y" to y)
                    db.collection(chartName).add(docData).addOnSuccessListener {
                        println("Dato añadido a $chartName: x=$x, y=$y")
                    }.addOnFailureListener { e ->
                        e.printStackTrace()
                    }
                }
            }.addOnFailureListener { e ->
                e.printStackTrace()
            }
        }

        // Genera datos dinámicos para cada colección
        val leadsData = generarDatosAleatorios()
        val salesData = generarDatosAleatorios()
        val ordersData = generarDatosAleatorios()

        // Agrega datos a las colecciones
        agregarDatosSinDuplicados("leads", leadsData)
        agregarDatosSinDuplicados("sales", salesData)
        agregarDatosSinDuplicados("orders", ordersData)
    }
}
