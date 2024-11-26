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
                description.isEnabled = false
                setDrawGridBackground(false)
                axisRight.isEnabled = false

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    textColor = android.graphics.Color.BLACK
                    granularity = 1f
                }

                axisLeft.apply {
                    textColor = android.graphics.Color.BLACK
                    setDrawGridLines(false)
                    axisMinimum = 0f
                }

                animateX(1000)
                animateY(1000)
            }
        },
        update = { chart ->
            val dataSet = LineDataSet(dataPoints, label).apply {
                color = android.graphics.Color.parseColor("#007AFF")
                setCircleColor(android.graphics.Color.parseColor("#3366FF"))
                lineWidth = 2f
                circleRadius = 4f
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }
            chart.data = LineData(dataSet)
            chart.invalidate()
        },
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}
