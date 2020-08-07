package com.showmiso.swipecontacts.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import androidx.core.content.ContextCompat
import com.showmiso.swipecontacts.R
import kotlin.random.Random

class DrawableManager {

    companion object {
        private var pastNumber: Int = 0

        fun changeDrawableSolidColor(context: Context, drawableId: Int): Drawable {
            val target = context.getDrawable(drawableId)
            val colors =
                randomColor()

//            if (target is ShapeDrawable) {
            val shapeDrawable: ShapeDrawable = target as ShapeDrawable
            shapeDrawable.paint.color = ContextCompat.getColor(context, colors[0])
            return shapeDrawable
//            } else if (target is ColorDrawable) {
//                val colorDrawable: ColorDrawable = target
//                colorDrawable.color = ContextCompat.getColor(context, colors[0])
//                return colorDrawable
//            }
        }

        fun randomColor(): Array<Int> {
            var randomNumber = 0
            do {
                randomNumber = Random.nextInt(colorSet.size / 2) * 2
            } while (pastNumber == randomNumber)
            pastNumber = randomNumber
            return arrayOf(colorSet[randomNumber], colorSet[randomNumber + 1])
        }

        private val colorSet = arrayOf(
            R.color.colorGreen,
            R.color.colorGreenDeep,
            R.color.colorBlue,
            R.color.colorBlueDeep,
            R.color.colorPurple,
            R.color.colorPurpleDeep,
            R.color.colorYellow,
            R.color.colorYellowDeep,
            R.color.colorOrange,
            R.color.colorOrangeDeep,
            R.color.colorRed,
            R.color.colorRedDeep
        )
    }
}