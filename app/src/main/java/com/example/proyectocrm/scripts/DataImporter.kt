package com.example.proyectocrm.scripts

import com.google.firebase.firestore.FirebaseFirestore

/**
 * Clase para importar datos de ejemplo a Firebase Firestore.
 */
object DataImporter {

    private val db = FirebaseFirestore.getInstance()

    /**
     * Método para importar datos a Firebase.
     */
    fun importarDatosAutomatizados() {
        // Datos de ejemplo para las colecciones
        val leadsData = (1..14).map { mapOf("x" to it.toDouble(), "y" to (5000 + it * 500).toDouble()) }
        val salesData = (1..14).map { mapOf("x" to it.toDouble(), "y" to (1.5 + it * 0.2).toDouble()) }
        val ordersData = (1..14).map { mapOf("x" to it.toDouble(), "y" to (200 + it * 50).toDouble()) }

        // Función para añadir datos a una colección
        fun agregarDatosAColeccion(coleccion: String, datos: List<Map<String, Double>>) {
            datos.forEachIndexed { index, data ->
                db.collection(coleccion).document("entry_$index").set(data)
                    .addOnSuccessListener { println("Documento añadido a $coleccion: $data") }
                    .addOnFailureListener { e -> println("Error al añadir documento a $coleccion: ${e.message}") }
            }
        }

        // Importar datos a las colecciones
        agregarDatosAColeccion("leads", leadsData)
        agregarDatosAColeccion("sales", salesData)
        agregarDatosAColeccion("orders_chart", ordersData)
    }
}
