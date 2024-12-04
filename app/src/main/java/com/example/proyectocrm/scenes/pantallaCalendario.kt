import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.Calendar
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleCalendarScreen() {
    val today = LocalDate.now()
    val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)
    val currentMonth = YearMonth.now()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Calendario",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Calendar(
            modifier = Modifier.fillMaxWidth(),
            state = rememberCalendarState(
                startMonth = currentMonth.minusMonths(12),
                endMonth = currentMonth.plusMonths(12),
                firstVisibleMonth = currentMonth
            ),
            dayContent = { day ->
                Text(
                    text = day.date.dayOfMonth.toString(),
                    modifier = Modifier.padding(8.dp)
                )
            }
        )
    }
}
