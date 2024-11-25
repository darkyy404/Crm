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
import com.github.mikephil.charting.utils.ColorTemplate
import androidx.compose.ui.graphics.Color as ComposeColor

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
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.textColor = android.graphics.Color.BLACK
                xAxis.granularity = 1f

                // Configuración del eje Y
                axisLeft.textColor = android.graphics.Color.BLACK
                axisLeft.setDrawGridLines(false)
                axisLeft.axisMinimum = 0f

                // Animaciones
                animateX(1000)
                animateY(1000)
            }
        },
        update = { chart ->
            // Configurar los datos del gráfico
            val dataSet = LineDataSet(dataPoints, label).apply {
                color = ColorTemplate.MATERIAL_COLORS[0]
                setCircleColor(ColorTemplate.MATERIAL_COLORS[0])
                lineWidth = 2f
                circleRadius = 4f
                setDrawValues(false) // No mostrar valores en cada punto
            }

            // Asignar los datos al gráfico
            chart.data = LineData(dataSet)
            chart.invalidate() // Refrescar el gráfico
        },
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .height(200.dp) // Asegurar que el gráfico tenga suficiente espacio
    )
}
