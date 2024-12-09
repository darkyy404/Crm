// ChartComponent.kt
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

/**
 * Componente para mostrar un gráfico de líneas utilizando MPAndroidChart.
 *
 * @param dataPoints Lista de puntos (Entry) a mostrar en el gráfico.
 * @param label Etiqueta del gráfico, utilizada como leyenda.
 */
@Composable
fun LineChartComponent(dataPoints: List<Entry>, label: String) {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                // Configuración básica del gráfico
                description.isEnabled = false
                setDrawGridBackground(false)
                axisRight.isEnabled = false

                // Configuración del eje X
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    textColor = android.graphics.Color.BLACK
                    granularity = 1f
                }

                // Configuración del eje Y
                axisLeft.apply {
                    textColor = android.graphics.Color.BLACK
                    setDrawGridLines(false)
                    axisMinimum = 0f
                }

                // Animaciones
                animateX(1000)
                animateY(1000)
            }
        },
        update = { chart ->
            // Configuración de los datos del gráfico
            val dataSet = LineDataSet(dataPoints, label).apply {
                color = android.graphics.Color.parseColor("#007AFF") // Color de la línea
                setCircleColor(android.graphics.Color.parseColor("#3366FF")) // Color de los puntos
                lineWidth = 2f
                circleRadius = 4f
                setDrawValues(false) // No mostrar valores en los puntos
                mode = LineDataSet.Mode.CUBIC_BEZIER // Líneas curvas
            }
            chart.data = LineData(dataSet)
            chart.invalidate() // Refrescar el gráfico
        },
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .height(200.dp) // Tamaño del gráfico
    )
}
