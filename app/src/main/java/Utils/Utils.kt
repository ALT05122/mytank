package Utils

import android.view.View
import ru.Shikhov.BattleTanks.binding
import ru.Shikhov.BattleTanks.models.Coordinate

 fun View.checkViewCanMoveThroughBorder(coordinate: Coordinate): Boolean {
    return coordinate.top >= 0 &&
        coordinate.top + this.height <= binding.container.height  &&
        coordinate.left >= 0 &&
        coordinate.left + this.width <= binding.container.width
}