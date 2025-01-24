import android.graphics.drawable.GradientDrawable
import android.graphics.Color
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.CalendarDay

class EventDecorator(
    private val dates: Collection<CalendarDay>,
    private val colorRes: Int
) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.any { it == day }
    }

    override fun decorate(view: DayViewFacade) {
        val shapeDrawable = GradientDrawable()
        shapeDrawable.shape = GradientDrawable.OVAL  // Rounded corners
        shapeDrawable.setColor(colorRes)  // Set the background color
        shapeDrawable.setSize(100, 100)  // Set the size of the rounded background

        // Apply the rounded background to the day
        view.setBackgroundDrawable(shapeDrawable)
    }
}
