package com.example.proyectocrm.scenes.home

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


@Composable
fun LineChartComponent(dataPoints: List<Entry>, label: String) {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                // Configuración básica del gráfico
                description.isEnabled = false // Ocultar descripción
                setDrawGridBackground(false) // Ocultar fondo del grid
                axisRight.isEnabled = false // Ocultar eje derecho

                // Configuración del eje X
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM // Posición en la parte inferior
                    setDrawGridLines(false) // Ocultar líneas del grid
                    textColor = android.graphics.Color.BLACK
                }

                // Configuración del eje Y
                axisLeft.apply {
                    textColor = android.graphics.Color.BLACK
                }

                // Animaciones
                animateX(1000)
                animateY(1000)

                // Datos del gráfico
                val lineDataSet = LineDataSet(dataPoints, label).apply {
                    color = android.graphics.Color.BLUE
                    valueTextColor = android.graphics.Color.BLACK
                    lineWidth = 2f
                    setCircleColor(android.graphics.Color.BLUE)
                    circleRadius = 4f
                }
                data = LineData(lineDataSet)
            }
        },
        update = { chart ->
            val lineDataSet = LineDataSet(dataPoints, label).apply {
                color = android.graphics.Color.BLUE
                valueTextColor = android.graphics.Color.BLACK
                lineWidth = 2f
                setCircleColor(android.graphics.Color.BLUE)
                circleRadius = 4f
            }
            chart.data = LineData(lineDataSet)
            chart.invalidate() // Actualizar el gráfico
        }
    )
}

