// components/ChartRepository.kt
package com.example.proyectocrm.components

import com.github.mikephil.charting.data.Entry
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Repositorio para manejar los datos de los gráficos desde Firebase Firestore.
 */
object ChartRepository {
    private val db = FirebaseFirestore.getInstance()

    /**
     * Obtiene los datos de un gráfico desde Firebase Firestore.
     *
     * @param chartName Nombre de la colección en Firestore.
     * @return Lista de puntos (Entry) para graficar.
     */
    suspend fun getChartData(chartName: String): List<Entry> {
        return try {
            val snapshot = db.collection(chartName).get().await()
            snapshot.documents.mapNotNull { document ->
                val x = document.getDouble("x")?.toFloat()
                val y = document.getDouble("y")?.toFloat()
                if (x != null && y != null) Entry(x, y) else null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

/**
 * ChartRepository es un objeto encargado de gestionar la obtención de datos de gráficos desde Firebase Firestore.
 * Utiliza una función suspendida para realizar las operaciones de manera asincrónica, permitiendo una experiencia
 * fluida para el usuario al no bloquear el hilo principal.
 *
 * Propiedades:
 * - db: Instancia de FirebaseFirestore para interactuar con la base de datos.
 *
 * Métodos:
 * - getChartData(chartName: String): Suspende la ejecución mientras obtiene los datos de la colección
 *   especificada en Firestore. Convierte los datos recuperados en una lista de objetos Entry,
 *   utilizados por la biblioteca MPAndroidChart para generar gráficos.
 *
 * Características:
 * - Los datos se recuperan mediante una consulta a Firestore.
 * - La función maneja errores devolviendo una lista vacía en caso de excepción.
 * - Utiliza `mapNotNull` para filtrar documentos que no contengan los valores requeridos (`x` y `y`).
 * - Es reutilizable para cualquier gráfico especificando el nombre de la colección en Firestore.
 *
 * Ejemplo de uso:
 * ```
 * val leadsData = ChartRepository.getChartData("leads")
 * ```
 *
 * Este enfoque centraliza la lógica de obtención de datos, facilitando su mantenimiento y reutilización
 * en toda la aplicación.
 */

